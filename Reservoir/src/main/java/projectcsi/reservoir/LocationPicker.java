
package projectcsi.reservoir;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LocationPicker {

    private final Stage owner;
    private final Consumer<String> onLocationSelected;

    private javafx.scene.web.WebEngine engine;
    private Label statusLabel;

    public LocationPicker(Stage owner, Consumer<String> onLocationSelected) {
        this.owner = owner;
        this.onLocationSelected = onLocationSelected;
    }

    public void show() {

        Stage mapStage = new Stage();
        mapStage.initOwner(owner);
        mapStage.initModality(Modality.WINDOW_MODAL);
        mapStage.setTitle("Select a Location");

        WebView map = new WebView();
        engine = map.getEngine();

        statusLabel = new Label("Click the map to select a location.");

        Button saveButton = new Button("Save Location");
        saveButton.setOnAction(event -> saveLocation(mapStage));

        HBox bottom = new HBox(15, statusLabel, saveButton);
        bottom.setStyle("-fx-padding: 10;");

        BorderPane root = new BorderPane();
        root.setCenter(map);
        root.setBottom(bottom);

        mapStage.setScene(new Scene(root, 850, 600));

        engine.loadContent(getMapHTML());

        mapStage.show();
    }

    // Displays the OpenStreetMap map.
    private String getMapHTML() {

        return "<html><head>"
            + "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>"
            + "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>"
            + "<style>html,body,#map{height:100%;margin:0;}</style>"
            + "</head><body>"
            + "<div id='map'></div>"
            + "<script>"
            + "var map = L.map('map').setView([39.5,-98.35],4);"
            + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',"
            + "{attribution:'OpenStreetMap'}).addTo(map);"
            + "var marker;"
            + "window.lat = null;"
            + "window.lon = null;"
            + "map.on('click',function(e){"
            + "window.lat=e.latlng.lat;"
            + "window.lon=e.latlng.lng;"
            + "if(marker) map.removeLayer(marker);"
            + "marker=L.marker(e.latlng).addTo(map);"
            + "});"
            + "</script></body></html>";
    }

    // Gets the selected coordinates and returns the address.
    private void saveLocation(Stage mapStage) {

        try {

            Object latValue = engine.executeScript("window.lat");
            Object lonValue = engine.executeScript("window.lon");

            if (latValue == null || lonValue == null) {
                statusLabel.setText("Please click the map first.");
                return;
            }

            double latitude = Double.parseDouble(latValue.toString());
            double longitude = Double.parseDouble(lonValue.toString());

            statusLabel.setText("Finding address...");

            String address = getAddress(latitude, longitude);

            if (address == null) {
                statusLabel.setText("Could not find an address.");
                return;
            }

            // Send the address back to CategoryEventsScreen.
            onLocationSelected.accept(address);

            mapStage.close();

        } catch (Exception e) {
            statusLabel.setText("Could not save location.");
            e.printStackTrace();
        }
    }

    // Uses the OpenStreetMap Nominatim reverse-geocoding API.
    private String getAddress(double latitude, double longitude)
            throws Exception {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String url = "https://nominatim.openstreetmap.org/reverse"
                + "?format=jsonv2"
                + "&lat=" + latitude
                + "&lon=" + longitude;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "ReservoirJavaFXApp/1.0")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "\"display_name\"\\s*:\\s*\"((?:\\\\.|[^\"])*)\""
        );

        Matcher matcher = pattern.matcher(response.body());

        if (matcher.find()) {
            return matcher.group(1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .replace("\\n", " ");
        }

        return null;
    }
}
