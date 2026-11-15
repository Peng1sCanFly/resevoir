
package projectcsi.reservoir;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.util.Duration;

public class PhotonAddressAutocomplete {

    // Stores an address and its geographic coordinates.
    // Uses a regular class instead of a record for Java 11.
    public static class AddressSuggestion {

        private final String displayName;
        private final double latitude;
        private final double longitude;

        public AddressSuggestion(
                String displayName,
                double latitude,
                double longitude
        ) {
            this.displayName = displayName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String displayName() {
            return displayName;
        }

        public double latitude() {
            return latitude;
        }

        public double longitude() {
            return longitude;
        }
    }

    private final TextField locationField;

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    private final ObjectMapper mapper =
            new ObjectMapper();

    // Dropdown containing address suggestions.
    private final ListView<AddressSuggestion> suggestions =
            new ListView<>();

    private final Popup popup = new Popup();

    // Wait before sending another request.
    private final PauseTransition delay =
            new PauseTransition(Duration.millis(1000));

    // Prevents older API responses replacing newer results.
    private final AtomicInteger requestNumber =
            new AtomicInteger(0);

    // Stores the address actually selected by the user.
    private AddressSuggestion selectedSuggestion;

    // Prevents selecting an address from triggering another search.
    private boolean selectingAddress = false;

    public PhotonAddressAutocomplete(TextField locationField) {

        this.locationField = locationField;

        // Configure the suggestion dropdown.
        suggestions.setPrefWidth(350);
        suggestions.setPrefHeight(180);

        // Display full addresses in the dropdown.
        suggestions.setCellFactory(list -> new ListCell<AddressSuggestion>() {

            @Override
            protected void updateItem(
                    AddressSuggestion item,
                    boolean empty
            ) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.displayName());
                }
            }
        });

        popup.getContent().add(suggestions);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        // Select an address when clicked.
        suggestions.setOnMouseClicked(event -> {

            AddressSuggestion selected =
                    suggestions.getSelectionModel().getSelectedItem();

            if (selected != null) {
                selectAddress(selected);
            }
        });

        // Allow Enter to select a highlighted suggestion.
        suggestions.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {

                AddressSuggestion selected =
                        suggestions.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    selectAddress(selected);
                }
            }
        });

        // Watch the text field for changes.
        locationField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    // Do not search when selecting a suggestion.
                    if (selectingAddress) {
                        return;
                    }

                    // Previous selection is no longer valid.
                    selectedSuggestion = null;

                    int currentRequest =
                            requestNumber.incrementAndGet();

                    // Hide suggestions for short searches.
                    if (newValue == null ||
                            newValue.trim().length() < 3) {

                        delay.stop();
                        popup.hide();
                        return;
                    }

                    // Wait until the user stops typing.
                    delay.stop();

                    delay.setOnFinished(event ->
                            searchAddresses(
                                    newValue.trim(),
                                    currentRequest
                            )
                    );

                    delay.playFromStart();
                }
        );
    }

    // Send a request to the Photon API.
    private void searchAddresses(
            String query,
            int currentRequest
    ) {

        try {

            String encodedQuery =
                    URLEncoder.encode(
                            query,
                            StandardCharsets.UTF_8
                    );

            String url =
                    "https://photon.komoot.io/api/?q="
                    + encodedQuery
                    + "&limit=6&lang=en";

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header(
                                    "User-Agent",
                                    "ReservoirApplication/1.0"
                            )
                            .GET()
                            .build();

            httpClient.sendAsync(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            ).thenAccept(response -> {

                // Check the API response.
                if (response.statusCode() != 200) {
                    System.err.println(
                            "Photon API error: "
                            + response.statusCode()
                    );
                    return;
                }

                try {

                    List<AddressSuggestion> results =
                            parseAddresses(response.body());

                    Platform.runLater(() -> {

                        // Ignore outdated API responses.
                        if (requestNumber.get() != currentRequest) {
                            return;
                        }

                        suggestions.getItems().setAll(results);

                        if (!results.isEmpty()) {
                            showPopup();
                        } else {
                            popup.hide();
                        }
                    });

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }).exceptionally(exception -> {

                exception.printStackTrace();
                return null;

            });

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Convert Photon's JSON response into address objects.
    private List<AddressSuggestion> parseAddresses(
            String json
    ) throws Exception {

        List<AddressSuggestion> results =
                new ArrayList<>();

        JsonNode root = mapper.readTree(json);

        JsonNode features = root.path("features");

        for (JsonNode feature : features) {

            JsonNode properties =
                    feature.path("properties");

            JsonNode coordinates =
                    feature.path("geometry")
                            .path("coordinates");

            if (!coordinates.isArray() ||
                    coordinates.size() < 2) {
                continue;
            }

            // GeoJSON stores longitude first, latitude second.
            double longitude = coordinates.get(0).asDouble();
            double latitude = coordinates.get(1).asDouble();

            String houseNumber =
                    properties.path("housenumber").asText("");

            String street =
                    properties.path("street").asText("");

            String name =
                    properties.path("name").asText("");

            String postcode =
                    properties.path("postcode").asText("");

            String city =
                    properties.path("city").asText("");

            if (city.isEmpty()) {
                city = properties.path("district").asText("");
            }

            String state =
                    properties.path("state").asText("");

            String country =
                    properties.path("country").asText("");

            // Build a readable address.
            Set<String> parts = new LinkedHashSet<>();

            if (!street.isEmpty()) {

                String streetAddress =
                        (houseNumber + " " + street).trim();

                parts.add(streetAddress);

            } else if (!name.isEmpty()) {

                parts.add(name);
            }

            if (!postcode.isEmpty()) {
                parts.add(postcode);
            }

            if (!city.isEmpty()) {
                parts.add(city);
            }

            if (!state.isEmpty()) {
                parts.add(state);
            }

            if (!country.isEmpty()) {
                parts.add(country);
            }

            if (parts.isEmpty()) {
                continue;
            }

            String displayName =
                    String.join(", ", parts);

            results.add(
                    new AddressSuggestion(
                            displayName,
                            latitude,
                            longitude
                    )
            );
        }

        return results;
    }

    // Position the dropdown below the TextField.
    private void showPopup() {

        Bounds bounds =
                locationField.localToScreen(
                        locationField.getBoundsInLocal()
                );

        if (bounds == null) {
            return;
        }

        popup.show(
                locationField,
                bounds.getMinX(),
                bounds.getMaxY()
        );
    }

    // Fill the TextField with the selected address.
    private void selectAddress(
            AddressSuggestion address
    ) {

        // Cancel any pending searches.
        requestNumber.incrementAndGet();
        delay.stop();

        // Prevent the text update from starting a new search.
        selectingAddress = true;

        locationField.setText(address.displayName());

        selectingAddress = false;

        // Store the selected address and coordinates.
        selectedSuggestion = address;

        popup.hide();
    }

    // Retrieve the selected address.
    public AddressSuggestion getSelectedSuggestion() {
        return selectedSuggestion;
    }
}
