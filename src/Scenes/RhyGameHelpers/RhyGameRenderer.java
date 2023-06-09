package Scenes.RhyGameHelpers;


import Scenes.RhyGameScene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static Scenes.MasterScene.heightAdjust;
import static Scenes.MasterScene.widthAdjust;
import static Scenes.RhyGameScene.realBoardLength;

public class RhyGameRenderer {
    GridPane P1grid = new GridPane();
    Label P1Score = new Label("Score");
    Label P1Combo = new Label("Combo");
    Label P1Accuracy = new Label("Accuracy");
    GridPane P2grid = new GridPane();

    Label P2Score = new Label("Score");
    Label P2Combo = new Label("Combo");
    Label P2Accuracy = new Label("Accuracy");

    public long[][] getScores() {
        return scores;
    }

    public double[][] getAccuracy() {
        return accuracy;
    }

    long[][] scores;
    double [][] accuracy ;
    Group mainPane;
    RhyGameScene rhythmScene;
    LoadRhyGame loader;
    ImageView[] resourcePane;
    public void setImagePane(int index, Image resource){
        resourcePane[index].setImage(resource);
    }

    public RhyGameRenderer(RhyGameScene rhythmScene, LoadRhyGame loader, Canvas canvas){
        mainPane = new Group();
        this.rhythmScene = rhythmScene;
        this.loader = loader;
        scores = new long[2][3];
        accuracy = new double[2][2];
        P1grid.setTranslateX(30*widthAdjust);
        P1grid.setTranslateY(80*heightAdjust);
        P1grid.setVgap(100*heightAdjust);
        P1Accuracy.setFont(new Font("Verdana", 30*widthAdjust));
        P1Combo.setFont(new Font("Verdana", 30*widthAdjust));
        P1Score.setFont(new Font("Verdana", 30*widthAdjust));
        P2Score.setFont(new Font("Verdana", 30*widthAdjust));
        P2Combo.setFont(new Font("Verdana", 30*widthAdjust));
        P2Accuracy.setFont(new Font("Verdana", 30*widthAdjust));

        P2grid.setTranslateX(770*widthAdjust);
        P2grid.setTranslateY(80*heightAdjust);
        P2grid.setVgap(100*heightAdjust);

        initializeRhythmGrid(P1grid, P1Score, P1Combo, P1Accuracy);
        initializeRhythmGrid(P2grid, P2Score, P2Combo, P2Accuracy);

        //Initializing resources
        ImageView backgroundImage = new ImageView(loader.getBackgroundImage());

        resourcePane = loader.loadResourcePane();
        mainPane.getChildren().addAll(backgroundImage,canvas,resourcePane[0],resourcePane[1],P2grid,P1grid);
    }
    public Group getMainPane(){
        return mainPane;
    }

    /**
     * Function to initialize the rhythm grids
     */
    public void initializeRhythmGrid(GridPane grid, Label score, Label combo, Label accuracy){
        grid.add(score, 0, 0);
        grid.add(combo, 0, 1);
        grid.add(accuracy, 0, 2);
    }

    /**
     * This method draws the rhythm game; the notes falling down, the changing labels, and the hit positions
     * @param gc - graphics context to call
     */
    public void draw(GraphicsContext gc) {

        //declaring variables
        gc.clearRect(0, 0, 1536*widthAdjust, 864*heightAdjust);
        gc.setFill(Color.GOLD);
        ArrayList<ArrayList<Note>> temp;
        ArrayList<Note> temp2;
        double initialY;
        double endY;
        double spacing = 125;
        double boardSpace = 0;
        int divisionFactor=2;

        //changing value of labels
        P1Score.setText(String.valueOf(scores[0][0]));
        P1Combo.setText(String.valueOf(scores[0][1]));
        P1Accuracy.setText(Math.round(accuracy[0][0]*10000)/100.0+"%");
        P2Score.setText(String.valueOf(scores[1][0]));
        P2Combo.setText(String.valueOf(scores[1][1]));
        P2Accuracy.setText(Math.round(accuracy[1][0]*10000)/100.0+"%");
        temp=rhythmScene.getNotesNow();

        double displayWidth = 60;
        //displaying notes both player1's board and player2's board
        for (int w = 0; w<2; w++) {
            if (w==1) {
                temp=rhythmScene.getNotesNow2();
                boardSpace=751;
            }
            for (int i = 0; i<temp.size(); i++) {
                temp2= temp.get(i);

                for (Note note : temp2) {
                    initialY = (note.getInitialYPosition() / divisionFactor) - 20;
                    endY = note.getEndYPosition() / divisionFactor;
                    double displayX = (225 + i * spacing + boardSpace + (spacing - displayWidth) / 2) * widthAdjust;

                    if (note.getType() == 1 || note.getType() == 5) {
                        gc.setFill(Color.GOLD);
                        gc.fillOval(displayX, initialY * heightAdjust, displayWidth * widthAdjust, displayWidth * widthAdjust);
                    } else {
                        if (note.getInitialYPosition() <= realBoardLength + 4000 && note.getEndYPosition() >= 0) {
                            gc.setFill(Color.BLUE);
                            gc.fillOval(displayX, (initialY) * heightAdjust, displayWidth * widthAdjust, displayWidth * widthAdjust);
                            gc.fillRect(displayX, (endY + (displayWidth * widthAdjust) / 2) * heightAdjust, displayWidth * widthAdjust, (initialY - endY) * heightAdjust);
                            gc.fillOval(displayX, (endY) * heightAdjust, displayWidth * widthAdjust, displayWidth * widthAdjust);
                        } else if (note.getInitialYPosition() > realBoardLength + 4000 && note.getEndYPosition() >= 0) {
                            gc.setFill(Color.BLUE);
                            gc.fillRect(displayX, (endY + (displayWidth * widthAdjust) / 2) * heightAdjust, displayWidth * widthAdjust, ((realBoardLength + 4000.0) / divisionFactor - endY) * heightAdjust);
                            gc.fillOval(displayX, (endY) * heightAdjust, displayWidth * widthAdjust, displayWidth * widthAdjust);
                        } else if (note.getInitialYPosition() <= (realBoardLength + 4000) && note.getEndYPosition() < 0) {
                            gc.setFill(Color.BLUE);
                            gc.fillOval(displayX, (initialY) * heightAdjust, displayWidth * widthAdjust, displayWidth * widthAdjust);
                            gc.fillRect(displayX, (displayWidth * widthAdjust / 2) * heightAdjust, displayWidth * widthAdjust, initialY * heightAdjust);
                        } else {
                            gc.setFill(Color.BLUE);
                            gc.fillRect(displayX, 0, (displayWidth * widthAdjust), (realBoardLength + 4000.0) / divisionFactor * heightAdjust);
                        }

                    }
                }
            }
        }
        gc.setFill(Color.WHITE);

        //displaying the hit positions for both boards
        for (int i = 0; i<4; i++) {
            gc.fillOval((225+spacing*i+(spacing-displayWidth)/2)*widthAdjust, (realBoardLength/1.0/divisionFactor-20)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
            gc.fillOval((225+boardSpace+spacing*i+(spacing-displayWidth)/2)*widthAdjust, (realBoardLength/1.0/divisionFactor-20)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
        }

    }


}
