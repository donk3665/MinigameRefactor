package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Note;
import main.TimingPoints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RhyGameScene extends MasterScene{
    int[] animationCounter = new int[2];
    ImageView[] resourcePane;
    Image [] resources;


    //variables and objects for the rhythm game
    ArrayList<Queue<Note>>notesRead = new ArrayList<>();
    ArrayList<ArrayList<Note>>notesNow = new ArrayList<>();
    ArrayList<ArrayList<Note>>notesNow2 = new ArrayList<>();
    Queue<TimingPoints> timingPoints= new LinkedList<>();
    ArrayList<TimingPoints> inheritingPoints= new ArrayList<>();
    TimingPoints currentTimingPoint;
    TimingPoints currentInheritingPoint;
    int columnTotal;
    double overallDifficulty;
    boolean[] keyReady = new boolean[4];
    boolean[] keyPress = new boolean[4];
    boolean[] keyReady2 = new boolean[4];
    boolean[] keyPress2 = new boolean[4];
    int songTimer;
    double readTimer;
    double scrollSpeed;
    double scrollConstant;
    int realBoardLength = 1300;
    MediaPlayer player;
    AudioClip[] sounds = new AudioClip[3];
    long[][] scores;
    double [][] accuracy ;
    int offset = -80;
    double timing;
    boolean[] soundBool = new boolean[3];
    GridPane P1grid = new GridPane();
    Label P1Score = new Label("Score");
    Label P1Combo = new Label("Combo");
    Label P1Accuracy = new Label("Accuracy");
    GridPane P2grid = new GridPane();
    Label P2Score = new Label("Score");
    Label P2Combo = new Label("Combo");
    Label P2Accuracy = new Label("Accuracy");

    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {


        //Initializing resources
        Image BackgroundImage = new Image("buttonImages/gameAssets/RhythmGame.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView backgroundImage = new ImageView(BackgroundImage);
        scores = new long[2][3];
        accuracy = new double[2][2];

        try {
            initRhythm(data.getFilename());
        }
        catch (Exception e){
            System.exit(1);
        }
        Group mainPane = new Group();
        resources = new Image[6];
        resources[0] = new Image("rhythm/rhythmFiles/hit0.png");
        resources[1] = new Image("rhythm/rhythmFiles/hitEmpty.png");
        resources[2] = new Image("rhythm/rhythmFiles/hit100.png");
        resources[3] = new Image("rhythm/rhythmFiles/hit200.png");
        resources[4] = new Image("rhythm/rhythmFiles/hit300.png");
        resources[5] = new Image("rhythm/rhythmFiles/hit300g.png");
        resourcePane = new ImageView[2];
        resourcePane[0]= new ImageView(resources[1]);
        resourcePane[1]= new ImageView(resources[1]);
        resourcePane[0].setFitWidth(200*widthAdjust);
        resourcePane[0].setFitHeight(120*heightAdjust);

        resourcePane[1].setFitWidth(200*widthAdjust);
        resourcePane[1].setFitHeight(120*heightAdjust);
        resourcePane[0].setX(370*widthAdjust);
        resourcePane[0].setY(200*heightAdjust);
        resourcePane[1].setX(1120*widthAdjust);
        resourcePane[1].setY(200*heightAdjust);
        Canvas canvas = new Canvas(1536*widthAdjust, 864*heightAdjust);
        final GraphicsContext gc = canvas.getGraphicsContext2D();

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

        //refreshes the screen
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                draw(gc);
            }
        };

        Timer timer2 = new Timer();
        Timer timer1 = new Timer();

        mainPane.getChildren().addAll(backgroundImage,canvas,resourcePane[0],resourcePane[1],P2grid,P1grid);

        timer.start();

        Scene rhythmGame = new Scene(mainPane);



        rhythmGame.setOnKeyPressed(event -> {

            KeyCode keyCode = event.getCode();

            /*
             * These if statements handle key presses and prevent input errors
             */
            if (keyCode.equals(KeyCode.A)&& !keyReady[0]) {

                keyReady[0]= true;
                keyPress[0] = true;
            }

            if (keyCode.equals(KeyCode.S)&& !keyReady[1]) {

                keyReady[1]= true;
                keyPress[1] = true;
            }

            if (keyCode.equals(KeyCode.D)&& !keyReady[2]) {

                keyReady[2]= true;
                keyPress[2] = true;
            }

            if (keyCode.equals(KeyCode.F)&& !keyReady[3]) {

                keyReady[3]= true;
                keyPress[3] = true;
            }
            if (keyCode.equals(KeyCode.J)&& !keyReady2[0]) {

                keyReady2[0]= true;
                keyPress2[0] = true;
            }

            if (keyCode.equals(KeyCode.K)&& !keyReady2[1]) {

                keyReady2[1]= true;
                keyPress2[1] = true;
            }

            if (keyCode.equals(KeyCode.L)&& !keyReady2[2]) {

                keyReady2[2]= true;
                keyPress2[2] = true;
            }

            if (keyCode.equals(KeyCode.SEMICOLON)&& !keyReady2[3]) {

                keyReady2[3]= true;
                keyPress2[3] = true;
            }
            event.consume();
        });
        rhythmGame.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();

            /*
             * These if statements handle key releases and prevent input errors
             */
            if (keyCode.equals(KeyCode.A)) {

                keyReady[0]= false;
            }
            if (keyCode.equals(KeyCode.S)) {

                keyReady[1]= false;
            }
            if (keyCode.equals(KeyCode.D)) {

                keyReady[2]= false;
            }
            if (keyCode.equals(KeyCode.F)) {

                keyReady[3]= false;
            }
            if (keyCode.equals(KeyCode.J)) {

                keyReady2[0]= false;
            }
            if (keyCode.equals(KeyCode.K)) {

                keyReady2[1]= false;
            }
            if (keyCode.equals(KeyCode.L)) {

                keyReady2[2]= false;
            }
            if (keyCode.equals(KeyCode.SEMICOLON)) {

                keyReady2[3]= false;
            }
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
                        resourcePane[i].setImage(resources[1]);
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
                        transferData.setScores(scores);
                        transferData.setAccuracy(accuracy);
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

                inputChecker(notesNow,keyPress,keyReady,scores,accuracy,0);
                inputChecker(notesNow2,keyPress2,keyReady2,scores,accuracy,1);


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
                resourcePane[player].setImage(resources[4]);
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
                resourcePane[player].setImage(resources[3]);
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
                    resourcePane[player].setImage(resources[5]);
                    animationCounter[player]=0;
                }
            }
            else {
                scores[player][0]+=accuracy[player][0]*scores[player][1]*10+100;

                //changing the resourcePane if enough time has passed
                if (animationCounter[player]>=40) {
                    resourcePane[player].setImage(resources[4]);
                    animationCounter[player]=0;
                }
            }
        }
        else if (keyPressTime<=127-(3*overallDifficulty)) {
            //calculating accuracy, combo, and score
            accuracy[player][0]= accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0]+= 2/3.0/(accuracy[player][1]+1);
            accuracy[player][1]++;
            scores[player][1]++;
            if (scores[player][1]>scores[player][2]) {
                scores[player][2]=scores[player][1];
            }
            scores[player][0]+=accuracy[player][0]*scores[player][1]*10+100;
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                resourcePane[player].setImage(resources[3]);
                animationCounter[player]=0;
            }
        }
        else if (keyPressTime<=151-(3*overallDifficulty)) {
            //calculating accuracy, combo, and score
            accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
            accuracy[player][0]+= 1/3.0/(accuracy[player][1]+1);
            accuracy[player][1]++;
            scores[player][1]++;
            if (scores[player][1]>scores[player][2]) {
                scores[player][2]=scores[player][1];
            }
            scores[player][0]+=accuracy[player][0]*scores[player][1]*10+100;
            //changing the resourcePane if enough time has passed
            if (animationCounter[player]>=40) {
                resourcePane[player].setImage(resources[2]);
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
                resourcePane[player].setImage(resources[0]);
                animationCounter[player]=0;
            }
        }

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
     * @param keyPress What keys are currently being pressed
     * @param keyReady What keys are allowed to be pressed
     * @param scores The scores of each player
     * @param accuracy The accuracy of each player
     * @param player What player we're checking the inputs for
     */
    public void inputChecker(ArrayList<ArrayList<Note>> notesNow, boolean[]keyPress,boolean[]keyReady,long[][] scores, double [][] accuracy, int player) {

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
                    if (keyPress[i] &&keyPressTime<=timing) {

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
                            if (!keyReady[i]) {
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
                            if (!keyReady[i]) {
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
                            if (keyPress[i]) {
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
        for (int i = 0; i<4; i++) {
            keyPress[i]=false;
        }

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
    /**
     * This method converts a file into its various notes, timing points, and loads its music file
     * @param gameFile - the game file to be converted
     */
    public void initRhythm(String gameFile) throws IOException {

        //receiving music file
        File file = new File("src/rhythm/rhythmFiles/"+gameFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input = br.readLine();
        while (!input.matches("(AudioFilename:.+)")) {
            input=br.readLine();
        }
        String[] fileName = input.split(":");
        String musicFile = "src/rhythm/rhythmFiles/"+fileName[1].trim();
        Media pick = new Media(new File(musicFile).toURI().toString());
        player = new MediaPlayer(pick);

        //loading up base sounds
        String[] soundFiles = new String[3];
        soundFiles[0]="normal-hitnormal.wav";
        soundFiles[1]="normal-slidertick.wav";
        soundFiles[2]="combobreak.mp3";
        for (int i = 0; i<soundFiles.length; i++) {
            musicFile = "src/rhythm/rhythmFiles/"+soundFiles[i];
            sounds[i] =  new AudioClip(new File(musicFile).toURI().toString());
            sounds[i].setVolume(0.4);

        }

        //getting other stats
        while (!input.matches("(CircleSize:[0-9.]+)")) {
            input=br.readLine();
        }
        String[] inputs = input.split(":");
        columnTotal = Integer.parseInt(inputs[1]);
        input=br.readLine();
        inputs = input.split(":");
        overallDifficulty=Double.parseDouble(inputs[1]);
        for (int i = 0; i<overallDifficulty; i++) {
            notesRead.add(new LinkedList<>());
            notesNow.add(new ArrayList<>());
            notesNow2.add(new ArrayList<>());
        }

        //receiving timing points
        while (!input.equals("[TimingPoints]")) {
            input=br.readLine();
        }
        input=br.readLine();

        while (!input.equals("")) {

            inputs = input.split(",");
            if (Integer.parseInt(inputs[6])==1) {
                timingPoints.add(new TimingPoints(Double.parseDouble(inputs[0]),Double.parseDouble(inputs[1]),Integer.parseInt(inputs[2]),Integer.parseInt(inputs[6])));
            }
            else {
                inheritingPoints.add(new TimingPoints(Double.parseDouble(inputs[0]),Double.parseDouble(inputs[1]),Integer.parseInt(inputs[2]),Integer.parseInt(inputs[6])));
            }
            input=br.readLine();
        }

        //receiving notes
        while (!input.equals("[HitObjects]")) {
            input=br.readLine();
        }
        int column;
        input=br.readLine();
        while (input!=null) {

            inputs=input.split(",");
            column = (int) Math.floor(Integer.parseInt(inputs[0])*columnTotal/512.0);
            Note temp = new Note();
            temp.setInitialTime(Integer.parseInt(inputs[2]));
            temp.setType(Integer.parseInt(inputs[3]));
            temp.setHitSound(Integer.parseInt(inputs[4]));
            inputs=inputs[5].split(":");
            temp.setEndTime(Integer.parseInt(inputs[0]));
            notesRead.get(column).add(temp);
            input=br.readLine();
        }
        //setting constants
        songTimer = -3000;
        currentInheritingPoint =new TimingPoints(songTimer,-100,-1,0);
        currentTimingPoint = timingPoints.remove();
        scrollConstant = 1.5;
        scrollSpeed = scrollConstant;
        timing =  (151-(3*overallDifficulty));
        br.close();




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
        temp=notesNow;

        double displayWidth = 60;
        //displaying notes both player1's board and player2's board
        for (int w = 0; w<2; w++) {
            if (w==1) {
                temp=notesNow2;
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
