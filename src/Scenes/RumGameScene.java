package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import Scenes.RumGameHelpers.LoadRumGame;
import Scenes.RumGameHelpers.RumGameRenderer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import Scenes.RumGameHelpers.FightingCharacter;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RumGameScene extends MasterScene{


    //variables and objects for the fighting game
    static FightingCharacter[] characters = new FightingCharacter[2];
    public static final double downBorder = 730;
    public static final double rightBorder = 1400;
    public static final int leftBorder = 100;
    int[] playerTimer = new int[2];
    static int []attackIndex = new int[2];
    Rectangle[]healthBar = new Rectangle[2];
    static double[][] tempHold;
    static int inputTimer = 0;
    int [] playerWinCounter = new int[2];
    boolean endGame = false;
    int roundStartTimer = 0;
    int[] animationCounter = new int[2];
    int logoVanishCounter = 0;
    int[] frameCounterIndex = {0,0};
    MediaPlayer backgroundMusic;
    static RumGameRenderer renderer;
    LoadRumGame loader = new LoadRumGame();
    AnimationTimer timer;
    Timer gameTimer;
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {
        
        try {
            initFighting(data.getPlayers());
        }
        catch (Exception e){
            System.err.println("initialization error");
            System.exit(1);
        }
        
        Canvas canvas = new Canvas(1536*widthAdjust,864*heightAdjust);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);

        renderer = new RumGameRenderer(loader, characters, canvas);
        playerWinCounter[0]=-1;
        playerWinCounter[1]=-1;

        //refreshes the screen
        timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                renderer.draw(gc, frameCounterIndex, healthBar, playerWinCounter);
            }
        };
        
        //Initializing objects and centering images
        gameTimer = new Timer();
        
        characters[1].setFacingRight(-1);
        
        Scene fightingGame = new Scene(renderer.getMainPane());

        timer.start();
        
        backgroundMusic = new MediaPlayer(loader.loadMusic());
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
        gameTimer.schedule(new TimerTask() {
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
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                gameLoop();
            }

        }, 0, 20);




        return fightingGame;
    }

    /**
     * This function performs the setup needed on round start
     */
    public void roundStartSetup(){
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
            renderer.getResourcePane(0).setImage(renderer.getResource(7));
            renderer.getNarrator(6).play();
        }
        if (playerWinCounter[1]==0&&playerWinCounter[0]==0) {
            //if this is during the initialization of the game, skip to player animations
            roundStartTimer=960;
        }
        //setting up variables
        animationCounter[0] = 0;
        animationCounter[1] = 0;
        healthBar[1].setWidth(500);
        healthBar[0].setWidth(500);
        roundStartTimer+=20;
    }
    public void characterStartAnimations(){
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

    /**
     * This function resets the characters to their initial positions and properties on round start
     */
    public void resetCharactersToRoundStart(){
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
        }
    }

    /**
     * This function sets up the ending animations when a player has won
     */
    public void endingAnimations(){
        if (roundStartTimer == 2500){
            //play sound and display image if a player won
            renderer.getResourcePane(0).setFitHeight(200*heightAdjust);
            renderer.getResourcePane(0).setFitWidth(600*widthAdjust);
            if (playerWinCounter[1]==2) {
                renderer.getResourcePane(0).setImage(renderer.getResource(10));
                renderer.getNarrator(9).play();
            }
            else {
                renderer.getResourcePane(0).setImage(renderer.getResource(9));
                renderer.getNarrator(8).play();
            }
        }
        else if (roundStartTimer == 5500){
            //if player won, return to character select screen, suspend all timers
            endGame=true;
            timer.stop();
            gameTimer.cancel();
            backgroundMusic.stop();
            Platform.runLater(() -> {
                menuMusic.play();
                SceneTransferData transferData = new SceneTransferData();

                controller.changeScenes(SceneEnums.RUM_CHAR_SELECT, transferData);
            });
        }
    }

    /**
     * This function sets up the round start animations and narration
     */
    public void roundStartAnimations(){
        if (roundStartTimer%80==0&&roundStartTimer>1000&&(playerWinCounter[0]==0&&playerWinCounter[1]==0)) {
            characterStartAnimations();
        }
        if (roundStartTimer==1000) {
            //showing the round number and playing the round narration
            renderer.getResourcePane(0).setImage(renderer.getResource(playerWinCounter[0]+playerWinCounter[1]));
            renderer.getNarrator(playerWinCounter[0]+playerWinCounter[1]).play();
            resetCharactersToRoundStart();
        }
        else if (roundStartTimer ==2500) {
            //play normal countdown sound and image
            renderer.getResourcePane(0).setImage(renderer.getResource(5));
            renderer.getNarrator(3).play();
        }
        else if (roundStartTimer ==3500) {
            //play normal countdown sound and image
            renderer.getResourcePane(0).setImage(renderer.getResource(4));
            renderer.getNarrator(4).play();

        }
        else if (roundStartTimer==4500) {
            //play normal countdown sound and image
            renderer.getResourcePane(0).setImage(renderer.getResource(3));
            renderer.getNarrator(5).play();
        }
        else if (roundStartTimer==5500) {
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
            renderer.getResourcePane(0).setImage(renderer.getResource(6));
            logoVanishCounter+=20;
            roundStartTimer=0;
            renderer.getNarrator(7).play();
        }
    }

    /**
     * This function is called when the timers begin for the game
     */
    public void gameLoop(){
        //if statement entered when one of the characters runs out of hp (or when game is initialized)
        if ((healthBar[1].getWidth()<=0||healthBar[0].getWidth()<=0|| roundStartTimer > 0)) {
            if (roundStartTimer == 0) {
                roundStartSetup();
            }
            else{
                roundStartTimer+=20;
                if (playerWinCounter[1]==2||playerWinCounter[0]==2) {
                    endingAnimations();
                }
                else{
                    roundStartAnimations();
                }
            }
        }
        else {
            gameplayLoop();
        }
        //removes fight logo after 1000 milliseconds
        if (logoVanishCounter>0) {
            if (logoVanishCounter>=1000) {
                renderer.getResourcePane(0).setImage(renderer.getResource(8));
                logoVanishCounter=0;
            }
            else {
                logoVanishCounter+=20;
            }
        }
    }

    /**
     * This function does the calculations for the actual gameplay of the game
     */
    public void gameplayLoop(){
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
                renderer.getHitSound((int) (Math.random()*9)).play();
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
     * This function loads 2 files for 2 characters and sets their stats according
     * to the text files.
     * @param loadingCharacters The names of the characters that will be loaded
     */
    public void initFighting(String[] loadingCharacters) throws IOException {

        roundStartTimer = 0;
        healthBar[0] = new Rectangle(130, 69, 0, 27);
        healthBar[1] = new Rectangle(1048, 69, 0, 27);
        playerTimer[0] = 0;
        playerTimer[1] = 0;
        characters = loader.loadCharacters(loadingCharacters);
    }
}
