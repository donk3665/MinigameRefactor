package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RumCharSelectScene extends MasterScene {
    Label playerChosen;
    int counter;

    @Override
    public Scene run(Stage primaryStage, SceneTransferData data){


        //setting up images
        Image BackgroundImage = new Image("SceneAssets/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView backgroundImage = new ImageView(BackgroundImage);
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImage.fitHeightProperty().bind(primaryStage.heightProperty());
        Image box = new Image("SceneAssets/misc/BigBox.png", 600*widthAdjust, 630*heightAdjust, false, false);
        Image hibiki = new Image("fightingFiles/characters/hibiki/images/Hibiki.jpg", 344*widthAdjust, 436*heightAdjust, false, false);
        Image rock = new Image("fightingFiles/characters/rock/images/Rock.jpg", 344*widthAdjust, 436*heightAdjust, false, false);

        //layouts and styling for displaying buttons and characters
        GridPane grid = new GridPane();
        StackPane stack1 = new StackPane();
        stack1.getChildren().addAll(new ImageView(box), new ImageView(hibiki));
        Button character1 = new Button("",stack1);

        StackPane stack2 = new StackPane();
        stack2.getChildren().addAll(new ImageView(box), new ImageView(rock));
        Button character2 = new Button("",stack2);

        character1.setStyle("-fx-background-color: transparent;");
        character2.setStyle("-fx-background-color: transparent;");

        //Names of characters displaying under picture
        Label character1Name = new Label();
        character1Name.setFont(new Font("Verdana", 30*widthAdjust));
        character1Name.setTextFill(Color.WHITE);
        character1Name.setTranslateX(370*widthAdjust);
        character1Name.setTranslateY(650*heightAdjust);
        character1Name.setText("Hibiki Takane");

        Label character2Name = new Label();
        character2Name.setFont(new Font("Verdana", 30*widthAdjust));
        character2Name.setTextFill(Color.WHITE);
        character2Name.setTranslateX(970*widthAdjust);
        character2Name.setTranslateY(650*heightAdjust);
        character2Name.setText("Rock Howard");

        //displays player1 over character player1 has chosen
        playerChosen = new Label();
        playerChosen.setFont(new Font("Verdana", 30*widthAdjust));
        playerChosen.setTextFill(Color.WHITE);

        //object contains the characters to load
        String[] players = new String[2];

        //variable to get both players' character
        counter = 0;

        SceneTransferData transferData = new SceneTransferData();
        //event handler for clicking first button
        character1.setOnAction(event->{

            if (counter<1) {
                //player1 chose Hibiki; display player1's choice
                players[counter]="hibiki";
                counter++;
                playerChosen.setText("Player 1");
                playerChosen.setTranslateX(400*widthAdjust);
                playerChosen.setTranslateY(130*heightAdjust);
            }
            else {
                //player2 chose Hibiki(this means player1 has chosen); stop music and enter scene
                menuMusic.stop();
                players[counter]="hibiki";
                transferData.setPlayers(players);
                controller.changeScenes(SceneEnums.RUM_GAME,transferData);

            }
        });

        //event handler for clicking second button
        character2.setOnAction(event->{
            if (counter<1) {
                //player1 chose Rock; display player1's choice
                players[counter]="rock";
                counter++;
                playerChosen.setText("Player 1");
                playerChosen.setTranslateX(1000*widthAdjust);
                playerChosen.setTranslateY(130*heightAdjust);
            }
            else {
                //player2 chose Rock(this means player1 has chosen); stop music and enter scene
                menuMusic.stop();
                players[counter]="rock";
                transferData.setPlayers(players);
                controller.changeScenes(SceneEnums.RUM_GAME,transferData);

            }
        });
        //adding buttons to layout and centering
        grid.add(character1, 0,0,1,1);
        grid.add(character2, 1,0,1,1);
        grid.setTranslateX(150*widthAdjust);
        grid.setTranslateY(100*heightAdjust);

        //creating scene
        Group main = new Group();
        main.getChildren().addAll(backgroundImage,grid, addTopAnchorPane(SceneEnums.GAME_SELECT),playerChosen,character1Name,character2Name);
        return new Scene(main);
    }
}
