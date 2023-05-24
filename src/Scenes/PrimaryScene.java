package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class PrimaryScene extends MasterScene{

    @Override
    public Scene run(Stage primaryStage, SceneTransferData data){
        Scene mainMenuScreen;

        //playing the main menu music looped
        File file = new File("src/buttonImages/otherAssets/Main_Menu.mp3");
        System.out.println(file.toURI());
        Media media = null;
        try {
            media = new Media(file.toURI().toString());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("HERE");
        menuMusic = new MediaPlayer(media);
        menuMusic.play();
        menuMusic.setOnEndOfMedia(() -> {
            menuMusic.stop();
            menuMusic.play();
        });
        //setting up the stage to be a maximized, borderless window
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Crazy MiniGames");
        primaryStage.setMaximized(true);

        //setting up graphics and groups
        Image BackgroundImage = new Image("buttonImages/mainMenu/Background.png", 1536*widthAdjust,864*heightAdjust , false, false);
        Image Logo = new Image("buttonImages/mainMenu/Logo.png", 1070*widthAdjust, 418*heightAdjust, false, false);
        Image PlayButton = new Image("buttonImages/mainMenu/PlayButton.png", 583.2*widthAdjust, 130*heightAdjust, false, false);
        Image CreditsButton = new Image("buttonImages/mainMenu/CreditsButton.png", 396.8*widthAdjust, 78*heightAdjust, false, false);
        Image QuitButton = new Image("buttonImages/mainMenu/QuitButton.png", 308*widthAdjust, 60*heightAdjust, false, false);
        Pane mainGroup = new Pane();
        ImageView Background = 	new ImageView(BackgroundImage);
        Group back = new Group();
        back.getChildren().addAll(Background);
        ImageView LogoScreen = new ImageView(Logo);
        LogoScreen.setTranslateX(400*widthAdjust);
        LogoScreen.setTranslateY(50*heightAdjust);
        // 3 Buttons

        // Play Button
        Button playButton = new Button();
        ImageView PlayBut = new ImageView(PlayButton);
        playButton.setGraphic(PlayBut);
        playButton.setStyle("-fx-background-color: transparent;");

        // Credit Button
        Button creditButton = new Button();
        ImageView creditBut = new ImageView(CreditsButton);
        creditButton.setGraphic(creditBut);
        creditButton.setStyle("-fx-background-color: transparent;");

        // Quit Button
        Button quitButton = new Button();
        ImageView quitBut = new ImageView(QuitButton);
        quitButton.setGraphic(quitBut);
        quitButton.setStyle("-fx-background-color: transparent;");

        // Adding Buttons to a Grid Pane
        GridPane grid = new GridPane();
        grid.add(playButton, 0,0,1,1);
        grid.add(creditButton, 0, 1, 1, 1);
        grid.add(quitButton, 0, 2,1,1);
        GridPane.setHalignment(creditButton, HPos.RIGHT);
        GridPane.setHalignment(quitButton, HPos.RIGHT);
        grid.setVgap(10);

        // Setting Location of Grid Pane
        grid.setTranslateX(850*widthAdjust);
        grid.setTranslateY(475*heightAdjust);


        //adding elements to mainGroup
        mainGroup.getChildren().add(back);
        mainGroup.getChildren().add(LogoScreen);
        mainGroup.getChildren().add(grid);


        // Button interaction when hovered over
        playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            Scale buttonScale = new Scale(0.8, 0.8);
            buttonScale.setPivotX(playButton.getWidth()/2);
            buttonScale.setPivotY(playButton.getHeight()/2);
            playButton.getTransforms().add(buttonScale);
        });

        playButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            Scale buttonScale = new Scale(1.25, 1.25);
            buttonScale.setPivotX(playButton.getWidth() / 2);
            buttonScale.setPivotY(playButton.getHeight() / 2);
            playButton.getTransforms().add(buttonScale);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {

            }
        });

        //button function
        playButton.setOnAction(event -> controller.changeScenes(SceneEnums.GAME_SELECT, null));
        creditButton.setOnAction(event -> controller.changeScenes(SceneEnums.CREDIT_SCREEN, null));
        quitButton.setOnAction(event -> System.exit(0));

        //setting up scene object and showing scene
        mainMenuScreen = new Scene(mainGroup,1536*widthAdjust,864*heightAdjust);
        primaryStage.setScene(mainMenuScreen);
        primaryStage.show();
        primaryStage.toFront();
        return mainMenuScreen;
    }
}
