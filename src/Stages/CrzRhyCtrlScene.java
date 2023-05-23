package Stages;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CrzRhyCtrlScene extends MasterScene{
//    public CrzRhyCtrlScene(SceneFactory factory) {
//        super(factory);
//    }

    @Override
    Scene run(Stage primaryStage, SceneTransferData data){


        //Showing image which displays controls and back button
        Image RhyTutorial = new Image("buttonImages/tutorials/TutorialRhythm.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView rhythmtutscreen = new ImageView(RhyTutorial);
        Group main = new Group();
        main.getChildren().addAll(rhythmtutscreen, addTopAnchorPane(SceneEnums.GAME_SELECT));
        Scene crazyRhyCtrlScreen = new Scene(main);
        return crazyRhyCtrlScreen;

    }
}
