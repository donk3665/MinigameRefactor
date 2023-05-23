package Stages;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.awt.*;


abstract class MasterScene{
    //TODO: CHILDREN NEED TO HAVE DEPENDENCY INVERSION IN ORDER TO MAKE IT CLEAN

    abstract Scene run(Stage primaryStage, SceneTransferData data);
    static ControllerTemplate controller;
    //capturing screen size of monitor to resize application for any screen size
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double widthAdjust = screenSize.getWidth()/1536.0;
    double heightAdjust = screenSize.getHeight()/864.0;

    //main menu music
    static MediaPlayer menuMusic;

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
        javafx.scene.image.Image BackButton = new javafx.scene.image.Image("buttonImages/gameSelect/BackButton.png", 418*widthAdjust, 77*heightAdjust, false, false);
        ImageView backBut = new ImageView(BackButton);
        javafx.scene.control.Button backbutton = new Button();
        backbutton.setGraphic(backBut);
        backbutton.setStyle("-fx-background-color: transparent;");

        // Gets the Logo Image and places it
        javafx.scene.image.Image Logo = new Image("buttonImages/mainMenu/Logo.png", 424.8*widthAdjust, 172*heightAdjust, false, false);
        ImageView logo = new ImageView(Logo);
        anchorpane.getChildren().addAll(logo, backbutton);
        // Anchoring the location
        AnchorPane.setTopAnchor(logo, 0d);
        AnchorPane.setLeftAnchor(logo, 550*widthAdjust);

        AnchorPane.setTopAnchor(backbutton, 0d);
        AnchorPane.setLeftAnchor(backbutton, 0d);

        backbutton.setOnAction(event->{
            controller.changeScenes(type, null);
        });

        return anchorpane;
    }
}
