package Scenes.RhyGameHelpers;

import javafx.scene.input.KeyCode;

import java.util.Arrays;


public class RhyInputController {
    KeyCode [] keyCodes;
    boolean[] keyPress;
    boolean[] keyReady;

    public RhyInputController(KeyCode[] keyCodes){
        this.keyCodes = keyCodes;
        keyPress = new boolean[keyCodes.length];
        keyReady = new boolean[keyCodes.length];
    }
    public boolean getKeyPress(int index){
        return keyPress[index];
    }
    public void resetKeyPress(){
        Arrays.fill(keyPress, false);
    }
    public boolean getKeyNotReady(int index){
        return !keyReady[index];
    }

    /**
     * This function handles the key inputs of the player
     */
    public void keyPressed(KeyCode keyCode) {
        for (int i = 0; i< keyCodes.length; i++){
            /*
             * This if statements handle key presses and prevent input errors
             */
            if (keyCode.equals(keyCodes[i]) && !keyReady[i]){
                keyPress[i] = true;
                keyReady[i] = true;
            }
        }
    }
    public void keyReleased(KeyCode keyCode){
        for (int i = 0; i< keyCodes.length; i++){
            /*
             * This if statements handle key releases and prevent input errors
             */
            if (keyCode.equals(keyCodes[i])){
                keyReady[i] = false;
            }
        }
    }

}
