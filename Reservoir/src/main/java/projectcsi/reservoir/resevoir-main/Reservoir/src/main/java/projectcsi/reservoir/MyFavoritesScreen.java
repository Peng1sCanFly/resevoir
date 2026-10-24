package projectcsi.reservoir;

import java.util.ArrayList;
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

public class MyFavoritesScreen {

    private Stage stage;
    private ArrayList<Favorite> favorites;
    private Runnable backToDashboard;

    public MyFavoritesScreen(
            Stage stage,
            ArrayList<Favorite> favorites,
            Runnable backToDashboard) {

        this.stage = stage;
        this.favorites = favorites;
        this.backToDashboard = backToDashboard;
    }

    public void show() {

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label("MY FAVORITES");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel =
                new Label(
                        "View or remove your saved favorites."
                );

        // =====================================================
        // FAVORITES LIST
        // =====================================================

        VBox favoritesList =
                new VBox(15);

        favoritesList.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // NO FAVORITES
        // =====================================================

        if (favorites.isEmpty()) {

            Label noFavoritesLabel =
                    new Label(
                            "You do not have any favorites yet."
                    );

            noFavoritesLabel.setStyle(
                    "-fx-font-size: 16px;"
            );

            favoritesList
                    .getChildren()
                    .add(noFavoritesLabel);

        } else {

            // =================================================
            // SHOW FAVORITES
            // =================================================

            for (Favorite favorite : favorites) {

                Label eventNameLabel =
                        new Label(
                                favorite.getEventName()
                        );

                eventNameLabel.setStyle(
                        "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
                );

                Label locationLabel =
                        new Label(
                                "Location: "
                                + favorite.getLocation()
                        );

                Label dateLabel =
                        new Label(
                                "Date: "
                                + favorite.getDate()
                        );

                Label timeLabel =
                        new Label(
                                "Time: "
                                + favorite.getTime()
                        );

                Label priceLabel =
                        new Label(
                                String.format(
                                        "Price: $%.2f",
                                        favorite.getPrice()
                                )
                        );

                // =================================================
                // REMOVE FAVORITE BUTTON
                // =================================================

                Button removeButton =
                        new Button(
                                "Remove from Favorites"
                        );

                removeButton.setPrefSize(
                        200,
                        35
                );

                removeButton.setStyle(
                        "-fx-font-weight: bold;"
                );

                removeButton.setOnAction(action -> {

                    // Ask before removing favorite
                    Alert confirmation =
                            new Alert(
                                    Alert.AlertType.CONFIRMATION
                            );

                    confirmation.setTitle(
                            "Remove Favorite"
                    );

                    confirmation.setHeaderText(
                            "Remove "
                            + favorite.getEventName()
                            + "?"
                    );

                    confirmation.setContentText(
                            "Are you sure you want to remove this from your favorites?"
                    );

                    confirmation.showAndWait()
                            .ifPresent(response -> {

                                if (response
                                        == ButtonType.OK) {

                                    favorites.remove(
                                            favorite
                                    );

                                    // Refresh screen
                                    show();
                                }
                            });
                });

                // =================================================
                // FAVORITE CARD
                // =================================================

                VBox favoriteCard =
                        new VBox(
                                6,
                                eventNameLabel,
                                locationLabel,
                                dateLabel,
                                timeLabel,
                                priceLabel,
                                removeButton
                        );

                favoriteCard.setAlignment(
                        Pos.CENTER_LEFT
                );

                favoriteCard.setMaxWidth(
                        500
                );

                favoriteCard.setPadding(
                        new Insets(15)
                );

                favoriteCard.setStyle(
                        "-fx-border-color: lightgray;"
                        + "-fx-border-width: 1px;"
                        + "-fx-border-radius: 8px;"
                        + "-fx-background-radius: 8px;"
                );

                favoritesList
                        .getChildren()
                        .add(favoriteCard);
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
                        favoritesList,
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

        stage.setScene(scene);
    }
}