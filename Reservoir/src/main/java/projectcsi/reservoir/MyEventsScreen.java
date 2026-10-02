package projectcsi.reservoir;

import java.util.ArrayList;
import java.util.Comparator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyEventsScreen {

    private Stage stage;
    private ArrayList<Event> events;
    private Runnable backToDashboard;

    public MyEventsScreen(
            Stage stage,
            ArrayList<Event> events,
            Runnable backToDashboard) {

        this.stage = stage;
        this.events = events;
        this.backToDashboard = backToDashboard;
    }

    public void show() {

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label("MY EVENTS");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel =
                new Label(
                        "Search and sort your created events."
                );

        // =====================================================
        // SEARCH
        // =====================================================

        Label searchLabel =
                new Label("Search Events");

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search by name, description, or location"
        );

        searchField.setMaxWidth(400);

        // =====================================================
        // SORT
        // =====================================================

        Label sortLabel =
                new Label("Sort By");

        ComboBox<String> sortBox =
                new ComboBox<>();

        sortBox.getItems().addAll(
                "Name",
                "Date",
                "Time",
                "Cost"
        );

        sortBox.setPromptText(
                "Choose how to sort"
        );

        sortBox.setPrefWidth(250);

        // =====================================================
        // EVENT LIST
        // =====================================================

        VBox eventList =
                new VBox(15);

        eventList.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // METHOD USED TO REFRESH EVENTS
        // =====================================================

        Runnable refreshEvents = () -> {

            eventList.getChildren().clear();

            String searchText =
                    searchField
                            .getText()
                            .trim()
                            .toLowerCase();

            // Make a copy so we do not change
            // the original ArrayList order
            ArrayList<Event> displayedEvents =
                    new ArrayList<>(events);

            // =================================================
            // SORT EVENTS
            // =================================================

            String selectedSort =
                    sortBox.getValue();

            if (selectedSort != null) {

                if (selectedSort.equals("Name")) {

                    displayedEvents.sort(
                            Comparator.comparing(
                                    Event::getEventName,
                                    String.CASE_INSENSITIVE_ORDER
                            )
                    );

                } else if (selectedSort.equals("Date")) {

                    displayedEvents.sort(
                            Comparator.comparing(
                                    Event::getDate
                            )
                    );

                } else if (selectedSort.equals("Time")) {

                    displayedEvents.sort(
                            Comparator.comparing(
                                    Event::getTime,
                                    String.CASE_INSENSITIVE_ORDER
                            )
                    );

                } else if (selectedSort.equals("Cost")) {

                    displayedEvents.sort(
                            Comparator.comparingDouble(
                                    Event::getPrice
                            )
                    );
                }
            }

            // =================================================
            // SHOW MATCHING EVENTS
            // =================================================

            int matches = 0;

            for (Event event : displayedEvents) {

                String eventName =
                        event.getEventName();

                String description =
                        event.getDescription();

                String location =
                        event.getLocation();

                boolean matchesSearch =
                        searchText.isEmpty()
                        || eventName
                                .toLowerCase()
                                .contains(searchText)
                        || description
                                .toLowerCase()
                                .contains(searchText)
                        || location
                                .toLowerCase()
                                .contains(searchText);

                if (!matchesSearch) {
                    continue;
                }

                matches++;

                // =================================================
                // EVENT NAME
                // =================================================

                Label nameLabel =
                        new Label(
                                event.getEventName()
                        );

                nameLabel.setStyle(
                        "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                );

                // =================================================
                // DESCRIPTION
                // =================================================

                Label descriptionLabel =
                        new Label(
                                "Description: "
                                + event.getDescription()
                        );

                descriptionLabel.setWrapText(
                        true
                );

                // =================================================
                // LOCATION
                // =================================================

                Label locationLabel =
                        new Label(
                                "Location: "
                                + event.getLocation()
                        );

                // =================================================
                // DATE
                // =================================================

                Label dateLabel =
                        new Label(
                                "Date: "
                                + event.getDate()
                        );

                // =================================================
                // TIME
                // =================================================

                Label timeLabel =
                        new Label(
                                "Time: "
                                + event.getTime()
                        );

                // =================================================
                // PEOPLE
                // =================================================

                Label peopleLabel =
                        new Label(
                                "People: "
                                + event.getCapacity()
                        );

                // =================================================
                // COST
                // =================================================

                Label costLabel =
                        new Label(
                                String.format(
                                        "Cost: $%.2f",
                                        event.getPrice()
                                )
                        );

                // =================================================
                // EVENT CARD
                // =================================================

                VBox card =
                        new VBox(
                                5,
                                nameLabel,
                                descriptionLabel,
                                locationLabel,
                                dateLabel,
                                timeLabel,
                                peopleLabel,
                                costLabel
                        );

                card.setMaxWidth(
                        500
                );

                card.setPadding(
                        new Insets(15)
                );

                card.setStyle(
                        "-fx-border-color: lightgray;"
                        + "-fx-border-width: 1px;"
                        + "-fx-border-radius: 8px;"
                        + "-fx-background-radius: 8px;"
                );

                eventList
                        .getChildren()
                        .add(card);
            }

            // =================================================
            // NOTHING FOUND
            // =================================================

            if (events.isEmpty()) {

                Label noEventsLabel =
                        new Label(
                                "You have not created any events yet."
                        );

                noEventsLabel.setStyle(
                        "-fx-font-size: 16px;"
                );

                eventList
                        .getChildren()
                        .add(noEventsLabel);

            } else if (matches == 0) {

                Label noResultsLabel =
                        new Label(
                                "No events match your search."
                        );

                noResultsLabel.setStyle(
                        "-fx-font-size: 16px;"
                );

                eventList
                        .getChildren()
                        .add(noResultsLabel);
            }
        };

        // =====================================================
        // SEARCH WHILE USER TYPES
        // =====================================================

        searchField.textProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                refreshEvents.run()
                );

        // =====================================================
        // SORT WHEN OPTION CHANGES
        // =====================================================

        sortBox.setOnAction(
                action ->
                        refreshEvents.run()
        );

        // =====================================================
        // DISPLAY EVENTS FIRST TIME
        // =====================================================

        refreshEvents.run();

        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button backButton =
                new Button(
                        "Back to Dashboard"
                );

        backButton.setPrefSize(
                250,
                40
        );

        backButton.setOnAction(
                action ->
                        backToDashboard.run()
        );

        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        VBox layout =
                new VBox(
                        15,
                        titleLabel,
                        subtitleLabel,
                        searchLabel,
                        searchField,
                        sortLabel,
                        sortBox,
                        eventList,
                        backButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(30)
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(layout);

        scrollPane.setFitToWidth(
                true
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        scrollPane,
                        900,
                        650
                );

        stage.setScene(scene);
    }
}