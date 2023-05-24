package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreditScene extends MasterScene{

    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {
        Scene creditScreen;

        //Setting up images
        Image BackgroundImage = new Image("buttonImages/mainMenu/CreditScreen.png", 1536*widthAdjust,864*heightAdjust , false, false);
        ImageView Background = 	new ImageView(BackgroundImage);

        // Creating main group to add into scene and adding back button
        Pane mainGroup = new Pane();
        mainGroup.getChildren().addAll(Background,addTopAnchorPane(SceneEnums.PRIMARY_SCENE));
        creditScreen = new Scene(mainGroup);
        return creditScreen;
    }
}
