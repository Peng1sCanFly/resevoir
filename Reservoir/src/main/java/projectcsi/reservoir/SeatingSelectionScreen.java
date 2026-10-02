package projectcsi.reservoir;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SeatingSelectionScreen {

    private Stage stage;
    private String eventType;
    private Consumer<String> sectionSelectedAction;
    private Runnable backAction;

    private String selectedSection = "";

    public SeatingSelectionScreen(
            Stage stage,
            String eventType,
            Consumer<String> sectionSelectedAction,
            Runnable backAction) {

        this.stage = stage;
        this.eventType = eventType;
        this.sectionSelectedAction = sectionSelectedAction;
        this.backAction = backAction;
    }

    public void show() {

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label("CHOOSE YOUR SECTION");

        titleLabel.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label eventTypeLabel =
                new Label(eventType);

        eventTypeLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        Label instructionLabel =
                new Label(
                        "Click a section on the seating chart."
                );

        // =====================================================
        // SELECTED SECTION
        // =====================================================

        Label selectedLabel =
                new Label(
                        "Selected Section: None"
                );

        selectedLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        // =====================================================
        // FIELD OR STAGE
        // =====================================================

        Label centerLabel =
                new Label();

        if (eventType.equalsIgnoreCase(
                "Concert")) {

            centerLabel.setText(
                    "STAGE"
            );

        } else {

            centerLabel.setText(
                    "FOOTBALL FIELD"
            );
        }

        centerLabel.setAlignment(
                Pos.CENTER
        );

        centerLabel.setPrefSize(
                240,
                160
        );

        centerLabel.setStyle(
                "-fx-background-color: #333333;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 22px;"
                + "-fx-font-weight: bold;"
                + "-fx-border-color: black;"
                + "-fx-border-width: 2px;"
        );

        // =====================================================
        // FLOOR / FIELD SECTIONS
        // =====================================================

        Button sectionA =
                createSectionButton(
                        "A",
                        selectedLabel
                );

        Button sectionB =
                createSectionButton(
                        "B",
                        selectedLabel
                );

        Button sectionC =
                createSectionButton(
                        "C",
                        selectedLabel
                );

        Button sectionD =
                createSectionButton(
                        "D",
                        selectedLabel
                );

        Button sectionE =
                createSectionButton(
                        "E",
                        selectedLabel
                );

        Button sectionF =
                createSectionButton(
                        "F",
                        selectedLabel
                );

        GridPane centerSections =
                new GridPane();

        centerSections.setHgap(6);
        centerSections.setVgap(6);

        centerSections.setAlignment(
                Pos.CENTER
        );

        centerSections.add(
                sectionC,
                0,
                0
        );

        centerSections.add(
                sectionD,
                1,
                0
        );

        centerSections.add(
                sectionB,
                0,
                1
        );

        centerSections.add(
                sectionE,
                1,
                1
        );

        centerSections.add(
                sectionA,
                0,
                2
        );

        centerSections.add(
                sectionF,
                1,
                2
        );

        // =====================================================
        // TOP STADIUM SECTIONS
        // =====================================================

        HBox topSections =
                new HBox(
                        5,
                        createSectionButton(
                                "125",
                                selectedLabel
                        ),
                        createSectionButton(
                                "126",
                                selectedLabel
                        ),
                        createSectionButton(
                                "127",
                                selectedLabel
                        ),
                        createSectionButton(
                                "128",
                                selectedLabel
                        ),
                        createSectionButton(
                                "101",
                                selectedLabel
                        ),
                        createSectionButton(
                                "102",
                                selectedLabel
                        ),
                        createSectionButton(
                                "103",
                                selectedLabel
                        ),
                        createSectionButton(
                                "104",
                                selectedLabel
                        )
                );

        topSections.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // LEFT STADIUM SECTIONS
        // =====================================================

        VBox leftSections =
                new VBox(
                        6,
                        createSectionButton(
                                "124",
                                selectedLabel
                        ),
                        createSectionButton(
                                "123",
                                selectedLabel
                        ),
                        createSectionButton(
                                "122",
                                selectedLabel
                        ),
                        createSectionButton(
                                "121",
                                selectedLabel
                        ),
                        createSectionButton(
                                "120",
                                selectedLabel
                        ),
                        createSectionButton(
                                "119",
                                selectedLabel
                        )
                );

        leftSections.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // RIGHT STADIUM SECTIONS
        // =====================================================

        VBox rightSections =
                new VBox(
                        6,
                        createSectionButton(
                                "105",
                                selectedLabel
                        ),
                        createSectionButton(
                                "106",
                                selectedLabel
                        ),
                        createSectionButton(
                                "107",
                                selectedLabel
                        ),
                        createSectionButton(
                                "108",
                                selectedLabel
                        ),
                        createSectionButton(
                                "109",
                                selectedLabel
                        ),
                        createSectionButton(
                                "110",
                                selectedLabel
                        )
                );

        rightSections.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // BOTTOM STADIUM SECTIONS
        // =====================================================

        HBox bottomSections =
                new HBox(
                        5,
                        createSectionButton(
                                "118",
                                selectedLabel
                        ),
                        createSectionButton(
                                "117",
                                selectedLabel
                        ),
                        createSectionButton(
                                "116",
                                selectedLabel
                        ),
                        createSectionButton(
                                "115",
                                selectedLabel
                        ),
                        createSectionButton(
                                "114",
                                selectedLabel
                        ),
                        createSectionButton(
                                "113",
                                selectedLabel
                        ),
                        createSectionButton(
                                "112",
                                selectedLabel
                        ),
                        createSectionButton(
                                "111",
                                selectedLabel
                        )
                );

        bottomSections.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // CENTER AREA
        // =====================================================

        HBox centerArea =
                new HBox(
                        20,
                        centerLabel,
                        centerSections
                );

        centerArea.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // STADIUM
        // =====================================================

        BorderPane stadium =
                new BorderPane();

        stadium.setTop(
                topSections
        );

        stadium.setLeft(
                leftSections
        );

        stadium.setCenter(
                centerArea
        );

        stadium.setRight(
                rightSections
        );

        stadium.setBottom(
                bottomSections
        );

        stadium.setPadding(
                new Insets(25)
        );

        stadium.setMaxWidth(
                800
        );

        stadium.setStyle(
                "-fx-border-color: #777777;"
                + "-fx-border-width: 2px;"
                + "-fx-border-radius: 20px;"
                + "-fx-background-radius: 20px;"
                + "-fx-background-color: #f4f4f4;"
        );

        // =====================================================
        // MESSAGE
        // =====================================================

        Label messageLabel =
                new Label();

        // =====================================================
        // CONTINUE BUTTON
        // =====================================================

        Button continueButton =
                new Button(
                        "Use Selected Section"
                );

        continueButton.setPrefSize(
                250,
                40
        );

        continueButton.setOnAction(action -> {

            if (selectedSection.isEmpty()) {

                messageLabel.setText(
                        "Please select a section first."
                );

                return;
            }

            sectionSelectedAction.accept(
                    selectedSection
            );
        });

        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button backButton =
                new Button("Back");

        backButton.setPrefSize(
                250,
                40
        );

        backButton.setOnAction(
                action ->
                        backAction.run()
        );

        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        VBox layout =
                new VBox(
                        15,
                        titleLabel,
                        eventTypeLabel,
                        instructionLabel,
                        stadium,
                        selectedLabel,
                        continueButton,
                        messageLabel,
                        backButton
                );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setPadding(
                new Insets(25)
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        layout,
                        1000,
                        750
                );

        stage.setScene(scene);
    }

    // =====================================================
    // CREATE SECTION BUTTON
    // =====================================================

    private Button createSectionButton(
            String section,
            Label selectedLabel) {

        Button button =
                new Button(section);

        button.setPrefSize(
                65,
                45
        );

        button.setStyle(
                "-fx-font-weight: bold;"
        );

        button.setOnAction(action -> {

            selectedSection =
                    section;

            selectedLabel.setText(
                    "Selected Section: "
                    + selectedSection
            );
        });

        return button;
    }

    // =====================================================
    // GET SELECTED SECTION
    // =====================================================

    public String getSelectedSection() {

        return selectedSection;
    }
}