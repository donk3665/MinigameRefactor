package Scenes;

import javafx.scene.input.KeyCode;
import main.FightingCharacter;

import static Scenes.RumGameScene.downBorder;

public class RumInputController {

    KeyCode [] keyCodes ;
    FightingCharacter character;
    public RumInputController(FightingCharacter character, KeyCode [] keyCode){
        this.keyCodes = keyCode;
        this.character = character;
    }

    public void keyPressed(KeyCode keyCode, int inputTimer) {
        /*
         * These if statements check for specific input and record it. Input for movement and attacks
         * are all processed here. Variables are in place to keep track of keyCodes and prevent input errors.
         */
        if (keyCode.equals(keyCodes[0]) && character.getKeyNotReady(3) && character.getCentreY() >= downBorder) {
            character.getMovement()[3] = 2;
            character.getInputs().add("w");
            character.getInputTime().add(inputTimer);
            character.setKeyReady(3, true);
        }
        if (keyCode.equals(keyCodes[1]) && character.getKeyNotReady(2)) {
            if (character.getMovement()[1] != 2) {
                character.getMovement()[2] = 2;
                if (inputTimer != 0) {
                    character.getInputs().add("a");
                    character.getInputTime().add(inputTimer);
                }
                character.setKeyReady(2, true);
            }
        }
        if (keyCode.equals(keyCodes[2]) && character.getKeyNotReady(0)) {
            character.getMovement()[0] = 2;
            if (inputTimer != 0) {
                character.getInputs().add("s");
                character.getInputTime().add(inputTimer);
            }
            character.setKeyReady(0, true);
        }
        if (keyCode.equals(keyCodes[3]) && character.getKeyNotReady(1)) {
            if (character.getMovement()[2] != 2) {
                character.getMovement()[1] = 2;
                if (inputTimer != 0) {
                    character.getInputs().add("d");
                    character.getInputTime().add(inputTimer);
                }
                character.setKeyReady(1, true);
            }
        }
        if (keyCode.equals(keyCodes[4]) && character.getKeyNotReady(4)) {
            if (character.getIsCrouching()) {
                character.setAttackInt(2);
            } else if (character.getCentreY() < downBorder) {
                character.setAttackInt(4);
            } else {
                character.setAttackInt(0);
            }
            character.setKeyReady(4, true);
        }

        if (keyCode.equals(keyCodes[5]) && character.getKeyNotReady(5)) {
            if (character.getIsCrouching()) {
                character.setAttackInt(3);
            } else if (character.getCentreY() < downBorder) {
                character.setAttackInt(5);
            } else {
                character.setAttackInt(1);
            }
            character.setKeyReady(5, true);

        }

        if (keyCode.equals(keyCodes[6]) && character.getKeyNotReady(6)) {
            if (character.getIsCrouching()) {
                character.setAttackInt(7);
            } else if (character.getCentreY() < downBorder) {
                character.setAttackInt(10);
            } else {
                character.setAttackInt(6);
            }
            character.setKeyReady(6, true);
        }
    }
    public void keyRelease(KeyCode keyCode){

        //RELEASE CODE
        /*
         * These if statements also handle movement and prevent input errors
         */

        if (keyCode.equals(keyCodes[2])) {
            character.getMovement()[0]=1;
            character.setKeyReady(0, false);
        }
        if (keyCode.equals(keyCodes[3])) {
            character.getMovement()[1]=1;
            character.setKeyReady(1, false);
        }
        if (keyCode.equals(keyCodes[1])) {
            character.getMovement()[2]=1;
            character.setKeyReady(2, false);
        }
        if (keyCode.equals(keyCodes[0])) {
            character.setKeyReady(3, false);
        }
        if (keyCode.equals(keyCodes[4])) {
            character.setKeyReady(4, false);
        }
        if (keyCode.equals(keyCodes[5])) {
            character.setKeyReady(5, false);
        }
        if (keyCode.equals(keyCodes[6])) {
            character.setKeyReady(6, false);
        }

    }
}
