package Scenes.RhyGameHelpers;

/**
 * This class contains all info for the Note object. The
 * Note object is used for each note that falls during 
 * gameplay. Info contained in this class include the sound
 * the note makes, whether the note is a press or hold, the
 * position of the note, the length of the note, and whether
 * the note is able to be hit.//add more if necessary
 * 
 * @author Daniel Kong, Richard Kha
 *
 */
public class Note{

	private double initialYPosition;
	private double endYPosition;

	private double initialTime;
	private int type;
	private double timePassed;
	private int hitSound;
	private double endTime;
	private boolean isDisabled;
	private boolean startClicked;

	/**
	 * This function gets the time passed while the note was still active
	 * @return The time passed while the was still active
	 */
	public double getTimePassed() {
		return timePassed;
	}
	/**
	 * This function sets the time passed while the note is still active
	 * @param timePassed The difference in time passed while the note is still active
	 */
	public void changeTimePassed(int timePassed) {
		this.timePassed+=timePassed;
	}
	/**
	 * This function returns the time when a hold note ends if it the note
	 * is indeed a hold note. Otherwise, it returns the time that the note
	 * can be hit if it is a normal note.
	 */
	public double getLastTime() {
		if (type==128) {
			return endTime;
		}
		else {
			return initialTime;
		}
	}
	
	/**
	 * This function gets whether the player has started to click on
	 * a hold note
	 * @return A boolean indicating whether the player has started to click
	 */
	public boolean getStartClicked() {
		return startClicked;
	}
	/**
	 * This function sets whether the player has started to click on a hold note
	 * @param startClicked A boolean indicating whether the player has started to click
	 */
	public void setStartClicked(boolean startClicked) {
		this.startClicked = startClicked;
	}

	/**
	 * This functions sets whether a hold note is disabled and can no longer be clicked
	 * (Happens when a player misses a hold note or lets go too early)
	 * @param isDisabled A boolean for whether a hold note is disabled
	 */
	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled=isDisabled;
	}
	/**
	 * This function gets whether a hold note is disable and can no longer be clicked
	 * @return A boolean for whether a hold note is disabled
	 */
	public boolean getIsDisabled() {
		return isDisabled;
	}
	public Note(){

	}
	/**
	 * A constructor which initializes its initial y position, end y position, initial time,
	 * type, hit sound, end time, and time passed. This is done by taking these values from
	 * the peek parameter. 
	 * @param peek The values that the note will be initialized to
	 */
	public Note(Note peek) {
		initialYPosition = peek.initialYPosition;
		endYPosition = peek.endYPosition;
		initialTime = peek.initialTime;
		type = peek.type;
		hitSound = peek.hitSound;
		endTime = peek.endTime;
		timePassed = peek.initialTime;
		isDisabled = peek.getIsDisabled();
		startClicked = peek.getStartClicked();
	}
	/**
	 * This function sets the y position that the note will be at initially
	 * @param initialYPosition The y position that the note will be at initially
	 */
	public void setInitialYPosition(double initialYPosition) {
		this.initialYPosition=initialYPosition;
	}
	/**
	 * This functions sets the y position that the note will be at the end
	 * @param endYPosition The y position that the note will be at the end
	 */
	public void setEndYPosition(double endYPosition) {
		this.endYPosition=endYPosition;
	}
	/**
	 * This function changes the y position that the note will be at initially
	 * @param change The difference in y position that the note will be at
	 */
	public void changeInitialPosition(double change) {
		if (initialYPosition+change>=endYPosition) {
		initialYPosition+=change;
		}
	}
	/**
	 * This function changes the y position that the note is currently at. If 
	 * the note is a hold note, the end of the hold note's y position is changed.
	 * If the note is normal, the note's position will be changed.
	 * @param change The difference in y position for the note
	 * @return The new position of the note or end of hold note
	 */
	public double changeYPosition(double change) {
		initialYPosition+=change;
		if (type==128) {
			endYPosition+=change;
			return endYPosition;
		}
		else {
			return initialYPosition;
		}
	}
	/**
	 * This function gets the y position that the note is at initially
	 * @return The y position that the note is at initially
	 */
	public double getInitialYPosition() {
		return initialYPosition;
	}
	/**
	 * This function gets the y position that the note will be at the end
	 * @return The y position the note will be a the end
	 */
	public double getEndYPosition() {
		return endYPosition;
	}
	/**
	 * This function changes the time that the note will appear at initially
	 * changing it with the bpm(beats per minute)
	 */
	public void changeInitialTime(double bpmMultiplier) {
		initialTime+=bpmMultiplier;
	}
	/**
	 * This function sets the time that the note will appear at initially
	 * @param initialTime The new time that the note will appear at
	 */
	public void setInitialTime(double initialTime) {
		this.initialTime=initialTime;
		timePassed=initialTime;
	}
	/**
	 * This function sets the time that a hold note will end at
	 * @param endTime The time that the hold note will end at
	 */
	public void setEndTime(double endTime) {
		this.endTime=endTime;
	}
	/**
	 * This function sets the type of note that the note is.(128 = hold note, 1 or 5 = normal note)
	 * @param type The type of note that the note will be changed to
	 */
	public void setType(int type) {
		this.type=type;
	}
	/**
	 * This function sets the sound of the note when it is hit/clicked/held
	 * @param hitSound The index of the sound of the note when it is hit/clicked.
	 */
	public void setHitSound(int hitSound) {
		this.hitSound=hitSound;
	}
	
	/**
	 * This function gets the time that a note will appear at initially
	 * @return The time that a note will appear at initially
	 */
	public double getInitialTime() {
		return initialTime;
	}
	/**
	 * This function gets the type of note that the note currently is (128 = hold note, 1 or 5 = normal)
	 * @return The type of note that the note currently is
	 */
	public int getType() {
		return type;
	}
	/**
	 * This function gets the sound of the note when it is hit/clicked/held
	 * @return The sound of the note when it is hit/clicked/held
	 */
	public int getHitSound() {
		return hitSound;
	}
	/**
	 * This function sets the time when a hold note will end at
	 * @return The time when a hold note will end at
	 */
	public double getEndTime() {
		return endTime;
	}
	
	/**
	 * This function gets the time a hold note will end at if it is a hold note.
	 * Otherwise, it gets the time the note will be at initially.
	 * @return The time a hold note/note will end at
	 */
	public double getOverallEndTime() {
		if (type==128) {
		return endTime;
		}
		else {
			return initialTime;
		}
	}


}
