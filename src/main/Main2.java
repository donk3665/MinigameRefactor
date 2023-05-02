package main;
import javafx.scene.control.Button;

import javafx.scene.control.Label;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <br> Main.java
 * <br> This class contains a fighting game, a rhythm game, and the various menus.
 * <br> 2021-04-19
 * @author Richard Kha, Daniel Kong, Raymond Ma, Ruby Miller
 *
 */
public class Main2 extends Application{

	
	
	//creating various scenes used throughout application
	Scene mainMenuScreen, gameSelect, crazyRumCtrlScreen, crazyRhyCtrlScreen, rhythmSongSelect, rumbleCharSelect, rhythmGame, fightingGame, rhythmGameEndScreen,creditScreen;
	
	//variable which assists in scene transition
	int count = 0;

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

	//capturing screen size of monitor to resize application for any screen size
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	static double widthAdjust;
	static double heightAdjust;

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
	Rectangle []healthBar = new Rectangle[2];
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
	MediaPlayer menuMusic;


	/**
	 * Entry point to the program
	 * @param args unused
	 */
	public static void main(String[] args) {
		
		launch(args); 
		
		
	}
	/**
	 * This method initializes variables needed throughout the application
	 */
	public void init() {
		P1grid.add(P1Score, 0, 0);
		P1grid.add(P1Combo, 0, 1);
		P1grid.add(P1Accuracy, 0, 2);
		P2grid.add(P2Score, 0, 0);
		P2grid.add(P2Combo, 0, 1);
		P2grid.add(P2Accuracy, 0, 2);
		widthAdjust=width/1536.0;
		heightAdjust=height/864.0;
		
	}

