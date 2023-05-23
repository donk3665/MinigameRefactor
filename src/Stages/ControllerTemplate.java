package Stages;

import javafx.stage.Stage;

abstract class ControllerTemplate {
    Stage currentStage;
    public ControllerTemplate(Stage currentStage){
        this.currentStage = currentStage;
    }
    abstract void changeScenes(SceneEnums scene, SceneTransferData data);
}
