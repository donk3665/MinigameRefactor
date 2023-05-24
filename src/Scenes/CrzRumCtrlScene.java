package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CrzRumCtrlScene extends MasterScene{
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {

        //Showing image which displays controls and back button
        Image RumTutorial = new Image("buttonImages/tutorials/RumbleTutorial.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView rumbleTutorialScreen = new ImageView(RumTutorial);
        Group main = new Group();
        main.getChildren().addAll(rumbleTutorialScreen, addTopAnchorPane(SceneEnums.GAME_SELECT));
        return new Scene(main);
    }
}