	/**
	 * This is the main menu of the program.
	 * It contains 3 buttons, a play, credits, and quit button. 
	 * @param primaryStage The stage that everything will be displayed on
	 */
	public void start(Stage primaryStage) throws Exception {
		
		//playing the main menu music looped
		String base =  getHostServices().getDocumentBase();
		File file = new File("src/buttonImages/otherAssets/Main_Menu.mp3");
		System.out.println(file.toURI().toString());
		Media media = null;
		try {
		media = new Media(file.toURI().toString());
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			Thread.sleep(10000);
		}
		
		System.out.println("HERE");
		menuMusic = new MediaPlayer(media);
		menuMusic.play();
		menuMusic.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				menuMusic.stop();
				menuMusic.play();
			}
		});
		//scene counter
		count = 0;
		
		//setting up the stage to be a maximized, borderless window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Crazy Minigames");
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
		grid.setHalignment(creditButton, HPos.RIGHT);
		grid.setHalignment(quitButton, HPos.RIGHT);
		grid.setVgap(10);

		// Setting Location of Grid Pane
		grid.setTranslateX(850*widthAdjust);
		grid.setTranslateY(475*heightAdjust);

		
		//adding elements to mainGroup
		mainGroup.getChildren().add(back);
		mainGroup.getChildren().add(LogoScreen);
		mainGroup.getChildren().add(grid);


		// Button interaction when hovered over
		playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				Scale sool = new Scale(0.8, 0.8);
				sool.setPivotX(playButton.getWidth()/2);
				sool.setPivotY(playButton.getHeight()/2);
				playButton.getTransforms().add(sool);
			}

		});
		playButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				Scale sools = new Scale(1.25, 1.25);
				sools.setPivotX(playButton.getWidth()/2);
				sools.setPivotY(playButton.getHeight()/2);
				playButton.getTransforms().add(sools);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
			}

		});

		//button function
		playButton.setOnAction(event ->{
			gameSelect = gameSelect(primaryStage);
			primaryStage.setScene(gameSelect);
			primaryStage.show();

		});
		creditButton.setOnAction(event ->{
			creditScreen = creditScreen(primaryStage);
			primaryStage.setScene(creditScreen);
			primaryStage.show();
		});
		quitButton.setOnAction(event ->{
			System.exit(0);
		});
		
		//setting up scene object and showing scene
		mainMenuScreen = new Scene(mainGroup,1536*widthAdjust,864*heightAdjust);
		primaryStage.setScene(mainMenuScreen);
		primaryStage.show();
		primaryStage.toFront();
	}
	/**
	 * Credit screen
	 * @param stage The stage used for the credit screen
	 */
	public Scene creditScreen(Stage stage) {
		//scene counter
		count = 1;
		
		//Setting up images
		Image BackgroundImage = new Image("buttonImages/mainMenu/CreditScreen.png", 1536*widthAdjust,864*heightAdjust , false, false);
		ImageView Background = 	new ImageView(BackgroundImage);
		
		// Creating main group to add into scene and adding back button
		Pane mainGroup = new Pane();
		mainGroup.getChildren().addAll(Background,addTopAnchorPane(count, stage));
		creditScreen = new Scene(mainGroup);
		return creditScreen;
	}
	/**
	 * Game select screen
	 * @param stage The stage used for the game select screen
	 */
	public Scene gameSelect(Stage stage) {
		
		//scene counter
		count = 1;
		
		//Setting up images
		Image BackgroundImage = new Image("buttonImages/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		Image ControlsButton = new Image("buttonImages/gameSelect/ControlsButton.png", 510*widthAdjust, 93*heightAdjust, false, false);
		Image CrazyRumbleButton = new Image("buttonImages/gameSelect/CrazyRumbleButton.png", 720*widthAdjust, 600*heightAdjust, false, false);
		Image CrazyRhythmButton = new Image("buttonImages/gameSelect/CrazyRhythmButton.png", 720*widthAdjust, 600*heightAdjust, false, false);

		// Creates background Image
		ImageView backgroundImage = new ImageView(BackgroundImage);

		//Crazy Rumble Button
		Button crazyRumble = new Button();
		ImageView crazyRumbleBut = new ImageView(CrazyRumbleButton);
		crazyRumble.setGraphic(crazyRumbleBut); // Setting Image
		crazyRumble.setStyle("-fx-background-color: transparent;");

		// Crazy Rhythm Button
		Button crazyRhythm = new Button();
		ImageView crazyRhythmBut = new ImageView(CrazyRhythmButton);
		crazyRhythm.setGraphic(crazyRhythmBut);// Setting Image
		crazyRhythm.setStyle("-fx-background-color: transparent;");

		// Creating Control Buttons
		Button crazyRumbleControls = new Button();
		Button crazyRhythmControls = new Button();
		ImageView control = new ImageView(ControlsButton);
		ImageView control2 = new ImageView(ControlsButton);
		crazyRumbleControls.setGraphic(control);
		crazyRumbleControls.setStyle("-fx-background-color: transparent;");
		crazyRhythmControls.setGraphic(control2);
		crazyRhythmControls.setStyle("-fx-background-color: transparent;");

		//adding various buttons to GridPane
		GridPane games = new GridPane();
		games.add(crazyRumble, 0, 0, 1, 1);
		games.add(crazyRhythm, 1, 0, 1, 1);
		games.add(crazyRumbleControls, 0, 1, 1, 1);
		games.add(crazyRhythmControls, 1, 1, 1, 1);
		games.setVgap(10);

		// Centering the Control Buttons
		games.setHalignment(crazyRumbleControls, HPos.CENTER);
		games.setHalignment(crazyRhythmControls, HPos.CENTER);

		// Positioning
		games.setTranslateX(25*widthAdjust);
		games.setTranslateY(75*heightAdjust);

		// Button on click methods
		// Crazy Rumble Button
		crazyRumble.setOnAction(event->{
			rumbleCharSelect = rumbleCharSelect(stage);
			stage.setScene(rumbleCharSelect);

			stage.show();
		});

		// Crazy Rhythm Button
		crazyRhythm.setOnAction(event->{
			rhythmSongSelect = rhythmSongSelect(stage);
			stage.setScene(rhythmSongSelect);

			stage.show();
		});

		// Controls Button
		crazyRumbleControls.setOnAction(event->{
			crazyRumCtrlScreen = crazyRumCtrlScreen(stage);
			stage.setScene(crazyRumCtrlScreen);

			stage.show();
		});

		// Crazy Rhythm Control
		crazyRhythmControls.setOnAction(event->{
			crazyRhyCtrlScreen = crazyRhyCtrlScreen(stage);
			stage.setScene(crazyRhyCtrlScreen);

			stage.show();
		});

		// Creating main group to add into scene and adding back button
		Group main = new Group();
		main.getChildren().addAll(backgroundImage,games,addTopAnchorPane(count, stage));
		gameSelect = new Scene(main);
		return gameSelect;
	}
	
	/**
	 * This scene shows controls for crazy rumble.
	 * @param stage The stage used for the fighting game controls screen
	 */
	public Scene crazyRumCtrlScreen(Stage stage) {
		
		//scene counter
		count = 2;

		//Showing image which displays controls and back button
		Image RumTutorial = new Image("buttonImages/tutorials/RumbleTutorial.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView rumbletutscreen = new ImageView(RumTutorial);
		Group main = new Group();
		main.getChildren().addAll(rumbletutscreen, addTopAnchorPane(count, stage));
		crazyRumCtrlScreen = new Scene(main);
		return crazyRumCtrlScreen;
	}
	/**
	 * This scene shows controls for crazy rhythm.
	 * @param stage The stage used for the rhythm game controls screen
	 */
	public Scene crazyRhyCtrlScreen(Stage stage) {
		//scene counter
		count = 2;
		
		//Showing image which displays controls and back button
		Image RhyTutorial = new Image("buttonImages/tutorials/TutorialRhythm.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView rhythmtutscreen = new ImageView(RhyTutorial);
		Group main = new Group();
		main.getChildren().addAll(rhythmtutscreen, addTopAnchorPane(count, stage));
		crazyRhyCtrlScreen = new Scene(main);
		return crazyRhyCtrlScreen;
	}
	/**
	 * 
	 * Character select screen for crazy rumble. Characters are selected in order of first player, then second player.
	 * @param stage The stage used for the fighting game character select screen
	 */
	public Scene rumbleCharSelect (Stage stage) {
		//scene counter
		count = 2;
		
		//setting up images 
		Image BackgroundImage = new Image("buttonImages/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView backgroundImage = new ImageView(BackgroundImage);
		backgroundImage.fitWidthProperty().bind(stage.widthProperty()); 
		backgroundImage.fitHeightProperty().bind(stage.heightProperty());
		Image box = new Image("buttonImages/otherAssets/BigBox.png", 600*widthAdjust, 630*heightAdjust, false, false);
		Image hibiki = new Image("main/hibiki/Hibiki.jpg", 344*widthAdjust, 436*heightAdjust, false, false);
		Image rock = new Image("main/rock/Rock.jpg", 344*widthAdjust, 436*heightAdjust, false, false);
		
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
		String players[] = new String[2];
		
		//variable to get both players' character
		counter = 0;
		
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
				try {
					players[counter]="hibiki";
					fightingGame = fightingGame(stage,players);
					
				} catch (IOException e) {
				}
				stage.setScene(fightingGame);
				stage.show();
				
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
				try {
					players[counter]="rock";
					fightingGame =	fightingGame(stage,players);
				} catch (IOException e) {
				}
				stage.setScene(fightingGame);
				stage.show();
				
			}
		});
		//adding buttons to layout and centering
		grid.add(character1, 0,0,1,1);
		grid.add(character2, 1,0,1,1);
		grid.setTranslateX(150*widthAdjust);
		grid.setTranslateY(100*heightAdjust);

		//creating scene
		Group main = new Group();
		main.getChildren().addAll(backgroundImage,grid, addTopAnchorPane(count, stage),playerChosen,character1Name,character2Name);
		rumbleCharSelect = new Scene(main);
		return rumbleCharSelect;		
	}
	/**
	 * This function contains the code for the fighting game. It's a two player game in 
	 * which each player tries to get the opposing player's health bar down to 0 with
	 * attacks. The player which wins 2 of 3 rounds is declared the winner. This function
	 * first calls the initFighting function and initializes all UI elements as well as
	 * sound effects. It then checks each player's inputs to see if movement or an attack
	 * has been inputted. It also checks whether any keys have been released. Then it calls
	 * the facingDirection function, the attackingRevamped function for both players, the 
	 * damageCalculation function, then finally the movement function. After that, it refreshes
	 * the input logs. It also checks whether each players are in hitstun and whether to keep 
	 * them stunned or let them act. 
	 * 
	 * @author Daniel
	 *
	 * @param stage The stage that the fighting game will be displayed in
	 * @param characterNames The names of the characters that player 1 and 2 picked
	 * @throws IOException
	 */
	public Scene fightingGame (Stage stage, String[] characterNames)  throws IOException{
		
		//Initializing resources
		Image BackgroundImage = new Image("buttonImages/gameAssets/rumbleBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView backgroundImage = new ImageView(BackgroundImage);
		
		initFighting(characterNames);

		Group mainPane = new Group();
		
		//Initializing all UI Images
		playerCounter[0]=-1;
		playerCounter[1]=-1;
		resources = new Image[15];
		resources[3] = new Image("main/fightingFiles/count1.png");
		resources[4] = new Image("main/fightingFiles/count2.png");
		resources[5] = new Image("main/fightingFiles/count3.png");
		resources[6] = new Image("main/fightingFiles/fight.png");
		resources[7] = new Image("main/fightingFiles/ko.png");
		resources[0] = new Image("main/fightingFiles/round1.png");
		resources[1] = new Image("main/fightingFiles/round2.png");
		resources[2] = new Image("main/fightingFiles/round3.png");
		resources[8] = new Image("main/fightingFiles/empty.png");
		resources[9] = new Image("main/fightingFiles/player1.png");
		resources[10] = new Image("main/fightingFiles/player2.png");
		resources[11] = new Image("buttonImages/gameAssets/RumblePlayerOne1.png");
		resources[12] = new Image("buttonImages/gameAssets/RumblePlayerOne2.png");
		resources[13] = new Image("buttonImages/gameAssets/RumblePlayerTwo1.png");
		resources[14] = new Image("buttonImages/gameAssets/RumblePlayerTwo2.png");

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

		//Initializing all sound effects
		hitSounds = new AudioClip[9];
		hitSounds[0]=new AudioClip(new File("src/main/fightingFiles/hit_0.wav").toURI().toString());
		hitSounds[0].setVolume(0.4);
		hitSounds[1]=new AudioClip(new File("src/main/fightingFiles/hit_1.wav").toURI().toString());
		hitSounds[1].setVolume(0.4);
		hitSounds[2]=new AudioClip(new File("src/main/fightingFiles/hit_2.wav").toURI().toString());
		hitSounds[2].setVolume(0.4);
		hitSounds[3]=new AudioClip(new File("src/main/fightingFiles/hit_3.wav").toURI().toString());
		hitSounds[3].setVolume(0.4);
		hitSounds[4]=new AudioClip(new File("src/main/fightingFiles/hit_4.wav").toURI().toString());
		hitSounds[4].setVolume(0.4);
		hitSounds[5]=new AudioClip(new File("src/main/fightingFiles/hit_5.wav").toURI().toString());
		hitSounds[5].setVolume(0.4);
		hitSounds[6]=new AudioClip(new File("src/main/fightingFiles/hit_6.wav").toURI().toString());
		hitSounds[6].setVolume(0.4);
		hitSounds[7]=new AudioClip(new File("src/main/fightingFiles/hit_7.wav").toURI().toString());
		hitSounds[7].setVolume(0.4);
		hitSounds[8]=new AudioClip(new File("src/main/fightingFiles/hit_8.wav").toURI().toString());
		hitSounds[8].setVolume(0.4);

		narrator = new AudioClip[10];
		narrator[0]=new AudioClip(new File("src/main/fightingFiles/Round1.mp3").toURI().toString());
		narrator[0].setVolume(0.4);
		narrator[1]=new AudioClip(new File("src/main/fightingFiles/Round2.mp3").toURI().toString());
		narrator[1].setVolume(0.4);
		narrator[2]=new AudioClip(new File("src/main/fightingFiles/Round3.mp3").toURI().toString());
		narrator[2].setVolume(0.4);
		narrator[3]=new AudioClip(new File("src/main/fightingFiles/Three.mp3").toURI().toString());
		narrator[3].setVolume(0.4);
		narrator[4]=new AudioClip(new File("src/main/fightingFiles/Two.mp3").toURI().toString());
		narrator[4].setVolume(0.4);
		narrator[5]=new AudioClip(new File("src/main/fightingFiles/One.mp3").toURI().toString());
		narrator[5].setVolume(0.4);
		narrator[6]=new AudioClip(new File("src/main/fightingFiles/KO.mp3").toURI().toString());
		narrator[6].setVolume(0.4);
		narrator[7]=new AudioClip(new File("src/main/fightingFiles/Fight.mp3").toURI().toString());
		narrator[7].setVolume(0.4);
		narrator[8]=new AudioClip(new File("src/main/fightingFiles/player1win.wav").toURI().toString());
		narrator[9]=new AudioClip(new File("src/main/fightingFiles/player2win.wav").toURI().toString());


		
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
		fightingGame = new Scene(mainPane);

		timer.start(); 

		Media music = new Media(new File("src/main/fightingFiles/Blue_Water_Blue_Sky.mp3").toURI().toString());
		backgroundMusic = new MediaPlayer(music);
		backgroundMusic.setVolume(0.4);


		//event handler for keyboard presses
		fightingGame.setOnKeyPressed(event -> {
			KeyCode keyCode = event.getCode();

			/*
			 * These if statements check for specific input and record it. Input for movement and attacks
			 * are all processed here. Variables are in place to keep track of inputs and prevent input errors.
			 */
			
			if (keyCode.equals(KeyCode.S)&&characters[0].getKeyReady(0)==false) {
				movement[0][0]=2;
				if (inputTimer != 0) {
					inputs.add("s");
					inputsTime.add(inputTimer);
				}
				characters[0].setKeyReady(0, true);

			}

			if (keyCode.equals(KeyCode.D)&&characters[0].getKeyReady(1)==false) {

				if (movement[0][2] != 2) {

					movement[0][1]=2;
					if (inputTimer != 0) {
						inputs.add("d");
						inputsTime.add(inputTimer);
					}
					characters[0].setKeyReady(1, true);

				}
			}
			if (keyCode.equals(KeyCode.A)&&characters[0].getKeyReady(2)==false) {
				
				if (movement[0][1] != 2) {

					movement[0][2]=2;
					if (inputTimer != 0) {
						inputs.add("a");
						inputsTime.add(inputTimer)
						;}
					characters[0].setKeyReady(2, true);
				}

			}
			if (keyCode.equals(KeyCode.W)&&characters[0].getKeyReady(3)==false&&characters[0].getCentreY()>=downBorder) {

				movement[0][3]=2;
				if (inputTimer != 0) {
					inputs.add("w");
					inputsTime.add(inputTimer);
				}
				characters[0].setKeyReady(3, true);

			}
			if (keyCode.equals(KeyCode.Y)&&characters[0].getKeyReady(4)==false && characters[0].getIsCrouching() == true) {

				attacks[0][2]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 2;
				}
				characters[0].setKeyReady(4, true);

			}
			if (keyCode.equals(KeyCode.Y)&&characters[0].getKeyReady(4)==false && characters[0].getCentreY()<downBorder) {

				attacks[0][4]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 4;
				}
				characters[0].setKeyReady(4, true);

			}
			if (keyCode.equals(KeyCode.Y)&&characters[0].getKeyReady(4)==false && characters[0].getCentreY()>=downBorder) {

				attacks[0][0]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 0;
				}
				characters[0].setKeyReady(4, true);

			}
			if (keyCode.equals(KeyCode.U)&&characters[0].getKeyReady(5)==false && characters[0].getIsCrouching()) {

				attacks[0][3]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 3;
				}
				characters[0].setKeyReady(5, true);

			}
			if (keyCode.equals(KeyCode.U)&&characters[0].getKeyReady(5)==false && characters[0].getCentreY()<downBorder) {

				attacks[0][5]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 5;
				}
				characters[0].setKeyReady(5, true);

			}
			if (keyCode.equals(KeyCode.U)&&characters[0].getKeyReady(5)==false && characters[0].getCentreY()>=downBorder) {

				attacks[0][1]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 1;
				}
				characters[0].setKeyReady(5, true);

			}
			if (keyCode.equals(KeyCode.I)&&characters[0].getKeyReady(6)==false && characters[0].getCentreY()>=downBorder && movement[0][2] == 2) {
				characters[0].setXSpeed(0);

				if (characters[0].getFacingRight()==1) {
					attacks[0][8]=true;
					if (attacks[0][attackIndex[0]] == false) {
						attackIndex[0] = 8;
					}	
				}
				else {
					attacks[0][9]=true;
					if (attacks[0][attackIndex[0]] == false) {
						attackIndex[0] = 9;
					}
				}

				characters[0].setKeyReady(6, true);

			}
			if (keyCode.equals(KeyCode.I)&&characters[0].getKeyReady(6)==false && characters[0].getCentreY()>=downBorder && movement[0][1] == 2) {
				characters[0].setXSpeed(0);
				if (characters[0].getFacingRight()==1) {
					attacks[0][9]=true;
					if (attacks[0][attackIndex[0]] == false) {
						attackIndex[0] = 9;
					}
				}
				else {
					attacks[0][8]=true;
					if (attacks[0][attackIndex[0]] == false) {
						attackIndex[0] = 8;
					}	
				}
				characters[0].setKeyReady(6, true);


			}
			if (keyCode.equals(KeyCode.I)&&characters[0].getKeyReady(6)==false && characters[0].getIsCrouching()) {

				attacks[0][7]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 7;
				}
				characters[0].setKeyReady(6, true);

			}
			if (keyCode.equals(KeyCode.I)&&characters[0].getKeyReady(6)==false && characters[0].getCentreY()<downBorder) {

				attacks[0][10]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 10;
				}
				characters[0].setKeyReady(6, true);

			}
			if (keyCode.equals(KeyCode.I)&&characters[0].getKeyReady(6)==false && characters[0].getCentreY()>=downBorder) {

				attacks[0][6]=true;
				if (attacks[0][attackIndex[0]] == false) {
					attackIndex[0] = 6;
				}
				characters[0].setKeyReady(6, true);

			}

			if (keyCode.equals(KeyCode.DOWN)&&characters[1].getKeyReady(0)==false) {


				movement[1][0]=2;
				if (inputTimer != 0) {
					inputs2.add("s");
					inputsTime2.add(inputTimer);
				}
				characters[1].setKeyReady(0, true);

			}

			if (keyCode.equals(KeyCode.RIGHT)&&characters[1].getKeyReady(1)==false) {

				if (movement[1][2] != 2) {

					movement[1][1]=2;
					if (inputTimer != 0) {
						inputs2.add("d");
						inputsTime2.add(inputTimer);
					}
					characters[1].setKeyReady(1, true);

				}
			}

			if (keyCode.equals(KeyCode.LEFT)&&characters[1].getKeyReady(2)==false) {

				if (movement[1][1] != 2) {

					movement[1][2]=2;
					if (inputTimer != 0) {
						inputs2.add("a");
						inputsTime2.add(inputTimer);
					}
					characters[1].setKeyReady(2, true);
				}

			}

			if (keyCode.equals(KeyCode.UP)&&characters[1].getKeyReady(3)==false&&characters[1].getCentreY()>=downBorder) {



				movement[1][3]=2;
				if (inputTimer != 0) {
					inputs2.add("w");
					inputsTime2.add(inputTimer);
				}
				characters[1].setKeyReady(3, true);

			}
			if (keyCode.equals(KeyCode.COMMA)&&characters[1].getKeyReady(4)==false && characters[1].getIsCrouching()) {

				attacks[1][2]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 2;
				}
				characters[1].setKeyReady(4, true);

			}
			if (keyCode.equals(KeyCode.COMMA)&&characters[1].getKeyReady(4)==false && characters[1].getCentreY()<downBorder) {

				attacks[1][4]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 4;
				}
				characters[1].setKeyReady(4, true);

			}
			if (keyCode.equals(KeyCode.COMMA)&&characters[1].getKeyReady(4)==false && characters[1].getCentreY()>=downBorder) {

				attacks[1][0]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 0;
				}
				characters[1].setKeyReady(4, true);

			}
			if (keyCode.equals(KeyCode.PERIOD)&&characters[1].getKeyReady(5)==false && characters[1].getIsCrouching()) {

				attacks[1][3]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 3;
				}
				characters[1].setKeyReady(5, true);

			}
			if (keyCode.equals(KeyCode.PERIOD)&&characters[1].getKeyReady(5)==false && characters[1].getCentreY()<downBorder) {

				attacks[1][5]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 5;
				}
				characters[1].setKeyReady(5, true);

			}
			if (keyCode.equals(KeyCode.PERIOD)&&characters[1].getKeyReady(5)==false && characters[1].getCentreY()>=downBorder) {

				attacks[1][1]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 1;
				}
				characters[1].setKeyReady(5, true);

			}
			if (keyCode.equals(KeyCode.SLASH)&&characters[1].getKeyReady(6)==false && characters[1].getIsCrouching()) {

				attacks[1][7]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 7;
				}
				characters[1].setKeyReady(6, true);

			}
			if (keyCode.equals(KeyCode.SLASH)&&characters[1].getKeyReady(6)==false && characters[1].getCentreY()<downBorder) {

				attacks[1][10]=true;
				if (attacks[1][attackIndex[1]] == false) {
					attackIndex[1] = 10;
				}
				characters[1].setKeyReady(6, true);

			}
			if (keyCode.equals(KeyCode.SLASH)&&characters[1].getKeyReady(6)==false && characters[1].getCentreY()>=downBorder && movement[1][1] == 2) {
				characters[1].setXSpeed(0);

				if (characters[1].getFacingRight()==-1) {

					attacks[1][8]=true;
					if (attacks[1][attackIndex[1]] == false) {
						attackIndex[1] = 8;
					}
					characters[1].setKeyReady(6, true);
				}
				else {
					attacks[1][9]=true;
					if (attacks[1][attackIndex[1]] == false) {
						attackIndex[1] = 9;
					}
					characters[1].setKeyReady(6, true);
				}

			}
			if (keyCode.equals(KeyCode.SLASH)&&characters[1].getKeyReady(6)==false && characters[1].getCentreY()>=downBorder&& movement[1][2] == 2) {
				characters[1].setXSpeed(0);
				if (characters[1].getFacingRight()==-1) {
					attacks[1][9]=true;
					if (attacks[1][attackIndex[1]] == false) {
						attackIndex[1] = 9;
					}
					characters[1].setKeyReady(6, true);

				}
				else {
					attacks[1][8]=true;
					if (attacks[1][attackIndex[1]] == false) {
						attackIndex[1] = 8;
					}
					characters[1].setKeyReady(6, true);
				}

			}
			if (keyCode.equals(KeyCode.SLASH)&&characters[1].getKeyReady(6)==false && characters[1].getCentreY()>=downBorder) {

				attacks[1][6]=true;
				if (attacks[1][attackIndex[1]] == false) {
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
								hitstun[character]=0;
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
								
								//if player won, return back to character select screen, suspend all timers
								endGame=true;
								timer.stop();
								timer2.cancel(); 
								backgroundMusic.stop();
								Platform.runLater(() -> {
									menuMusic.play();
									rumbleCharSelect = rumbleCharSelect(stage);
									stage.setScene(rumbleCharSelect);
									stage.show();
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
						if (hitstun[i] > 0) {
							characters[i].setIsActionable(false);
							characters[i].setState(2);
							hitstun[i]--;
							if (hitstun[i]==0) {
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
		boolean playerHit[] = new boolean[2];
		int dependent = 1;
		for (int i = 0; i<2; i++) {
			ArrayList<Ellipse> hitBoxes= characters[i].getHitBoxes();
			if (i==1) {
				dependent=0;
			}
			ArrayList<Rectangle> hurtBoxes= characters[dependent].getHurtBoxes();
			
			//checks if player's hitboxes collides with hurtboxes of enemy
			for (int q = 0; q<hitBoxes.size(); q++) {
				for (int t = 0; t<hurtBoxes.size(); t++) {
					if (hitBoxes.get(q).intersects(hurtBoxes.get(t).getBoundsInLocal())) {
						playerHit[i]=true;
					}
				}
			}
			//if statement for when enemy successfully blocks attack
			if (playerHit[i] == true && characters[i].getHitCharacter()==false && blocking(dependent)[characters[i].getAttackCurrent().getBlockingType()]) {

				
				if (characters[dependent].getIsCrouching()) {
					characters[dependent].setIsBlocking(0,true);
				}
				else {
					characters[dependent].setIsBlocking(1, true);
				}
				characters[i].setHitCharacter(true);

				//damage, stun, and knockback calculations
				hitstun[dependent] = characters[i].getAttackCurrent().getShieldStun();
				characters[dependent].setXSpeed(0);
				characters[dependent].changeXSpeed(characters[i].getAttackCurrent().getKnockBackX() * characters[i].getFacingRight() / 6);
				characters[dependent].setYSpeed(0);
				characters[dependent].changeYSpeed(characters[i].getAttackCurrent().getKnockBackY()/ 2);
				characters[i].changeXSpeed(-characters[i].getAttackCurrent().getSelfKnockBack() * characters[i].getFacingRight());
				healthBar[dependent].setWidth(healthBar[dependent].getWidth()-characters[i].getAttackCurrent().getShieldDamage());
			}
			//if statement for when enemy fails to block attack
			else if (playerHit[i] == true && characters[i].getHitCharacter()==false) {

				if (characters[i].getAttackCurrent().getMultihit() == 0) {
					characters[i].setHitCharacter(true);
				}
				
				//play hurt sound if specific attacks are landed
				if ((characters[i].getName().equals("Hibiki")&&(attackIndex[i]==7||attackIndex[i]==9||attackIndex[i]==10))||(characters[i].getName().equals("Rock")&&(attackIndex[i]==2||attackIndex[i]==8||attackIndex[i]==10))) {
					characters[dependent].getHurtSound().play();
				}
				//play sound effect
				hitSounds[(int) (Math.random()*9)].play();
				hitstun[dependent] = characters[i].getAttackCurrent().getHitStun();

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
				if (input.matches("(.*d, d])"))
				{
					return true;
				}
			}

			else
			{
				if (input.matches("(.*a, a])"))
				{
					return true;
				}
			}
		}
		//checks forward dash input for player2
		else if (character==1&&characters[character].getState()==0){
			String input = inputs2.toString();

			if (characters[0].getFacingRight() == -1) {
				if (input.matches("(.*d, d])"))
				{
					return true;
				}


			}
			else
			{
				if (input.matches("(.*a, a])"))
				{
					return true;
				}
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

		if (attacks[attack] == true&&hitstun[character]==0) {
			
			
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
				for (int i = 0; i<tempHold.length; i++) {
					Ellipse temp = new Ellipse(tempHold[i][0] * characters[character].getFacingRight() + xPosition ,tempHold[i][1]+yPosition,tempHold[i][2],tempHold[i][2]);

					characters[character].addHitBox(temp);
					characters[character].addHitBoxCounter(1);

				}
				tempHold = characters[character].getAttack(attack).getHurtBoxes(playerTimer);
				
				//adding new hurtboxes
				for (int i = 0; i<tempHold.length; i++) {
					if (characters[character].getFacingRight()==1) {
						Rectangle temp = new Rectangle(tempHold[i][0]  + xPosition,tempHold[i][1]+yPosition,tempHold[i][2],tempHold[i][3]);
						characters[character].addHurtBox(temp);
						characters[character].addHurtBoxCounter(1);
					}
					else{
						Rectangle temp = new Rectangle(-(tempHold[i][0])+ xPosition-tempHold[i][2],tempHold[i][1]+yPosition,tempHold[i][2],tempHold[i][3]);
						characters[character].addHurtBox(temp);
						characters[character].addHurtBoxCounter(1);
					}
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
		else if (attacks[attack] == true&&hitstun[character]>0) {
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
		if (characters[character].getIsActionable()==true&&(characters[character].getState()==0||characters[character].getState()==5||characters[character].getState()==6)&&characters[character].getCentreY()>=downBorder) {

			if (movement[1] == 2 && characters[character].getIsCrouching() == false) {
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
			if (checkForwardDash(character) && characters[character].getJump() == false) {
				characters[character].changeXSpeed(3 * characters[character].getFacingRight());
			}

			
			if (movement[2]==2 && characters[character].getIsCrouching() == false){
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
		
			if (characters[character].getIsActionable()==true && characters[character].getJump() == false) {
				
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
			if (characters[i].getIsCrouching() == true && characters[i].getIsActionable()) {
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
	 * @throws IOException
	 */
	public void initFighting(String[] loadingCharacters) throws IOException {

		
		timerInterval=0;
		healthBar[0]= new Rectangle(130,69,0,27);
		healthBar[1]= new Rectangle(1048,69,0,27);
		playerTimer[0] = 0;
		playerTimer[1] = 0;
		String fileNames[] = new String[2];
		fileNames[0]= "src/main/"+loadingCharacters[0]+"/";
		fileNames[1]= "src/main/"+loadingCharacters[1]+"/";
		BufferedReader br;
		//loading 2 different characters
		for (int i = 0; i<2; i++) {

			String temp2 = fileNames[i]+loadingCharacters[i]+".txt";
			File file = new File(temp2);
			br = new BufferedReader(new FileReader(file));
			String input = br.readLine();

			//loading stats
			while (!input.equals("[STATS]")) {
				input = br.readLine();
			}
			input = br.readLine();
			String []d = input.split(" ");
			double d2[] = new double[2];
			d2[0] = Integer.parseInt(d[0]);
			d2[1] = Integer.parseInt(d[1]);
			input = br.readLine();
			String inputArr[]=input.split(" ");
			int e=1;
			if (i==1) {
				e=0;
			}
			characters[i] = new FightingCharacter(Math.abs((e*leftBorder+100)+i*(rightBorder-100)-Integer.parseInt(inputArr[0])), downBorder-(Integer.parseInt(inputArr[1])/2), Integer.parseInt(inputArr[0]), Integer.parseInt(inputArr[1]));
			characters[i].setDimensions(d2);
			input = br.readLine();
			characters[i].setWalkSpeed(Integer.parseInt(input));
			input=br.readLine();
			Image temp = new Image(new FileInputStream(fileNames[i]+input)); 
			characters[i].setImageCharacter(temp);
			characters[i].setImageDisplay(temp);
			input=br.readLine();
			characters[i].setOffsetX(Integer.parseInt(input));
			input=br.readLine();
			characters[i].setOffsetY(Integer.parseInt(input));
			input=br.readLine();
			Animation tempAnimation = new Animation();
			tempAnimation.setAnimationTime(Integer.parseInt(input));
			Image[] imageTemp = new Image[Integer.parseInt(input)];
			for (int q = 0; q<imageTemp.length; q++) {
				input=br.readLine();
				imageTemp[q] = new Image(new FileInputStream(fileNames[i]+input)); 
			}
			tempAnimation.setFrames(imageTemp);
			characters[i].setStartingAnimation(tempAnimation);
			AudioClip[] tempAudio = new AudioClip[3];
			for (int q = 0; q<3; q++) {
				input=br.readLine();
				tempAudio[q] = new AudioClip(new File(fileNames[i]+input).toURI().toString());
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
				idleAnimation[a] = new Image(new FileInputStream(fileNames[i]+input));
			}
			characters[i].setIdleAnimation(idleAnimation);

			input = br.readLine();
			characters[i].setCrouch(new Image(new FileInputStream(fileNames[i]+input)));

			frames = Integer.parseInt(br.readLine());
			Image[] walkAnimation = new Image[frames];
			frameTotals[i][5] = frames * 4;
			for (int a = 0; a < frames; a++) {
				input = br.readLine();
				walkAnimation[a] = new Image(new FileInputStream(fileNames[i]+input));
			}
			characters[i].setWalkAnimation(walkAnimation);			

			frames = Integer.parseInt(br.readLine());
			Image[] backWalkAnimation = new Image[frames];
			frameTotals[i][6] = frames * 4;
			for (int a = 0; a < frames; a++) {
				input = br.readLine();
				backWalkAnimation[a] = new Image(new FileInputStream(fileNames[i]+input));
			}
			characters[i].setBackWalkAnimation(backWalkAnimation);

			frames = Integer.parseInt(br.readLine());
			Image[] jumpAnimation = new Image[frames];
			frameTotals[i][4] = frames * 6;
			for (int a = 0; a < frames; a++) {
				input = br.readLine();
				jumpAnimation[a] = new Image(new FileInputStream(fileNames[i]+input));
			}
			characters[i].setJumpAnimation(jumpAnimation);

			input = br.readLine();
			characters[i].setBlock(new Image(new FileInputStream(fileNames[i]+input)));
			input = br.readLine();
			characters[i].setLowBlock(new Image(new FileInputStream(fileNames[i]+input)));
			input = br.readLine();
			characters[i].setHurt(new Image(new FileInputStream(fileNames[i]+input)));
			
			//loading attacks
			while (!input.equals("[ATTACKS]")) {
				input = br.readLine();
			}
			int attackNumber=0;
			int frameAmount = 0;
			int numberOfShapes=0;
			String [] hitBoxes;
			String [] hitBoxData;
			double [][][] hitBoxTotal;
			double [][][] hurtBoxTotal;
			double [][] speedChanges;
			String [] divide;
			Image [] images;
			for (int a = 0; a<11; a++) {
				
				Attack tempAttack = characters[i].getAttack(attackNumber);
				input=br.readLine();
				attackNumber=Integer.parseInt(input);
				input=br.readLine();
				tempAttack.setDamage(Integer.parseInt(input));
				input=br.readLine();
				tempAttack.setShieldDamage(Integer.parseInt(input));
				input=br.readLine();
				tempAttack.setShieldStun(Integer.parseInt(input));
				input=br.readLine();
				tempAttack.setHitStun(Integer.parseInt(input));
				input=br.readLine();
				tempAttack.setBlockingType(Integer.parseInt(input));
				input = br.readLine();
				tempAttack.setMultihit(Byte.parseByte(input));
				input = br.readLine();
				tempAttack.setSfx(new AudioClip(new File(fileNames[i]+input).toURI().toString()));
				br.readLine();
				input=br.readLine();
				tempAttack.setKnockBackY(Integer.parseInt(input));
				input=br.readLine();
				tempAttack.setKnockBackX(Integer.parseInt(input));
				input = br.readLine();
				tempAttack.setSelfKnockBack(Integer.parseInt(input));
				input=br.readLine();
				frameAmount = Integer.parseInt(input);
				tempAttack.setFrameTime(frameAmount);
				input=br.readLine();
				numberOfShapes = Integer.parseInt(input);
				hitBoxTotal = new double[frameAmount][numberOfShapes][3];
				hurtBoxTotal=new double[frameAmount][numberOfShapes][4];
				images = new Image[frameAmount];
				speedChanges= new double[frameAmount][2];
				for (int q = 0; q<frameAmount; q++) {

					input=br.readLine();
					if (input.matches(".*[.].*")) {
						images[q-1] = new Image(new FileInputStream(fileNames[i]+input)); 

						q--;
						continue;
					}
					divide = input.split(" ");

					for (int u = 0; u<3; u++) {
						if (divide[0].equals("NC")) {
							hitBoxTotal[q]=hitBoxTotal[q-1];
							hurtBoxTotal[q]=hurtBoxTotal[q-1];
							images[q]=images[q-1];
							speedChanges[q][0]=0;
							speedChanges[q][1]=0;
						}
						else {
							hitBoxes = divide[u].split(":");
							for (int w = 0; w<hitBoxes.length; w++) {
								hitBoxData = hitBoxes[w].split(",");
								if (u==0) {
									hitBoxTotal[q][w][0] = Integer.parseInt(hitBoxData[0]);
									hitBoxTotal[q][w][1] = Integer.parseInt(hitBoxData[1]);
									hitBoxTotal[q][w][2] = Integer.parseInt(hitBoxData[2]);
								}
								else if (u==1){
									hurtBoxTotal[q][w][0]= Integer.parseInt(hitBoxData[0]);
									hurtBoxTotal[q][w][1]= Integer.parseInt(hitBoxData[1]);
									hurtBoxTotal[q][w][2]= Integer.parseInt(hitBoxData[2]);
									hurtBoxTotal[q][w][3]= Integer.parseInt(hitBoxData[3]);
								}
								else if (u==2) {
									speedChanges[q][0]=Integer.parseInt(hitBoxData[0]);
									speedChanges[q][1]=Integer.parseInt(hitBoxData[1]);
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
	/**
	 * Song select screen for the rhythm game
	 * @param stage The stage that the song select screen will be displayed on
	 */
	public Scene rhythmSongSelect (Stage stage) {
		//scene counter
		count = 2;
		
		//background image and layout setup
		Image BackgroundImage = new Image("buttonImages/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView backgroundImage = new ImageView(BackgroundImage);
		backgroundImage.fitWidthProperty().bind(stage.widthProperty()); 
		backgroundImage.fitHeightProperty().bind(stage.heightProperty());

		Image box = new Image("buttonImages/otherAssets/Box.png", 350*widthAdjust, 90*heightAdjust, false, false);
		GridPane grid = new GridPane();
		Font basic = new Font("Verdana", 12*widthAdjust);

		
		//24 buttons with 24 labels going to 24 different rhythm game maps
		
		Label temp = new Label("BlackYooh vs. siromaru - BLACK or WHITE [BASIC]");
		temp.setFont(basic);
		StackPane stack1 = new StackPane();
		stack1.getChildren().addAll(new ImageView(box),temp );
		Button songSelect1 = new Button("",stack1);

		temp =new Label("Porter Robinson & Madeon - Shelter [Easy]");
		temp.setFont(basic);
		StackPane stack2 = new StackPane();
		stack2.getChildren().addAll(new ImageView(box), temp);
		Button songSelect2 = new Button("",stack2);

		temp = new Label("t+pazolite - QLWA [Normal]");
		temp.setFont(basic);
		StackPane stack3 = new StackPane();
		stack3.getChildren().addAll(new ImageView(box),temp);
		Button songSelect3 = new Button("",stack3);

		temp =  new Label("NOMA - PEPSI  MAN [NORMAL MAN]");
		temp.setFont(basic);
		StackPane stack4 = new StackPane();
		stack4.getChildren().addAll(new ImageView(box),temp);
		Button songSelect4 = new Button("",stack4);

		temp = new Label("KANA-BOON - Silhouette [EZ]");
		temp.setFont(basic);
		StackPane stack5 = new StackPane();
		stack5.getChildren().addAll(new ImageView(box),temp );
		Button songSelect5 = new Button("",stack5);

		temp = new Label("MAKOOTO - Tanukichi no Bouken [Easy]");
		temp.setFont(basic);
		StackPane stack6 = new StackPane();
		stack6.getChildren().addAll(new ImageView(box),temp );
		Button songSelect6 = new Button("",stack6);

		temp = new Label("Lindsey Stirling - Senbonzakura [Harby's NM]");
		temp.setFont(basic);
		StackPane stack7 = new StackPane();
		stack7.getChildren().addAll(new ImageView(box), temp);
		Button songSelect7 = new Button("",stack7);

		temp = new Label("Jin ft. MARiA from GARNiDELiA - daze [NM]");
		temp.setFont(basic);
		StackPane stack8 = new StackPane();
		stack8.getChildren().addAll(new ImageView(box), temp);
		Button songSelect8 = new Button("",stack8);

		temp = new Label("Fairouz Ai & Ishikawa Kaito - Onegai Muscle [Easy]");
		temp.setFont(basic);
		StackPane stack9 = new StackPane();
		stack9.getChildren().addAll(new ImageView(box), temp);
		Button songSelect9 = new Button("",stack9);

		temp = new Label("Soleily - Renatus [Normal]");
		temp.setFont(basic);
		StackPane stack10 = new StackPane();
		stack10.getChildren().addAll(new ImageView(box), temp);
		Button songSelect10 = new Button("",stack10);

		temp = new Label("Soleily - Renatus [Hard]");
		temp.setFont(basic);
		StackPane stack11 = new StackPane();
		stack11.getChildren().addAll(new ImageView(box), temp );
		Button songSelect11 = new Button("",stack11);

		temp = new Label("TK from Ling tosite sigure - unravel [Normal]");
		temp.setFont(basic);
		StackPane stack12 = new StackPane();
		stack12.getChildren().addAll(new ImageView(box), temp);
		Button songSelect12 = new Button("",stack12);

		temp = new Label("BlackYooh vs. siromaru - BLACK or WHITE [NOVICE]");
		temp.setFont(basic);
		StackPane stack13 = new StackPane();
		stack13.getChildren().addAll(new ImageView(box), temp);
		Button songSelect13 = new Button("",stack13);

		temp = new Label("C-Show - Invitation from Mr.C [NOVICE]");
		temp.setFont(basic);
		StackPane stack14 = new StackPane();
		stack14.getChildren().addAll(new ImageView(box), temp);
		Button songSelect14 = new Button("",stack14);

		temp = new Label("DJ OKAWARI - Flower Dance [Normal]");
		temp.setFont(basic);
		StackPane stack15 = new StackPane();
		stack15.getChildren().addAll(new ImageView(box), temp);
		Button songSelect15 = new Button("",stack15);

		temp = new Label("Fairouz Ai & Ishikawa Kaito - Onegai Muscle[Normal]");
		temp.setFont(basic);
		StackPane stack16 = new StackPane();
		stack16.getChildren().addAll(new ImageView(box), temp);
		Button songSelect16 = new Button("",stack16);

		temp = new Label("KANA-BOON - Silhouette [NM]");
		temp.setFont(basic);
		StackPane stack17 = new StackPane();
		stack17.getChildren().addAll(new ImageView(box), temp);
		Button songSelect17 = new Button("",stack17);

		temp = new Label("MAKOOTO - Tanukichi no Bouken [Leni's Normal]");
		temp.setFont(basic);
		StackPane stack18 = new StackPane();
		stack18.getChildren().addAll(new ImageView(box), temp);
		Button songSelect18 = new Button("",stack18);

		temp = new Label("Kairiki bear - Inai Inai Izonshou [timing hell]");
		temp.setFont(basic);
		StackPane stack19 = new StackPane();
		stack19.getChildren().addAll(new ImageView(box), temp);
		Button songSelect19 = new Button("",stack19);

		temp = new Label("BlackYooh vs. siromaru - BLACK or WHITE [ADVANCED]");
		temp.setFont(basic);
		StackPane stack20 = new StackPane();
		stack20.getChildren().addAll(new ImageView(box), temp );
		Button songSelect20 = new Button("",stack20);

		temp = new Label("Porter Robinson & Madeon - Shelter [Loneliness]");
		temp.setFont(basic);
		StackPane stack21 = new StackPane();
		stack21.getChildren().addAll(new ImageView(box), temp);
		Button songSelect21 = new Button("",stack21);

		temp = new Label("Soleily - Renatus [Insane]");
		temp.setFont(basic);
		StackPane stack22 = new StackPane();
		stack22.getChildren().addAll(new ImageView(box), temp);
		Button songSelect22 = new Button("",stack22);

		temp = new Label("LeaF - I [Limbo]");
		temp.setFont(basic);
		StackPane stack23 = new StackPane();
		stack23.getChildren().addAll(new ImageView(box), temp);
		Button songSelect23 = new Button("",stack23);

		temp = new Label("Soulja Baka - Soulja Baka [SMITH RMX]");
		temp.setFont(basic);
		StackPane stack24 = new StackPane();
		stack24.getChildren().addAll(new ImageView(box), temp);
		Button songSelect24 = new Button("",stack24);

		//setting style of buttons
		songSelect1.setStyle("-fx-background-color: transparent;");
		songSelect2.setStyle("-fx-background-color: transparent;");
		songSelect3.setStyle("-fx-background-color: transparent;");
		songSelect4.setStyle("-fx-background-color: transparent;");
		songSelect5.setStyle("-fx-background-color: transparent;");
		songSelect6.setStyle("-fx-background-color: transparent;");
		songSelect7.setStyle("-fx-background-color: transparent;");
		songSelect8.setStyle("-fx-background-color: transparent;");
		songSelect9.setStyle("-fx-background-color: transparent;");
		songSelect10.setStyle("-fx-background-color: transparent;");
		songSelect11.setStyle("-fx-background-color: transparent;");
		songSelect12.setStyle("-fx-background-color: transparent;");
		songSelect13.setStyle("-fx-background-color: transparent;");
		songSelect14.setStyle("-fx-background-color: transparent;");
		songSelect15.setStyle("-fx-background-color: transparent;");
		songSelect16.setStyle("-fx-background-color: transparent;");
		songSelect17.setStyle("-fx-background-color: transparent;");
		songSelect18.setStyle("-fx-background-color: transparent;");
		songSelect19.setStyle("-fx-background-color: transparent;");
		songSelect20.setStyle("-fx-background-color: transparent;");
		songSelect21.setStyle("-fx-background-color: transparent;");
		songSelect22.setStyle("-fx-background-color: transparent;");
		songSelect23.setStyle("-fx-background-color: transparent;");
		songSelect24.setStyle("-fx-background-color: transparent;");


		//event handlers for all 24 buttons
		songSelect1.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"BlackYooh vs. siromaru - BLACK or WHITE (DE-CADE) [Usagi's BASIC Lv.6].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect2.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Porter Robinson & Madeon - Shelter (Dellvangel) [Easy].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect3.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"t+pazolite - QLWA (Yugu) [ExNeko's Normal].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect4.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"NOMA - PEPSI  MAN (Zetera) [HD MAN].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect5.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"KANA-BOON - Silhouette (ExKagii-) [Kyou's EZ].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect6.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"MAKOOTO - Tanukichi no Bouken (Xinely) [Leni's Easy].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect7.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Lindsey Stirling - Senbonzakura (MrSergio) [Harby's NM].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect8.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Jin ft. MARiA from GARNiDELiA - daze (short ver.) (Takane6) [NM].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect9.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Hibiki Sakura (CV Fairouz Ai) & Naruzo Machio (CV Ishikawa Kaito) - Onegai Muscle (TV Size) (Syadow-) [Easy].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect10.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Soleily - Renatus (ExPew) [Normal].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect11.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Soleily - Renatus (Tidek) [Hard].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect12.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"TK from Ling tosite sigure - unravel (TV edit) (Desperate-kun) [Marirose's Normal].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect13.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"BlackYooh vs. siromaru - BLACK or WHITE (DE-CADE) [NOVICE Lv.9].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect14.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"C-Show - Invitation from Mr.C (_FrEsH_ChICkEn_) [NOVICE].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect15.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"DJ OKAWARI - Flower Dance (Narcissu) [CS' Normal].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect16.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Hibiki Sakura (CV Fairouz Ai) & Naruzo Machio (CV Ishikawa Kaito) - Onegai Muscle (TV Size) (Syadow-) [Normal].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect17.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"KANA-BOON - Silhouette (ExKagii-) [NM].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect18.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"MAKOOTO - Tanukichi no Bouken (Xinely) [Leni's Normal].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect19.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Kairiki bear feat. GUMI, Kagamine Rin - Inai Inai Izonshou (juankristal) [timing hell].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect20.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"BlackYooh vs. siromaru - BLACK or WHITE (DE-CADE) [ADVANCED Lv.14].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect21.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Porter Robinson & Madeon - Shelter (Dellvangel) [Loneliness].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect22.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Soleily - Renatus (Tidek) [Insane].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect23.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"LeaF - I (Tidek) [Limbo].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});
		songSelect24.setOnAction(event->{
			menuMusic.stop();
			try {
				rhythmGame = rhythmGame(stage,"Soulja Baka - Soulja Baka (jackylam5) [SMITH RMX].osu");
			} catch (IOException e) {
			}
			stage.setScene(rhythmGame);
			stage.show();
		});

		//adding all buttons to grid
		grid.add(songSelect1, 0,0,1,1);
		grid.add(songSelect2, 1,0,1,1);
		grid.add(songSelect3, 2,0,1,1);
		grid.add(songSelect4, 3,0,1,1);
		grid.add(songSelect5, 0,1,1,1);
		grid.add(songSelect6, 1,1,1,1);
		grid.add(songSelect7, 2,1,1,1);
		grid.add(songSelect8, 3,1,1,1);
		grid.add(songSelect9, 0,2,1,1);
		grid.add(songSelect10, 1,2,1,1);
		grid.add(songSelect11, 2,2,1,1);
		grid.add(songSelect12, 3,2,1,1);
		grid.add(songSelect13, 0,3,1,1);
		grid.add(songSelect14, 1,3,1,1);
		grid.add(songSelect15, 2,3,1,1);
		grid.add(songSelect16, 3,3,1,1);
		grid.add(songSelect17, 0,4,1,1);
		grid.add(songSelect18, 1,4,1,1);
		grid.add(songSelect19, 2,4,1,1);
		grid.add(songSelect20, 3,4,1,1);
		grid.add(songSelect21, 0,5,1,1);
		grid.add(songSelect22, 1,5,1,1);
		grid.add(songSelect23, 2,5,1,1);
		grid.add(songSelect24, 3,5,1,1);

		//placement adjustments
		grid.setVgap(10*heightAdjust);
		grid.setHgap(10*widthAdjust);
		grid.setTranslateX(30*widthAdjust);
		grid.setTranslateY(200*heightAdjust);
		
		//setting up group and scene
		Group main = new Group();
		main.getChildren().addAll(backgroundImage,grid, addTopAnchorPane(count, stage));
		rhythmSongSelect = new Scene(main);
		return rhythmSongSelect;
	}
	/**
	 * 
	 * The scene containing the rhythm game. Player1 uses keys A,S,D,F player2 uses J,K,L.;. Note: keys may be 
	 * unresponsive due to a hardware issue called keyboard ghosting. Mechanical keyboards do not have this issue but everyday
	 * keyboards often do.
	 * @param stage - The stage that the rhythm game will be displayed on
	 * @param fileName - name of file to read and process for game play.
	 * @throws IOException
	 */
	public Scene rhythmGame (Stage stage, String fileName)  throws IOException{
		
		//Initializing resources
		Image BackgroundImage = new Image("buttonImages/gameAssets/RhythmGame.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView backgroundImage = new ImageView(BackgroundImage);
		scores = new long[2][3];
		accuracy = new double[2][2];

		initRhythm(fileName);

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

		rhythmGame = new Scene(mainPane);

		
		
		rhythmGame.setOnKeyPressed(event -> {

			KeyCode keyCode = event.getCode();
			
			/*
			 * These if statements handle key presses and prevent input errors
			 */
			if (keyCode.equals(KeyCode.A)&&keyReady[0]==false) {

				keyReady[0]= true;
				keyPress[0] = true;
			}

			if (keyCode.equals(KeyCode.S)&&keyReady[1]==false) {

				keyReady[1]= true;
				keyPress[1] = true;
			}

			if (keyCode.equals(KeyCode.D)&&keyReady[2]==false) {

				keyReady[2]= true;
				keyPress[2] = true;
			}

			if (keyCode.equals(KeyCode.F)&&keyReady[3]==false) {

				keyReady[3]= true;
				keyPress[3] = true;
			}
			if (keyCode.equals(KeyCode.J)&&keyReady2[0]==false) {

				keyReady2[0]= true;
				keyPress2[0] = true;
			}

			if (keyCode.equals(KeyCode.K)&&keyReady2[1]==false) {

				keyReady2[1]= true;
				keyPress2[1] = true;
			}

			if (keyCode.equals(KeyCode.L)&&keyReady2[2]==false) {

				keyReady2[2]= true;
				keyPress2[2] = true;
			}

			if (keyCode.equals(KeyCode.SEMICOLON)&&keyReady2[3]==false) {

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
					if (soundBool[i]==true) {
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
				player.setOnEndOfMedia(new Runnable() {
					@Override
					public void run() {
						timer.stop();
						timer1.cancel();
						timer2.cancel();
						Platform.runLater(() -> {
							menuMusic.play();
							rhythmGameEndScreen = rhythmGameEndScreen(stage,scores,accuracy);
							stage.setScene(rhythmGameEndScreen);
							stage.show();
						});
					}
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
					for (int i = 0; i<temp.size(); i++) {
						temp3= temp.get(i);
						x = temp3.size();
						for (int q = 0; q<x; q++) {

							if ((temp3.get(q).getIsDisabled()==true||temp3.get(q).getEndYPosition()+scrollSpeed<realBoardLength)&&temp3.get(q).changeYPosition(scrollSpeed)>=realBoardLength+1000) {
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
					if (!temp2.isEmpty()&&temp2.peek().getInitialTime()<=readTimer) {
						
						//calculates position of note with readTimer and method positionReturn
						temp2.peek().setInitialYPosition( (realBoardLength-positionReturn(songTimer,temp2.peek().getInitialTime(),scrollSpeed,-1,inheritingPoints)));
						if (temp2.peek().getType() ==128) {
							temp2.peek().setEndYPosition( (realBoardLength-positionReturn(songTimer,temp2.peek().getEndTime(),scrollSpeed,-1,inheritingPoints)));	
						}
						notesNow2.get(i).add(new Note(temp2.peek()));
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
			accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
			accuracy[player][0]+=2/3/(accuracy[player][1]+1);
			accuracy[player][1]++;
			//changing the resourcePane if enough time has passed
			if (animationCounter[player]>=40) {
				resourcePane[player].setImage(resources[3]);
				animationCounter[player]=0;
			}
		}
		else if (keyPressTime<=64-(3*overallDifficulty)) {
			//calculating accuracy, combo, and score
			accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
			accuracy[player][0]+=1/(accuracy[player][1]+1);
			accuracy[player][1]++;
			scores[player][1]++;
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
			accuracy[player][0]=accuracy[player][0]*accuracy[player][1]/(accuracy[player][1]+1);
			accuracy[player][0]+=2/3/(accuracy[player][1]+1);
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
			accuracy[player][0]+=1/3/(accuracy[player][1]+1);
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
	 * all keypresses are reset.
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
				
				//if statement checks if note is hold type
				if (temp2.getType()==128) {
					
					double initialTime = temp2.getInitialTime();
					double endTime = temp2.getEndTime();

					//sets boolean to true if user clicks in time
					if (keyPress[i]==true &&keyPressTime<=timing) {

						temp2.setStartClicked(true);
						
						//applies score, accuracy, and combo correction
						scoreAccuracyCalculation(scores,accuracy,keyPressTime,player);

						soundBool[0]=true;
						soundBool[1]=true;
					}
					//makes hold note disabled if user does not click in time
					if (temp2.getIsDisabled()==false&&temp2.getStartClicked()==false&&(initialTime-songTimer)<-timing) {

						temp2.setIsDisabled(true);
						
						//applies score, accuracy, and combo correction
						scoreAccuracyCalculation(scores,accuracy,keyPressTime,player);

					}
					//sending the note to the back of the array so it is still drawn, but is not considered when user clicks
					if (temp2.getIsDisabled()==true) {

						temp.add(temp.remove(0));

					}
					//holding note code
					if (temp2.getIsDisabled() == false&&(initialTime-songTimer)<-timing) {

						
						if (songTimer<endTime-timing) {
							//if user releases too early, the note is disabled
							if (keyReady[i]==false) {
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
							if (keyReady[i]==false) {
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
					if (temp2.getStartClicked()==true&&temp2.getIsDisabled()==false&&songTimer<temp2.getEndTime()+timing&&temp2.getInitialYPosition()>realBoardLength) {
						//making hold sliders stop at the hitposition (visual effect only)
						temp2.setInitialYPosition(realBoardLength);
					}
				}
				//else the note is a click type
				else {
					
					// Checking whether the note can be hit
					if (temp2.getIsDisabled()==false) {

						if (keyPressTime<=timing){
							//if hit in time
							if (keyPress[i]==true) {
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
	 * to determine when notes in notesRead should be transfered over to notesNow. For an example of a song with scroll speed changes, check out song Leaf-I in the rhythm game song select.
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
	 * @throws IOException
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
		String soundFiles[] = new String[3];
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
		String inputs[] = input.split(":");
		columnTotal = Integer.parseInt(inputs[1]);
		input=br.readLine();
		inputs = input.split(":");
		overallDifficulty=Double.parseDouble(inputs[1]);
		for (int i = 0; i<overallDifficulty; i++) {
			notesRead.add(new LinkedList<Note>());
			notesNow.add(new ArrayList<Note>());
			notesNow2.add(new ArrayList<Note>());
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

			inputs=input.split("[,]");
			column = (int) Math.floor(Integer.parseInt(inputs[0])*columnTotal/512);
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
		P1Accuracy.setText(String.valueOf(Math.round(accuracy[0][0]*10000)/100.0)+"%");
		P2Score.setText(String.valueOf(scores[1][0]));
		P2Combo.setText(String.valueOf(scores[1][1]));
		P2Accuracy.setText(String.valueOf(Math.round(accuracy[1][0]*10000)/100.0)+"%");
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

				for (int q = 0; q<temp2.size(); q++) {
					initialY = (temp2.get(q).getInitialYPosition()/divisionFactor)-20;
					endY = temp2.get(q).getEndYPosition()/divisionFactor;
					double displayX =(225+i*spacing+boardSpace+(spacing-displayWidth)/2)*widthAdjust;

					if (temp2.get(q).getType()==1||temp2.get(q).getType()==5) {
						gc.setFill(Color.GOLD);
						gc.fillOval(displayX, initialY*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
					}
					else {
						if (temp2.get(q).getInitialYPosition()<=realBoardLength+4000&&temp2.get(q).getEndYPosition()>=0) {
							gc.setFill(Color.BLUE);
							gc.fillOval(displayX, (initialY)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
							gc.fillRect(displayX, (endY+(displayWidth*widthAdjust)/2)*heightAdjust, displayWidth*widthAdjust, (initialY-endY)*heightAdjust);
							gc.fillOval(displayX, (endY)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
						}
						else if (temp2.get(q).getInitialYPosition()>realBoardLength+4000&&temp2.get(q).getEndYPosition()>=0) {
							gc.setFill(Color.BLUE);
							gc.fillRect(displayX, (endY+(displayWidth*widthAdjust)/2)*heightAdjust, displayWidth*widthAdjust, ((realBoardLength+4000)/divisionFactor-endY)*heightAdjust);
							gc.fillOval(displayX, (endY)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
						}
						else if (temp2.get(q).getInitialYPosition()<=(realBoardLength+4000)&&temp2.get(q).getEndYPosition()<0){
							gc.setFill(Color.BLUE);
							gc.fillOval(displayX, (initialY)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
							gc.fillRect(displayX, (displayWidth*widthAdjust/2)*heightAdjust, displayWidth*widthAdjust, initialY*heightAdjust);
						}
						else {
							gc.setFill(Color.BLUE);
							gc.fillRect(displayX, 0, (displayWidth*widthAdjust), (realBoardLength+4000)/divisionFactor*heightAdjust);
						}

					}
				}
			}
		}
		gc.setFill(Color.WHITE);
		
		//displaying the hit positions for both boards
		for (int i = 0; i<4; i++) {
			gc.fillOval((225+spacing*i+(spacing-displayWidth)/2)*widthAdjust, (realBoardLength/divisionFactor-20)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
			gc.fillOval((225+boardSpace+spacing*i+(spacing-displayWidth)/2)*widthAdjust, (realBoardLength/divisionFactor-20)*heightAdjust, displayWidth*widthAdjust, displayWidth*widthAdjust);
		}

	}
/**
 * The game end screen for the rhythm game. This screen shows the scores, the highest combo, and the accuracies of the two players.
 * @param stage - the stage
 * @param scores - the scores, combo, and max combo of the players
 * @param accuracy - the accuracies of the player
 * @return
 */
	public Scene rhythmGameEndScreen (Stage stage, long [][]scores, double[][] accuracy){
		//scene counter
		count=3;
		
		//setting images
		Image BackgroundImage = new Image("buttonImages/gameAssets/GameOverRhythmBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
		ImageView background = new ImageView(BackgroundImage);
		
		//positioning grids
		P1grid.setTranslateX(880*widthAdjust);
		P1grid.setTranslateY(440*heightAdjust);
		P1grid.setVgap(80*heightAdjust);
		P1Accuracy.setFont(new Font("Verdana", 30*widthAdjust));
		P1Combo.setFont(new Font("Verdana", 30*widthAdjust));
		P1Score.setFont(new Font("Verdana", 30*widthAdjust));
		P2Score.setFont(new Font("Verdana", 30*widthAdjust));
		P2Combo.setFont(new Font("Verdana", 30*widthAdjust));
		P2Accuracy.setFont(new Font("Verdana", 30*widthAdjust));
		P2grid.setTranslateX(1200*widthAdjust);
		P2grid.setTranslateY(440*heightAdjust);
		P2grid.setVgap(80*heightAdjust);

		P1Score.setText(String.valueOf(scores[0][0]));
		P1Combo.setText(String.valueOf(scores[0][2]));
		P1Accuracy.setText(String.valueOf(Math.round(accuracy[0][0]*10000)/100.0)+"%");
		P2Score.setText(String.valueOf(scores[1][0]));
		P2Combo.setText(String.valueOf(scores[1][2]));
		P2Accuracy.setText(String.valueOf(Math.round(accuracy[1][0]*10000)/100.0)+"%");
		Image winner;
		
		//displaying who won
		if (scores[0][0]>scores[0][1]) {
			winner = new Image("main/fightingFiles/player1.png", 600*widthAdjust, 100*heightAdjust, false, false);
		}
		else{
			winner = new Image("main/fightingFiles/player1.png", 600*widthAdjust, 100*heightAdjust, false, false);	
		}
		ImageView playerWon = new ImageView(winner);
		playerWon.setX(850*widthAdjust);
		playerWon.setY(200*heightAdjust);
		
		//creating scene
		Group mainPane = new Group();
		mainPane.getChildren().addAll(background,playerWon,P1grid,P2grid,addTopAnchorPane(count,stage));
		rhythmGameEndScreen = new Scene(mainPane);
		return rhythmGameEndScreen;

	}
	/**
	 * This method creates a back button which goes to the previous scene and displays the logo in the middle of the screen
	 * @param count - scene counter
	 * @param stage - the stage
	 * @return
	 */
	public AnchorPane addTopAnchorPane(int count, Stage stage) {

		//Creates Anchor Pane
		AnchorPane anchorpane = new AnchorPane();

		//Gets the Back Button image from package, sets into ImageView, creates button and sets graphic for button.
		Image BackButton = new Image("buttonImages/gameSelect/BackButton.png", 418*widthAdjust, 77*heightAdjust, false, false);
		ImageView backBut = new ImageView(BackButton);
		Button backbutton = new Button();
		backbutton.setGraphic(backBut);
		backbutton.setStyle("-fx-background-color: transparent;");

		// Gets the Logo Image and places it
		Image Logo = new Image("buttonImages/mainMenu/Logo.png", 424.8*widthAdjust, 172*heightAdjust, false, false);
		ImageView logo = new ImageView(Logo);
		anchorpane.getChildren().addAll(logo, backbutton);
		// Anchoring the location
		AnchorPane.setTopAnchor(logo, 0d);
		AnchorPane.setLeftAnchor(logo, 550*widthAdjust);

		AnchorPane.setTopAnchor(backbutton, 0d);
		AnchorPane.setLeftAnchor(backbutton, 0d);

		// Checking for which scene it is
		if (count == 1) {
			backbutton.setOnAction(event->{ 
				stage.setScene(mainMenuScreen);
				stage.show();

			});
		}
		else if (count == 2) {
			backbutton.setOnAction(event->{ 
				stage.setScene(gameSelect);
				stage.show();

			});
		}
		else if (count == 3) {
			backbutton.setOnAction(event ->{
				stage.setScene(rhythmSongSelect);
				stage.show();

			});
		}
	
		return anchorpane;
	}

	/**
	 * This method is called implicitly as the program is closing
	 */
	public void stop() {
		System.exit(0);
	}

}