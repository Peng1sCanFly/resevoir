package projectcsi.reservoir;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentScreen {

    private Stage stage;
    private String eventName;
    private double totalCost;
    private Runnable paymentSuccessAction;
    private Runnable backAction;

    public PaymentScreen(
            Stage stage,
            String eventName,
            double totalCost,
            Runnable paymentSuccessAction,
            Runnable backAction) {

        this.stage = stage;
        this.eventName = eventName;
        this.totalCost = totalCost;
        this.paymentSuccessAction = paymentSuccessAction;
        this.backAction = backAction;
    }

    public void show() {

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label("PAYMENT");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel =
                new Label(
                        "Complete your payment to confirm your reservation."
                );

        // =====================================================
        // EVENT INFORMATION
        // =====================================================

        Label eventLabel =
                new Label(
                        "Event: "
                        + eventName
                );

        eventLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        Label totalLabel =
                new Label(
                        String.format(
                                "Total: $%.2f",
                                totalCost
                        )
                );

        totalLabel.setStyle(
                "-fx-font-size: 20px;"
                + "-fx-font-weight: bold;"
        );

        // =====================================================
        // NAME ON CARD
        // =====================================================

        Label nameLabel =
                new Label("Name on Card");

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter name on card"
        );

        nameField.setMaxWidth(350);

        // =====================================================
        // CARD NUMBER
        // =====================================================

        Label cardNumberLabel =
                new Label("Card Number");

        TextField cardNumberField =
                new TextField();

        cardNumberField.setPromptText(
                "1234 5678 9012 3456"
        );

        cardNumberField.setMaxWidth(350);

        // =====================================================
        // EXPIRATION DATE
        // =====================================================

        Label expirationLabel =
                new Label("Expiration Date");

        TextField expirationField =
                new TextField();

        expirationField.setPromptText(
                "MM/YY"
        );

        expirationField.setMaxWidth(350);

        // =====================================================
        // CVV
        // =====================================================

        Label cvvLabel =
                new Label("CVV");

        PasswordField cvvField =
                new PasswordField();

        cvvField.setPromptText(
                "3 digits"
        );

        cvvField.setMaxWidth(350);

        // =====================================================
        // BILLING ZIP
        // =====================================================

        Label zipLabel =
                new Label("Billing ZIP Code");

        TextField zipField =
                new TextField();

        zipField.setPromptText(
                "Enter ZIP code"
        );

        zipField.setMaxWidth(350);

        // =====================================================
        // MESSAGE
        // =====================================================

        Label messageLabel =
                new Label();

        messageLabel.setWrapText(true);

        // =====================================================
        // PAY BUTTON
        // =====================================================

        Button payButton =
                new Button(
                        String.format(
                                "Pay $%.2f",
                                totalCost
                        )
                );

        payButton.setPrefSize(
                250,
                40
        );

        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button backButton =
                new Button(
                        "Back to Reservation"
                );

        backButton.setPrefSize(
                250,
                40
        );

        // =====================================================
        // PAY BUTTON ACTION
        // =====================================================

        payButton.setOnAction(action -> {

            String name =
                    nameField
                            .getText()
                            .trim();

            String cardNumber =
                    cardNumberField
                            .getText()
                            .replace(" ", "")
                            .replace("-", "")
                            .trim();

            String expiration =
                    expirationField
                            .getText()
                            .trim();

            String cvv =
                    cvvField
                            .getText()
                            .trim();

            String zip =
                    zipField
                            .getText()
                            .trim();

            // =================================================
            // CHECK EMPTY FIELDS
            // =================================================

            if (name.isEmpty()
                    || cardNumber.isEmpty()
                    || expiration.isEmpty()
                    || cvv.isEmpty()
                    || zip.isEmpty()) {

                messageLabel.setText(
                        "Please complete all payment fields."
                );

                return;
            }

            // =================================================
            // CARD NUMBER VALIDATION
            // =================================================

            if (!cardNumber.matches("\\d{16}")) {

                messageLabel.setText(
                        "Card number must contain 16 digits."
                );

                return;
            }

            // =================================================
            // EXPIRATION VALIDATION
            // =================================================

            if (!expiration.matches(
                    "(0[1-9]|1[0-2])/\\d{2}"
            )) {

                messageLabel.setText(
                        "Expiration date must use MM/YY."
                );

                return;
            }

            // =================================================
            // CVV VALIDATION
            // =================================================

            if (!cvv.matches("\\d{3}")) {

                messageLabel.setText(
                        "CVV must contain 3 digits."
                );

                return;
            }

            // =================================================
            // ZIP CODE VALIDATION
            // =================================================

            if (!zip.matches("\\d{5}")) {

                messageLabel.setText(
                        "ZIP code must contain 5 digits."
                );

                return;
            }

            // =================================================
            // MOCK PAYMENT SUCCESS
            // =================================================

            messageLabel.setText(
                    "Payment successful!"
            );

            // Disable fields after successful payment
            nameField.setDisable(true);
            cardNumberField.setDisable(true);
            expirationField.setDisable(true);
            cvvField.setDisable(true);
            zipField.setDisable(true);

            payButton.setDisable(true);
            backButton.setDisable(true);

            // Continue reservation process
            paymentSuccessAction.run();
        });

        // =====================================================
        // BACK BUTTON ACTION
        // =====================================================

        backButton.setOnAction(
                action ->
                        backAction.run()
        );

        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        VBox layout =
                new VBox(
                        9,
                        titleLabel,
                        subtitleLabel,

                        eventLabel,
                        totalLabel,

                        nameLabel,
                        nameField,

                        cardNumberLabel,
                        cardNumberField,

                        expirationLabel,
                        expirationField,

                        cvvLabel,
                        cvvField,

                        zipLabel,
                        zipField,

                        payButton,
                        messageLabel,
                        backButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(30)
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        layout,
                        900,
                        700
                );

        stage.setScene(scene);
    }
}