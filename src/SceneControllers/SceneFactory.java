package SceneControllers;

import Scenes.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneFactory {
    Stage stage;
    Scene primaryScene;
    Scene creditScene;
    Scene gameSelectScene;
    Scene crzRumCtrlScene;
    Scene crzRhyCtrlScene;
    Scene rhySongSelectScene;

    public SceneFactory(Stage stage) {
        this.stage = stage;
        primaryScene = new PrimaryScene().run(stage, null);
        creditScene = new CreditScene().run(stage, null);
        gameSelectScene = new GameSelectScene().run(stage, null);
        crzRumCtrlScene = new CrzRumCtrlScene().run(stage, null);
        crzRhyCtrlScene = new CrzRhyCtrlScene().run(stage, null);
        rhySongSelectScene = new RhySongSelectScene().run(stage, null);
    }
    public Scene getScene(SceneEnums sceneType, SceneTransferData data){
        switch (sceneType){
            case PRIMARY_SCENE -> {
                return primaryScene;
            }
            case CREDIT_SCREEN -> {
                return creditScene;
            }
            case GAME_SELECT -> {
                return gameSelectScene;
            }
            case CRZ_RUM_CTRL_SCREEN -> {
                return crzRumCtrlScene;
            }
            case CRZ_RHY_CTRL_SCREEN -> {
                return crzRhyCtrlScene;
            }
            case RHY_END_SCREEN -> {
                return new RhyEndScene().run(stage, data);
            }
            case RHY_SONG_SELECT -> {
                return rhySongSelectScene;
            }
            case RUM_CHAR_SELECT -> {
                return new RumCharSelectScene().run(stage, null);
            }
            case RUM_GAME -> {
                return new RumGameScene().run(stage, data);
            }
            case RHY_GAME -> {
                return new RhyGameScene().run(stage, data);
            }
        }
        return null;
    }

}
