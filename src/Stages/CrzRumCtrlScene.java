package Stages;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CrzRumCtrlScene extends MasterScene{
//    public CrzRumCtrlScene(SceneFactory factory) {
//        super(factory);
//    }

    @Override
    Scene run(Stage primaryStage, SceneTransferData data) {

        //Showing image which displays controls and back button
        Image RumTutorial = new Image("buttonImages/tutorials/RumbleTutorial.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView rumbletutscreen = new ImageView(RumTutorial);
        Group main = new Group();
        main.getChildren().addAll(rumbletutscreen, addTopAnchorPane(SceneEnums.GAME_SELECT));
        Scene crazyRumCtrlScreen = new Scene(main);
        return crazyRumCtrlScreen;
    }
}
