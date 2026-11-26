package projectcsi.reservoir;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.UUID;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class App extends Application {

        private Stage stage;
        private Account currentAccount;

        public Account getCurrentAccount() {
                return currentAccount;
        }

        private ArrayList<Event> events = new ArrayList<>();

        private ArrayList<Reservation> reservations = new ArrayList<>();

        private ArrayList<Favorite> favorites = new ArrayList<>();

        private static App instance;

        public static App getInstance() {
                return instance;
        }

        @Override
        public void start(Stage stage) {
                instance = this;
                this.stage = stage;

                stage.setTitle("Reservoir");

                // Automatically apply CSS to every new scene.
                stage.sceneProperty().addListener(
                                (observable, oldScene, newScene) -> {
                                        if (newScene != null) {
                                                applyStylesheet(newScene);
                                        }
                                });

                showWelcomeScreen();
                stage.show();
        }
        // =====================================================
        // Stylesheet
        // =====================================================

        public static void applyStylesheet(Scene scene) {

                try {
                        java.net.URL cssResource = App.class.getResource("/projectcsi/reservoir/reservoir.css");

                        String stylesheet = cssResource.toExternalForm();

                        // Apply stylesheet if it has not already been applied.
                        if (!scene.getStylesheets().contains(stylesheet)) {
                                scene.getStylesheets().add(stylesheet);
                        }
                } catch (Exception e) {
                        System.err.println("Failed to load CSS:");
                        e.printStackTrace();

                }
        }

        // =====================================================
        // HOME
        // =====================================================

        private void showWelcomeScreen() {

                Label title = new Label("RESERVOIR");
                title.getStyleClass().add("welcome-title");

                Label subtitle = new Label("All Your Plans. One Place.");

                Label description = new Label("Plan, reserve, and manage your events all in one place.");

                Button loginButton = new Button("Login");
                loginButton.getStyleClass().add("action-button");

                Button createAccountButton = new Button("Create Account");
                createAccountButton.getStyleClass().add("action-button");

                loginButton.setOnAction(action -> showLoginScreen());

                createAccountButton.setOnAction(action -> showCreateAccountScreen());

                VBox layout = new VBox(
                                title,
                                subtitle,
                                description,
                                loginButton,
                                createAccountButton);
                layout.getStyleClass().add("welcome-layout");

                stage.setScene(new Scene(layout, 900, 600));
        }

        // =====================================================
        // LOGIN
        // =====================================================

        private void showLoginScreen() {
                // Create the login form
                Label title = new Label("LOGIN");
                title.getStyleClass().add("page-title");

                TextField emailField = new TextField();
                // Set prompt text and style class for the email field
                emailField.setPromptText("Email");
                emailField.getStyleClass().add("form-field");

                PasswordField passwordField = new PasswordField();
                // Set prompt text and style class for the password field
                passwordField.setPromptText("Password");
                passwordField.getStyleClass().add("form-field");
                // Create a label for displaying messages to the user
                Label messageLabel = new Label();
                messageLabel.getStyleClass().add("status-message");
                // Create the login and back buttons
                Button loginButton = new Button("Login");
                loginButton.getStyleClass().add("action-button");
                // Create the back button to return to the home screen
                Button backButton = new Button("Back to Home");
                backButton.getStyleClass().add("navigation-button");

                loginButton.setOnAction(action -> {

                        if (emailField.getText().trim().isEmpty() || passwordField.getText().isEmpty()) {

                                messageLabel.setText("Please enter your email and password.");

                        } else {
                                try {
                                        Account account = AccountDAO.login(emailField.getText(),
                                                        passwordField.getText());
                                        if (account == null) {
                                                messageLabel.setText("Invalid email or password.");
                                                passwordField.clear();
                                                return;
                                        }
                                        currentAccount = account;
                                        passwordField.clear();
                                        showDashboard();
                                } catch (SQLException | IllegalStateException e) {
                                        messageLabel.setText("Login unavailable. Please try again.");
                                }
                        }
                });

                backButton.setOnAction(action -> showWelcomeScreen());

                VBox layout = new VBox(
                                title,
                                emailField,
                                passwordField,
                                loginButton,
                                messageLabel,
                                backButton);
                layout.getStyleClass().add("login-layout");

                stage.setScene(new Scene(layout, 900, 600));
        }

        // =====================================================
        // CREATE ACCOUNT
        // =====================================================

        private void showCreateAccountScreen() {

                Label title = new Label("CREATE ACCOUNT");
                title.getStyleClass().add("page-title");

                TextField firstName = new TextField();

                firstName.setPromptText("First Name");
                firstName.getStyleClass().add("form-field");

                TextField lastName = new TextField();

                lastName.setPromptText("Last Name");
                lastName.getStyleClass().add("form-field");

                TextField email = new TextField();

                email.setPromptText("Email");
                email.getStyleClass().add("form-field");

                PasswordField password = new PasswordField();

                password.setPromptText("Password");
                password.getStyleClass().add("form-field");

                PasswordField confirmPassword = new PasswordField();

                confirmPassword.setPromptText("Confirm Password");
                confirmPassword.getStyleClass().add("form-field");

                Label messageLabel = new Label();
                messageLabel.getStyleClass().add("status-message");

                Button createButton = new Button("Create Account");
                createButton.getStyleClass().add("action-button");

                Button backButton = new Button("Back to Home");
                backButton.getStyleClass().add("navigation-button");

                createButton.setOnAction(action -> {
                        if (firstName.getText().trim().isEmpty()
                                        || lastName.getText().trim().isEmpty()
                                        || email.getText().trim().isEmpty()
                                        || password.getText().isEmpty()
                                        || confirmPassword.getText().isEmpty()) {

                                messageLabel.setText("Please complete all fields.");
                                return;
                        }

                        if (!password.getText().equals(confirmPassword.getText())) {
                                messageLabel.setText("Passwords do not match.");
                                return;
                        }

                        if (!email.getText().trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
                                messageLabel.setText("Please enter a valid email address.");
                                return;
                        }
                        if (password.getText().length() < 5) {
                                messageLabel.setText("Use a password of at least 5 characters.");
                                return;
                        }
                        try {
                                boolean created = AccountDAO.createAccount(
                                                firstName.getText(), lastName.getText(),
                                                email.getText(), password.getText());
                                if (created) {
                                        messageLabel.setText("Account created successfully! Return home to log in.");
                                        password.clear();
                                        confirmPassword.clear();
                                } else {
                                        messageLabel.setText("An account with this email already exists.");
                                }
                        } catch (SQLException | IllegalStateException e) {
                                messageLabel.setText(
                                                "Account creation unavailable. Please try again." + e.getMessage());
                        }
                });

                backButton.setOnAction(action -> showWelcomeScreen());

                VBox layout = new VBox(
                                title,
                                firstName,
                                lastName,
                                email,
                                password,
                                confirmPassword,
                                createButton,
                                messageLabel,
                                backButton);
                layout.getStyleClass().add("create-account-layout");

                stage.setScene(new Scene(layout, 900, 650));
        }

        // =====================================================
        // DASHBOARD
        // =====================================================

        private void showDashboard() {

                Label title = new Label("RESERVOIR");
                title.getStyleClass().add("dashboard-title");

                Label welcome = new Label("Welcome, " + currentAccount.getFirstName() + "!");

                Button eventsButton = new Button("Join Local Events");
                eventsButton.getStyleClass().add("dashboard-button");

                Button createEventButton = new Button("Create Event");
                createEventButton.getStyleClass().add("dashboard-button");

                Button reservationsButton = new Button("My Reservations");
                reservationsButton.getStyleClass().add("dashboard-button");

                Button myEventsButton = new Button("My Events");
                myEventsButton.getStyleClass().add("dashboard-button");

                Button favoritesButton = new Button("My Favorites");
                favoritesButton.getStyleClass().add("dashboard-button");

                Button logoutButton = new Button("Logout");
                logoutButton.getStyleClass().add("dashboard-button");

                // ====================
                // Calendar
                // ====================
                java.time.YearMonth currentMonth = java.time.YearMonth.now();
                java.time.LocalDate today = java.time.LocalDate.now();

                GridPane calendar = new GridPane();
                calendar.getStyleClass().add("calendar-grid");

                // Month heading
                Label monthLabel = new Label(
                                currentMonth.getMonth().toString() + " " + currentMonth.getYear());
                monthLabel.getStyleClass().add("calendar-month");

                // Weekday headings
                String[] weekdays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

                for (int col = 0; col < 7; col++) {
                        Label weekday = new Label(weekdays[col]);
                        weekday.getStyleClass().add("calendar-weekday");
                        calendar.add(weekday, col, 0);
                }

                // Find the first weekday of the month
                int startDay = currentMonth.atDay(1).getDayOfWeek().getValue() % 7;

                // Add the numbered days
                for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {

                        java.time.LocalDate date = currentMonth.atDay(day);

                        Label dateLabel = new Label(String.valueOf(day));

                        dateLabel.getStyleClass().add("calendar-day");
                        if (date.equals(today)) {
                                dateLabel.getStyleClass().add("today");
                        }

                        int index = startDay + day - 1;

                        calendar.add(
                                        dateLabel,
                                        index % 7,
                                        (index / 7) + 1);
                }

                // Calendar layout
                VBox calendarBox = new VBox(monthLabel, calendar);
                calendarBox.getStyleClass().add("calendar-container");

                // EVENTS
                eventsButton.setOnAction(action -> showEventsScreen());

                // CREATE EVENT
                createEventButton.setOnAction(action -> {
                        CreateEventScreen screen = new CreateEventScreen(
                                        stage,
                                        events,
                                        this::showDashboard);

                        screen.show();
                });

                // MY RESERVATIONS
                reservationsButton.setOnAction(action -> {

                        MyReservationsScreen screen = new MyReservationsScreen(
                                        stage,
                                        reservations,
                                        this::showDashboard);

                        screen.setAccount(currentAccount);
                        screen.show();
                });

                // MY EVENTS
                myEventsButton.setOnAction(action -> {

                        MyEventsScreen screen = new MyEventsScreen(
                                        stage,
                                        events,
                                        this::showDashboard);

                        screen.show();
                });

                // MY FAVORITES
                favoritesButton.setOnAction(action -> {

                        MyFavoritesScreen screen = new MyFavoritesScreen(
                                        stage,
                                        favorites,
                                        this::showDashboard);

                        screen.show();
                });

                // LOGOUT
                logoutButton.setOnAction(action -> {
                        currentAccount = null;
                        // Clear temporary account data until database storage is integrated.
                        reservations.clear();
                        favorites.clear();
                        events.clear();
                        showWelcomeScreen();
                });

                // ======================ACCOUNT BUTTON===============================
                java.io.File imageFile = new java.io.File("Reservoir/mediaContent/AccountImage.png");

                Image accountImage = new Image(
                                imageFile.toURI().toString(),
                                40, 40, true, true);

                ImageView accountImageView = new ImageView(accountImage);

                accountImageView.setClip(new Circle(20, 20, 20));

                // Create button
                Button accountButton = new Button();
                accountButton.getStyleClass().add("account-button");
                accountButton.setGraphic(accountImageView);

                accountButton.setOnAction(event -> showAccountPage());
                // ======================Main Menu===============================
                // Create buttons list
                VBox sublayout = new VBox(
                                eventsButton,
                                createEventButton,
                                reservationsButton,
                                myEventsButton,
                                favoritesButton,
                                logoutButton

                );
                sublayout.getStyleClass().add("dashboard-menu");

                // Add the calender formating
                VBox calenderlayout = new VBox(calendarBox);
                calenderlayout.getStyleClass().add("calendar-layout");

                // Layout for the buttons and calendar (The Content)
                HBox contentLayout = new HBox(sublayout, calenderlayout);

                contentLayout.getStyleClass().add("dashboard-content");
                HBox headerLayout = new HBox(accountButton, title);
                headerLayout.getStyleClass().add("dashboard-header");
                // Combine the sublayout and calendar layout into a single HBox (Aligning Left
                // and Right) also adds in the "Title" and "Welcome" labels to the top of the
                // screen
                VBox layout = new VBox(headerLayout, welcome, contentLayout);
                layout.getStyleClass().add("dashboard-layout");

                // ================================================================

                // create scene
                stage.setScene(new Scene(layout, 900, 650));
        }

        // =====================================================
        // ACCOUNT PAGE
        // =====================================================
        private void showAccountPage() {

                Label title = new Label("ACCOUNT");
                title.getStyleClass().add("page-title");

                // Account Information
                Label nameLabel = new Label(
                                "Name: " + currentAccount.getFullName());

                Label emailLabel = new Label(
                                "Email: " + currentAccount.getEmail());

                Label phoneLabel = new Label(
                                "Phone: " + (currentAccount.getPhoneNumber() == null
                                                ? "Not provided"
                                                : currentAccount.getPhoneNumber()));

                Label addressLabel = new Label(
                                "Billing address: " + (currentAccount.getBillingAddress() == null
                                                ? "Not provided"
                                                : currentAccount.getBillingAddress()));

                // =====================================================
                // SAVE PAYMENT CARD
                // =====================================================

                Label savedCardLabel = new Label("No saved card.");

                TextField cardNumberField = new TextField();
                cardNumberField.setPromptText("1234 5678 9012 3456");
                cardNumberField.getStyleClass().add("form-field");

                TextField expiryField = new TextField();
                expiryField.setPromptText("MM/YY");
                expiryField.getStyleClass().add("form-field");

                Label paymentMessage = new Label();
                paymentMessage.getStyleClass().add("status-message");

                Button saveCardButton = new Button("Save Payment Card");
                saveCardButton.getStyleClass().add("action-button");

                saveCardButton.setOnAction(event -> {

                        String cardNumber = cardNumberField.getText().replaceAll("\\s", "");
                        String expiry = expiryField.getText().trim();

                        if (!cardNumber.matches("\\d{13,19}")) {
                                paymentMessage.setText("Invalid card number.");
                                return;
                        }

                        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                                paymentMessage.setText("Enter expiration as MM/YY.");
                                return;
                        }

                        try {
                                int expirationMonth = Integer.parseInt(expiry.substring(0, 2));

                                int expirationYear = 2000 + Integer.parseInt(expiry.substring(3, 5));

                                String lastFour = cardNumber.substring(cardNumber.length() - 4);

                                String sql = "INSERT INTO payment_methods ("
                                                + "account_id, cardholder_name, card_type, last_four, "
                                                + "expiration_month, expiration_year, payment_token, "
                                                + "billing_address, card_number"
                                                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                                try (Connection connection = DatabaseConnection.getConnection();
                                                PreparedStatement statement = connection.prepareStatement(sql)) {

                                        statement.setInt(1, currentAccount.getAccountId());
                                        statement.setString(2, currentAccount.getFullName());
                                        statement.setString(3, "Demo");
                                        statement.setString(4, lastFour);
                                        statement.setInt(5, expirationMonth);
                                        statement.setInt(6, expirationYear);
                                        statement.setString(7, UUID.randomUUID().toString());
                                        statement.setString(8, currentAccount.getBillingAddress());
                                        statement.setString(9, cardNumber);

                                        statement.executeUpdate();
                                }

                                savedCardLabel.setText(
                                                "Saved Card: **** **** **** "
                                                                + lastFour + " | Expires: " + expiry);

                                paymentMessage.setText("Payment card saved successfully.");

                                cardNumberField.clear();

                        } catch (Exception e) {
                                e.printStackTrace();
                                paymentMessage.setText("Unable to save payment card.");
                        }
                });
                // =====================================================
                // DISPLAY SAVED CARD INFORMATION IF AVAILABLE
                // =====================================================

                try {
                        String sql = "SELECT last_four, expiration_month, expiration_year "
                                        + "FROM payment_methods "
                                        + "WHERE account_id = ? "
                                        + "ORDER BY payment_id DESC LIMIT 1";

                        try (Connection connection = DatabaseConnection.getConnection();
                                        PreparedStatement statement = connection.prepareStatement(sql)) {

                                statement.setInt(1, currentAccount.getAccountId());

                                try (ResultSet resultSet = statement.executeQuery()) {

                                        if (resultSet.next()) {
                                                String lastFour = resultSet.getString("last_four");
                                                int expMonth = resultSet.getInt("expiration_month");
                                                int expYear = resultSet.getInt("expiration_year");

                                                savedCardLabel.setText(
                                                                "Saved Card: **** **** **** "
                                                                                + lastFour + " | Expires: "
                                                                                + String.format("%02d", expMonth)
                                                                                + "/" + (expYear % 100));
                                        }
                                }
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                        paymentMessage.setText("Unable to retrieve saved card information.");
                }
                // =====================================================
                // NAVIGATION
                // =====================================================

                Button backButton = new Button("Back to Dashboard");
                backButton.getStyleClass().add("navigation-button");

                backButton.setOnAction(action -> showDashboard());

                // =====================================================
                // PAGE LAYOUT
                // =====================================================

                VBox layout = new VBox(
                                title,
                                nameLabel,
                                emailLabel,
                                phoneLabel,
                                addressLabel,
                                savedCardLabel,
                                cardNumberField,
                                expiryField,
                                saveCardButton,
                                paymentMessage,
                                backButton);
                layout.getStyleClass().add("account-layout");

                stage.setScene(new Scene(layout, 900, 650));
        }

        // =====================================================
        // EVENTS
        // =====================================================

        private void showEventsScreen() {

                Label title = new Label("EVENTS");
                title.getStyleClass().add("page-title");

                Label subtitle = new Label("Choose an event category.");

                Button restaurants = new Button("Restaurants");
                restaurants.getStyleClass().add("event-button");

                Button concerts = new Button("Concerts");
                concerts.getStyleClass().add("event-button");

                Button sports = new Button("Sports");
                sports.getStyleClass().add("event-button");

                Button movies = new Button("Movies");
                movies.getStyleClass().add("event-button");

                Button community = new Button("Community Events");
                community.getStyleClass().add("event-button");

                Button back = new Button("Back to Dashboard");
                back.getStyleClass().add("event-button");

                restaurants.setOnAction(action -> showCategoryEvents("Restaurants"));

                concerts.setOnAction(action -> showCategoryEvents("Concerts"));

                sports.setOnAction(action -> showCategoryEvents("Sports"));

                movies.setOnAction(action -> showCategoryEvents("Movies"));

                community.setOnAction(action -> showCategoryEvents("Community Events"));

                back.setOnAction(action -> showDashboard());

                VBox layout = new VBox(
                                title,
                                subtitle,
                                restaurants,
                                concerts,
                                sports,
                                movies,
                                community,
                                back);
                layout.getStyleClass().add("events-layout");

                stage.setScene(new Scene(layout, 900, 650));
        }

        // =====================================================
        // CATEGORY EVENTS
        // =====================================================

        private void showCategoryEvents(String category) {

                CategoryEventsScreen screen = new CategoryEventsScreen(
                                stage,
                                events,
                                reservations,
                                favorites,
                                category,
                                this::showEventsScreen);

                screen.setAccount(currentAccount);
                screen.show();
        }

        // =====================================================
        // MAIN
        // =====================================================

        public static void main(
                        String[] args) {
                launch();
        }
}
