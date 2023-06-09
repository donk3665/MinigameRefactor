package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import Scenes.RhyGameHelpers.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.*;

public class RhyGameScene extends MasterScene{
    int[] animationCounter = new int[2];

    //variables and objects for the rhythm game
    ArrayList<Queue<Note>>notesRead;
    ArrayList<ArrayList<Note>>notesNow;
    ArrayList<ArrayList<Note>>notesNow2;
    Queue<TimingPoints> timingPoints;
    ArrayList<TimingPoints> inheritingPoints;
    TimingPoints currentTimingPoint;
    TimingPoints currentInheritingPoint;
    int columnTotal;
    double overallDifficulty;
    int songTimer;
    double readTimer;
    double scrollSpeed;
    double scrollConstant;
    public static final int realBoardLength = 1300;
    MediaPlayer player;
    int offset = -30;
    double timing;
    boolean[] soundBool = new boolean[3];
    LoadRhyGame loader;
    AudioClip[] sounds;
    RhyGameRenderer renderer;

    public ArrayList<ArrayList<Note>> getNotesNow(){
        return notesNow;
    }
    public ArrayList<ArrayList<Note>> getNotesNow2(){
        return notesNow2;
    }

    public void importFromLoader(){
        sounds = loader.getSounds();
        notesNow = loader.getNotesNow();
        notesNow2 = loader.getNotesNow2();
        player = loader.getPlayer();
        columnTotal = loader.getColumnTotal();
        overallDifficulty = loader.getOverallDifficulty();
        notesRead = loader.getNotesRead();
        timingPoints = loader.getTimingPoints();
        inheritingPoints = loader.getInheritingPoints();
    }
    public void setConstants(){
        //setting constants
        songTimer = -3000;
        currentInheritingPoint =new TimingPoints(songTimer,-100,-1,0);
        currentTimingPoint = timingPoints.remove();
        scrollConstant = 1.5;
        scrollSpeed = scrollConstant;
        timing =  (151-(3*overallDifficulty));
    }

    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {
        loader = new LoadRhyGame();
        RhyInputController player1Input = new RhyInputController(new KeyCode[]{KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F});
        RhyInputController player2Input = new RhyInputController(new KeyCode[]{KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.SEMICOLON});
        try {
            loader.initRhythm(data.getFilename(), data.getFolderName());
        }
        catch (Exception e){
            System.err.println("Initialization error");
            System.exit(1);
        }
        importFromLoader();
        setConstants();

        Canvas canvas = new Canvas(1536*widthAdjust, 864*heightAdjust);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        renderer = new RhyGameRenderer(this, loader, canvas);

        //refreshes the screen
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                renderer.draw(gc);
            }
        };

        Timer timer2 = new Timer();
        Timer timer1 = new Timer();

        timer.start();

        Scene rhythmGame = new Scene(renderer.getMainPane());



        rhythmGame.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            player1Input.keyPressed(keyCode);
            player2Input.keyPressed(keyCode);
            event.consume();
        });
        rhythmGame.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            player1Input.keyReleased(keyCode);
            player2Input.keyReleased(keyCode);
            event.consume();
        });

        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //plays sounds effects
                for (int i = 0; i<soundBool.length; i++) {
                    if (soundBool[i]) {
                        soundBool[i]=false;
                        sounds[i].play();
                    }
                }

                //vanishes image after 0.5s
                for (int i = 0; i<2; i++) {
                    animationCounter[i]+=20;
                    if (animationCounter[i]>500) {
                        animationCounter[i]=40;
                        renderer.setImagePane(i, loader.getResource(1));
                    }
                }
            }
        },3000,20);
        //plays music after delay
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                player.play();
                player.stop();
                player.play();
            }
        },3000+offset);
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //when song ends, show rhythm game end screen
                player.setOnEndOfMedia(() -> {
                    timer.stop();
                    timer1.cancel();
                    timer2.cancel();
                    Platform.runLater(() -> {
                        menuMusic.play();
                        SceneTransferData transferData = new SceneTransferData();
                        transferData.setScores(renderer.getScores());
                        transferData.setAccuracy(renderer.getAccuracy());
                        controller.changeScenes(SceneEnums.RHY_END_SCREEN, transferData);

                    });
                });
                songTimer+=1;
                ArrayList<ArrayList<Note>> temp;
                ArrayList<Note> temp3;
                int x;
                temp = notesNow;

                //looking through both note arraylists, if the note is off the screen it can be deleted from the arraylists
                for (int w = 0; w<2; w++) {
                    if (w==1) {
                        temp=notesNow2;
                    }
                    for (ArrayList<Note> notes : temp) {
                        temp3 = notes;
                        x = temp3.size();
                        for (int q = 0; q < x; q++) {

                            if ((temp3.get(q).getIsDisabled() || temp3.get(q).getEndYPosition() + scrollSpeed < realBoardLength) && temp3.get(q).changeYPosition(scrollSpeed) >= realBoardLength + 1000) {
                                temp3.remove(q);
                                q--;
                                x--;
                            }

                        }
                    }
                }

                inputChecker(notesNow,player1Input,renderer.getScores(),renderer.getAccuracy(),0);
                inputChecker(notesNow2,player2Input,renderer.getScores(),renderer.getAccuracy(),1);


                //changing inheriting points when the time is correct
                if (!inheritingPoints.isEmpty() &&songTimer>= inheritingPoints.get(0).getStartTime()) {
                    currentInheritingPoint = inheritingPoints.get(0);
                    inheritingPoints.remove(0);

                    //changing scroll speed
                    scrollSpeed =  (-1/(currentInheritingPoint.getBeatLength()/100.0)*scrollConstant);
                }
                //changing timing points when the time is correct
                if (!timingPoints.isEmpty() &&songTimer>= timingPoints.peek().getStartTime()) {
                    currentTimingPoint = timingPoints.remove();
                }
                readTimer = readTimerReturn(songTimer,realBoardLength,scrollSpeed,-1,inheritingPoints);

                Queue<Note> temp2;

                /*
                 * if a note in the notesRead arraylist has its initial time greater than or equal to readTimer,
                 * it is time to put that note in the active arraylists
                 */
                for (int i = 0; i<columnTotal; i++) {
                    temp2 = notesRead.get(i);
                    Note tempNote = temp2.peek();
                    if (!temp2.isEmpty()&&tempNote.getInitialTime()<=readTimer) {

                        //calculates position of note with readTimer and method positionReturn
                        tempNote.setInitialYPosition( (realBoardLength-positionReturn(songTimer,tempNote.getInitialTime(),scrollSpeed,-1,inheritingPoints)));
                        if (tempNote.getType() ==128) {
                            tempNote.setEndYPosition( (realBoardLength-positionReturn(songTimer,tempNote.getEndTime(),scrollSpeed,-1,inheritingPoints)));
                        }
                        notesNow2.get(i).add(new Note(tempNote));
                        notesNow.get(i).add(temp2.remove());
                    }

                }

            }
        }, 0, 1);

        return rhythmGame;
    }
    /**
     * Calculates scores, combo and note accuracy of the players
     * @param scores - array containing the scores and combo of the players
     * @param accuracy - array containing the accuracy and total amount of notes
     * @param keyPressTime - positive time differential between noteTime and keyPress, -2 and -1 are flags for special conditions
     * @param player - player whose score, accuracy and combo is being evaluated
     */
    public void scoreAccuracyCalculation(long[][]scores, double[][]accuracy,double keyPressTime, int player) {

        if (keyPressTime==-2) {
            //calculating accuracy, combo, and score
            accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0]+=1/(accuracy[player][1]+1);
            accuracy[player][1]++;
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                renderer.setImagePane(player,loader.getResource(4) );

                animationCounter[player]=0;
            }
        }
        else if (keyPressTime==-1) {
            //calculating accuracy, combo, and score
            accuracy[player][0] = accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0] += 2/3.0/(accuracy[player][1]+1);
            accuracy[player][1] ++;
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                renderer.setImagePane(player,loader.getResource(3) );
                animationCounter[player]=0;
            }
        }
        else if (keyPressTime<=64-(3*overallDifficulty)) {
            //calculating accuracy, combo, and score
            accuracy[player][0] = accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0] += 1/(accuracy[player][1]+1);
            accuracy[player][1] ++;
            scores[player][1] ++;
            if (scores[player][1]>scores[player][2]) {
                scores[player][2]=scores[player][1];
            }

            //if keyPressTime is very small, add additional points and show a different image
            if (keyPressTime<=16) {
                scores[player][0]+=(accuracy[player][0]*scores[player][1]*10+100)*2;

                //changing the resourcePane if enough time has passed
                if (animationCounter[player]>=40) {
                    renderer.setImagePane(player,loader.getResource(5) );
                    animationCounter[player]=0;
                }
            }
            else {
                scores[player][0]+=accuracy[player][0]*scores[player][1]*10+100;

                //changing the resourcePane if enough time has passed
                if (animationCounter[player]>=40) {
                    renderer.setImagePane(player,loader.getResource(4) );
                    animationCounter[player]=0;
                }
            }
        }
        else if (keyPressTime<=127-(3*overallDifficulty)) {
            //calculating accuracy, combo, and score
            accuracy[player][0]= accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0]+= 2/3.0/(accuracy[player][1]+1);
            maxComboAndScoreCalculation(scores, accuracy, player);
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                renderer.setImagePane(player,loader.getResource(3) );
                animationCounter[player]=0;
            }
        }
        else if (keyPressTime<=151-(3*overallDifficulty)) {
            //calculating accuracy, combo, and score
            accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0]+= 1/3.0/(accuracy[player][1]+1);
            maxComboAndScoreCalculation(scores, accuracy, player);
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                renderer.setImagePane(player,loader.getResource(2) );
                animationCounter[player]=0;
            }
        }
        else {
            //calculating accuracy, combo, and score
            accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][1]++;
            if (scores[player][1]>20) {
                soundBool[2]=true;
            }
            scores[player][1]=0;
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                renderer.setImagePane(player,loader.getResource(0) );
                animationCounter[player]=0;
            }
        }

    }

    private void maxComboAndScoreCalculation(long[][] scores, double[][] accuracy, int player) {
        accuracy[player][1]++;
        scores[player][1]++;
        if (scores[player][1]>scores[player][2]) {
            scores[player][2]=scores[player][1];
        }
        scores[player][0]+=accuracy[player][0]*scores[player][1]*10+100;
    }

    /**
     * This function goes through every note that is currently on screen and checks
     * to see if they are clicked. The program checks if the note is a normal or hold
     * note. If it is a hold note, it checks whether the note is disabled, whether the
     * player hit the note on time, and whether the player missed the note. Then it plays
     * a hit noise, does nothing, or disables the note accordingly. If the note is a normal
     * note, it does the same calculation but for a regular note instead of a hold note. Lastly,
     * all key presses are reset.
     * @param notesNow All notes that are currently on screen
     * @param playerController The controller of the player
     * @param scores The scores of each player
     * @param accuracy The accuracy of each player
     * @param player What player we're checking the inputs for
     */
    public void inputChecker(ArrayList<ArrayList<Note>> notesNow, RhyInputController playerController,long[][] scores, double [][] accuracy, int player) {

        for (int i = 0; i<notesNow.size(); i++) {
            ArrayList<Note>temp= notesNow.get(i);

            if (temp.size()>0) {

                Note temp2 = temp.get(0);
                double keyPressTime = Math.abs(temp2.getInitialTime()-songTimer);

                //if statement checks whether note is hold type
                if (temp2.getType()==128) {

                    double initialTime = temp2.getInitialTime();
                    double endTime = temp2.getEndTime();

                    //sets boolean to true if user clicks in time
                    if (playerController.getKeyPress(i) &&keyPressTime<=timing) {

                        temp2.setStartClicked(true);

                        //applies score, accuracy, and combo correction
                        scoreAccuracyCalculation(scores,accuracy,keyPressTime,player);

                        soundBool[0]=true;
                        soundBool[1]=true;
                    }
                    //makes hold note disabled if user does not click in time
                    if (!temp2.getIsDisabled() && !temp2.getStartClicked() &&(initialTime-songTimer)<-timing) {

                        temp2.setIsDisabled(true);

                        //applies score, accuracy, and combo correction
                        scoreAccuracyCalculation(scores,accuracy,keyPressTime,player);

                    }
                    //sending the note to the back of the array, so it is still drawn, but is not considered when user clicks
                    if (temp2.getIsDisabled()) {

                        temp.add(temp.remove(0));

                    }
                    //holding note code
                    if (!temp2.getIsDisabled() &&(initialTime-songTimer)<-timing) {


                        if (songTimer<endTime-timing) {
                            //if user releases too early, the note is disabled
                            if (playerController.getKeyNotReady(i)) {
                                //applies score, accuracy, and combo correction
                                scoreAccuracyCalculation(scores,accuracy,Math.abs(temp2.getEndTime()-songTimer),player);

                                temp2.setIsDisabled(true);

                            }
                            else {
                                //hold note gives one combo every beatLength/meter milliseconds.
                                if (songTimer-temp2.getTimePassed()>=currentTimingPoint.getBeatLength()/currentTimingPoint.getMeter())	{
                                    temp2.changeTimePassed((int) currentTimingPoint.getBeatLength()/currentTimingPoint.getMeter());
                                    scores[player][1]++;
                                    soundBool[1]=true;
                                }
                            }
                        }
                        else if (Math.abs(temp2.getEndTime()-songTimer)<=timing){
                            //if release in time
                            if (playerController.getKeyNotReady(i)) {
                                //applies score, accuracy, and combo correction
                                scoreAccuracyCalculation(scores,accuracy,-2,player);

                                temp.remove(0);
                                soundBool[0]=true;
                            }

                        }
                        else if (songTimer>temp2.getEndTime()+timing){
                            //if release too late
                            scoreAccuracyCalculation(scores,accuracy,-1,player);
                            temp.remove(0);
                        }

                    }
                    if (temp2.getStartClicked() && !temp2.getIsDisabled() &&songTimer<temp2.getEndTime()+timing&&temp2.getInitialYPosition()>realBoardLength) {
                        //making hold sliders stop at the hit position (visual effect only)
                        temp2.setInitialYPosition(realBoardLength);
                    }
                }
                //else the note is a click type
                else {

                    // Checking whether the note can be hit
                    if (!temp2.getIsDisabled()) {

                        if (keyPressTime<=timing){
                            //if hit in time
                            if (playerController.getKeyPress(i)) {
                                //applies score, accuracy, and combo correction
                                scoreAccuracyCalculation(scores,accuracy,keyPressTime,player);

                                temp.remove(0);
                                soundBool[0]=true;

                            }
                        }
                        //if hit too late
                        else if (temp2.getInitialTime()-songTimer<=-timing) {
                            scoreAccuracyCalculation(scores,accuracy,keyPressTime,player);
                            temp2.setIsDisabled(true);
                            temp.add(temp.remove(0));

                        }
                    }

                }
            }


        }
        //resetting inputs
        playerController.resetKeyPress();


    }
    /**
     * This method uses recursion and the current time
     * to determine when notes in notesRead should be transferred over to notesNow. For an example of a song with scroll speed changes, check out song Leaf-I in the rhythm game song select.
     * @param timeNow - the current time
     * @param boardLength - the length of the board
     * @param scrollSpeed - the current scroll speed
     * @param index - the index of the inheriting point currently being evaluated
     * @param inheritingPoints - arrayList of inheriting points (inheriting points alter scroll speed)
     * @return time in which notes with initial times behind this value should begin falling
     */
    public double readTimerReturn(double timeNow,double boardLength, double scrollSpeed,int index, ArrayList<TimingPoints> inheritingPoints ) {
        index++;

        //calculating the time needed to traverse the board at current speed
        double timeNeeded = boardLength	/(scrollSpeed);

        double totalTime;

        //finding time till next inheriting point
        if (inheritingPoints.size()<=index){
            totalTime=Integer.MAX_VALUE;
        }
        else {
            totalTime = (inheritingPoints.get(index).getStartTime()-timeNow);
        }

        //if the time till the next inheriting point is larger than the time needed to traverse the board, return
        if (timeNeeded<=totalTime) {
            return timeNow + timeNeeded;
        }
        else {
            //else recursively enter the same method and change the scroll speed, the board length, and the initial time
            boardLength=boardLength - (totalTime*scrollSpeed);
            scrollSpeed = (-1/(inheritingPoints.get(index).getBeatLength()/100.0)*scrollConstant);
            return readTimerReturn(timeNow+totalTime,boardLength,scrollSpeed,index,inheritingPoints);

        }


    }
    /**
     * Finds the initial position of the note with recursion, so that when the note reaches its hit position
     * it is timed with the music
     * @param timeNow - the current time
     * @param initialTime - the initial time of the note
     * @param scrollSpeed - the current scroll speed
     * @param index - the index of the inheriting point currently being evaluated
     * @param inheritingPoints - arrayList of inheriting points (inheriting points alter scroll speed)
     * @return the position of the note in reference to the hit position. Greater number = further from hit position.
     */
    public double positionReturn(double timeNow,double initialTime, double scrollSpeed,int index, ArrayList<TimingPoints> inheritingPoints) {

        index++;
        double totalTime;

        //finds the time till next inheriting point
        if (inheritingPoints.size()<=index){
            totalTime=Integer.MAX_VALUE;
        }
        else {
            totalTime = (inheritingPoints.get(index).getStartTime()-timeNow);
        }

        //if the time till the next inheriting point is larger than the time till the initial time of the note, return
        if (totalTime >=(initialTime-timeNow)) {
            return (initialTime-timeNow)*scrollSpeed;
        }
        else {
            //else add up the distance which can be covered, recursively enter method with different start time, scroll speed and index
            double newScrollSpeed =  (-1/(inheritingPoints.get(index).getBeatLength()/100.0)*scrollConstant);

            return totalTime*scrollSpeed + positionReturn(timeNow+totalTime,initialTime,newScrollSpeed,index,inheritingPoints);
        }

    }
}
