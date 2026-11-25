package projectcsi.reservoir;

import java.util.ArrayList;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    // Automatically apply CSS to every new scene.
    stage.sceneProperty().addListener(
            (observable, oldScene, newScene) -> {

                if (newScene != null) {

                    applyStylesheet(newScene);
                }
            }
    );

    showWelcomeScreen();

    stage.show();
}
    // =====================================================
    // Stylesheet
    // =====================================================

public static void applyStylesheet(Scene scene) {

        java.io.File cssFile = new java.io.File(
                "src/main/java/projectcsi/reservoir/reservoir.css"
        );

        if (!cssFile.exists()) {
                System.err.println(
                        "Could not find reservoir.css at: "
                        + cssFile.getAbsolutePath()
                );
                return;
        }

        String stylesheet = cssFile.toURI().toString();

        // Avoid adding the same stylesheet more than once.
        if (!scene.getStylesheets().contains(stylesheet)) {
                scene.getStylesheets().add(stylesheet);
        }

        System.out.println(
                "Stylesheet applied to scene: "
                + scene.getRoot().getClass().getSimpleName()
        );
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
                try {
                    Account account = AccountDAO.login(
                            emailField.getText(), passwordField.getText());
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

            if (!email.getText().trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
                messageLabel.setText("Please enter a valid email address.");
                return;
            }
            if (password.getText().length() < 15) {
                messageLabel.setText("Use a password of at least 15 characters.");
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
                messageLabel.setText("Account creation unavailable. Please try again.");
            }
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
                new Label("Welcome, " + currentAccount.getFirstName() + "!");

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
                
        eventsButton.getStyleClass().add("dashboard-button");
        createEventButton.getStyleClass().add("dashboard-button");
        reservationsButton.getStyleClass().add("dashboard-button");
        myEventsButton.getStyleClass().add("dashboard-button");
        favoritesButton.getStyleClass().add("dashboard-button");
        logoutButton.getStyleClass().add("dashboard-button");
        //====================
        // Calendar
        //====================
        // Calendar
java.time.YearMonth currentMonth = java.time.YearMonth.now();
java.time.LocalDate today = java.time.LocalDate.now();

GridPane calendar = new GridPane();
calendar.setHgap(10);
calendar.setVgap(10);
calendar.setAlignment(Pos.CENTER);

// Month heading
Label monthLabel = new Label(
        currentMonth.getMonth().toString() + " " + currentMonth.getYear()
);

monthLabel.setStyle(
        "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #12284B;"
);

// Weekday headings
String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

for (int col = 0; col < 7; col++) {
    Label weekday = new Label(weekdays[col]);
    weekday.setAlignment(Pos.CENTER);
    weekday.setStyle(
            "-fx-text-fill: #12284B;" +
            "-fx-font-weight: bold;"
    );
    calendar.add(weekday, col, 0);
}

// Find the first weekday of the month
int startDay = currentMonth.atDay(1).getDayOfWeek().getValue() % 7;

// Add the numbered days
for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {

    java.time.LocalDate date = currentMonth.atDay(day);

    Label dateLabel = new Label(String.valueOf(day));

    dateLabel.setMinSize(30, 30);
    dateLabel.setAlignment(Pos.CENTER);

    // Individual square borders and yellow highlight for today
    dateLabel.setStyle(
            "-fx-border-color: #12284B;" +
            "-fx-border-width: 1px;" +
            "-fx-text-fill: #12284B;" +
            (date.equals(today)
                    ? "-fx-background-color: yellow; -fx-font-weight: bold;"
                    : "")
    );

    int index = startDay + day - 1;

    calendar.add(
            dateLabel,
            index % 7,
            (index / 7) + 1
    );
}

// Calendar layout
VBox calendarBox = new VBox(
        10,
        monthLabel,
        calendar
);

calendarBox.setAlignment(Pos.CENTER);

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

            screen.setAccount(currentAccount);
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
        logoutButton.setOnAction(action -> {
            currentAccount = null;
            // Clear temporary account data until database storage is integrated.
            reservations.clear();
            favorites.clear();
            events.clear();
            showWelcomeScreen();
        });


       //ACCOUNT BUTTON

        //Load AccountImage.png from the mediaContent folder
        Image accountImage = new Image(
        new java.io.File("mediaContent/AccountImage.png").toURI().toString(),
                40,
                40,
                true,
                true
        );
        //Display the image
        ImageView accountImageView = new ImageView(accountImage);
        accountImageView.setFitWidth(40);
        accountImageView.setFitHeight(40);
        accountImageView.setPreserveRatio(false);

        //Make the content the correct size for the border
        Circle clip = new Circle(20, 20, 20);
        accountImageView.setClip(clip);

        //Create the button
        Button accountButton = new Button();
        accountButton.setGraphic(accountImageView);
        accountButton.setPrefSize(44, 44);
        accountButton.setMinSize(44, 44);
        accountButton.setMaxSize(44, 44);


        //Add new style to the button
        accountButton.setStyle(
        "-fx-background-color: transparent;" +
        "-fx-padding: 2;" +
        "-fx-cursor: hand;" +
        "-fx-border-color: black;" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 22;" +
        "-fx-background-radius: 22;"
        );

        //Navigate to the account page when clicked
        accountButton.setOnAction(event -> {showAccountPage();});
        //======================Main Menu===============================
        //Create buttons list
        VBox sublayout =
                new VBox(
                        12,
                        eventsButton,
                        createEventButton,
                        reservationsButton,
                        myEventsButton,
                        favoritesButton,
                        logoutButton
                        
                );        

        //Add the calender formating
        VBox calenderlayout = new VBox(
                12,
                calendarBox
        );


        //Layout for the buttons and calendar (The Content)
        HBox contentLayout = new HBox(
                50,
                sublayout,
                calenderlayout
        );
        contentLayout.setAlignment(Pos.CENTER);
        HBox headerLayout = new HBox(
                50,
                accountButton,
                title
                
        );
        headerLayout.setAlignment(Pos.CENTER);
        // Combine the sublayout and calendar layout into a single HBox (Aligning Left and Right) also adds in the "Title" and "Welcome" labels to the top of the screen
        VBox layout = new VBox(
                50,
                headerLayout,
                welcome,
                contentLayout
        );
        layout.setAlignment(Pos.CENTER);

        layout.setPadding(
                new Insets(30)
        );

//================================================================


        //create scene
        stage.setScene(
                new Scene(
                        layout,
                        900,
                        650
                )
        );
    }
    // =====================================================
    // ACCOUNT PAGE
    // =====================================================
        private void showAccountPage() {
                Label title =
                        new Label("ACCOUNT");

                title.setStyle(
                        "-fx-font-size: 32px;"
                        + "-fx-font-weight: bold;"
                );

                Button backButton =
                        new Button("Back to Dashboard");

                backButton.setPrefSize(
                        200,
                        35
                );

                backButton.setOnAction(
                        action ->
                                showDashboard()
                );

                VBox layout =
                        new VBox(
                                15,
                                title,
                                new Label("Name: " + currentAccount.getFullName()),
                                new Label("Email: " + currentAccount.getEmail()),
                                new Label("Phone: " + (currentAccount.getPhoneNumber() == null
                                        ? "Not provided" : currentAccount.getPhoneNumber())),
                                new Label("Billing address: " + (currentAccount.getBillingAddress() == null
                                        ? "Not provided" : currentAccount.getBillingAddress())),
                                backButton
                        );

                layout.setAlignment(Pos.CENTER);

                layout.setPadding(new Insets(40));


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
