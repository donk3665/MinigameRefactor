package Scenes;

import SceneControllers.ControllerTemplate;
import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.awt.*;


public abstract class MasterScene{
    static ControllerTemplate controller;
    //capturing screen size of monitor to resize application for any screen size
    static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public static final double widthAdjust = Math.min(screenSize.getWidth()/1536.0, screenSize.getHeight()/864.0);
    public static final double heightAdjust = Math.min(screenSize.getWidth()/1536.0, screenSize.getHeight()/864.0);

    static MediaPlayer menuMusic;

    /**
     * Function to create the scene object.
     */
    public abstract Scene run(Stage primaryStage, SceneTransferData data);
    /**
     * Setting the controller of the scenes
     */
    public static void setController(ControllerTemplate controller1){
        controller = controller1;
    }

    /**
     * This method creates a back button which goes to the previous scene and displays the logo in the middle of the screen
     */
    public AnchorPane addTopAnchorPane(SceneEnums type) {

        //Creates Anchor Pane
        AnchorPane anchorpane = new AnchorPane();

        //Gets the Back Button image from package, sets into ImageView, creates button and sets graphic for button.
        javafx.scene.image.Image BackButton = new javafx.scene.image.Image("SceneAssets/misc/BackButton.png", 418*widthAdjust, 77*heightAdjust, false, false);
        ImageView backBut = new ImageView(BackButton);
        javafx.scene.control.Button backButton = new Button();
        backButton.setGraphic(backBut);
        backButton.setStyle("-fx-background-color: transparent;");

        // Gets the Logo Image and places it
        javafx.scene.image.Image Logo = new Image("SceneAssets/misc/Logo.png", 424.8*widthAdjust, 172*heightAdjust, false, false);
        ImageView logo = new ImageView(Logo);
        anchorpane.getChildren().addAll(logo, backButton);
        // Anchoring the location
        AnchorPane.setTopAnchor(logo, 0d);
        AnchorPane.setLeftAnchor(logo, 550*widthAdjust);

        AnchorPane.setTopAnchor(backButton, 0d);
        AnchorPane.setLeftAnchor(backButton, 0d);

        backButton.setOnAction(event-> controller.changeScenes(type, null));

        return anchorpane;
    }
    /**
     * Function to initialize new buttons
     */
    public Button createStandardButton(Image image){
        Button tempButton = new Button();
        ImageView buttonImage = new ImageView(image);
        tempButton.setGraphic(buttonImage);
        tempButton.setStyle("-fx-background-color: transparent;");
        return tempButton;
    }
    /**
     * Function to initialize the rhythm grids
     */
    public void initializeRhythmGrid(GridPane grid, Label score, Label combo, Label accuracy){
        grid.add(score, 0, 0);
        grid.add(combo, 0, 1);
        grid.add(accuracy, 0, 2);
    }
}
