package projectcsi.reservoir;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//This file may be erroneus
public class ReservationScreen {

    private Stage stage;
    private Event selectedEvent;
    private ArrayList<Reservation> reservations;
    private Runnable backToEvents;

    public ReservationScreen(
            Stage stage,
            Event selectedEvent,
            ArrayList<Reservation> reservations,
            Runnable backToEvents) {

        this.stage = stage;
        this.selectedEvent = selectedEvent;
        this.reservations = reservations;
        this.backToEvents = backToEvents;
    }

    public void show() {

        Label titleLabel =
                new Label("RESERVE EVENT");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label eventNameLabel =
                new Label(
                        selectedEvent.getEventName()
                );

        eventNameLabel.setStyle(
                "-fx-font-size: 22px;"
                + "-fx-font-weight: bold;"
        );

        Label descriptionLabel =
                new Label(
                        "Description: "
                        + selectedEvent.getDescription()
                );

        Label locationLabel =
                new Label(
                        "Location: "
                        + selectedEvent.getLocation()
                );

        Label dateLabel =
                new Label(
                        "Date: "
                        + selectedEvent.getDate()
                );

        Label timeLabel =
                new Label(
                        "Time: "
                        + selectedEvent.getTime()
                );

        Label priceLabel =
                new Label(
                        String.format(
                                "Cost Per Person: $%.2f",
                                selectedEvent.getPrice()
                        )
                );

        Label quantityLabel =
                new Label("Number of People");

        TextField quantityField =
                new TextField();

        quantityField.setPromptText(
                "Example: 2"
        );

        quantityField.setMaxWidth(250);

        Label totalLabel =
                new Label("Total: $0.00");

        totalLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        Label messageLabel =
                new Label();


        Button confirmButton =
                new Button("Confirm Reservation");

        confirmButton.setPrefSize(
                250,
                40
        );

        Button backButton =
                new Button("Back");

        backButton.setPrefSize(
                250,
                40
        );

        confirmButton.setOnAction(action -> {

            try {

                int quantity =
                        Integer.parseInt(
                                quantityField
                                        .getText()
                                        .trim()
                        );

                if (quantity <= 0) {

                    messageLabel.setText(
                            "Please enter at least 1 person."
                    );

                    return;
                }

                if (quantity
                        > selectedEvent.getCapacity()) {

                    messageLabel.setText(
                            "Not enough availability."
                    );

                    return;
                }

                Reservation reservation =
                        new Reservation(
                                selectedEvent.getEventName(),
                                selectedEvent.getLocation(),
                                selectedEvent.getDate(),
                                selectedEvent.getTime(),
                                quantity,
                                selectedEvent.getPrice()
                        );

                reservations.add(reservation);

                totalLabel.setText(
                        String.format(
                                "Total: $%.2f",
                                reservation.getTotalCost()
                        )
                );

                messageLabel.setText(
                        "Reservation confirmed!"
                );

                quantityField.setDisable(true);

                confirmButton.setDisable(true);

            } catch (NumberFormatException e) {

                messageLabel.setText(
                        "Please enter a valid number."
                );
            }
        });

        backButton.setOnAction(
                action -> backToEvents.run()
        );

        VBox layout =
                new VBox(
                        12,
                        titleLabel,
                        eventNameLabel,
                        descriptionLabel,
                        locationLabel,
                        dateLabel,
                        timeLabel,
                        priceLabel,
                        quantityLabel,
                        quantityField,
                        totalLabel,
                        confirmButton,
                        messageLabel,
                        backButton
                );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Scene scene =
                new Scene(
                        layout,
                        900,
                        650
                );

        stage.setScene(scene);
    }
}