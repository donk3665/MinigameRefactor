package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RhyEndScene extends MasterScene{
    GridPane P1grid = new GridPane();
    Label P1Score = new Label("Score");
    Label P1Combo = new Label("Combo");
    Label P1Accuracy = new Label("Accuracy");
    GridPane P2grid = new GridPane();
    Label P2Score = new Label("Score");
    Label P2Combo = new Label("Combo");
    Label P2Accuracy = new Label("Accuracy");


    public void initializeRhythmGrid(GridPane grid, Label score, Label combo, Label accuracy){
        grid.add(score, 0, 0);
        grid.add(combo, 0, 1);
        grid.add(accuracy, 0, 2);
    }
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data){
        long [][] scores = data.getScores();
        double [][] accuracy = data.getAccuracy();
        //setting images
        Image BackgroundImage = new Image("SceneAssets/rhyEndScreen/GameOverRhythmBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView background = new ImageView(BackgroundImage);

        //positioning grids
        P1grid.setTranslateX(880*widthAdjust);
        P1grid.setTranslateY(440*heightAdjust);
        P1grid.setVgap(80*heightAdjust);
        setLabelFont(P1Accuracy);
        setLabelFont(P1Combo);
        setLabelFont(P1Score);
        setLabelFont(P2Score);
        setLabelFont(P2Combo);
        setLabelFont(P2Accuracy);
        P2grid.setTranslateX(1200*widthAdjust);
        P2grid.setTranslateY(440*heightAdjust);
        P2grid.setVgap(80*heightAdjust);

        initializeRhythmGrid(P1grid, P1Score, P1Combo, P1Accuracy);
        initializeRhythmGrid(P2grid, P2Score, P2Combo, P2Accuracy);

        P1Score.setText(String.valueOf(scores[0][0]));
        P1Combo.setText(String.valueOf(scores[0][2]));
        P1Accuracy.setText(Math.round(accuracy[0][0] * 10000) / 100.0 +"%");
        P2Score.setText(String.valueOf(scores[1][0]));
        P2Combo.setText(String.valueOf(scores[1][2]));
        P2Accuracy.setText(Math.round(accuracy[1][0]*10000)/100.0+"%");
        Image winner;

        //displaying who won
        if (scores[0][0]>scores[1][0]) {
            winner = new Image("/fightingFiles/baseFiles/images/player1.png", 600*widthAdjust, 100*heightAdjust, false, false);
        }
        else{
            winner = new Image("/fightingFiles/baseFiles/images/player2.png", 600*widthAdjust, 100*heightAdjust, false, false);
        }
        ImageView playerWon = new ImageView(winner);
        playerWon.setX(850*widthAdjust);
        playerWon.setY(200*heightAdjust);

        //creating scene
        Group mainPane = new Group();
        mainPane.getChildren().addAll(background,playerWon,P1grid,P2grid,addTopAnchorPane(SceneEnums.RHY_SONG_SELECT));
        return new Scene(mainPane);
    }
    public void setLabelFont(Label string){
        string.setFont(new Font("Verdana", 30*widthAdjust));
    }
}
