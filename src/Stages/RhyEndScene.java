package Stages;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RhyEndScene extends MasterScene{

    @Override
    Scene run(Stage primaryStage, SceneTransferData data){
        scores = data.getScores();
        accuracy = data.getAccuracy();
        //setting images
        Image BackgroundImage = new Image("buttonImages/gameAssets/GameOverRhythmBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView background = new ImageView(BackgroundImage);

        //positioning grids
        P1grid.setTranslateX(880*widthAdjust);
        P1grid.setTranslateY(440*heightAdjust);
        P1grid.setVgap(80*heightAdjust);
        P1Accuracy.setFont(new Font("Verdana", 30*widthAdjust));
        P1Combo.setFont(new Font("Verdana", 30*widthAdjust));
        P1Score.setFont(new Font("Verdana", 30*widthAdjust));
        P2Score.setFont(new Font("Verdana", 30*widthAdjust));
        P2Combo.setFont(new Font("Verdana", 30*widthAdjust));
        P2Accuracy.setFont(new Font("Verdana", 30*widthAdjust));
        P2grid.setTranslateX(1200*widthAdjust);
        P2grid.setTranslateY(440*heightAdjust);
        P2grid.setVgap(80*heightAdjust);

        P1Score.setText(String.valueOf(scores[0][0]));
        P1Combo.setText(String.valueOf(scores[0][2]));
        P1Accuracy.setText(String.valueOf(Math.round(accuracy[0][0]*10000)/100.0)+"%");
        P2Score.setText(String.valueOf(scores[1][0]));
        P2Combo.setText(String.valueOf(scores[1][2]));
        P2Accuracy.setText(String.valueOf(Math.round(accuracy[1][0]*10000)/100.0)+"%");
        Image winner;

        //displaying who won
        if (scores[0][0]>scores[0][1]) {
            winner = new Image("main/fightingFiles/player1.png", 600*widthAdjust, 100*heightAdjust, false, false);
        }
        else{
            winner = new Image("main/fightingFiles/player1.png", 600*widthAdjust, 100*heightAdjust, false, false);
        }
        ImageView playerWon = new ImageView(winner);
        playerWon.setX(850*widthAdjust);
        playerWon.setY(200*heightAdjust);

        //creating scene
        Group mainPane = new Group();
        mainPane.getChildren().addAll(background,playerWon,P1grid,P2grid,addTopAnchorPane(SceneEnums.RHY_SONG_SELECT));
        Scene rhythmGameEndScreen = new Scene(mainPane);
        return rhythmGameEndScreen;
    }
}
