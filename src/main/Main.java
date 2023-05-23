package main;

import Stages.SceneController;
import Stages.SceneEnums;
import Stages.SceneTransferData;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    /**
     * Entry point to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        SceneController mainController = new SceneController(stage);
        mainController.changeScenes(SceneEnums.PRIMARY_SCENE, null);
    }
    public void stop() {
		System.exit(0);
	}
}