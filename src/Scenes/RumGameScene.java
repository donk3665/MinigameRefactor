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
    ImageView[] characterDraw = new ImageView[2];
    int[] playerTimer = new int[2];
    static int []attackIndex = new int[2];
    Rectangle[]healthBar = new Rectangle[2];
    static double[][] tempHold;
    static int inputTimer = 0;
    int [] playerWinCounter = new int[2];
    boolean endGame = false;
    int timerInterval = 0;
    int[] animationCounter = new int[2];
    ImageView[] resourcePane;
    Image [] resources;
    int logoVanishCounter = 0;
    static AudioClip[] hitSounds;
    MediaPlayer backgroundMusic;
    static AudioClip[] narrator;
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
        }
        Group mainPane = new Group();

        //Initializing all UI Images
        playerWinCounter[0]=-1;
        playerWinCounter[1]=-1;
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
            for (int i = 0; i<2; i++) {
                characters[i].getInputController().keyPressed(keyCode, inputTimer);
            }
        });
        //event handler for keyboard releases
        fightingGame.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            for (int i = 0; i<2; i++) {
                characters[i].getInputController().keyRelease(keyCode);
            }

        });
        /*
         * Plays the media on game start, loops when media finishes
         */
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                backgroundMusic.play();

                backgroundMusic.setOnEndOfMedia(() -> {
                    backgroundMusic.stop();
                    backgroundMusic.play();
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
                            playerWinCounter[1]++;
                            playerWinCounter[0]++;
                        }
                        else if (healthBar[1].getWidth()<=0) {
                            playerWinCounter[0]++;
                        }
                        else {
                            playerWinCounter[1]++;

                        }


                        if (playerWinCounter[1]!=0||playerWinCounter[0]!=0) {
                            //if one player has points, show an image and play a sound
                            resourcePane[0].setImage(resources[7]);
                            narrator[6].play();
                        }
                        if (playerWinCounter[1]==0&&playerWinCounter[0]==0) {
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
                        if (timerInterval%80==0&&timerInterval>1000&&(playerWinCounter[0]==0&&playerWinCounter[1]==0)) {
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
                        if (timerInterval==1000&&(playerWinCounter[0]!=2&&playerWinCounter[1]!=2)) {
                            //show image and play a sound
                            resourcePane[0].setImage(resources[playerWinCounter[0]+playerWinCounter[1]]);
                            narrator[playerWinCounter[0]+playerWinCounter[1]].play();

                            //resetting characters after round end
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
                                characters[character].setHitstun(0);
                                //hitStun[character]=0;
                            }

                        }
                        else if (timerInterval ==2500) {

                            if (playerWinCounter[1]==2||playerWinCounter[0]==2) {
                                //play sound and display image if a player won
                                resourcePane[0].setFitHeight(200*heightAdjust);
                                resourcePane[0].setFitWidth(600*widthAdjust);
                                if (playerWinCounter[1]==2) {
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
                        else if (timerInterval ==3500&&(playerWinCounter[0]!=2&&playerWinCounter[1]!=2)) {
                            //play normal countdown sound and image
                            resourcePane[0].setImage(resources[4]);
                            narrator[4].play();

                        }
                        else if (timerInterval==4500&&(playerWinCounter[0]!=2&&playerWinCounter[1]!=2)) {
                            //play normal countdown sound and image
                            resourcePane[0].setImage(resources[3]);
                            narrator[5].play();
                        }
                        else if (timerInterval==5500) {
                            if (playerWinCounter[1]==2||playerWinCounter[0]==2) {

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
                                    characters[character].resetAttackInt();
                                    characters[character].resetMovement();
                                    characters[character].resetFrameCounters();
                                }

                                //display an image and sound, allow game to start
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
                    attackingRevamped(characters[0]);
                    attackingRevamped(characters[1]);
                    damageCalculation(healthBar);

                    //hitstun calculations (how long a player cannot do any actions due to being hit)
                    for (int i = 0; i < 2; i++) {
                        if (characters[i].getHitstun() > 0) {
                            characters[i].setIsActionable(false);
                            characters[i].setState(2);
                            characters[i].setHitstun(characters[i].getHitstun() - 1 );
                            if (characters[i].getHitstun()==0) {
                                characters[i].setIsActionable(true);
                                characters[i].setIsBlocking(1, false);
                                characters[i].setIsBlocking(0, false);
                                characters[i].setState(0);
                            }
                        }
                    }

                    movement(characters[0]);
                    movement(characters[1]);


                    //holds most recent inputs, decays over time
                    inputTimer++;

                    for (int i = 0; i<2; i++){
                        if (characters[i].getInputTime().contains(inputTimer)){
                            int index = characters[i].getInputTime().indexOf(inputTimer);
                            characters[i].getInputTime().remove(index);
                            characters[i].getInputs().remove(index);
                        }

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
     * This function looks at each hit box that is active and compares them with every active hurt box.
     * If a hit box overlaps a hurt box and the opponent isn't blocking, the opponent is hurt and is
     * put into hitstun(a state in which they cannot perform any actions). Then calculations are done
     * for how much knock-back the move deals to the opponent and the user, and finally damage is taken
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

            //checks if player's hit boxes collides with hurt boxes of enemy
            for (Ellipse hitBox : hitBoxes) {
                for (Rectangle hurtBox : hurtBoxes) {
                    if (hitBox.intersects(hurtBox.getBoundsInLocal())) {
                        playerHit[i] = true;
                    }
                }
            }
            //if statement for when enemy successfully blocks attack
            if (playerHit[i] && characters[i].getNotHitCharacter() && blocking(characters[dependent])[characters[i].getAttackCurrent().getBlockingType()]) {


                if (characters[dependent].getIsCrouching()) {
                    characters[dependent].setIsBlocking(0,true);
                }
                else {
                    characters[dependent].setIsBlocking(1, true);
                }
                characters[i].setHitCharacter(true);

                //damage, stun, and knock-back calculations
                characters[dependent].setHitstun(characters[i].getAttackCurrent().getShieldStun());
                characters[dependent].setXSpeed(0);
                characters[dependent].changeXSpeed(characters[i].getAttackCurrent().getKnockBackX() * characters[i].getFacingRight() / 6);
                characters[dependent].setYSpeed(0);
                characters[dependent].changeYSpeed(characters[i].getAttackCurrent().getKnockBackY()/ 2);
                characters[i].changeXSpeed(-characters[i].getAttackCurrent().getSelfKnockBack() * characters[i].getFacingRight());
                healthBar[dependent].setWidth(healthBar[dependent].getWidth()-characters[i].getAttackCurrent().getShieldDamage());
            }
            //if statement for when enemy fails to block attack
            else if (playerHit[i] && characters[i].getNotHitCharacter()) {

                if (characters[i].getAttackCurrent().getMultihit() == 0) {
                    characters[i].setHitCharacter(true);
                }

                //play hurt sound if specific attacks are landed
                if ((characters[i].getName().equals("Hibiki")&&(attackIndex[i]==7||attackIndex[i]==9||attackIndex[i]==10))||(characters[i].getName().equals("Rock")&&(attackIndex[i]==2||attackIndex[i]==8||attackIndex[i]==10))) {
                    characters[dependent].getHurtSound().play();
                }
                //play sound effect
                hitSounds[(int) (Math.random()*9)].play();
                characters[dependent].setHitstun(characters[i].getAttackCurrent().getHitStun());

                //damage, stun, and knock-back calculations
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
    public static boolean checkForwardDash(FightingCharacter character)
    {

        //checks forward dash input for players
        if (character.getState()==0) {
            String input = character.getInputs().toString();

            if (character.getFacingRight() == 1) {
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
     * hit boxes and hurt boxes of the character is changed to be that of the attack's.
     * The character's speed is also changed by the attack's speedChange value. While
     * the attack hasn't reached its end, the next frame of the attack will play and
     * the character will remain in an attacking state. If the attack has reached its
     * end, the character's hit boxes and hurt boxes will return to default and the
     * character will be put back in an idle state.
     * @param character The character whose attacks will be checked
     */
    public static void attackingRevamped(FightingCharacter character) {
        if (character.getAttackInt() != -1 && character.getHitstun()==0) {
            character.setAttackCurrent(character.getAttackInt());

            if (character.getPlayerTimer()==0) {
                //removing base hit box, playing sfx
                character.removeHurtBox(1);
                character.getAttackCurrent().getSfx().play();
            }

            //removing the character's hit boxes and hurt boxes
            character.removeHitBox(character.getHitBoxCounter());
            character.setHitBoxCounter(0);
            character.removeHurtBox(character.getHurtBoxCounter());
            character.setHurtBoxCounter(0);
            int playerTimer = character.getPlayerTimer();
            int xPosition = character.getCentreX();
            int yPosition = character.getCentreY();

            //setting the character's state
            character.setIsActionable(false);
            character.setState(1);

            if (playerTimer<character.getAttackCurrent().getFrameTime()) {

                //horizontal and vertical speed changes
                character.changeXSpeed(character.getAttackCurrent().getSpeedChangeX(playerTimer)*character.getFacingRight());
                character.changeYSpeed(character.getAttackCurrent().getSpeedChangeY(playerTimer));
                tempHold = character.getAttackCurrent().getHitBoxes(playerTimer);

                //adding new hit boxes
                for (double[] doubles : tempHold) {
                    Ellipse temp = new Ellipse(doubles[0] * character.getFacingRight() + xPosition, doubles[1] + yPosition, doubles[2], doubles[2]);

                    character.addHitBox(temp);
                    character.addHitBoxCounter(1);

                }
                tempHold = character.getAttackCurrent().getHurtBoxes(playerTimer);

                //adding new hurt boxes
                for (double[] doubles : tempHold) {
                    Rectangle temp;
                    if (character.getFacingRight() == 1) {
                        temp = new Rectangle(doubles[0] + xPosition, doubles[1] + yPosition, doubles[2], doubles[3]);
                    } else {
                        temp = new Rectangle(-(doubles[0]) + xPosition - doubles[2], doubles[1] + yPosition, doubles[2], doubles[3]);
                    }
                    character.addHurtBox(temp);
                    character.addHurtBoxCounter(1);
                }

                //setting associated image with attack frame
                character.setImageDisplay(character.getAttackCurrent().getImages(playerTimer));

                character.addPlayerTimer(1);
            }
            else {
                //character is no longer in attacking state, return hit boxes and hurt boxes to normal

                setCharacterToDefaultState(character);

            }
        }
        else if (character.getAttackInt() != -1 && character.getHitstun()>0) {
            //character is no longer in attacking state due to being hit, return hit boxes and hurt boxes to normal
            character.setAttackCurrent(character.getAttackInt());
            character.removeHitBox(character.getHitBoxCounter());
            character.setHitBoxCounter(0);
            character.removeHurtBox(character.getTotalHurtBoxCounter());
            character.setHurtBoxCounter(0);
            setCharacterToDefaultState(character);
        }

    }

    private static void setCharacterToDefaultState(FightingCharacter character) {
        character.addHurtBox(character.getBaseHurtBox());
        character.getHurtBox(0).setX(character.getX());
        character.getHurtBox(0).setY(character.getY());
        character.setImageDisplay(character.getImageCharacter());
        character.setIsActionable(true);
        character.setPlayerTimer(0);
        character.setHurtBoxCounter(0);
        character.setHitCharacter(false);
        character.setState(0);
        character.resetAttackInt();
    }

    /**
     * This function first checks whether the character is able to move. If they are
     * able to, it checks which movement buttons they are pressing and moves them
     * in that direction. The character also changes states with accordance to what
     * direction they're going in. At the end regardless of whether the character is
     * able to move, the collision function is called with the character being checked.
     * @param character The character whose movement inputs are being checked.
//     * @param movement A byte array of all the player's movement inputs (movement[0] = down, [1] = right, [2] = left, [3] = up)
     * (movement[_] = 2 means the button is being pressed, movement[_] = 1 means the button is being released, and movement[_]
     *  = 0 means the button isn't being pressed.
     */
    public static void movement(FightingCharacter character) {

        //character has to be in normal states to move
        if (character.getIsActionable() &&(character.getState()==0||character.getState()==5||character.getState()==6)&&character.getCentreY()>=downBorder) {

            if (character.getMovement()[1] == 2 && !character.getIsCrouching()) {
                //allows character to move, switches state depending on direction
                character.setXSpeed(character.getWalkSpeed());
                if (character.getFacingRight() == 1) {
                    character.setState(5);
                }
                else {
                    character.setState(6);
                }
            }
            else if(character.getMovement()[1]==1) {
                //stopping character when key is released
                character.setXSpeed(0);
                character.setState(0);

                character.getMovement()[1]=0;
            }

            //allowing character to dash forward
            if (checkForwardDash(character) && character.getNotJump()) {
                character.changeXSpeed(3 * character.getFacingRight());
            }


            if (character.getMovement()[2]==2 && !character.getIsCrouching()){
                //allows character to move, switches state depending on direction
                character.setXSpeed(-character.getWalkSpeed());
                if (character.getFacingRight() == 1) {
                    character.setState(6);
                }
                else {
                    character.setState(5);
                }

            }
            else if (character.getMovement()[2]==1){
                //stopping character when key is released
                character.setXSpeed(0);
                character.setState(0);
                character.getMovement()[2]=0;
            }

            if (character.getMovement()[0] == 2) {
                //crouching state
                character.setIsCrouching(true);

                character.setXSpeed(0);
                character.getMovement()[0]=-1;

            }
            else if(character.getMovement()[0]==1) {
                //crouch released
                character.setIsCrouching(false);
                character.setState(0);
                character.getMovement()[0]=0;
            }

            if (character.getMovement()[3]==2){
                //setting character to jump state
                character.setJump(true);
                character.getFrameCounters()[4] = 0;
                character.changeYSpeed(-28);
                character.getMovement()[3]=0;
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
    public static boolean[] blocking(FightingCharacter character) {
        boolean[] isBlocking = new boolean[3];

        if (character.getIsActionable() && character.getNotJump()) {

            /*
             * checks which direction player is moving and what direction the player is facing,
             * determining whether player is blocking. Also checks if the player is blocking low or high.
             */
            if (character.getFacingRight() == 1) {

                if (character.getMovement()[2] == 2) {
                    isBlocking[1] = true;

                    if (character.getMovement()[0] == -1) {
                        isBlocking[0] = true;
                    }
                    else {
                        isBlocking[2] = true;
                    }
                }
            }
            else {
                if (character.getMovement()[1] == 2) {
                    isBlocking[1] = true;

                    if (character.getMovement()[0] == -1) {
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
    public static void collision(FightingCharacter independent) {

        FightingCharacter dependent;
        if (independent.equals(characters[0])) {
            dependent=characters[1];
        }
        else {
            dependent=characters[0];
        }

        //determining future position of character with current speeds
        double currentXDistance = Math.abs(dependent.getCentreX()-independent.getCentreX())-dependent.getWidth()/2-independent.getWidth()/2;
        double currentYDistance = Math.abs(dependent.getCentreY()-independent.getCentreY())-dependent.getHeight()/2-independent.getHeight()/2;
        double futureXPosition = independent.getCentreX()+independent.getXSpeed();
        double futureYPosition = independent.getCentreY()+independent.getYSpeed();
        double xDistance = Math.abs(dependent.getCentreX()-futureXPosition)-dependent.getWidth()/2-independent.getWidth()/2;
        double yDistance = Math.abs(dependent.getCentreY()-futureYPosition)-dependent.getHeight()/2-independent.getHeight()/2;

        //pushing characters apart when overlapping
        if (independent.getBaseHurtBox().intersects(dependent.getBaseHurtBox().getBoundsInLocal())&&(independent.getState()!=1&&dependent.getState()!=1)) {
            if (independent.getX()>=dependent.getX()) {
                independent.setXSpeed(10);
            }
            else {
                independent.setXSpeed(-10);
            }
            if (independent.getX()==dependent.getX()) {
                if (characters[0].getX()<rightBorder) {
                    characters[0].setX(characters[0].getX()-1);
                }

            }

            //fixes specific interaction with borders of screen
            if (independent.getX()==dependent.getX()&&characters[0].getX()<rightBorder) {
                characters[0].setX(characters[0].getX()-1);
            }

        }

        //allows for smooth movement when walking together
        if (xDistance<0&&yDistance<0&&(currentXDistance>0||currentYDistance>0)) {
            independent.setXSpeed(0);
        }

        //calculates new future position
        futureXPosition = independent.getCentreX()+independent.getXSpeed();
        futureYPosition = independent.getCentreY()+independent.getYSpeed();

        //changes speed if future position exceeds borders
        if (futureXPosition>rightBorder) {
            independent.changeXSpeed(-(futureXPosition-rightBorder));
        }
        else if (futureXPosition<leftBorder) {
            independent.changeXSpeed(-(futureXPosition-leftBorder));
        }
        if (futureYPosition>downBorder) {
            independent.changeYSpeed(-(futureYPosition-downBorder));
        }
        if (futureYPosition<0) {
            independent.changeYSpeed(-futureYPosition);
        }

        independent.update(1);
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
                characters[i].setImageDisplay(characters[i].getIdleAnimation(characters[i].getFrameCounters()[0]/5));
            }
            if (characters[i].getState() == 5  && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 5;
                characters[i].setImageDisplay(characters[i].getWalkAnimation(characters[i].getFrameCounters()[5]/4));
            }
            if (characters[i].getState() == 6  && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 6;
                characters[i].setImageDisplay(characters[i].getBackWalkAnimation(characters[i].getFrameCounters()[6]/4));
            }
            if (characters[i].getIsCrouching() && characters[i].getIsActionable()) {
                characters[i].setImageDisplay(characters[i].getCrouch());
            }
            if (characters[i].getCentreY()<downBorder && characters[i].getIsActionable()) {
                frameCounterIndex[i] = 4;
                characters[i].setImageDisplay(characters[i].getJumpAnimation(characters[i].getFrameCounters()[4] / 6));
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
            characters[i].getFrameCounters()[frameCounterIndex[i]]++;
            if (characters[i].getFrameCounters()[frameCounterIndex[i]] >= characters[i].getFrameTotals()[frameCounterIndex[i]]) {
                characters[i].getFrameCounters()[frameCounterIndex[i]] = 0;
            }

            characterDraw[i].setImage(characters[i].getImageDisplay());

            //drawing health bars
            gc.setFill(Color.RED);
            gc.fillRect(((healthBar[i].getX()+i*(500-healthBar[i].getWidth())*0.715))*widthAdjust, healthBar[i].getY()*heightAdjust, healthBar[i].getWidth()*0.715*widthAdjust, healthBar[i].getHeight()*heightAdjust);
            for (int e = 0; e<2; e++) {
                for (int o = 0; o<playerWinCounter[e]; o++) {
                    gc.setFill(Color.AQUA);
                    gc.fillOval((150+50*o-100*o*e+1200*e) * widthAdjust, 115*heightAdjust, 40*widthAdjust, 40*heightAdjust);
                }
                for (int o = playerWinCounter[e]; o<2; o++) {
                    gc.setFill(Color.WHITE);
                    gc.fillOval((150+50*o+1200*e-100*o*e) * widthAdjust, 115*heightAdjust, 40*widthAdjust, 40*heightAdjust);
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

            KeyCode [] keyArray = {KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.Y, KeyCode.U, KeyCode.I};
            //"W A S D Y U I"

            if (i == 1) {
                e = 0;
                keyArray = new KeyCode[]{KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.COMMA, KeyCode.PERIOD, KeyCode.SLASH};
                // "38 37 40 39 COMMA PERIOD SLASH";
            }
            characters[i] = new FightingCharacter(Math.abs((e * leftBorder + 100) + i * (rightBorder - 100) - Integer.parseInt(inputArr[0])),
                    downBorder - (Integer.parseInt(inputArr[1]) / 2.0), Integer.parseInt(inputArr[0]), Integer.parseInt(inputArr[1]), keyArray);
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
            characters[i].getFrameTotals()[0] = frames * 5;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                idleAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            characters[i].setIdleAnimation(idleAnimation);
            input = br.readLine();
            characters[i].setCrouch(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input))));

            frames = Integer.parseInt(br.readLine());
            Image[] walkAnimation = new Image[frames];
            characters[i].getFrameTotals()[5] = frames * 4;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                walkAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            characters[i].setWalkAnimation(walkAnimation);
            frames = Integer.parseInt(br.readLine());
            Image[] backWalkAnimation = new Image[frames];
            characters[i].getFrameTotals()[6] = frames * 4;
            for (int a = 0; a < frames; a++) {
                input = br.readLine();
                backWalkAnimation[a] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + input)));
            }
            characters[i].setBackWalkAnimation(backWalkAnimation);

            frames = Integer.parseInt(br.readLine());
            Image[] jumpAnimation = new Image[frames];
            characters[i].getFrameTotals()[4] = frames * 6;
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
            int frameAmount;
            int numberOfShapes;
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
