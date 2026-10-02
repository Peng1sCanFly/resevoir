package projectcsi.reservoir;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryEventsScreen {

    private Stage stage;
    private ArrayList<Event> events;
    private ArrayList<Reservation> reservations;
    private ArrayList<Favorite> favorites;
    private String category;
    private Runnable backToEvents;

    // Remember form information
    private String savedType = "";
    private String savedEventName = "";
    private String savedLocation = "";
    private String savedDate = "";
    private String savedTime = "";
    private String savedQuantity = "";
    private String savedCost = "";

    // Selected seating section
    private String selectedSection = "";

    public CategoryEventsScreen(
            Stage stage,
            ArrayList<Event> events,
            ArrayList<Reservation> reservations,
            ArrayList<Favorite> favorites,
            String category,
            Runnable backToEvents) {

        this.stage = stage;
        this.events = events;
        this.reservations = reservations;
        this.favorites = favorites;
        this.category = category;
        this.backToEvents = backToEvents;
    }

    public void show() {

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label(category.toUpperCase());

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel =
                new Label(
                        "Enter what you are looking for."
                );

        // =====================================================
        // TYPE
        // =====================================================

        Label typeLabel =
                new Label("Type");

        ComboBox<String> typeBox =
                new ComboBox<>();

        typeBox.setPrefWidth(350);

        // =====================================================
        // EVENT NAME
        // =====================================================

        Label nameLabel =
                new Label("Event Name");

        TextField nameField =
                new TextField();

        nameField.setMaxWidth(350);

        // =====================================================
        // LOCATION
        // =====================================================

        Label locationLabel =
                new Label("Location");

        TextField locationField =
                new TextField();

        locationField.setMaxWidth(350);

        // =====================================================
        // CATEGORY OPTIONS
        // =====================================================

        if (category.equals("Restaurants")) {

            typeLabel.setText("Restaurant Type");

            typeBox.getItems().addAll(
                    "American & Comfort",
                    "Italian & Pizzeria",
                    "Mexican & Latin American",
                    "Asian",
                    "Mediterranean & Greek"
            );

            typeBox.setPromptText(
                    "Choose restaurant type"
            );

            nameLabel.setText(
                    "Restaurant Name"
            );

            nameField.setPromptText(
                    "Type restaurant name"
            );

            locationField.setPromptText(
                    "Type restaurant location"
            );

        } else if (category.equals("Concerts")) {

            typeLabel.setText("Music Genre");

            typeBox.getItems().addAll(
                    "Rock & Pop",
                    "Electronic",
                    "Hip-Hop",
                    "R&B",
                    "Country",
                    "Indie"
            );

            typeBox.setPromptText(
                    "Choose music genre"
            );

            nameLabel.setText(
                    "Concert / Artist"
            );

            nameField.setPromptText(
                    "Type concert or artist"
            );

            locationField.setPromptText(
                    "Type arena or concert location"
            );

        } else if (category.equals("Sports")) {

            typeLabel.setText("Sport");

            typeBox.getItems().addAll(
                    "Football",
                    "FIFA / Soccer",
                    "Basketball",
                    "Baseball",
                    "Hockey"
            );

            typeBox.setPromptText(
                    "Choose sport"
            );

            nameLabel.setText(
                    "Sporting Event"
            );

            nameField.setPromptText(
                    "Type sporting event"
            );

            locationField.setPromptText(
                    "Type stadium or arena"
            );

        } else if (category.equals("Movies")) {

            typeLabel.setText("Movie Genre");

            typeBox.getItems().addAll(
                    "Comedy",
                    "Drama / Sad",
                    "Romance",
                    "Adventure",
                    "Sci-Fi",
                    "Documentary"
            );

            typeBox.setPromptText(
                    "Choose movie genre"
            );

            nameLabel.setText("Movie");

            nameField.setPromptText(
                    "Type movie name"
            );

            locationField.setPromptText(
                    "Type movie theater"
            );

        } else if (category.equals(
                "Community Events")) {

            typeLabel.setText(
                    "Community Event Type"
            );

            typeBox.getItems().addAll(
                    "Festival",
                    "Parade",
                    "Fair / Expo",
                    "Charity / Fundraiser",
                    "Cultural Event",
                    "Community Gathering",
                    "Other"
            );

            typeBox.setPromptText(
                    "Choose event type"
            );

            nameLabel.setText(
                    "Community Event"
            );

            nameField.setPromptText(
                    "Type community event"
            );

            locationField.setPromptText(
                    "Type event location"
            );
        }

        // =====================================================
        // DATE
        // =====================================================

        Label dateLabel =
                new Label("Date");

        DatePicker datePicker =
                new DatePicker();

        datePicker.setPromptText(
                "Choose a date"
        );

        datePicker.setPrefWidth(350);

        // =====================================================
        // TIME
        // =====================================================

        Label timeLabel =
                new Label("Time");

        TextField timeField =
                new TextField();

        timeField.setPromptText(
                "Example: 7:00 PM"
        );

        timeField.setMaxWidth(350);

        // =====================================================
        // PEOPLE / TICKETS
        // =====================================================

        Label quantityLabel =
                new Label();

        TextField quantityField =
                new TextField();

        quantityField.setMaxWidth(350);

        if (category.equals("Restaurants")) {

            quantityLabel.setText(
                    "How Many People"
            );

            quantityField.setPromptText(
                    "Example: 4"
            );

        } else {

            quantityLabel.setText(
                    "How Many Tickets"
            );

            quantityField.setPromptText(
                    "Example: 2"
            );
        }

        // =====================================================
        // COST
        // =====================================================

        Label costLabel =
                new Label(
                        "Cost Per Person / Ticket"
                );

        TextField costField =
                new TextField();

        costField.setPromptText(
                "Example: 25.00"
        );

        costField.setMaxWidth(350);

        // =====================================================
        // RESTORE SAVED INFORMATION
        // =====================================================

        if (!savedType.isEmpty()) {
            typeBox.setValue(savedType);
        }

        nameField.setText(savedEventName);
        locationField.setText(savedLocation);
        timeField.setText(savedTime);
        quantityField.setText(savedQuantity);
        costField.setText(savedCost);

        if (!savedDate.isEmpty()) {

            datePicker.setValue(
                    java.time.LocalDate.parse(
                            savedDate
                    )
            );
        }

        // =====================================================
        // SEATING
        // =====================================================

        Label seatingLabel =
                new Label();

        if (selectedSection.isEmpty()) {

            seatingLabel.setText(
                    "Selected Section: None"
            );

        } else {

            seatingLabel.setText(
                    "Selected Section: "
                    + selectedSection
            );
        }

        seatingLabel.setStyle(
                "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
        );

        Button seatingButton =
                new Button(
                        "Choose Seating Section"
                );

        seatingButton.setPrefSize(
                250,
                40
        );

        seatingButton.setVisible(false);
        seatingButton.setManaged(false);

        seatingLabel.setVisible(false);
        seatingLabel.setManaged(false);

        // =====================================================
        // TYPE CHANGE
        // =====================================================

        typeBox.setOnAction(action -> {

            String selectedType =
                    typeBox.getValue();

            boolean showSeating = false;

            if (category.equals("Concerts")) {
                showSeating = true;
            }

            if (category.equals("Sports")
                    && selectedType != null
                    && selectedType.equals("Football")) {

                showSeating = true;
            }

            seatingButton.setVisible(showSeating);
            seatingButton.setManaged(showSeating);

            seatingLabel.setVisible(showSeating);
            seatingLabel.setManaged(showSeating);

            if (category.equals("Sports")
                    && selectedType != null
                    && !selectedType.equals("Football")) {

                selectedSection = "";

                seatingLabel.setText(
                        "Selected Section: None"
                );
            }
        });

        // =====================================================
        // RESTORE SEATING VISIBILITY
        // =====================================================

        if (!savedType.isEmpty()) {

            boolean showSeating = false;

            if (category.equals("Concerts")) {
                showSeating = true;
            }

            if (category.equals("Sports")
                    && savedType.equals("Football")) {

                showSeating = true;
            }

            seatingButton.setVisible(showSeating);
            seatingButton.setManaged(showSeating);

            seatingLabel.setVisible(showSeating);
            seatingLabel.setManaged(showSeating);
        }

        // =====================================================
        // TOTAL
        // =====================================================

        Label totalLabel =
                new Label("Total: $0.00");

        totalLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        Label messageLabel =
                new Label();

        // =====================================================
        // BUTTONS
        // =====================================================

        Button calculateButton =
                new Button(
                        "Calculate Total"
                );

        calculateButton.setPrefSize(
                250,
                40
        );

        Button paymentButton =
                new Button(
                        "Continue to Payment"
                );

        paymentButton.setPrefSize(
                250,
                40
        );

        Button favoriteButton =
                new Button(
                        "Add to Favorites"
                );

        favoriteButton.setPrefSize(
                250,
                40
        );

        Button backButton =
                new Button(
                        "Back to Events"
                );

        backButton.setPrefSize(
                250,
                40
        );

        // =====================================================
        // CHOOSE SEATING
        // =====================================================

        seatingButton.setOnAction(action -> {

            String selectedType =
                    typeBox.getValue();

            if (selectedType == null) {

                messageLabel.setText(
                        "Please choose a type first."
                );

                return;
            }

            // Save everything before leaving this screen
            savedType = selectedType;

            savedEventName =
                    nameField
                            .getText()
                            .trim();

            savedLocation =
                    locationField
                            .getText()
                            .trim();

            if (datePicker.getValue() != null) {

                savedDate =
                        datePicker
                                .getValue()
                                .toString();
            }

            savedTime =
                    timeField
                            .getText()
                            .trim();

            savedQuantity =
                    quantityField
                            .getText()
                            .trim();

            savedCost =
                    costField
                            .getText()
                            .trim();

            String seatingType;

            if (category.equals("Concerts")) {

                seatingType = "Concert";

            } else {

                seatingType = "Football";
            }

            SeatingSelectionScreen screen =
                    new SeatingSelectionScreen(
                            stage,
                            seatingType,

                            section -> {

                                selectedSection =
                                        section;

                                show();
                            },

                            this::show
                    );

            screen.show();
        });

        // =====================================================
        // CALCULATE TOTAL
        // =====================================================

        calculateButton.setOnAction(action -> {

            try {

                int quantity =
                        Integer.parseInt(
                                quantityField
                                        .getText()
                                        .trim()
                        );

                double cost =
                        Double.parseDouble(
                                costField
                                        .getText()
                                        .trim()
                        );

                if (quantity <= 0) {

                    messageLabel.setText(
                            "Please enter at least 1 person/ticket."
                    );

                    return;
                }

                if (cost < 0) {

                    messageLabel.setText(
                            "Cost cannot be negative."
                    );

                    return;
                }

                double total =
                        quantity * cost;

                totalLabel.setText(
                        String.format(
                                "Total: $%.2f",
                                total
                        )
                );

                messageLabel.setText("");

            } catch (NumberFormatException e) {

                messageLabel.setText(
                        "Please enter valid numbers."
                );
            }
        });

        // =====================================================
        // CONTINUE TO PAYMENT
        // =====================================================

        paymentButton.setOnAction(action -> {

            String selectedType =
                    typeBox.getValue();

            String eventName =
                    nameField
                            .getText()
                            .trim();

            String location =
                    locationField
                            .getText()
                            .trim();

            String time =
                    timeField
                            .getText()
                            .trim();

            String quantityText =
                    quantityField
                            .getText()
                            .trim();

            String costText =
                    costField
                            .getText()
                            .trim();

            // =================================================
            // CHECK REQUIRED FIELDS
            // =================================================

            if (selectedType == null
                    || eventName.isEmpty()
                    || location.isEmpty()
                    || datePicker.getValue() == null
                    || time.isEmpty()
                    || quantityText.isEmpty()
                    || costText.isEmpty()) {

                messageLabel.setText(
                        "Please complete all fields."
                );

                return;
            }

            // =================================================
            // CONCERT SEATING REQUIRED
            // =================================================

            if (category.equals("Concerts")
                    && selectedSection.isEmpty()) {

                messageLabel.setText(
                        "Please choose a seating section."
                );

                return;
            }

            // =================================================
            // FOOTBALL SEATING REQUIRED
            // =================================================

            if (category.equals("Sports")
                    && selectedType.equals("Football")
                    && selectedSection.isEmpty()) {

                messageLabel.setText(
                        "Please choose a seating section."
                );

                return;
            }

            try {

                int quantity =
                        Integer.parseInt(
                                quantityText
                        );

                double cost =
                        Double.parseDouble(
                                costText
                        );

                if (quantity <= 0) {

                    messageLabel.setText(
                            "Please enter at least 1 person/ticket."
                    );

                    return;
                }

                if (cost < 0) {

                    messageLabel.setText(
                            "Cost cannot be negative."
                    );

                    return;
                }

                String date =
                        datePicker
                                .getValue()
                                .toString();

                // =================================================
                // DOUBLE-BOOKING PREVENTION
                // =================================================

                if (alreadyReserved(
                        eventName,
                        location,
                        date,
                        time)) {

                    messageLabel.setText(
                            "You already have a reservation for this event."
                    );

                    return;
                }

                double total =
                        quantity * cost;

                // Show total before leaving
                totalLabel.setText(
                        String.format(
                                "Total: $%.2f",
                                total
                        )
                );

                // =================================================
                // SAVE FORM INFORMATION
                // =================================================

                savedType =
                        selectedType;

                savedEventName =
                        eventName;

                savedLocation =
                        location;

                savedDate =
                        date;

                savedTime =
                        time;

                savedQuantity =
                        quantityText;

                savedCost =
                        costText;

                // =================================================
                // CREATE RESERVATION OBJECT
                // BUT DO NOT ADD IT YET
                // =================================================

                Reservation pendingReservation;

                if (!selectedSection.isEmpty()) {

                    pendingReservation =
                            new Reservation(
                                    eventName,
                                    location,
                                    date,
                                    time,
                                    quantity,
                                    cost,
                                    selectedSection
                            );

                } else {

                    pendingReservation =
                            new Reservation(
                                    eventName,
                                    location,
                                    date,
                                    time,
                                    quantity,
                                    cost
                            );
                }

                // =================================================
                // OPEN PAYMENT SCREEN
                // =================================================

                PaymentScreen paymentScreen =
                        new PaymentScreen(
                                stage,
                                eventName,
                                total,

                                // PAYMENT SUCCESS
                                () -> {

                                    reservations.add(
                                            pendingReservation
                                    );

                                    showPaymentSuccessScreen(
                                            pendingReservation
                                    );
                                },

                                // BACK TO RESERVATION
                                this::show
                        );

                paymentScreen.show();

            } catch (NumberFormatException e) {

                messageLabel.setText(
                        "Please enter valid numbers."
                );
            }
        });

        // =====================================================
        // ADD TO FAVORITES
        // =====================================================

        favoriteButton.setOnAction(action -> {

            String eventName =
                    nameField
                            .getText()
                            .trim();

            String location =
                    locationField
                            .getText()
                            .trim();

            String time =
                    timeField
                            .getText()
                            .trim();

            String costText =
                    costField
                            .getText()
                            .trim();

            if (typeBox.getValue() == null
                    || eventName.isEmpty()
                    || location.isEmpty()
                    || datePicker.getValue() == null
                    || time.isEmpty()
                    || costText.isEmpty()) {

                messageLabel.setText(
                        "Complete the event information before adding to favorites."
                );

                return;
            }

            try {

                double cost =
                        Double.parseDouble(
                                costText
                        );

                if (cost < 0) {

                    messageLabel.setText(
                            "Cost cannot be negative."
                    );

                    return;
                }

                String date =
                        datePicker
                                .getValue()
                                .toString();

                Favorite favorite =
                        new Favorite(
                                eventName,
                                location,
                                date,
                                time,
                                cost
                        );

                favorites.add(
                        favorite
                );

                messageLabel.setText(
                        "Added to favorites!"
                );

                favoriteButton.setDisable(
                        true
                );

                favoriteButton.setText(
                        "Added to Favorites"
                );

            } catch (NumberFormatException e) {

                messageLabel.setText(
                        "Please enter a valid cost."
                );
            }
        });

        // =====================================================
        // BACK
        // =====================================================

        backButton.setOnAction(
                action ->
                        backToEvents.run()
        );

        // =====================================================
        // LAYOUT
        // =====================================================

        VBox layout =
                new VBox(
                        8,
                        titleLabel,
                        subtitleLabel,

                        typeLabel,
                        typeBox,

                        nameLabel,
                        nameField,

                        locationLabel,
                        locationField,

                        dateLabel,
                        datePicker,

                        timeLabel,
                        timeField,

                        quantityLabel,
                        quantityField,

                        costLabel,
                        costField,

                        seatingButton,
                        seatingLabel,

                        calculateButton,
                        totalLabel,

                        paymentButton,
                        favoriteButton,

                        messageLabel,
                        backButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(25)
        );

        Scene scene =
                new Scene(
                        layout,
                        900,
                        800
                );

        stage.setScene(scene);
    }

    // =========================================================
    // CHECK FOR EXISTING RESERVATION
    // =========================================================

    private boolean alreadyReserved(
            String eventName,
            String location,
            String date,
            String time) {

        for (Reservation reservation : reservations) {

            boolean sameEvent =
                    reservation
                            .getEventName()
                            .equalsIgnoreCase(
                                    eventName
                            );

            boolean sameLocation =
                    reservation
                            .getLocation()
                            .equalsIgnoreCase(
                                    location
                            );

            boolean sameDate =
                    reservation
                            .getDate()
                            .equals(
                                    date
                            );

            boolean sameTime =
                    reservation
                            .getTime()
                            .equalsIgnoreCase(
                                    time
                            );

            if (sameEvent
                    && sameLocation
                    && sameDate
                    && sameTime) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // PAYMENT SUCCESS SCREEN
    // =========================================================

    private void showPaymentSuccessScreen(
            Reservation reservation) {

        Label titleLabel =
                new Label(
                        "PAYMENT SUCCESSFUL!"
                );

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label confirmationLabel =
                new Label(
                        "Your reservation has been confirmed."
                );

        confirmationLabel.setStyle(
                "-fx-font-size: 18px;"
        );

        Label eventLabel =
                new Label(
                        "Event: "
                        + reservation.getEventName()
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
                );

        Label quantityLabel =
                new Label(
                        "Tickets/People: "
                        + reservation.getNumberOfTickets()
                );

        Label totalLabel =
                new Label(
                        String.format(
                                "Total Paid: $%.2f",
                                reservation.getTotalCost()
                        )
                );

        totalLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        VBox informationBox =
                new VBox(
                        8,
                        eventLabel,
                        locationLabel,
                        dateLabel,
                        timeLabel
                );

        informationBox.setAlignment(
                Pos.CENTER
        );

        // Show seating section only when applicable
        if (reservation.hasSection()) {

            Label sectionLabel =
                    new Label(
                            "Section: "
                            + reservation.getSection()
                    );

            sectionLabel.setStyle(
                    "-fx-font-weight: bold;"
            );

            informationBox
                    .getChildren()
                    .add(sectionLabel);
        }

        informationBox
                .getChildren()
                .addAll(
                        quantityLabel,
                        totalLabel
                );

        Button doneButton =
                new Button(
                        "Done"
                );

        doneButton.setPrefSize(
                250,
                40
        );

        doneButton.setOnAction(action -> {

            clearSavedInformation();

            backToEvents.run();
        });

        VBox layout =
                new VBox(
                        20,
                        titleLabel,
                        confirmationLabel,
                        informationBox,
                        doneButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(30)
        );

        Scene scene =
                new Scene(
                        layout,
                        900,
                        650
                );

        stage.setScene(scene);
    }

    // =========================================================
    // CLEAR FORM AFTER SUCCESSFUL RESERVATION
    // =========================================================

    private void clearSavedInformation() {

        savedType = "";
        savedEventName = "";
        savedLocation = "";
        savedDate = "";
        savedTime = "";
        savedQuantity = "";
        savedCost = "";
        selectedSection = "";
    }
}