package Stages;

import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameSelectScene extends MasterScene{
//    public GameSelectScene(SceneFactory factory) {
//        super(factory);
//    }

    @Override
    Scene run(Stage primaryStage, SceneTransferData data){

        //Setting up images
        Image BackgroundImage = new Image("buttonImages/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        Image ControlsButton = new Image("buttonImages/gameSelect/ControlsButton.png", 510*widthAdjust, 93*heightAdjust, false, false);
        Image CrazyRumbleButton = new Image("buttonImages/gameSelect/CrazyRumbleButton.png", 720*widthAdjust, 600*heightAdjust, false, false);
        Image CrazyRhythmButton = new Image("buttonImages/gameSelect/CrazyRhythmButton.png", 720*widthAdjust, 600*heightAdjust, false, false);

        // Creates background Image
        ImageView backgroundImage = new ImageView(BackgroundImage);

        //Crazy Rumble Button
        Button crazyRumble = new Button();
        ImageView crazyRumbleBut = new ImageView(CrazyRumbleButton);
        crazyRumble.setGraphic(crazyRumbleBut); // Setting Image
        crazyRumble.setStyle("-fx-background-color: transparent;");

        // Crazy Rhythm Button
        Button crazyRhythm = new Button();
        ImageView crazyRhythmBut = new ImageView(CrazyRhythmButton);
        crazyRhythm.setGraphic(crazyRhythmBut);// Setting Image
        crazyRhythm.setStyle("-fx-background-color: transparent;");

        // Creating Control Buttons
        Button crazyRumbleControls = new Button();
        Button crazyRhythmControls = new Button();
        ImageView control = new ImageView(ControlsButton);
        ImageView control2 = new ImageView(ControlsButton);
        crazyRumbleControls.setGraphic(control);
        crazyRumbleControls.setStyle("-fx-background-color: transparent;");
        crazyRhythmControls.setGraphic(control2);
        crazyRhythmControls.setStyle("-fx-background-color: transparent;");

        //adding various buttons to GridPane
        GridPane games = new GridPane();
        games.add(crazyRumble, 0, 0, 1, 1);
        games.add(crazyRhythm, 1, 0, 1, 1);
        games.add(crazyRumbleControls, 0, 1, 1, 1);
        games.add(crazyRhythmControls, 1, 1, 1, 1);
        games.setVgap(10);

        // Centering the Control Buttons
        games.setHalignment(crazyRumbleControls, HPos.CENTER);
        games.setHalignment(crazyRhythmControls, HPos.CENTER);

        // Positioning
        games.setTranslateX(25*widthAdjust);
        games.setTranslateY(75*heightAdjust);

        // Button on click methods
        // Crazy Rumble Button
        crazyRumble.setOnAction(event->{
            controller.changeScenes(SceneEnums.RUM_CHAR_SELECT, null);
//            rumbleCharSelect = rumbleCharSelect(stage);
//            stage.setScene(rumbleCharSelect);
//
//            stage.show();
        });

        // Crazy Rhythm Button
        crazyRhythm.setOnAction(event->{
            controller.changeScenes(SceneEnums.RHY_SONG_SELECT, null);
//            rhythmSongSelect = rhythmSongSelect(stage);
//            stage.setScene(rhythmSongSelect);
//
//            stage.show();
        });

        // Controls Button
        crazyRumbleControls.setOnAction(event->{
            controller.changeScenes(SceneEnums.CRZ_RUM_CTRL_SCREEN, null);
//            crazyRumCtrlScreen = crazyRumCtrlScreen(stage);
//            stage.setScene(crazyRumCtrlScreen);
//
//            stage.show();
        });

        // Crazy Rhythm Control
        crazyRhythmControls.setOnAction(event->{
            controller.changeScenes(SceneEnums.CRZ_RHY_CTRL_SCREEN, null);
//            crazyRhyCtrlScreen = crazyRhyCtrlScreen(stage);
//            stage.setScene(crazyRhyCtrlScreen);
//
//            stage.show();
        });

        // Creating main group to add into scene and adding back button
        Group main = new Group();
        main.getChildren().addAll(backgroundImage,games,addTopAnchorPane(SceneEnums.PRIMARY_SCENE));
        Scene gameSelect = new Scene(main);
        return gameSelect;
    }
}
