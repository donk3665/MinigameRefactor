package Scenes;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import main.Animation;
import main.Attack;
import main.FightingCharacter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static Scenes.RumGameScene.*;

public class LoadRumGame {
    public Image[] loadImageResources(){
        Image[] resources = new Image[15];
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
        return resources;
    }
    public AudioClip[] loadSoundEffects(){
        AudioClip[] hitSounds = new AudioClip[9];
        //Initializing all sound effects
        for (int i = 0; i< 9; i++){
            hitSounds[i]= new AudioClip(Objects.requireNonNull(getClass().getResource("/fightingFiles/baseFiles/audio/hit_"+i+".wav")).toExternalForm());
            hitSounds[i].setVolume(0.4);
        }
        return hitSounds;
    }
    public AudioClip[] loadNarratorAudio(){
        AudioClip[] narrator = new AudioClip[10];
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
        return narrator;
    }


    public FightingCharacter[] loadCharacters(String[] loadingCharacters) throws IOException {
        String[] fileNames = new String[2];
        fileNames[0] = "/fightingFiles/characters/" + loadingCharacters[0] + "/";
        fileNames[1] = "/fightingFiles/characters/" + loadingCharacters[1] + "/";
        BufferedReader br;
        FightingCharacter[] characters = new FightingCharacter[2];
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

            KeyCode[] keyArray = {KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.Y, KeyCode.U, KeyCode.I};
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

        return characters;
    }
}
