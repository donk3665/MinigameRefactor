package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CrzRhyCtrlScene extends MasterScene{
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data){


        //Showing image which displays controls and back button
        Image RhyTutorial = new Image("buttonImages/tutorials/TutorialRhythm.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView rhythmTutorialScreen = new ImageView(RhyTutorial);
        Group main = new Group();
        main.getChildren().addAll(rhythmTutorialScreen, addTopAnchorPane(SceneEnums.GAME_SELECT));
        return new Scene(main);

    }
}
