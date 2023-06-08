package Scenes;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.FightingCharacter;

import static Scenes.MasterScene.heightAdjust;
import static Scenes.MasterScene.widthAdjust;
import static Scenes.RumGameScene.downBorder;

public class RumGameRenderer {
    ImageView[] resourcePane;
    Image[] resources;
    AudioClip[] narrator;
    AudioClip[] hitSounds;
    ImageView[] characterDraw = new ImageView[2];
    Group mainPane = new Group();
    FightingCharacter [] characters;

    public Image getResource(int index){
        return resources[index];
    }
    public ImageView getResourcePane(int index){
        return resourcePane[index];
    }
    public AudioClip getNarrator(int index){
        return narrator[index];
    }
    public AudioClip getHitSound(int index){
        return hitSounds[index];
    }

    public RumGameRenderer(LoadRumGame loader, FightingCharacter [] characters, Canvas canvas){
        this.characters = characters;
        Image BackgroundImage = new Image("SceneAssets/rumGame/rumbleBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView backgroundImage = new ImageView(BackgroundImage);

        resources = loader.loadImageResources();

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

        resourcePane[0].setX(500*widthAdjust);
        resourcePane[0].setY(200*heightAdjust);
        resourcePane[0].setFitWidth(500*widthAdjust);
        resourcePane[0].setFitHeight(400*heightAdjust);

        hitSounds = loader.loadSoundEffects();

        narrator = loader.loadNarratorAudio();

        characterDraw[0] = new ImageView();
        characterDraw[1] = new ImageView();

        characterDraw[0].setImage(characters[0].getImageCharacter());


        characterDraw[0].setFitWidth(characters[0].getDimensionX()*widthAdjust);
        characterDraw[0].setFitHeight(characters[0].getDimensionY()*heightAdjust);

        characterDraw[1].setImage(characters[1].getImageCharacter());
        characterDraw[1].setFitWidth(characters[1].getDimensionX()*widthAdjust);
        characterDraw[1].setFitHeight(characters[1].getDimensionY()*heightAdjust);

        Pane box = new Pane();
        box.getChildren().add(characterDraw[0]);
        box.getChildren().add(characterDraw[1]);

        mainPane.getChildren().addAll(backgroundImage,box,resourcePane[1],resourcePane[3],canvas,resourcePane[0],resourcePane[2],resourcePane[4]);
    }
    public Group getMainPane(){
        return mainPane;
    }
    /**
     * Clears the canvas and renders the characters, health bars and the amount of rounds each character won.
     * This function checks whether a character is facing right or left and flips the image accordingly. It
     * also checks what state the character is in and plays the animation for that state.
     * @param gc - graphics context to call
     */
    public void draw(GraphicsContext gc, int [] frameCounterIndex, Rectangle[] healthBar, int[] playerWinCounter) {


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


}
