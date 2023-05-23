package Stages;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.FightingCharacter;
import main.Note;
import main.TimingPoints;


import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

abstract class MasterScene{
    //TODO: CHILDREN NEED TO HAVE DEPENDENCY INVERSION IN ORDER TO MAKE IT CLEAN
//    SceneFactory factoryOwner;
//    public MasterScene(SceneFactory factory) {
//        factoryOwner = factory;
//    }
    abstract Scene run(Stage primaryStage, SceneTransferData data);
    static ControllerTemplate controller;
    //capturing screen size of monitor to resize application for any screen size
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double widthAdjust = screenSize.getWidth()/1536.0;
    double heightAdjust = screenSize.getHeight()/864.0;

    //TODO: CLEAN UP THESE
    //variables and objects for the rhythm game
    ArrayList<Queue<Note>>notesRead = new ArrayList<Queue<Note>>();
    ArrayList<ArrayList<Note>>notesNow = new ArrayList<ArrayList<Note>>();
    ArrayList<ArrayList<Note>>notesNow2 = new ArrayList<ArrayList<Note>>();
    Queue<TimingPoints> timingPoints= new LinkedList<TimingPoints>();
    ArrayList<TimingPoints> inheritingPoints= new ArrayList<TimingPoints>();
    TimingPoints currentTimingPoint;
    TimingPoints currentInheritingPoint;
    int columnTotal;
    double overallDifficulty;
    boolean keyReady[] = new boolean[4];
    boolean keyPress[] = new boolean[4];
    boolean keyReady2[] = new boolean[4];
    boolean keyPress2[] = new boolean[4];
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
    boolean soundBool[] = new boolean[3];
    GridPane P1grid = new GridPane();
    Label P1Score = new Label("Score");
    Label P1Combo = new Label("Combo");
    Label P1Accuracy = new Label("Accuracy");
    GridPane P2grid = new GridPane();
    Label P2Score = new Label("Score");
    Label P2Combo = new Label("Combo");
    Label P2Accuracy = new Label("Accuracy");

    //variables and objects for the fighting game
    static FightingCharacter[] characters = new FightingCharacter[2];
    static double downBorder = 730;
    static double rightBorder = 1400;
    static int leftBorder = 100;
    static byte movement[][] = new byte[2][4];
    ImageView[] characterDraw = new ImageView[2];
    int playerTimer[] = new int[2];
    boolean attacks[][] = new boolean[2][11];
    static int []attackIndex = new int[2];
    Rectangle[]healthBar = new Rectangle[2];
    static double[][] tempHold;
    static ArrayList<String> inputs = new ArrayList<String>();
    static ArrayList<Integer> inputsTime = new ArrayList<Integer>();
    static ArrayList<String> inputs2 = new ArrayList<String>();
    static ArrayList<Integer> inputsTime2 = new ArrayList<Integer>();
    static int inputTimer = 0;
    static int[] hitstun = new int[2];
    byte counter;
    int [] playerCounter = new int[2];
    boolean endGame = false;
    int timerInterval = 0;
    int animationCounter[] = new int[2];
    ImageView[] resourcePane;
    Image [] resources;
    int logoVanishCounter = 0;
    static AudioClip[] hitSounds;
    MediaPlayer backgroundMusic;
    static AudioClip[] narrator;
    int[][] frameTotals = new int[2][8];
    static int[][] frameCounters = new int[2][7];
    int frameCounterIndex[] = {0,0};
    Label playerChosen;

    //main menu music
    static MediaPlayer menuMusic;

    //TODO: CLEAN UP THESE

    public static void setController(ControllerTemplate controller1){
        controller = controller1;
    }
    /**
     * This method creates a back button which goes to the previous scene and displays the logo in the middle of the screen
     * @return
     */
    public AnchorPane addTopAnchorPane(SceneEnums type) {

        //Creates Anchor Pane
        AnchorPane anchorpane = new AnchorPane();

        //Gets the Back Button image from package, sets into ImageView, creates button and sets graphic for button.
        javafx.scene.image.Image BackButton = new javafx.scene.image.Image("buttonImages/gameSelect/BackButton.png", 418*widthAdjust, 77*heightAdjust, false, false);
        ImageView backBut = new ImageView(BackButton);
        javafx.scene.control.Button backbutton = new Button();
        backbutton.setGraphic(backBut);
        backbutton.setStyle("-fx-background-color: transparent;");

        // Gets the Logo Image and places it
        javafx.scene.image.Image Logo = new Image("buttonImages/mainMenu/Logo.png", 424.8*widthAdjust, 172*heightAdjust, false, false);
        ImageView logo = new ImageView(Logo);
        anchorpane.getChildren().addAll(logo, backbutton);
        // Anchoring the location
        AnchorPane.setTopAnchor(logo, 0d);
        AnchorPane.setLeftAnchor(logo, 550*widthAdjust);

        AnchorPane.setTopAnchor(backbutton, 0d);
        AnchorPane.setLeftAnchor(backbutton, 0d);

        backbutton.setOnAction(event->{
            controller.changeScenes(type, null);
        });
//
//        // TODO: changing scenes should be left to controller
//        // Checking for which scene it is
//        if (count == 1) {
//            backbutton.setOnAction(event->{
//             //   stage.setScene(mainMenuScreen);
//             //   stage.show();
//
//            });
//        }
//        else if (count == 2) {
//            backbutton.setOnAction(event->{
//              //  stage.setScene(gameSelect);
//              //  stage.show();
//
//            });
//        }
//        else if (count == 3) {
//            backbutton.setOnAction(event ->{
//             //   stage.setScene(rhythmSongSelect);
//             //  stage.show();
//
//            });
//        }

        return anchorpane;
    }
}
