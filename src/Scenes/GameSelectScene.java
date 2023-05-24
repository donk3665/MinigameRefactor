package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameSelectScene extends MasterScene{

    @Override
    public Scene run(Stage primaryStage, SceneTransferData data){

        //Setting up images
        Image BackgroundImage = new Image("buttonImages/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        Image ControlsButton = new Image("buttonImages/gameSelect/ControlsButton.png", 510*widthAdjust, 93*heightAdjust, false, false);
        Image CrazyRumbleButton = new Image("buttonImages/gameSelect/CrazyRumbleButton.png", 720*widthAdjust, 600*heightAdjust, false, false);
        Image CrazyRhythmButton = new Image("buttonImages/gameSelect/CrazyRhythmButton.png", 720*widthAdjust, 600*heightAdjust, false, false);

        // Creates background Image
        ImageView backgroundImage = new ImageView(BackgroundImage);

        //Crazy Rumble Button
        Button crazyRumble = createStandardButton(CrazyRumbleButton);

        // Crazy Rhythm Button
        Button crazyRhythm = createStandardButton(CrazyRhythmButton);

        // Creating Control Buttons
        Button crazyRumbleControls = createStandardButton(ControlsButton);
        Button crazyRhythmControls = createStandardButton(ControlsButton);

        //adding various buttons to GridPane
        GridPane games = new GridPane();
        games.add(crazyRumble, 0, 0, 1, 1);
        games.add(crazyRhythm, 1, 0, 1, 1);
        games.add(crazyRumbleControls, 0, 1, 1, 1);
        games.add(crazyRhythmControls, 1, 1, 1, 1);
        games.setVgap(10);

        // Centering the Control Buttons
        GridPane.setHalignment(crazyRumbleControls, HPos.CENTER);
        GridPane.setHalignment(crazyRhythmControls, HPos.CENTER);

        // Positioning
        games.setTranslateX(25*widthAdjust);
        games.setTranslateY(75*heightAdjust);

        // Button on click methods
        // Crazy Rumble Button
        crazyRumble.setOnAction(event-> controller.changeScenes(SceneEnums.RUM_CHAR_SELECT, null));

        // Crazy Rhythm Button
        crazyRhythm.setOnAction(event-> controller.changeScenes(SceneEnums.RHY_SONG_SELECT, null));

        // Controls Button
        crazyRumbleControls.setOnAction(event-> controller.changeScenes(SceneEnums.CRZ_RUM_CTRL_SCREEN, null));

        // Crazy Rhythm Control
        crazyRhythmControls.setOnAction(event-> controller.changeScenes(SceneEnums.CRZ_RHY_CTRL_SCREEN, null));

        // Creating main group to add into scene and adding back button
        Group main = new Group();
        main.getChildren().addAll(backgroundImage,games,addTopAnchorPane(SceneEnums.PRIMARY_SCENE));
        return new Scene(main);
    }
}
