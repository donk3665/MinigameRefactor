//package Stages;
//
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//
//import java.awt.*;
//
//public class StageLogic {
//    SceneController controller;
//    //capturing screen size of monitor to resize application for any screen size
//    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
//    double widthAdjust = screenSize.getWidth()/1536.0;
//    double heightAdjust = screenSize.getHeight()/864.0;
//
//    public void setController(SceneController controller){
//        this.controller = controller;
//    }
//    /**
//     * This method creates a back button which goes to the previous scene and displays the logo in the middle of the screen
//     * @param count - scene counter
//     * @param stage - the stage
//     * @return
//     */
//    public AnchorPane addTopAnchorPane(int count, Stage stage) {
//
//        //Creates Anchor Pane
//        AnchorPane anchorpane = new AnchorPane();
//
//        //Gets the Back Button image from package, sets into ImageView, creates button and sets graphic for button.
//        Image BackButton = new Image("buttonImages/gameSelect/BackButton.png", 418*widthAdjust, 77*heightAdjust, false, false);
//        ImageView backBut = new ImageView(BackButton);
//        Button backbutton = new Button();
//        backbutton.setGraphic(backBut);
//        backbutton.setStyle("-fx-background-color: transparent;");
//
//        // Gets the Logo Image and places it
//        Image Logo = new Image("buttonImages/mainMenu/Logo.png", 424.8*widthAdjust, 172*heightAdjust, false, false);
//        ImageView logo = new ImageView(Logo);
//        anchorpane.getChildren().addAll(logo, backbutton);
//        // Anchoring the location
//        AnchorPane.setTopAnchor(logo, 0d);
//        AnchorPane.setLeftAnchor(logo, 550*widthAdjust);
//
//        AnchorPane.setTopAnchor(backbutton, 0d);
//        AnchorPane.setLeftAnchor(backbutton, 0d);
//
//        // TODO: changing scenes should be left to controller
//        // Checking for which scene it is
//        if (count == 1) {
//            backbutton.setOnAction(event->{
//                stage.setScene(mainMenuScreen);
//                stage.show();
//
//            });
//        }
//        else if (count == 2) {
//            backbutton.setOnAction(event->{
//                stage.setScene(gameSelect);
//                stage.show();
//
//            });
//        }
//        else if (count == 3) {
//            backbutton.setOnAction(event ->{
//                stage.setScene(rhythmSongSelect);
//                stage.show();
//
//            });
//        }
//
//        return anchorpane;
//    }
//}
