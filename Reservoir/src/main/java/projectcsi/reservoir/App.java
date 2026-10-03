package projectcsi.reservoir;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private Stage stage;

    private ArrayList<Event> events =
            new ArrayList<>();

    private ArrayList<Reservation> reservations =
            new ArrayList<>();

    private ArrayList<Favorite> favorites =
            new ArrayList<>();

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        stage.setTitle("Reservoir");

        showWelcomeScreen();

        stage.show();
    }

    // =====================================================
    // HOME
    // =====================================================

    private void showWelcomeScreen() {

        Label title =
                new Label("RESERVOIR");

        title.setStyle(
                "-fx-font-size: 36px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "All Your Plans. One Place."
                );

        Label description =
                new Label(
                        "Plan, reserve, and manage your events all in one place."
                );

        Button loginButton =
                new Button("Login");

        Button createAccountButton =
                new Button("Create Account");

        loginButton.setPrefSize(
                200,
                35
        );

        createAccountButton.setPrefSize(
                200,
                35
        );

        loginButton.setOnAction(
                action ->
                        showLoginScreen()
        );

        createAccountButton.setOnAction(
                action ->
                        showCreateAccountScreen()
        );

        VBox layout =
                new VBox(
                        15,
                        title,
                        subtitle,
                        description,
                        loginButton,
                        createAccountButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(40)
        );

        stage.setScene(
                new Scene(
                        layout,
                        900,
                        600
                )
        );
    }

    // =====================================================
    // LOGIN
    // =====================================================

    private void showLoginScreen() {

        Label title =
                new Label("LOGIN");

        title.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Email"
        );

        emailField.setMaxWidth(
                300
        );

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Password"
        );

        passwordField.setMaxWidth(
                300
        );

        Label messageLabel =
                new Label();

        Button loginButton =
                new Button("Login");

        Button backButton =
                new Button("Back to Home");

        loginButton.setPrefSize(
                200,
                35
        );

        backButton.setPrefSize(
                200,
                35
        );

        loginButton.setOnAction(action -> {

            if (emailField
                    .getText()
                    .trim()
                    .isEmpty()
                    || passwordField
                            .getText()
                            .isEmpty()) {

                messageLabel.setText(
                        "Please enter your email and password."
                );

            } else {

                showDashboard();
            }
        });

        backButton.setOnAction(
                action ->
                        showWelcomeScreen()
        );

        VBox layout =
                new VBox(
                        12,
                        title,
                        emailField,
                        passwordField,
                        loginButton,
                        messageLabel,
                        backButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(40)
        );

        stage.setScene(
                new Scene(
                        layout,
                        900,
                        600
                )
        );
    }

    // =====================================================
    // CREATE ACCOUNT
    // =====================================================

    private void showCreateAccountScreen() {

        Label title =
                new Label(
                        "CREATE ACCOUNT"
                );

        title.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        TextField firstName =
                new TextField();

        firstName.setPromptText(
                "First Name"
        );

        firstName.setMaxWidth(
                300
        );

        TextField lastName =
                new TextField();

        lastName.setPromptText(
                "Last Name"
        );

        lastName.setMaxWidth(
                300
        );

        TextField email =
                new TextField();

        email.setPromptText(
                "Email"
        );

        email.setMaxWidth(
                300
        );

        PasswordField password =
                new PasswordField();

        password.setPromptText(
                "Password"
        );

        password.setMaxWidth(
                300
        );

        PasswordField confirmPassword =
                new PasswordField();

        confirmPassword.setPromptText(
                "Confirm Password"
        );

        confirmPassword.setMaxWidth(
                300
        );

        Label messageLabel =
                new Label();

        Button createButton =
                new Button(
                        "Create Account"
                );

        Button backButton =
                new Button(
                        "Back to Home"
                );

        createButton.setPrefSize(
                200,
                35
        );

        backButton.setPrefSize(
                200,
                35
        );

        createButton.setOnAction(action -> {

            if (firstName
                    .getText()
                    .trim()
                    .isEmpty()
                    || lastName
                            .getText()
                            .trim()
                            .isEmpty()
                    || email
                            .getText()
                            .trim()
                            .isEmpty()
                    || password
                            .getText()
                            .isEmpty()
                    || confirmPassword
                            .getText()
                            .isEmpty()) {

                messageLabel.setText(
                        "Please complete all fields."
                );

                return;
            }

            if (!password
                    .getText()
                    .equals(
                            confirmPassword
                                    .getText()
                    )) {

                messageLabel.setText(
                        "Passwords do not match."
                );

                return;
            }

            messageLabel.setText(
                    "Account created successfully!"
            );
        });

        backButton.setOnAction(
                action ->
                        showWelcomeScreen()
        );

        VBox layout =
                new VBox(
                        10,
                        title,
                        firstName,
                        lastName,
                        email,
                        password,
                        confirmPassword,
                        createButton,
                        messageLabel,
                        backButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(30)
        );

        stage.setScene(
                new Scene(
                        layout,
                        900,
                        650
                )
        );
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    private void showDashboard() {

        Label title =
                new Label("RESERVOIR");

        title.setStyle(
                "-fx-font-size: 34px;"
                + "-fx-font-weight: bold;"
        );

        Label welcome =
                new Label("Welcome!");

        Button eventsButton =
                new Button("Join Local Events");

        Button createEventButton =
                new Button("Create Event");

        Button reservationsButton =
                new Button(
                        "My Reservations"
                );

        Button myEventsButton =
                new Button("My Events");

        Button favoritesButton =
                new Button("My Favorites");

        Button logoutButton =
                new Button("Logout");

        eventsButton.setPrefSize(
                300,
                40
        );

        createEventButton.setPrefSize(
                300,
                40
        );

        reservationsButton.setPrefSize(
                300,
                40
        );

        myEventsButton.setPrefSize(
                300,
                40
        );

        favoritesButton.setPrefSize(
                300,
                40
        );

        logoutButton.setPrefSize(
                300,
                40
        );

        // EVENTS
        eventsButton.setOnAction(
                action ->
                        showEventsScreen()
        );

        // CREATE EVENT
        createEventButton.setOnAction(action -> {

            CreateEventScreen screen =
                    new CreateEventScreen(
                            stage,
                            events,
                            this::showDashboard
                    );

            screen.show();
        });

        // MY RESERVATIONS
        reservationsButton.setOnAction(action -> {

            MyReservationsScreen screen =
                    new MyReservationsScreen(
                            stage,
                            reservations,
                            this::showDashboard
                    );

            screen.show();
        });

        // MY EVENTS
        myEventsButton.setOnAction(action -> {

            MyEventsScreen screen =
                    new MyEventsScreen(
                            stage,
                            events,
                            this::showDashboard
                    );

            screen.show();
        });

        // MY FAVORITES
        favoritesButton.setOnAction(action -> {

            MyFavoritesScreen screen =
                    new MyFavoritesScreen(
                            stage,
                            favorites,
                            this::showDashboard
                    );

            screen.show();
        });

        // LOGOUT
        logoutButton.setOnAction(
                action ->
                        showWelcomeScreen()
        );

        VBox layout =
                new VBox(
                        12,
                        title,
                        welcome,
                        eventsButton,
                        createEventButton,
                        reservationsButton,
                        myEventsButton,
                        favoritesButton,
                        logoutButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(30)
        );

        stage.setScene(
                new Scene(
                        layout,
                        900,
                        650
                )
        );
    }

    // =====================================================
    // EVENTS
    // =====================================================

    private void showEventsScreen() {

        Label title =
                new Label("EVENTS");

        title.setStyle(
                "-fx-font-size: 32px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "Choose an event category."
                );

        Button restaurants =
                new Button(
                        "Restaurants"
                );

        Button concerts =
                new Button(
                        "Concerts"
                );

        Button sports =
                new Button(
                        "Sports"
                );

        Button movies =
                new Button(
                        "Movies"
                );

        Button community =
                new Button(
                        "Community Events"
                );

        Button back =
                new Button(
                        "Back to Dashboard"
                );

        restaurants.setPrefSize(
                300,
                45
        );

        concerts.setPrefSize(
                300,
                45
        );

        sports.setPrefSize(
                300,
                45
        );

        movies.setPrefSize(
                300,
                45
        );

        community.setPrefSize(
                300,
                45
        );

        back.setPrefSize(
                300,
                40
        );

        restaurants.setOnAction(
                action ->
                        showCategoryEvents(
                                "Restaurants"
                        )
        );

        concerts.setOnAction(
                action ->
                        showCategoryEvents(
                                "Concerts"
                        )
        );

        sports.setOnAction(
                action ->
                        showCategoryEvents(
                                "Sports"
                        )
        );

        movies.setOnAction(
                action ->
                        showCategoryEvents(
                                "Movies"
                        )
        );

        community.setOnAction(
                action ->
                        showCategoryEvents(
                                "Community Events"
                        )
        );

        back.setOnAction(
                action ->
                        showDashboard()
        );

        VBox layout =
                new VBox(
                        15,
                        title,
                        subtitle,
                        restaurants,
                        concerts,
                        sports,
                        movies,
                        community,
                        back
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(40)
        );

        stage.setScene(
                new Scene(
                        layout,
                        900,
                        650
                )
        );
    }

    // =====================================================
    // CATEGORY EVENTS
    // =====================================================

    private void showCategoryEvents(
            String category) {

        CategoryEventsScreen screen =
                new CategoryEventsScreen(
                        stage,
                        events,
                        reservations,
                        favorites,
                        category,
                        this::showEventsScreen
                );

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