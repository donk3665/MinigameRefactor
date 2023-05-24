package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.Animation;
import main.Attack;
import main.FightingCharacter;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class RumGameScene extends MasterScene{


    //variables and objects for the fighting game
    static FightingCharacter[] characters = new FightingCharacter[2];
    static double downBorder = 730;
    static double rightBorder = 1400;
    static int leftBorder = 100;
    static byte[][] movement = new byte[2][4];
    ImageView[] characterDraw = new ImageView[2];
    int[] playerTimer = new int[2];
    boolean[][] attacks = new boolean[2][11];
    static int []attackIndex = new int[2];
    Rectangle[]healthBar = new Rectangle[2];
    static double[][] tempHold;
    static ArrayList<String> inputs = new ArrayList<>();
    static ArrayList<Integer> inputsTime = new ArrayList<>();
    static ArrayList<String> inputs2 = new ArrayList<>();
    static ArrayList<Integer> inputsTime2 = new ArrayList<>();
    static int inputTimer = 0;
    static int[] hitStun = new int[2];
    int [] playerCounter = new int[2];
    boolean endGame = false;
    int timerInterval = 0;
    int[] animationCounter = new int[2];
    ImageView[] resourcePane;
    Image [] resources;
    int logoVanishCounter = 0;
    static AudioClip[] hitSounds;
    MediaPlayer backgroundMusic;
    static AudioClip[] narrator;
    int[][] frameTotals = new int[2][8];
    static int[][] frameCounters = new int[2][7];
    int[] frameCounterIndex = {0,0};


    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {

        //Initializing resources
        Image BackgroundImage = new Image("SceneAssets/rumGame/rumbleBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView backgroundImage = new ImageView(BackgroundImage);
        try {
            initFighting(data.getPlayers());
        }
        catch (Exception e){
            System.err.println("initialization error");
            System.exit(1);
        };
        Group mainPane = new Group();

        //Initializing all UI Images
        playerCounter[0]=-1;
        playerCounter[1]=-1;
        resources = new Image[15];
        resources[3] = new Image("fightingFiles/baseFiles/images/count1.png");
        resources[4] = new Image("fightingFiles/baseFiles/images/count2.png");
        resources[5] = new Image("fightingFiles/baseFiles/images/count3.png");
        resources[6] = new Image("fightingFiles/baseFiles/images/fight.png");
        resources[7] = new Image("fightingFiles/baseFiles/images/ko.png");
        resources[0] = new Image("fightingFiles/baseFiles/images/round1.png");
        resources[1] = new Image("fightingFiles/baseFiles/images/round2.png");
        resources[2] = new Image("fightingFiles/baseFiles/images/round3.png");
        resources[8] = new Image("fightingFiles/baseFiles/images/empty.png");
        resources[9] = new Image("fightingFiles/baseFiles/images/player1.png");
        resources[10] = new Image("fightingFiles/baseFiles/images/player2.png");
        resources[11] = new Image("SceneAssets/rumGame/RumblePlayerOne1.png");
        resources[12] = new Image("SceneAssets/rumGame/RumblePlayerOne2.png");
        resources[13] = new Image("SceneAssets/rumGame/RumblePlayerTwo1.png");
        resources[14] = new Image("SceneAssets/rumGame/RumblePlayerTwo2.png");

        resourcePane = new ImageView[5];
        resourcePane[0] = new ImageView(resources[8]);

        resourcePane[1]= new ImageView(resources[11]);
        resourcePane[1].setFitWidth(500*widthAdjust);
        resourcePane[1].setFitHeight(150*heightAdjust);

        resourcePane[2]= new ImageView(resources[12]);
        resourcePane[2].setFitWidth(500*widthAdjust);
        resourcePane[2].setFitHeight(150*heightAdjust);

        resourcePane[3]= new ImageView(resources[13]);
        resourcePane[3].setFitWidth(500*widthAdjust);
        resourcePane[3].setFitHeight(150*heightAdjust);
        resourcePane[3].setX(1035*widthAdjust);

        resourcePane[4]= new ImageView(resources[14]);
        resourcePane[4].setFitWidth(500*widthAdjust);
        resourcePane[4].setFitHeight(150*heightAdjust);
        resourcePane[4].setX(1035*widthAdjust);

        Canvas canvas = new Canvas(1536*widthAdjust,864*heightAdjust);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);

        resourcePane[0].setX(500*widthAdjust);
        resourcePane[0].setY(200*heightAdjust);
        resourcePane[0].setFitWidth(500*widthAdjust);
        resourcePane[0].setFitHeight(400*heightAdjust);


        hitSounds = new AudioClip[9];
        //Initializing all sound effects
        for (int i = 0; i< 9; i++){
            hitSounds[i]= new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/hit_"+i+".wav")).toExternalForm());
            hitSounds[i].setVolume(0.4);
        }


        narrator = new AudioClip[10];

        narrator[0] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Round1.mp3")).toExternalForm());
        narrator[1] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Round2.mp3")).toExternalForm());
        narrator[2] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Round3.mp3")).toExternalForm());
        narrator[3] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Three.mp3")).toExternalForm());
        narrator[4] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Two.mp3")).toExternalForm());
        narrator[5] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/One.mp3")).toExternalForm());
        narrator[6] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/KO.mp3")).toExternalForm());
        narrator[7] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Fight.mp3")).toExternalForm());
        narrator[8] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/player1win.wav")).toExternalForm());
        narrator[9] = new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/player2win.wav")).toExternalForm());

        for (int i = 0; i< 8; i++){
            narrator[i].setVolume(0.4);
        }


        //refreshes the screen
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                draw2(gc);
            }
        };


        //Initializing objects and centering images
        Timer timer2 = new Timer();
        characterDraw[0] = new ImageView();
        characterDraw[1] = new ImageView();

        characterDraw[0].setImage(characters[0].getImageCharacter());

        characters[1].setFacingRight(-1);
        characterDraw[0].setFitWidth(characters[0].getDimensionX()*widthAdjust);
        characterDraw[0].setFitHeight(characters[0].getDimensionY()*heightAdjust);

        characterDraw[1].setImage(characters[1].getImageCharacter());
        characterDraw[1].setFitWidth(characters[1].getDimensionX()*widthAdjust);
        characterDraw[1].setFitHeight(characters[1].getDimensionY()*heightAdjust);

        Pane box = new Pane();
        box.getChildren().add(characterDraw[0]);
        box.getChildren().add(characterDraw[1]);

        mainPane.getChildren().addAll(backgroundImage,box,resourcePane[1],resourcePane[3],canvas,resourcePane[0],resourcePane[2],resourcePane[4]);
        Scene fightingGame = new Scene(mainPane);

        timer.start();

        //Media music = new Media(new File("src/main/fightingFiles/Blue_Water_Blue_Sky.mp3").toURI().toString());
        Media music = new Media(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/Blue_Water_Blue_Sky.mp3")).toExternalForm());
        backgroundMusic = new MediaPlayer(music);
        backgroundMusic.setVolume(0.4);


        //event handler for keyboard presses
        fightingGame.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();

            /*
             * These if statements check for specific input and record it. Input for movement and attacks
             * are all processed here. Variables are in place to keep track of inputs and prevent input errors.
             */

            if (keyCode.equals(KeyCode.S)&& !characters[0].getKeyReady(0)) {
                movement[0][0]=2;
                if (inputTimer != 0) {
                    inputs.add("s");
                    inputsTime.add(inputTimer);
                }
                characters[0].setKeyReady(0, true);

            }

            if (keyCode.equals(KeyCode.D)&& !characters[0].getKeyReady(1)) {

                if (movement[0][2] != 2) {

                    movement[0][1]=2;
                    if (inputTimer != 0) {
                        inputs.add("d");
                        inputsTime.add(inputTimer);
                    }
                    characters[0].setKeyReady(1, true);

                }
            }
            if (keyCode.equals(KeyCode.A)&& !characters[0].getKeyReady(2)) {

                if (movement[0][1] != 2) {

                    movement[0][2]=2;
                    if (inputTimer != 0) {
                        inputs.add("a");
                        inputsTime.add(inputTimer)
                        ;}
                    characters[0].setKeyReady(2, true);
                }

            }
            if (keyCode.equals(KeyCode.W)&& !characters[0].getKeyReady(3) &&characters[0].getCentreY()>=downBorder) {

                movement[0][3]=2;
                if (inputTimer != 0) {
                    inputs.add("w");
                    inputsTime.add(inputTimer);
                }
                characters[0].setKeyReady(3, true);

            }
            if (keyCode.equals(KeyCode.Y)&& !characters[0].getKeyReady(4) && characters[0].getIsCrouching()) {

                attacks[0][2]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 2;
                }
                characters[0].setKeyReady(4, true);

            }
            if (keyCode.equals(KeyCode.Y)&& !characters[0].getKeyReady(4) && characters[0].getCentreY()<downBorder) {

                attacks[0][4]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 4;
                }
                characters[0].setKeyReady(4, true);

            }
            if (keyCode.equals(KeyCode.Y)&& !characters[0].getKeyReady(4) && characters[0].getCentreY()>=downBorder) {

                attacks[0][0]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 0;
                }
                characters[0].setKeyReady(4, true);

            }
            if (keyCode.equals(KeyCode.U)&& !characters[0].getKeyReady(5) && characters[0].getIsCrouching()) {

                attacks[0][3]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 3;
                }
                characters[0].setKeyReady(5, true);

            }
            if (keyCode.equals(KeyCode.U)&& !characters[0].getKeyReady(5) && characters[0].getCentreY()<downBorder) {

                attacks[0][5]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 5;
                }
                characters[0].setKeyReady(5, true);

            }
            if (keyCode.equals(KeyCode.U)&& !characters[0].getKeyReady(5) && characters[0].getCentreY()>=downBorder) {

                attacks[0][1]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 1;
                }
                characters[0].setKeyReady(5, true);

            }
            if (keyCode.equals(KeyCode.I)&& !characters[0].getKeyReady(6) && characters[0].getCentreY()>=downBorder && movement[0][2] == 2) {
                characters[0].setXSpeed(0);

                if (characters[0].getFacingRight()==1) {
                    attacks[0][8]=true;
                    if (!attacks[0][attackIndex[0]]) {
                        attackIndex[0] = 8;
                    }
                }
                else {
                    attacks[0][9]=true;
                    if (!attacks[0][attackIndex[0]]) {
                        attackIndex[0] = 9;
                    }
                }

                characters[0].setKeyReady(6, true);

            }
            if (keyCode.equals(KeyCode.I)&& !characters[0].getKeyReady(6) && characters[0].getCentreY()>=downBorder && movement[0][1] == 2) {
                characters[0].setXSpeed(0);
                if (characters[0].getFacingRight()==1) {
                    attacks[0][9]=true;
                    if (!attacks[0][attackIndex[0]]) {
                        attackIndex[0] = 9;
                    }
                }
                else {
                    attacks[0][8]=true;
                    if (!attacks[0][attackIndex[0]]) {
                        attackIndex[0] = 8;
                    }
                }
                characters[0].setKeyReady(6, true);


            }
            if (keyCode.equals(KeyCode.I)&& !characters[0].getKeyReady(6) && characters[0].getIsCrouching()) {

                attacks[0][7]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 7;
                }
                characters[0].setKeyReady(6, true);

            }
            if (keyCode.equals(KeyCode.I)&& !characters[0].getKeyReady(6) && characters[0].getCentreY()<downBorder) {

                attacks[0][10]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 10;
                }
                characters[0].setKeyReady(6, true);

            }
            if (keyCode.equals(KeyCode.I)&& !characters[0].getKeyReady(6) && characters[0].getCentreY()>=downBorder) {

                attacks[0][6]=true;
                if (!attacks[0][attackIndex[0]]) {
                    attackIndex[0] = 6;
                }
                characters[0].setKeyReady(6, true);

            }

            if (keyCode.equals(KeyCode.DOWN)&& !characters[1].getKeyReady(0)) {


                movement[1][0]=2;
                if (inputTimer != 0) {
                    inputs2.add("s");
                    inputsTime2.add(inputTimer);
                }
                characters[1].setKeyReady(0, true);

            }

            if (keyCode.equals(KeyCode.RIGHT)&& !characters[1].getKeyReady(1)) {

                if (movement[1][2] != 2) {

                    movement[1][1]=2;
                    if (inputTimer != 0) {
                        inputs2.add("d");
                        inputsTime2.add(inputTimer);
                    }
                    characters[1].setKeyReady(1, true);

                }
            }

            if (keyCode.equals(KeyCode.LEFT)&& !characters[1].getKeyReady(2)) {

                if (movement[1][1] != 2) {

                    movement[1][2]=2;
                    if (inputTimer != 0) {
                        inputs2.add("a");
                        inputsTime2.add(inputTimer);
                    }
                    characters[1].setKeyReady(2, true);
                }

            }

            if (keyCode.equals(KeyCode.UP)&& !characters[1].getKeyReady(3) &&characters[1].getCentreY()>=downBorder) {



                movement[1][3]=2;
                if (inputTimer != 0) {
                    inputs2.add("w");
                    inputsTime2.add(inputTimer);
                }
                characters[1].setKeyReady(3, true);

            }
            if (keyCode.equals(KeyCode.COMMA)&& !characters[1].getKeyReady(4) && characters[1].getIsCrouching()) {

                attacks[1][2]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 2;
                }
                characters[1].setKeyReady(4, true);

            }
            if (keyCode.equals(KeyCode.COMMA)&& !characters[1].getKeyReady(4) && characters[1].getCentreY()<downBorder) {

                attacks[1][4]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 4;
                }
                characters[1].setKeyReady(4, true);

            }
            if (keyCode.equals(KeyCode.COMMA)&& !characters[1].getKeyReady(4) && characters[1].getCentreY()>=downBorder) {

                attacks[1][0]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 0;
                }
                characters[1].setKeyReady(4, true);

            }
            if (keyCode.equals(KeyCode.PERIOD)&& !characters[1].getKeyReady(5) && characters[1].getIsCrouching()) {

                attacks[1][3]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 3;
                }
                characters[1].setKeyReady(5, true);

            }
            if (keyCode.equals(KeyCode.PERIOD)&& !characters[1].getKeyReady(5) && characters[1].getCentreY()<downBorder) {

                attacks[1][5]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 5;
                }
                characters[1].setKeyReady(5, true);

            }
            if (keyCode.equals(KeyCode.PERIOD)&& !characters[1].getKeyReady(5) && characters[1].getCentreY()>=downBorder) {

                attacks[1][1]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 1;
                }
                characters[1].setKeyReady(5, true);

            }
            if (keyCode.equals(KeyCode.SLASH)&& !characters[1].getKeyReady(6) && characters[1].getIsCrouching()) {

                attacks[1][7]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 7;
                }
                characters[1].setKeyReady(6, true);

            }
            if (keyCode.equals(KeyCode.SLASH)&& !characters[1].getKeyReady(6) && characters[1].getCentreY()<downBorder) {

                attacks[1][10]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 10;
                }
                characters[1].setKeyReady(6, true);

            }
            if (keyCode.equals(KeyCode.SLASH)&& !characters[1].getKeyReady(6) && characters[1].getCentreY()>=downBorder && movement[1][1] == 2) {
                characters[1].setXSpeed(0);

                if (characters[1].getFacingRight()==-1) {

                    attacks[1][8]=true;
                    if (!attacks[1][attackIndex[1]]) {
                        attackIndex[1] = 8;
                    }
                    characters[1].setKeyReady(6, true);
                }
                else {
                    attacks[1][9]=true;
                    if (!attacks[1][attackIndex[1]]) {
                        attackIndex[1] = 9;
                    }
                    characters[1].setKeyReady(6, true);
                }

            }
            if (keyCode.equals(KeyCode.SLASH)&& !characters[1].getKeyReady(6) && characters[1].getCentreY()>=downBorder&& movement[1][2] == 2) {
                characters[1].setXSpeed(0);
                if (characters[1].getFacingRight()==-1) {
                    attacks[1][9]=true;
                    if (!attacks[1][attackIndex[1]]) {
                        attackIndex[1] = 9;
                    }
                    characters[1].setKeyReady(6, true);

                }
                else {
                    attacks[1][8]=true;
                    if (!attacks[1][attackIndex[1]]) {
                        attackIndex[1] = 8;
                    }
                    characters[1].setKeyReady(6, true);
                }

            }
            if (keyCode.equals(KeyCode.SLASH)&& !characters[1].getKeyReady(6) && characters[1].getCentreY()>=downBorder) {

                attacks[1][6]=true;
                if (!attacks[1][attackIndex[1]]) {
                    attackIndex[1] = 6;
                }
                characters[1].setKeyReady(6, true);


            }
        });
        //event handler for keyboard releases
        fightingGame.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();

            /*
             * These if statements also handle movement and prevent input errors
             */

            if (keyCode.equals(KeyCode.S)) {
                movement[0][0]=1;
                characters[0].setKeyReady(0, false);
            }
            if (keyCode.equals(KeyCode.D)) {
                movement[0][1]=1;
                characters[0].setKeyReady(1, false);
            }
            if (keyCode.equals(KeyCode.A)) {
                movement[0][2]=1;
                characters[0].setKeyReady(2, false);
            }
            if (keyCode.equals(KeyCode.W)) {
                characters[0].setKeyReady(3, false);
            }
            if (keyCode.equals(KeyCode.Y)) {
                characters[0].setKeyReady(4, false);
            }
            if (keyCode.equals(KeyCode.U)) {
                characters[0].setKeyReady(5, false);
            }
            if (keyCode.equals(KeyCode.I)) {
                characters[0].setKeyReady(6, false);
            }


            if (keyCode.equals(KeyCode.DOWN)) {
                movement[1][0]=1;
                characters[1].setKeyReady(0, false);
            }
            if (keyCode.equals(KeyCode.RIGHT)) {
                movement[1][1]=1;
                characters[1].setKeyReady(1, false);
            }
            if (keyCode.equals(KeyCode.LEFT)) {
                movement[1][2]=1;
                characters[1].setKeyReady(2, false);
            }
            if (keyCode.equals(KeyCode.UP)) {
                movement[1][3]=1;
                characters[1].setKeyReady(3, false);
            }
            if (keyCode.equals(KeyCode.COMMA)) {
                characters[1].setKeyReady(4, false);
            }
            if (keyCode.equals(KeyCode.PERIOD)) {
                characters[1].setKeyReady(5, false);
            }
            if (keyCode.equals(KeyCode.SLASH)) {
                characters[1].setKeyReady(6, false);
            }


        });
        /*
         * Plays the media on game start, loops when media finishes
         */
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                backgroundMusic.play();

                backgroundMusic.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        backgroundMusic.stop();
                        backgroundMusic.play();
                    }
                });
            }
        },0);
        /*
         * Main game loop
         */
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //if statement entered when one of the characters runs out of hp (or when game is initialized)
                if (healthBar[1].getWidth()<=0||healthBar[0].getWidth()<=0||timerInterval>0) {


                    if (timerInterval==0) {

                        //adds points to first and/or second player, based on whoever won the round
                        if (healthBar[1].getWidth()<=0&&healthBar[0].getWidth()<=0) {
                            playerCounter[1]++;
                            playerCounter[0]++;
                        }
                        else if (healthBar[1].getWidth()<=0) {
                            playerCounter[0]++;
                        }
                        else {
                            playerCounter[1]++;

                        }


                        if (playerCounter[1]!=0||playerCounter[0]!=0) {
                            //if one player has points, show an image and play a sound
                            resourcePane[0].setImage(resources[7]);
                            narrator[6].play();
                        }
                        if (playerCounter[1]==0&&playerCounter[0]==0) {
                            //if this is during the initialization of the game, skip to player animations
                            timerInterval=960;
                        }
                        //setting up variables
                        animationCounter[0] = 0;
                        animationCounter[1] = 0;
                        healthBar[1].setWidth(500);
                        healthBar[0].setWidth(500);
                        timerInterval+=20;
                    }
                    else {
                        timerInterval+=20;
                        if (timerInterval%80==0&&timerInterval>1000&&(playerCounter[0]==0&&playerCounter[1]==0)) {
                            //if this is during initialization of the game, show beginning animations
                            for (int q = 0; q<2; q++) {

                                //preventing interference with drawing method
                                characters[q].setState(10);

                                //drawing the animation
                                characters[q].setImageDisplay(characters[q].getStartingAnimation().getFrame(animationCounter[q]));
                                if (animationCounter[q]<characters[q].getStartingAnimation().getAnimationTime()-1) {
                                    animationCounter[q]++;
                                }
                                else {
                                    characters[q].setState(0);
                                }
                            }
                        }
                        if (timerInterval==1000&&(playerCounter[0]!=2&&playerCounter[1]!=2)) {
                            //show image and play a sound
                            resourcePane[0].setImage(resources[playerCounter[0]+playerCounter[1]]);
                            narrator[playerCounter[0]+playerCounter[1]].play();

                            //reseting characters after round end
                            int e = 1;
                            for (int character = 0; character<2; character++) {
                                if (character==1) {
                                    e=0;
                                    characters[character].setFacingRight(-1);
                                }
                                else {
                                    characters[character].setFacingRight(1);
                                }
                                characters[character].setImageDisplay(characters[character].getImageCharacter());
                                characters[character].setXSpeed(0);
                                characters[character].setYSpeed(0);
                                characters[character].setX(Math.abs((e*leftBorder+100)+character*(rightBorder-100)-characters[character].getWidth()));
                                characters[character].setY(downBorder-(characters[character].getHeight()/2));
                                characters[character].removeHitBox(characters[character].getHitBoxCounter());
                                characters[character].setHitBoxCounter(0);
                                characters[character].removeHurtBox(characters[character].getTotalHurtBoxCounter());
                                characters[character].setHurtBoxCounter(0);
                                characters[character].addHurtBox(characters[character].getBaseHurtBox());
                                characters[character].getHurtBox(0).setX(characters[character].getX());
                                characters[character].getHurtBox(0).setY(characters[character].getY());
                                characters[character].setIsCrouching(false);
                                characters[character].setIsActionable(true);
                                characters[character].setPlayerTimer(0);
                                characters[character].setHurtBoxCounter(0);
                                characters[character].setHitCharacter(false);
                                characters[character].setState(0);
                                hitStun[character]=0;
                            }

                        }
                        else if (timerInterval ==2500) {

                            if (playerCounter[1]==2||playerCounter[0]==2) {
                                //play sound and display image if a player won
                                resourcePane[0].setFitHeight(200*heightAdjust);
                                resourcePane[0].setFitWidth(600*widthAdjust);
                                if (playerCounter[1]==2) {
                                    resourcePane[0].setImage(resources[10]);
                                    narrator[9].play();
                                }
                                else {
                                    resourcePane[0].setImage(resources[9]);
                                    narrator[8].play();
                                }
                            }
                            else {
                                //play normal countdown sound and image
                                resourcePane[0].setImage(resources[5]);
                                narrator[3].play();
                            }
                        }
                        else if (timerInterval ==3500&&(playerCounter[0]!=2&&playerCounter[1]!=2)) {
                            //play normal countdown sound and image
                            resourcePane[0].setImage(resources[4]);
                            narrator[4].play();

                        }
                        else if (timerInterval==4500&&(playerCounter[0]!=2&&playerCounter[1]!=2)) {
                            //play normal countdown sound and image
                            resourcePane[0].setImage(resources[3]);
                            narrator[5].play();
                        }
                        else if (timerInterval==5500) {
                            if (playerCounter[1]==2||playerCounter[0]==2) {

                                //if player won, return to character select screen, suspend all timers
                                endGame=true;
                                timer.stop();
                                timer2.cancel();
                                backgroundMusic.stop();
                                Platform.runLater(() -> {
                                    menuMusic.play();
                                    SceneTransferData transferData = new SceneTransferData();

                                    controller.changeScenes(SceneEnums.RUM_CHAR_SELECT, transferData);
                                });
                            }
                            else {
                                //reset player inputs
                                for (int character= 0; character<2; character++) {
                                    for (int count = 0; count<7;count++) {
                                        characters[character].setKeyReady(count, false);
                                    }
                                }
                                frameCounters = new int[2][7];
                                attacks = new boolean[2][11];
                                movement = new byte[2][4];

                                //display a image and sound, allow game to start
                                resourcePane[0].setImage(resources[6]);
                                logoVanishCounter+=20;
                                timerInterval=0;
                                narrator[7].play();
                            }
                        }


                    }

                }
                else {

                    //gravity and friction calculations
                    for (int i = 0; i<2; i++) {
                        characters[i].getHurtBox(0).setX(characters[i].getX());
                        characters[i].getHurtBox(0).setY(characters[i].getY());
                        if (characters[i].getCentreY()<downBorder) {
                            characters[i].changeYSpeed(1);
                        }
                        else if (characters[i].getCentreY()>=downBorder){
                            characters[i].setJump(false);
                            if (characters[i].getXSpeed()>0){
                                characters[i].changeXSpeed(-1);
                            }
                            if (characters[i].getXSpeed()<0){
                                characters[i].changeXSpeed(1);
                            }
                        }

                    }

                    facingDirection();
                    attackingRevamped(0, attacks[0], attackIndex[0]);
                    attackingRevamped(1, attacks[1], attackIndex[1]);
                    damageCalculation(healthBar);

                    //hitstun calculations (how long a player cannot do any actions due to being hit)
                    for (int i = 0; i < 2; i++) {
                        if (hitStun[i] > 0) {
                            characters[i].setIsActionable(false);
                            characters[i].setState(2);
                            hitStun[i]--;
                            if (hitStun[i]==0) {
                                characters[i].setIsActionable(true);
                                characters[i].setIsBlocking(1, false);
                                characters[i].setIsBlocking(0, false);
                                characters[i].setState(0);
                            }
                        }
                    }

                    movement(0,movement[0]);
                    movement(1,movement[1]);


                    //holds most recent inputs, decays over time
                    inputTimer++;
                    if (inputsTime.contains(inputTimer))
                    {
                        int index = inputsTime.indexOf(inputTimer);
                        inputsTime.remove(index);
                        inputs.remove(index);
                    }
                    if (inputsTime2.contains(inputTimer))
                    {
                        int index = inputsTime2.indexOf(inputTimer);
                        inputsTime2.remove(index);
                        inputs2.remove(index);
                    }
                    if (inputTimer == 20)
                    {
                        inputTimer = 0;
                    }
                }

                //removes fight logo after 1000 milliseconds
                if (logoVanishCounter>0) {
                    if (logoVanishCounter>=1000) {
                        resourcePane[0].setImage(resources[8]);
                        logoVanishCounter=0;
                    }
                    else {
                        logoVanishCounter+=20;
                    }
                }
            }

        }, 0, 20);




        return fightingGame;
    }
    /**
     * This function looks at each hitbox that is active and compares them with every active hurtbox.
     * If a hitbox overlaps a hurtbox and the opponent isn't blocking, the opponent is hurt and is
     * put into hitstun(a state in which they cannot perform any actions). Then calculations are done
     * for how much knockback the move deals to the opponent and the user, and finally damage is taken
     * off of the victim's health. If the opponent is blocking, they will go through the same calculations
     * but with lower hitstun and damage.
     * @param healthBar The visual representation of a player's health. Will go down if player is attacked
     */
    public static void damageCalculation(Rectangle[] healthBar) {
        boolean[] playerHit = new boolean[2];
        int dependent = 1;
        for (int i = 0; i<2; i++) {
            ArrayList<Ellipse> hitBoxes= characters[i].getHitBoxes();
            if (i==1) {
                dependent=0;
            }
            ArrayList<Rectangle> hurtBoxes= characters[dependent].getHurtBoxes();

            //checks if player's hitboxes collides with hurtboxes of enemy
            for (Ellipse hitBox : hitBoxes) {
                for (Rectangle hurtBox : hurtBoxes) {
                    if (hitBox.intersects(hurtBox.getBoundsInLocal())) {
                        playerHit[i] = true;
                    }
                }
            }
            //if statement for when enemy successfully blocks attack
            if (playerHit[i] && !characters[i].getHitCharacter() && blocking(dependent)[characters[i].getAttackCurrent().getBlockingType()]) {


                if (characters[dependent].getIsCrouching()) {
                    characters[dependent].setIsBlocking(0,true);
                }
                else {
                    characters[dependent].setIsBlocking(1, true);
                }
                characters[i].setHitCharacter(true);

                //damage, stun, and knockback calculations
                hitStun[dependent] = characters[i].getAttackCurrent().getShieldStun();
                characters[dependent].setXSpeed(0);
                characters[dependent].changeXSpeed(characters[i].getAttackCurrent().getKnockBackX() * characters[i].getFacingRight() / 6);
                characters[dependent].setYSpeed(0);
                characters[dependent].changeYSpeed(characters[i].getAttackCurrent().getKnockBackY()/ 2);
                characters[i].changeXSpeed(-characters[i].getAttackCurrent().getSelfKnockBack() * characters[i].getFacingRight());
                healthBar[dependent].setWidth(healthBar[dependent].getWidth()-characters[i].getAttackCurrent().getShieldDamage());
            }
            //if statement for when enemy fails to block attack
            else if (playerHit[i] && !characters[i].getHitCharacter()) {

                if (characters[i].getAttackCurrent().getMultihit() == 0) {
                    characters[i].setHitCharacter(true);
                }

                //play hurt sound if specific attacks are landed
                if ((characters[i].getName().equals("Hibiki")&&(attackIndex[i]==7||attackIndex[i]==9||attackIndex[i]==10))||(characters[i].getName().equals("Rock")&&(attackIndex[i]==2||attackIndex[i]==8||attackIndex[i]==10))) {
                    characters[dependent].getHurtSound().play();
                }
                //play sound effect
                hitSounds[(int) (Math.random()*9)].play();
                hitStun[dependent] = characters[i].getAttackCurrent().getHitStun();

                //damage, stun, and knockback calculations
                characters[dependent].setXSpeed(0);
                characters[dependent].changeXSpeed(characters[i].getAttackCurrent().getKnockBackX()* characters[i].getFacingRight());
                characters[dependent].setYSpeed(0);
                characters[dependent].changeYSpeed(characters[i].getAttackCurrent().getKnockBackY());
                characters[i].changeXSpeed(-characters[i].getAttackCurrent().getSelfKnockBack()* characters[i].getFacingRight());
                healthBar[dependent].setWidth(healthBar[dependent].getWidth()-characters[i].getAttackCurrent().getDamage());
            }



        }




    }
    /**
     * This function checks if a player inputted a dash in their forward direction.
     * This works by converting the players' inputs into a String. It then checks
     * the string to see if a forward dash input is in the String.
     * @param character The character that is being checked for dashes
     * @return Whether a forward dash is inputted
     */
    public static boolean checkForwardDash(int character)
    {


        //checks forward dash input for player1
        if (character == 0&&characters[character].getState()==0) {
            String input = inputs.toString();

            if (characters[0].getFacingRight() == 1) {
                return input.matches("(.*d, d])");
            }

            else
            {
                return input.matches("(.*a, a])");
            }
        }
        //checks forward dash input for player2
        else if (character==1&&characters[character].getState()==0){
            String input = inputs2.toString();

            if (characters[0].getFacingRight() == -1) {
                return input.matches("(.*d, d])");


            }
            else
            {
                return input.matches("(.*a, a])");
            }

        }
        return false;
    }
    /**
     * This function checks to see whether an attack is inputted and if it is, the
     * hitboxes and hurtboxes of the character is changed to be that of the attack's.
     * The character's speed is also changed by the attack's speedChange value. While
     * the attack hasn't reached its end, the next frame of the attack will play and
     * the character will remain in an attacking state. If the attack has reached its
     * end, the character's hitboxes and hurtboxes will return to default and the
     * character will be put back in an idle state.
     * @param character The character whose attacks will be checked
     * @param attacks A boolean of the character's full set of attacks and which ones are pressed
     * @param attack The attack that is currently being pressed
     */
    public static void attackingRevamped(int character, boolean [] attacks, int attack) {

        if (attacks[attack] && hitStun[character]==0) {


            characters[character].setAttackCurrent(attack);

            if (characters[character].getPlayerTimer()==0) {
                //removing base hitbox, playing sfx
                characters[character].removeHurtBox(1);
                characters[character].getAttackCurrent().getSfx().play();
            }

            //removing the character's hitboxes and hurtboxes
            characters[character].removeHitBox(characters[character].getHitBoxCounter());
            characters[character].setHitBoxCounter(0);
            characters[character].removeHurtBox(characters[character].getHurtBoxCounter());
            characters[character].setHurtBoxCounter(0);
            int playerTimer = characters[character].getPlayerTimer();
            int xPosition = characters[character].getCentreX();
            int yPosition = characters[character].getCentreY();

            //setting the character's state
            characters[character].setIsActionable(false);
            characters[character].setState(1);

            if (playerTimer<characters[character].getAttack(attack).getFrameTime()) {

                //horizontal and vertical speed changes
                characters[character].changeXSpeed(characters[character].getAttack(attack).getSpeedChangeX(playerTimer)*characters[character].getFacingRight());
                characters[character].changeYSpeed(characters[character].getAttack(attack).getSpeedChangeY(playerTimer));
                tempHold = characters[character].getAttack(attack).getHitBoxes(playerTimer);

                //adding new hitboxes
                for (double[] doubles : tempHold) {
                    Ellipse temp = new Ellipse(doubles[0] * characters[character].getFacingRight() + xPosition, doubles[1] + yPosition, doubles[2], doubles[2]);

                    characters[character].addHitBox(temp);
                    characters[character].addHitBoxCounter(1);

                }
                tempHold = characters[character].getAttack(attack).getHurtBoxes(playerTimer);

                //adding new hurtboxes
                for (double[] doubles : tempHold) {
                    Rectangle temp;
                    if (characters[character].getFacingRight() == 1) {
                        temp = new Rectangle(doubles[0] + xPosition, doubles[1] + yPosition, doubles[2], doubles[3]);
                    } else {
                        temp = new Rectangle(-(doubles[0]) + xPosition - doubles[2], doubles[1] + yPosition, doubles[2], doubles[3]);
                    }
                    characters[character].addHurtBox(temp);
                    characters[character].addHurtBoxCounter(1);
                }

                //setting associated image with attack frame
                characters[character].setImageDisplay(characters[character].getAttack(attack).getImages(playerTimer));

                characters[character].addPlayerTimer(1);
            }
            else {
                //character is no longer in attacking state, return hitboxes and hurtboxes to normal

                characters[character].addHurtBox(characters[character].getBaseHurtBox());
                characters[character].getHurtBox(0).setX(characters[character].getX());
                characters[character].getHurtBox(0).setY(characters[character].getY());
                characters[character].setImageDisplay(characters[character].getImageCharacter());
                attacks[attack] = false;
                characters[character].setIsActionable(true);
                characters[character].setPlayerTimer(0);
                characters[character].setHurtBoxCounter(0);
                characters[character].setHitCharacter(false);
                characters[character].setState(0);

            }
        }
        else if (attacks[attack] && hitStun[character]>0) {
            //character is no longer in attacking state due to being hit, return hitboxes and hurtboxes to normal
            characters[character].setAttackCurrent(attack);
            characters[character].removeHitBox(characters[character].getHitBoxCounter());
            characters[character].setHitBoxCounter(0);
            characters[character].removeHurtBox(characters[character].getTotalHurtBoxCounter());
            characters[character].setHurtBoxCounter(0);
            characters[character].addHurtBox(characters[character].getBaseHurtBox());
            characters[character].getHurtBox(0).setX(characters[character].getX());
            characters[character].getHurtBox(0).setY(characters[character].getY());
            characters[character].setImageDisplay(characters[character].getImageCharacter());
            attacks[attack] = false;
            characters[character].setIsActionable(true);
            characters[character].setPlayerTimer(0);
            characters[character].setHurtBoxCounter(0);
            characters[character].setHitCharacter(false);
            characters[character].setState(0);
        }

    }
    /**
     * This function first checks whether the character is able to move. If they are
     * able to, it checks which movement buttons they are pressing and moves them
     * in that direction. The character also changes states with accordance to what
     * direction they're going in. At the end regardless of whether the character is
     * able to move, the collision function is called with the character being checked.
     * @param character The character whose movement inputs are being checked.
     * @param movement A byte array of all the player's movement inputs (movement[0] = down, [1] = right, [2] = left, [3] = up)
     * (movement[_] = 2 means the button is being pressed, movement[_] = 1 means the button is being released, and movement[_]
     *  = 0 means the button isn't being pressed.
     */
    public static void movement(int character, byte [] movement) {

        //character has to be in normal states to move
        if (characters[character].getIsActionable() &&(characters[character].getState()==0||characters[character].getState()==5||characters[character].getState()==6)&&characters[character].getCentreY()>=downBorder) {

            if (movement[1] == 2 && !characters[character].getIsCrouching()) {
                //allows character to move, switches state depending on direction
                characters[character].setXSpeed(characters[character].getWalkSpeed());
                if (characters[character].getFacingRight() == 1) {
                    characters[character].setState(5);
                }
                else {
                    characters[character].setState(6);
                }

            }
            else if(movement[1]==1) {
                //stopping character when key is released
                characters[character].setXSpeed(0);
                characters[character].setState(0);

                movement[1]=0;
            }

            //allowing character to dash forward
            if (checkForwardDash(character) && !characters[character].getJump()) {
                characters[character].changeXSpeed(3 * characters[character].getFacingRight());
            }


            if (movement[2]==2 && !characters[character].getIsCrouching()){
                //allows character to move, switches state depending on direction
                characters[character].setXSpeed(-characters[character].getWalkSpeed());
                if (characters[character].getFacingRight() == 1) {
                    characters[character].setState(6);
                }
                else {
                    characters[character].setState(5);
                }

            }
            else if (movement[2]==1){
                //stopping character when key is released
                characters[character].setXSpeed(0);
                characters[character].setState(0);
                movement[2]=0;
            }

            if (movement[0] == 2) {
                //crouching state
                characters[character].setIsCrouching(true);

                characters[character].setXSpeed(0);
                movement[0]=-1;

            }
            else if(movement[0]==1) {
                //crouch released
                characters[character].setIsCrouching(false);
                characters[character].setState(0);
                movement[0]=0;
            }

            if (movement[3]==2){
                //jump state
                characters[character].setJump(true);
                frameCounters[character][4] = 0;
                characters[character].changeYSpeed(-28);
                movement[3]=0;
            }


        }
        collision(character);


    }

    /**
     * This function checks the direction that each player is facing.
     * It's called every 20ms and only called when the player is in
     * an idle state.
     */
    public static void facingDirection () {

        if (characters[0].getState()==0&&characters[0].getCentreY()>=downBorder) {

            //checks where player 2 is, changes the direction player 1 is facing based on this
            if (characters[0].getX() < characters[1].getX()) {
                characters[0].setFacingRight(1);
            }
            else {
                characters[0].setFacingRight(-1);
            }
        }
        if (characters[1].getState()==0&&characters[1].getCentreY()>=downBorder) {

            //checks where player 1 is, changes the direction player 2 is facing based on this
            if (characters[0].getX() < characters[1].getX()) {
                characters[1].setFacingRight(-1);
            }
            else  {
                characters[1].setFacingRight(1);
            }
        }

    }

    /**
     * This function checks whether a character is blocking and at what height they are blocking.
     * It first checks if they are in a state where they can block and checks which direction they're
     * facing. If they are walking in the opposite direction and are standing, they are blocking high.
     *  If they are walking in the opposite direction and are crouching, they are blocking low.
     * @param character The character that will be checked whether they are blocking
     * @return A boolean array which has all the heights at which the character is blocking
     * (isBlocking[0] = blocking low, [1] = blocking mid, [2] = blocking high)
     */
    public static boolean[] blocking(int character) {
        boolean[] isBlocking = new boolean[3];

        if (characters[character].getIsActionable() && !characters[character].getJump()) {

            /*
             * checks which direction player is moving and what direction the player is facing,
             * determining whether player is blocking. Also checks if the player is blocking low or high.
             */
            if (characters[character].getFacingRight() == 1) {

                if (movement[character][2] == 2) {
                    isBlocking[1] = true;

                    if (movement[character][0] == -1) {
                        isBlocking[0] = true;
                    }
                    else {
                        isBlocking[2] = true;
                    }
                }
            }
            else {
                if (movement[character][1] == 2) {
                    isBlocking[1] = true;

                    if (movement[character][0] == -1) {
                        isBlocking[0] = true;
                    }
                    else {
                        isBlocking[2] = true;
                    }
                }
            }
        }


        return isBlocking;
    }

    /**
     * This method checks for collisions between character independent and the other character and walls.
     * This method updates the character's location after being called.
     * @param independent - character being evaluated
     */
    public static void collision(int independent) {

        int dependent;
        if (independent ==0) {
            dependent=1;
        }
        else {
            dependent=0;
        }

        //determining future position of character with current speeds
        double currentXDistance = Math.abs(characters[dependent].getCentreX()-characters[independent].getCentreX())-characters[dependent].getWidth()/2-characters[independent].getWidth()/2;
        double currentYDistance = Math.abs(characters[dependent].getCentreY()-characters[independent].getCentreY())-characters[dependent].getHeight()/2-characters[independent].getHeight()/2;
        double futureXPosition = characters[independent].getCentreX()+characters[independent].getXSpeed();
        double futureYPosition = characters[independent].getCentreY()+characters[independent].getYSpeed();
        double xDistance = Math.abs(characters[dependent].getCentreX()-futureXPosition)-characters[dependent].getWidth()/2-characters[independent].getWidth()/2;
        double yDistance = Math.abs(characters[dependent].getCentreY()-futureYPosition)-characters[dependent].getHeight()/2-characters[independent].getHeight()/2;

        //pushing characters apart when overlapping
        if (characters[independent].getBaseHurtBox().intersects(characters[dependent].getBaseHurtBox().getBoundsInLocal())&&(characters[independent].getState()!=1&&characters[dependent].getState()!=1)) {
            if (characters[independent].getX()>=characters[dependent].getX()) {
                characters[independent].setXSpeed(10);
            }
            else {
                characters[independent].setXSpeed(-10);
            }
            if (characters[independent].getX()==characters[dependent].getX()) {

                if (characters[0].getX()<rightBorder) {
                    characters[0].setX(characters[0].getX()-1);
                }

            }

            //fixes specific interaction with borders of screen
            if (characters[independent].getX()==characters[dependent].getX()&&characters[0].getX()<rightBorder) {
                characters[0].setX(characters[0].getX()-1);
            }

        }

        //allows for smooth movement when walking together
        if (xDistance<0&&yDistance<0&&(currentXDistance>0||currentYDistance>0)) {
            characters[independent].setXSpeed(0);
        }

        //calculates new future position
        futureXPosition = characters[independent].getCentreX()+characters[independent].getXSpeed();
        futureYPosition = characters[independent].getCentreY()+characters[independent].getYSpeed();

        //changes speed if future position exceeds borders
        if (futureXPosition>rightBorder) {
            characters[independent].changeXSpeed(-(futureXPosition-rightBorder));
        }
        else if (futureXPosition<leftBorder) {
            characters[independent].changeXSpeed(-(futureXPosition-leftBorder));
        }
        if (futureYPosition>downBorder) {
            characters[independent].changeYSpeed(-(futureYPosition-downBorder));
        }
        if (futureYPosition<0) {
            characters[independent].changeYSpeed(-futureYPosition);
        }

        characters[independent].update(1);
    }



    /**
     * Clears the canvas and renders the characters, health bars and the amount of rounds each character won.
     * This function checks whether a character is facing right or left and flips the image accordingly. It
     * also checks what state the character is in and plays the animation for that state.
     * @param gc - graphics context to call
     */
    public void draw2(GraphicsContext gc) {


        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (int i = 0; i < 2; i++) {

            //displays characters
            if (characters[i].getFacingRight()==1) {
                characterDraw[i].setScaleX(-1);
            }
            else{
                characterDraw[i].setScaleX(1);
            }
            characters[i].setHeightAdjust(heightAdjust);
            characters[i].setWidthAdjust(widthAdjust);

            characterDraw[i].setX((characters[i].getCentreX()-characters[i].getOffsetX())*widthAdjust);
            characterDraw[i].setY((characters[i].getCentreY()-characters[i].getOffsetY())*heightAdjust);


            //various states cause various animation
            if (characters[i].getState() == 0 && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 0;
                characters[i].setImageDisplay(characters[i].getIdleAnimation(frameCounters[i][0]/5));
            }
            if (characters[i].getState() == 5  && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 5;
                characters[i].setImageDisplay(characters[i].getWalkAnimation(frameCounters[i][5]/4));
            }
            if (characters[i].getState() == 6  && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 6;
                characters[i].setImageDisplay(characters[i].getBackWalkAnimation(frameCounters[i][6]/4));
            }
            if (characters[i].getIsCrouching() && characters[i].getIsActionable()) {
                characters[i].setImageDisplay(characters[i].getCrouch());
            }
            if (characters[i].getCentreY()<downBorder && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 4;
                characters[i].setImageDisplay(characters[i].getJumpAnimation(frameCounters[i][4] / 6));
            }
            if (characters[i].getState() == 2) {
                characters[i].setImageDisplay(characters[i].getHurt());
            }
            if (characters[i].getIsBlocking(1)) {
                characters[i].setImageDisplay(characters[i].getBlock());
            }
            if (characters[i].getIsBlocking(0)) {
                characters[i].setImageDisplay(characters[i].getLowBlock());
            }
            frameCounters[i][frameCounterIndex[i]]++;
            if (frameCounters[i][frameCounterIndex[i]] >= frameTotals[i][frameCounterIndex[i]]) {
                frameCounters[i][frameCounterIndex[i]] = 0;
            }

            characterDraw[i].setImage(characters[i].getImageDisplay());

            //drawing health bars
            gc.setFill(Color.RED);
            gc.fillRect(((healthBar[i].getX()+i*(500-healthBar[i].getWidth())*0.715))*widthAdjust, healthBar[i].getY()*heightAdjust, healthBar[i].getWidth()*0.715*widthAdjust, healthBar[i].getHeight()*heightAdjust);
            for (int e = 0; e<2; e++) {
                for (int o = 0; o<playerCounter[e]; o++) {
                    gc.setFill(Color.AQUA);
                    gc.fillOval((150+50*o-100*o*e+1200*e)*widthAdjust, 115*heightAdjust, 40*widthAdjust, 40*heightAdjust);
                }
                for (int o = playerCounter[e]; o<2; o++) {
                    gc.setFill(Color.WHITE);
                    gc.fillOval((150+50*o+1200*e-100*o*e)*widthAdjust, 115*heightAdjust, 40*widthAdjust, 40*heightAdjust);
                }
            }

        }
    }

    /**
     * This function loads 2 files for 2 characters and sets their stats according
     * to the text files.
     * @param loadingCharacters The names of the characters that will be loaded
     */
    public void initFighting(String[] loadingCharacters) throws IOException {

        timerInterval = 0;
        healthBar[0] = new Rectangle(130, 69, 0, 27);
        healthBar[1] = new Rectangle(1048, 69, 0, 27);
        playerTimer[0] = 0;
        playerTimer[1] = 0;
        String[] fileNames = new String[2];
        fileNames[0] = "/fightingFiles/characters/" + loadingCharacters[0] + "/";
        fileNames[1] = "/fightingFiles/characters/" + loadingCharacters[1] + "/";
        BufferedReader br;
        //loading 2 different characters
        for (int i = 0; i < 2; i++) {
            String imagePath = fileNames[i] + "images/";
            String audioPath = fileNames[i] + "audio/";

            String temp2 = fileNames[i] + loadingCharacters[i] + ".txt";

            br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(temp2))));
            String input = br.readLine();

            //loading stats
            while (!input.equals("[STATS]")) {
                input = br.readLine();
            }
            input = br.readLine();
            String[] d = input.split(" ");
            double[] d2 = new double[2];
            d2[0] = Integer.parseInt(d[0]);
            d2[1] = Integer.parseInt(d[1]);
            input = br.readLine();
            String[] inputArr = input.split(" ");
            int e = 1;
            if (i == 1) {
                e = 0;
            }
            characters[i] = new FightingCharacter(Math.abs((e * leftBorder + 100) + i * (rightBorder - 100) - Integer.parseInt(inputArr[0])), downBorder - (Integer.parseInt(inputArr[1]) / 2.0), Integer.parseInt(inputArr[0]), Integer.parseInt(inputArr[1]));
            characters[i].setDimensions(d2);
            input = br.readLine();
            characters[i].setWalkSpeed(Integer.parseInt(input));
            input = br.readLine();
            Image temp = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            characters[i].setImageCharacter(temp);
            characters[i].setImageDisplay(temp);
            input = br.readLine();
            characters[i].setOffsetX(Integer.parseInt(input));
            input = br.readLine();
            characters[i].setOffsetY(Integer.parseInt(input));
            input = br.readLine();
            Animation tempAnimation = new Animation();
            tempAnimation.setAnimationTime(Integer.parseInt(input));
            Image[] imageTemp = new Image[Integer.parseInt(input)];
            for (int q = 0; q < imageTemp.length; q++) {
                input = br.readLine();
                imageTemp[q] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            tempAnimation.setFrames(imageTemp);
            characters[i].setStartingAnimation(tempAnimation);
            AudioClip[] tempAudio = new AudioClip[3];
            for (int q = 0; q < 3; q++) {
                input = br.readLine();
                tempAudio[q] = new AudioClip(Objects.requireNonNull(getClass().getResource(audioPath + input)).toExternalForm());
            }
            characters[i].setHurtSound(tempAudio);

            //loading names and animations
            while (!input.equals("[NAME]")) {
                input = br.readLine();
            }
            input = br.readLine();
            characters[i].setName(input);
            int frames = Integer.parseInt(br.readLine());
            Image[] idleAnimation = new Image[frames];
            frameTotals[i][0] = frames * 5;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                idleAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            characters[i].setIdleAnimation(idleAnimation);
            input = br.readLine();
            characters[i].setCrouch(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input))));

            frames = Integer.parseInt(br.readLine());
            Image[] walkAnimation = new Image[frames];
            frameTotals[i][5] = frames * 4;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                walkAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            characters[i].setWalkAnimation(walkAnimation);
            frames = Integer.parseInt(br.readLine());
            Image[] backWalkAnimation = new Image[frames];
            frameTotals[i][6] = frames * 4;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                backWalkAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            characters[i].setBackWalkAnimation(backWalkAnimation);

            frames = Integer.parseInt(br.readLine());
            Image[] jumpAnimation = new Image[frames];
            frameTotals[i][4] = frames * 6;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                jumpAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));

            }
            characters[i].setJumpAnimation(jumpAnimation);
            input = br.readLine();
            characters[i].setBlock(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input))));

            input = br.readLine();
            characters[i].setLowBlock(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input))));

            input = br.readLine();
            characters[i].setHurt(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input))));


            //loading attacks
            while (!input.equals("[ATTACKS]")) {
                input = br.readLine();
            }
            int attackNumber = 0;
            int frameAmount = 0;
            int numberOfShapes = 0;
            String[] hitBoxes;
            String[] hitBoxData;
            double[][][] hitBoxTotal;
            double[][][] hurtBoxTotal;
            double[][] speedChanges;
            String[] divide;
            Image[] images;
            for (int a = 0; a < 11; a++) {
                Attack tempAttack = characters[i].getAttack(attackNumber);
                input = br.readLine();
                attackNumber = Integer.parseInt(input);
                input = br.readLine();
                tempAttack.setDamage(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setShieldDamage(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setShieldStun(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setHitStun(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setBlockingType(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setMultihit(Byte.parseByte(input));
                input = br.readLine();
                tempAttack.setSfx(new AudioClip(Objects.requireNonNull(getClass().getResource(audioPath+input)).toExternalForm()));

                br.readLine();
                input = br.readLine();
                tempAttack.setKnockBackY(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setKnockBackX(Integer.parseInt(input));
                input = br.readLine();
                tempAttack.setSelfKnockBack(Integer.parseInt(input));
                input = br.readLine();
                frameAmount = Integer.parseInt(input);
                tempAttack.setFrameTime(frameAmount);
                input = br.readLine();
                numberOfShapes = Integer.parseInt(input);
                hitBoxTotal = new double[frameAmount][numberOfShapes][3];
                hurtBoxTotal = new double[frameAmount][numberOfShapes][4];
                images = new Image[frameAmount];
                speedChanges = new double[frameAmount][2];
                for (int q = 0; q < frameAmount; q++) {

                    input = br.readLine();
                    if (input.matches(".*[.].*")) {
                        images[q - 1] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
                        q--;
                        continue;
                    }
                    divide = input.split(" ");

                    for (int u = 0; u < 3; u++) {
                        if (divide[0].equals("NC")) {
                            hitBoxTotal[q] = hitBoxTotal[q - 1];
                            hurtBoxTotal[q] = hurtBoxTotal[q - 1];
                            images[q] = images[q - 1];
                            speedChanges[q][0] = 0;
                            speedChanges[q][1] = 0;
                        } else {
                            hitBoxes = divide[u].split(":");
                            for (int w = 0; w < hitBoxes.length; w++) {
                                hitBoxData = hitBoxes[w].split(",");
                                if (u == 0) {
                                    hitBoxTotal[q][w][0] = Integer.parseInt(hitBoxData[0]);
                                    hitBoxTotal[q][w][1] = Integer.parseInt(hitBoxData[1]);
                                    hitBoxTotal[q][w][2] = Integer.parseInt(hitBoxData[2]);
                                } else if (u == 1) {
                                    hurtBoxTotal[q][w][0] = Integer.parseInt(hitBoxData[0]);
                                    hurtBoxTotal[q][w][1] = Integer.parseInt(hitBoxData[1]);
                                    hurtBoxTotal[q][w][2] = Integer.parseInt(hitBoxData[2]);
                                    hurtBoxTotal[q][w][3] = Integer.parseInt(hitBoxData[3]);
                                } else {
                                    speedChanges[q][0] = Integer.parseInt(hitBoxData[0]);
                                    speedChanges[q][1] = Integer.parseInt(hitBoxData[1]);
                                }
                            }

                        }

                    }

                }
                tempAttack.setImages(images);
                tempAttack.setHitboxes(hitBoxTotal);
                tempAttack.setHurtboxes(hurtBoxTotal);
                tempAttack.setSpeedChanges(speedChanges);
                br.readLine();
                attackNumber++;
            }

        }
    }
}
