package Scenes.RhyGameHelpers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static Scenes.MasterScene.heightAdjust;
import static Scenes.MasterScene.widthAdjust;

public class LoadRhyGame {


    Image[] resources;
    public Image getBackgroundImage() {
        return BackgroundImage;
    }
    Image BackgroundImage;

    public AudioClip[] getSounds() {
        return sounds;
    }

    AudioClip[] sounds = new AudioClip[3];

    public MediaPlayer getPlayer() {
        return player;
    }

    MediaPlayer player;

    public int getColumnTotal() {
        return columnTotal;
    }

    int columnTotal;

    public double getOverallDifficulty() {
        return overallDifficulty;
    }

    double overallDifficulty;

    public ArrayList<Queue<Note>> getNotesRead() {
        return notesRead;
    }

    public ArrayList<ArrayList<Note>> getNotesNow() {
        return notesNow;
    }

    public ArrayList<ArrayList<Note>> getNotesNow2() {
        return notesNow2;
    }

    public Queue<TimingPoints> getTimingPoints() {
        return timingPoints;
    }

    public ArrayList<TimingPoints> getInheritingPoints() {
        return inheritingPoints;
    }

    ArrayList<Queue<Note>>notesRead = new ArrayList<>();
    ArrayList<ArrayList<Note>>notesNow = new ArrayList<>();
    ArrayList<ArrayList<Note>>notesNow2 = new ArrayList<>();
    Queue<TimingPoints> timingPoints= new LinkedList<>();
    ArrayList<TimingPoints> inheritingPoints= new ArrayList<>();

    public LoadRhyGame(){
        loadResources();
        BackgroundImage = new Image("SceneAssets/rhyGame/RhythmGame.png", 1536*widthAdjust, 864*heightAdjust, false, false);
    }

    public void loadResources(){
        resources = new Image[6];
        resources[0] = new Image("rhythmFiles/baseFiles/images/hit0.png");
        resources[1] = new Image("rhythmFiles/baseFiles/images/hitEmpty.png");
        resources[2] = new Image("rhythmFiles/baseFiles/images/hit100.png");
        resources[3] = new Image("rhythmFiles/baseFiles/images/hit200.png");
        resources[4] = new Image("rhythmFiles/baseFiles/images/hit300.png");
        resources[5] = new Image("rhythmFiles/baseFiles/images/hit300g.png");
    }
    public ImageView[] loadResourcePane(){
        ImageView[] resourcePane = new ImageView[2];
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
        return resourcePane;
    }

    public Image getResource(int index){
        return resources[index];
    }

    /**
     * This method converts a file into its various notes, timing points, and loads its music file
     *
     * @param gameFile   - the game file to be converted
     * @param folderName - the folder name of the song
     */
    public void initRhythm(String gameFile, String folderName) throws IOException {

        //receiving music file
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/rhythmFiles/songs/" + folderName + "/" + gameFile))));

        String input = br.readLine();
        while (!input.matches("(AudioFilename:.+)")) {
            input=br.readLine();
        }
        String[] fileName = input.split(":");
        Media pick = new Media(Objects.requireNonNull(getClass().getResource("/rhythmFiles/songs/" + folderName + "/" +fileName[1].trim())).toExternalForm());

        player = new MediaPlayer(pick);

        //loading up base sounds
        String[] soundFiles = new String[3];
        soundFiles[0]="normal-hitnormal.wav";
        soundFiles[1]="normal-slidertick.wav";
        soundFiles[2]="combobreak.mp3";
        for (int i = 0; i<soundFiles.length; i++) {
            sounds[i] =  new AudioClip(Objects.requireNonNull(getClass().getResource("/rhythmFiles/baseFiles/audio/" +soundFiles[i])).toExternalForm());
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
        br.close();

    }
}
