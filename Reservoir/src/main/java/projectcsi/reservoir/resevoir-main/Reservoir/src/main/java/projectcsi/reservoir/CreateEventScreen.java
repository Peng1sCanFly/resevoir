package projectcsi.reservoir;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateEventScreen {

    private Stage stage;
    private ArrayList<Event> events;
    private Runnable backToDashboard;

    public CreateEventScreen(
            Stage stage,
            ArrayList<Event> events,
            Runnable backToDashboard) {

        this.stage = stage;
        this.events = events;
        this.backToDashboard = backToDashboard;
    }

    public void show() {

        Label titleLabel =
                new Label("CREATE EVENT");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel =
                new Label(
                        "Enter the information for your event."
                );

        // EVENT NAME
        Label eventNameLabel =
                new Label("Event Name");

        TextField eventNameField =
                new TextField();

        eventNameField.setPromptText(
                "Enter event name"
        );

        eventNameField.setMaxWidth(350);

        // DESCRIPTION
        Label descriptionLabel =
                new Label("Description");

        TextArea descriptionArea =
                new TextArea();

        descriptionArea.setPromptText(
                "Enter event description"
        );

        descriptionArea.setMaxWidth(350);
        descriptionArea.setPrefHeight(80);
        descriptionArea.setWrapText(true);

        // LOCATION
        Label locationLabel =
                new Label("Location");

        TextField locationField =
                new TextField();

        locationField.setPromptText(
                "Enter event location"
        );

        locationField.setMaxWidth(350);

        // DATE
        Label dateLabel =
                new Label("Date");

        DatePicker datePicker =
                new DatePicker();

        datePicker.setPromptText(
                "Choose a date"
        );

        datePicker.setPrefWidth(350);

        // TIME
        Label timeLabel =
                new Label("Time");

        TextField timeField =
                new TextField();

        timeField.setPromptText(
                "Example: 7:00 PM"
        );

        timeField.setMaxWidth(350);

        // HOW MANY PEOPLE
        Label capacityLabel =
                new Label("How Many People");

        TextField capacityField =
                new TextField();

        capacityField.setPromptText(
                "Enter number of people"
        );

        capacityField.setMaxWidth(350);

        // COST
        Label priceLabel =
                new Label("Cost");

        TextField priceField =
                new TextField();

        priceField.setPromptText(
                "Example: 25.00"
        );

        priceField.setMaxWidth(350);

        // MESSAGE
        Label messageLabel =
                new Label();

        // CREATE BUTTON
        Button createButton =
                new Button("Create Event");

        createButton.setPrefSize(
                250,
                40
        );

        // BACK BUTTON
        Button backButton =
                new Button("Back to Dashboard");

        backButton.setPrefSize(
                250,
                40
        );

        // CREATE EVENT
        createButton.setOnAction(action -> {

            String eventName =
                    eventNameField.getText().trim();

            String description =
                    descriptionArea.getText().trim();

            String location =
                    locationField.getText().trim();

            String time =
                    timeField.getText().trim();

            String capacityText =
                    capacityField.getText().trim();

            String priceText =
                    priceField.getText().trim();

            if (eventName.isEmpty()
                    || description.isEmpty()
                    || location.isEmpty()
                    || datePicker.getValue() == null
                    || time.isEmpty()
                    || capacityText.isEmpty()
                    || priceText.isEmpty()) {

                messageLabel.setText(
                        "Please complete all fields."
                );

                return;
            }

            try {

                int capacity =
                        Integer.parseInt(
                                capacityText
                        );

                double price =
                        Double.parseDouble(
                                priceText
                        );

                if (capacity <= 0) {

                    messageLabel.setText(
                            "Number of people must be greater than 0."
                    );

                    return;
                }

                if (price < 0) {

                    messageLabel.setText(
                            "Cost cannot be negative."
                    );

                    return;
                }

                String date =
                        datePicker
                                .getValue()
                                .toString();

                Event newEvent =
                        new Event(
                                eventName,
                                description,
                                location,
                                date,
                                time,
                                capacity,
                                price
                        );

                events.add(newEvent);

                messageLabel.setText(
                        "Event created successfully!"
                );

                eventNameField.clear();
                descriptionArea.clear();
                locationField.clear();
                datePicker.setValue(null);
                timeField.clear();
                capacityField.clear();
                priceField.clear();

            } catch (NumberFormatException e) {

                messageLabel.setText(
                        "Number of people and cost must be numbers."
                );
            }
        });

        backButton.setOnAction(
                action -> backToDashboard.run()
        );

        VBox layout =
                new VBox(
                        8,
                        titleLabel,
                        subtitleLabel,
                        eventNameLabel,
                        eventNameField,
                        descriptionLabel,
                        descriptionArea,
                        locationLabel,
                        locationField,
                        dateLabel,
                        datePicker,
                        timeLabel,
                        timeField,
                        capacityLabel,
                        capacityField,
                        priceLabel,
                        priceField,
                        createButton,
                        messageLabel,
                        backButton
                );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));

        Scene scene =
                new Scene(
                        layout,
                        900,
                        700
                );

        stage.setScene(scene);
    }
}