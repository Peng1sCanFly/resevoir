package projectcsi.reservoir;

import java.util.ArrayList;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyReservationsScreen {

    private Stage stage;
    private ArrayList<Reservation> reservations;
    private Runnable backToDashboard;
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public MyReservationsScreen(
            Stage stage,
            ArrayList<Reservation> reservations,
            Runnable backToDashboard) {

        this.stage = stage;
        this.reservations = reservations;
        this.backToDashboard = backToDashboard;
    }

    public void show() {
        if (account == null) {
            showError("Please log in to view reservations.");
            backToDashboard.run();
            return;
        }

        try {
            ArrayList<Reservation> saved = ReservationDAO.getReservations(
                    account.getAccountId());
            reservations.clear();
            reservations.addAll(saved);
        } catch (SQLException e) {
            showError("Could not load reservations. Please try again.");
            backToDashboard.run();
            return;
        }


        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label("MY RESERVATIONS");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel =
                new Label(
                        "View or cancel your confirmed reservations."
                );

        // =====================================================
        // RESERVATION LIST
        // =====================================================

        VBox reservationList =
                new VBox(15);

        reservationList.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // NO RESERVATIONS
        // =====================================================

        if (reservations.isEmpty()) {

            Label noReservationsLabel =
                    new Label(
                            "You do not have any reservations yet."
                    );

            noReservationsLabel.setStyle(
                    "-fx-font-size: 16px;"
            );

            reservationList
                    .getChildren()
                    .add(noReservationsLabel);

        } else {

            // =================================================
            // SHOW RESERVATIONS
            // =================================================

            for (Reservation reservation : reservations) {

                Label eventNameLabel =
                        new Label(
                                reservation.getEventName()
                        );

                eventNameLabel.setStyle(
                        "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
                );

                Label locationLabel =
                        new Label(
                                "Location: "
                                + reservation.getLocation()
                        );

                Label dateLabel =
                        new Label(
                                "Date: "
                                + reservation.getDate()
                        );

                Label timeLabel =
                        new Label(
                                "Time: "
                                + reservation.getTime()
                                + (reservation.getEndTime() == null
                                   || reservation.getEndTime().isEmpty()
                                   ? "" : " - " + reservation.getEndTime())
                        );

                Label quantityLabel =
                        new Label(
                                "Tickets/People: "
                                + reservation.getNumberOfTickets()
                        );

                Label priceLabel =
                        new Label(
                                String.format(
                                        "Price Per Person/Ticket: $%.2f",
                                        reservation.getPricePerTicket()
                                )
                        );

                Label totalLabel =
                        new Label(
                                String.format(
                                        "Total: $%.2f",
                                        reservation.getTotalCost()
                                )
                        );

                totalLabel.setStyle(
                        "-fx-font-weight: bold;"
                );

                // =================================================
                // CANCEL RESERVATION BUTTON
                // =================================================

                Button cancelButton =
                        new Button(
                                "Cancel Reservation"
                        );

                cancelButton.setPrefSize(
                        200,
                        35
                );

                cancelButton.setStyle(
                        "-fx-font-weight: bold;"
                );

                cancelButton.setOnAction(action -> {

                    // Ask user before deleting reservation
                    Alert confirmation =
                            new Alert(
                                    Alert.AlertType.CONFIRMATION
                            );

                    confirmation.setTitle(
                            "Cancel Reservation"
                    );

                    confirmation.setHeaderText(
                            "Cancel "
                            + reservation.getEventName()
                            + "?"
                    );

                    confirmation.setContentText(
                            "Are you sure you want to cancel this reservation?"
                    );

                    confirmation.showAndWait()
                            .ifPresent(response -> {

                                if (response
                                        == ButtonType.OK) {

                                    try {
                                        boolean canceled = ReservationDAO.cancelReservation(
                                                account.getAccountId(),
                                                reservation.getReservationId());
                                        if (!canceled) {
                                            showError("Reservation no longer exists or cannot be canceled.");
                                        }
                                        show();
                                    } catch (SQLException e) {
                                        showError("Could not cancel reservation. Please try again.");
                                    }
                                }
                            });
                });

                // =================================================
                // RESERVATION CARD
                // =================================================

                VBox reservationCard =
                        new VBox(6);

                reservationCard.getChildren().addAll(
                        eventNameLabel,
                        locationLabel,
                        dateLabel,
                        timeLabel
                );

                // =================================================
                // ADD SECTION ONLY IF THERE IS ONE
                // =================================================

                if (reservation.hasSection()) {

                    Label sectionLabel =
                            new Label(
                                    "Section: "
                                    + reservation.getSection()
                            );

                    sectionLabel.setStyle(
                            "-fx-font-weight: bold;"
                    );

                    reservationCard
                            .getChildren()
                            .add(sectionLabel);
                }

                // =================================================
                // ADD REMAINING INFORMATION
                // =================================================

                reservationCard.getChildren().addAll(
                        quantityLabel,
                        priceLabel,
                        totalLabel,
                        cancelButton
                );

                reservationCard.setAlignment(
                        Pos.CENTER_LEFT
                );

                reservationCard.setMaxWidth(
                        550
                );

                reservationCard.setPadding(
                        new Insets(15)
                );

                reservationCard.setStyle(
                        "-fx-border-color: lightgray;"
                        + "-fx-border-width: 1px;"
                        + "-fx-border-radius: 8px;"
                        + "-fx-background-radius: 8px;"
                );

                reservationList
                        .getChildren()
                        .add(reservationCard);
            }
        }

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
                        20,
                        titleLabel,
                        subtitleLabel,
                        reservationList,
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

        scrollPane.setFitToWidth(true);

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        scrollPane,
                        900,
                        650
                );
        App.applyStylesheet(scene);
        stage.setScene(scene);
    }
}