package Scenes.RumGameHelpers;

import java.util.ArrayList;

import interfaces.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * This class contains all info pertaining to the character object.
 * This info includes what the character is doing at any given
 * moment, the size of the character, all non-attack animations
 * of a character, the speed of the character, the position of the character,
 * the attacks of a character, and the hit boxes/hurt boxes that are currently
 * active. 
 * @author Daniel
 *
 */
public class FightingCharacter extends Rectangle implements Drawable{

	private final RumInputController inputController;
	private final int[] frameTotals = new int[8];
	private int [] frameCounters = new int[7];
	private final ArrayList<Integer> inputTime = new ArrayList<>();
	private final ArrayList<String> inputs = new ArrayList<>();
	private byte [] movement = new byte[4];
	private int attackInt = -1;
	private int hitstun;
	private final Attack[] attackList = new Attack[11];
	private int attackCurrent;
	private String name;
	private final boolean [] keyReady = new boolean[11];
	private double walkSpeed;
	private boolean jumpAvailable = false;
	private boolean isActionable = true;
	private double xSpeed;
	private double ySpeed;
	private final ArrayList<Rectangle> hurtBoxes = new ArrayList<>();
	private final ArrayList<Ellipse> hitBoxes = new ArrayList<>();
	private int state;
	private int hitBoxCounter;
	private int hurtBoxCounter;
	private int playerTimer;
	private int facingRight = 1;
	private boolean hitCharacter = false;
	private boolean isCrouching = false;
	private Image imageCharacter;
	private double offsetX;
	private double offsetY;
	private double []dimensions;
	private Image imageDisplay;
	private final Rectangle baseHurtBox;
	private double heightAdjust;
	private double widthAdjust;
	Animation startingAnimation;
	AudioClip[] hurtSound = new AudioClip[3];
	private Image[] idleAnimation;
	private Image crouch;
	private Image[] walkAnimation;
	private Image[] backWalkAnimation;
	private Image[] jumpAnimation;
	private Image block;
	private Image lowBlock;
	private final boolean[] isBlocking = new boolean[2];
	private Image hurt;
	/**
	 * This function sets an image for when a character is hurt
	 * @param hurt The image for when a character is hurt
	 */
	public void setHurt (Image hurt) {
		this.hurt = hurt;
	}
	/**
	 * This function returns an image for when a character is hurt
	 * @return The image for when a character is hurt
	 */
	public Image getHurt () {
		return hurt;
	}
	/**
	 * This function sets an image for when a character is blocking low
	 * @param lowBlock The image for when a character is blocking low
	 */
	public void setLowBlock(Image lowBlock) {
		this.lowBlock = lowBlock;
	}
	/**
	 * This function returns an image for when a character is blocking low
	 * @return The image for when a character is blocking low
	 */
	public Image getLowBlock() {
		return lowBlock;
	}
	/**
	 * This function sets a boolean for whether a character is currently blocking (first index
	 * means the character is blocking low and the second index means the character is blocking high)
	 * @param index The index of the array that will be set
	 * @param isBlocking The value that the array index will be set to
	 */
	public void setIsBlocking(int index, boolean isBlocking) {
		this.isBlocking[index] = isBlocking;
	}
	/**
	 * This function returns a boolean for when a character is currently blocking
	 * @param index The index in the array that will be returned
	 * @return A boolean value for whether a character is currently blocking
	 */
	public boolean getIsBlocking(int index) {
		return isBlocking[index];
	}  
	/**
	 * This function sets an image array for when a character is idle
	 * @param idleAnimation The image array for when a character is idle
	 */
	public void setIdleAnimation(Image[] idleAnimation) {
		this.idleAnimation = idleAnimation;
	}
	/**
	 * This function returns an image array for when a character is idle
	 * @param index The index of the array that will be returned
	 * @return The image of the idle animation at the given index
	 */
	public Image getIdleAnimation(int index) {
		return idleAnimation[index];
	}
	/**
	 * This function sets an image for when a character is crouching
	 * @param crouch The image for when a character is crouching
	 */
	public void setCrouch(Image crouch) {
		this.crouch = crouch;
	}
	/**
	 * This function returns an image for when a character is crouching
	 * @return The image for when a character is crouching
	 */
	public Image getCrouch() {
		return crouch;
	}
	/**
	 * This function sets an image array for when a character is walking forward
	 * @param walkAnimation The image array for when a character is walking forward
	 */
	public void setWalkAnimation(Image[] walkAnimation) {
		this.walkAnimation = walkAnimation;
	}
	/**
	 * This function returns an image array for when a character is walking forward
	 * @param index The index of the array that will be returned
	 * @return The image of the walking forward animation at the given index
	 */
	public Image getWalkAnimation(int index) {
		return walkAnimation[index];
	}
	/**
	 * This function sets an image for when a character is walking backwards
	 * @param backWalkAnimation The image for when a character is walking backwards
	 */
	public void setBackWalkAnimation(Image[] backWalkAnimation) {
		this.backWalkAnimation = backWalkAnimation;
	}
	/**
	 * This function returns an image array for when a character is walking backwards
	 * @param index The index of the array that will be returned
	 * @return The image of the walking backwards animation at the given index
	 */
	public Image getBackWalkAnimation(int index) {
		return backWalkAnimation[index];
	}
	/**
	 * This function sets an image for when a character is jumping
	 * @param jumpAnimation The image for when a character is jumping
	 */
	public void setJumpAnimation(Image [] jumpAnimation) {
		this.jumpAnimation = jumpAnimation;
	}
	/**
	 * This function returns an image array for when a character is jumping
	 * @param index The index of the array that will be returned
	 * @return The image of the jumping animation at the given index
	 */
	public Image getJumpAnimation(int index) {
		return jumpAnimation[index];
	}
	/**
	 * This function sets an image for when a character is blocking high
	 * @param block The image for when a character is blocking high
	 */
	public void setBlock(Image block) {
		this.block = block;
	}
	/**
	 * This function returns an image for when a character is blocking
	 * @return The image for when a character is blocking
	 */
	public Image getBlock() {
		return block;
	}

	/**
	 * This function gets the sound for when a character is hurt
	 * @return The AudioClip for when a character is hurt
	 */
	public AudioClip getHurtSound() {

		return hurtSound[(int) (Math.random()*3)];
	}
	/**
	 * This function sets the sound for when a character is hurt
	 * @param hurtSound The AudioClip for when a character is hurt
	 */
	public void setHurtSound(AudioClip[] hurtSound) {
		this.hurtSound=hurtSound;
	}
	/**
	 * This function gets the startingAnimation for a character 
	 * @return Animation for when the round starts
	 */
	public Animation getStartingAnimation() {
		return startingAnimation;
	}
	/**
	 * This function sets the startingAnimation for a character
	 * @param startingAnimation Animation for when the round starts
	 */
	public void setStartingAnimation(Animation startingAnimation) {
		this.startingAnimation=startingAnimation;
	}

	public void setHeightAdjust (double heightAdjust) {
		this.heightAdjust= heightAdjust; 
	}
	public void setWidthAdjust (double widthAdjust) {
		this.widthAdjust= widthAdjust; 
	}
	/**
	 * This function sets what image will be displayed for the character
	 * @param imageDisplay The image that will be displayed
	 */
	public void setImageDisplay(Image imageDisplay) {
		this.imageDisplay=imageDisplay;
	}
	/**
	 * This function gets what image that is currently displayed for the character
	 * @return The image that is currently displayed for the character
	 */
	public Image getImageDisplay() {
		return imageDisplay;
	}

	/**
	 * This function sets the dimensions of the character
	 */
	public void setDimensions(double []dimensions) {
		this.dimensions=dimensions;
	}
	/**
	 * This function gets the x dimension of the character
	 * @return x dimension of the character
	 */
	public double getDimensionX() {
		return dimensions[0];
	}
	/**
	 * This function gets the y dimension of the character
	 * @return y dimension of the character
	 */
	public double getDimensionY() {
		return dimensions[1];
	}

	/**
	 * This function sets the X value that the sprite will be shifted by
	 * @param offsetX The X value that the sprite will be shifted by
	 */
	public void setOffsetX(double offsetX) {
		this.offsetX=offsetX;
	}
	/**
	 * This function sets the Y value that the sprite will be shifted by
	 * @param offsetY The Y value that the sprite will be shifted by
	 */
	public void setOffsetY(double offsetY) {
		this.offsetY=offsetY;
	}
	/**
	 * This function gets the X value that the sprite is shifted by
	 * @return The X value that the sprite is shifted by
	 */
	public double getOffsetX() {
		return offsetX;
	}
	/**
	 * This function gets the Y value that the sprite is shifted by
	 * @return The Y value that the sprite is shifted by
	 */
	public double getOffsetY() {
		return offsetY;
	}

	/**
	 * This function sets the Image that the character will be set to by default
	 * @param file The image that will be set to imageDisplay
	 */
	public void setImageCharacter(Image file) {
		imageCharacter = file; 
		imageDisplay = imageCharacter;
	}
	/**
	 * This function gets the image that the character is set to by default
	 * @return The image that is set to the character by default
	 */
	public Image getImageCharacter() {
		return imageCharacter; 
	}

	/**
	 * This function gets the attack that is currently active
	 * @return The Attack object for what attack is currently active
	 */
	public Attack getAttackCurrent() {
		return attackList[attackCurrent];
	}
	/**
	 * This functions sets the attack that is currently active
	 * @param attackCurrent The index of the attack that is currently active
	 */
	public void setAttackCurrent(int attackCurrent) {
		this.attackCurrent=attackCurrent;
	}

	/**
	 * This function gets whether the character is currently not hitting the opponent
	 * @return Returns whether the character is currently not hitting the opponent
	 */
	public boolean getNotHitCharacter() {
		return !hitCharacter;
	}
	/**
	 * This function sets whether the character is currently hitting the opponent
	 */
	public void setHitCharacter(boolean hitCharacter) {
		this.hitCharacter=hitCharacter;
	}

	/**
	 * This function gets the hurt boxes that are on the character
	 * @return Returns an ArrayList of rectangles which are the hurt boxes
	 */
	public ArrayList<Rectangle> getHurtBoxes(){
		return hurtBoxes;
	}
	/**
	 * This function gets the hit boxes that are on the character
	 * @return Returns an ArrayList of ellipses which are the hit boxes
	 */
	public ArrayList<Ellipse> getHitBoxes(){
		return hitBoxes;
	}
	/**
	 * This function sets the playerTimer on the character
	 * @param playerTimer The int value the playerTimer will be set to
	 */
	public void setPlayerTimer(int playerTimer) {
		this.playerTimer= playerTimer;
	}
	/**
	 * This function adds onto the playerTimer on the character
	 * @param amount The int value the playerTimer will be increased by
	 */
	public void addPlayerTimer(int amount) {
		this.playerTimer+=amount;
	}
	/**
	 * This function sets the amount of hit boxes that are on the character
	 * @param hitBoxCounter Returns an int of how many hit boxes are on the character
	 */
	public void setHitBoxCounter(int hitBoxCounter) {
		this.hitBoxCounter= hitBoxCounter;
	}
	/**
	 * This function adds the hit boxes that are on the character
	 * @param amount The amount that the hitBoxCounter that will be increased by
	 */
	public void addHitBoxCounter(int amount) {
		this.hitBoxCounter+=amount;
	}
	/**
	 * This function sets the amount of hurt boxes that are on the character
	 * @param hurtBoxCounter Returns an int of how many hurt boxes are on the character
	 */
	public void setHurtBoxCounter(int hurtBoxCounter) {
		this.hurtBoxCounter= hurtBoxCounter;
	}
	/**
	 * This function adds the hurt boxes that are on the character
	 * @param amount The amount that the hurtBoxCounter that will be increased by
	 */
	public void addHurtBoxCounter(int amount) {
		this.hurtBoxCounter+=amount;
	}
	/**
	 * This function gets the hurtBoxCounter which is how many hurt boxes are on the character
	 * @return The hurtBoxCounter
	 */
	public int getHurtBoxCounter() {
		return hurtBoxCounter;
	}
	/**
	 * This function gets the size of the hurtBoxes ArrayList
	 * @return The size of the hurtBoxes ArrayList
	 */
	public int getTotalHurtBoxCounter() {
		return hurtBoxes.size();
	}
	/**
	 * This function gets the size of the hitBoxes ArrayList
	 * @return The size of the hitBoxes ArrayList
	 */
	public int getHitBoxCounter() {
		return hitBoxCounter;
	}
	/**
	 * This function gets the playerTimer
	 * @return The current value of the playerTimer
	 */
	public int getPlayerTimer() {
		return playerTimer;
	}
	/**
	 * This function gets a hurt box from an ArrayList
	 * @param index The index that will be searched in the ArrayList
	 * @return A Rectangle which is the hurt box
	 */
	public Rectangle getHurtBox(int index) {
		return hurtBoxes.get(index);
	}
	/**
	 * This function gets an attack from an array
	 * @param index The index that will be searched in the array
	 * @return An Attack that is searched from the array
	 */
	public Attack getAttack(int index) {
		return attackList[index];
	}
	/**
	 * This function adds a hit box from an ArrayList
	 * @param hitCircle The Ellipse that will be added in the ArrayList
	 */
	public void addHitBox(Ellipse hitCircle) {
		hitBoxes.add(hitCircle);
	}
	/**
	 * This function removes as many hit boxes as wanted
	 * @param value How many hit boxes that will be removed
	 */
	public void removeHitBox(int value) {
		for (int i = 0; i<value; i++) {
			hitBoxes.remove(hitBoxes.size()-1);
		}
	}
	/**
	 * This function gets a hurt box from an ArrayList
	 * @param hurtBox The hurt box that will be added in the ArrayList
	 */
	public void addHurtBox(Rectangle hurtBox) {
		hurtBoxes.add(hurtBox);
	}
	/**
	 * This function removes as many hurt boxes as wanted
	 * @param value How many hurt boxes that will be removed
	 */
	public void removeHurtBox(int value) {
		for (int i = 0; i<value; i++) {
			hurtBoxes.remove(hurtBoxes.size()-1);
		}
	}
	/**
	 * This function sets the name of the character
	 * @param name The name of the character
	 */
	public void setName(String name) {
		this.name=name;
	}
	/**
	 * This function gets the name of the character
	 * @return The name of the character
	 */
	public String getName() {
		return name;
	}
	/**
	 * This function sets whether the character is currently jumping
	 * @param value The value for if the character is currently jumping
	 */
	public void setJump (boolean value) {
		jumpAvailable=value;
	}
	/**
	 * This function gets whether the character is currently not jumping
	 * @return The value for if the character is currently not jumping
	 */
	public boolean getNotJump() {
		return !jumpAvailable;
	}

	/**
	 * This constructor sets a characters x and y position and their width and height. It also initializes
	 * walkSpeed, xSpeed, ySpeed, playerTimer, hitBoxCounter, hurtBoxCounter, and attackList.
	 * @param positionX The x position of the character on the screen
	 * @param positionY The y position of the character on the screen 
	 * @param width The width of the character
	 * @param height The height of the character
	 */
	public FightingCharacter(double positionX, double positionY, double width, double height, KeyCode[] keys) {
		super(positionX,positionY,width, height);
		baseHurtBox = new Rectangle(positionX,positionY,width, height);
		walkSpeed=0;
		xSpeed=0;
		ySpeed=0;
		playerTimer=0;
		hitBoxCounter=0;
		hurtBoxCounter=0;
		inputController = new RumInputController(this, keys);

		for (int i = 0; i <attackList.length; i++) {
			attackList[i] = new Attack();
		}
	}
	/**
	 * This function returns a character's base hurt box (The character's default hurt box)
	 * @return The character's base hurt box
	 */
	public Rectangle getBaseHurtBox() {

		return baseHurtBox;
	}

	/**
	 * This function sets whether a key is ready (whether a key can be pressed)
	 * @param index The index of the key
	 * @param value The value of the key (2 being pressed, 1 being released, 0 being not pressed)
	 */
	public void setKeyReady(int index, boolean value) {
		keyReady[index]=value;
	}
	/**
	 * This function gets whether a key is not ready
	 * @param index The index of the key
	 * @return The value of the key
	 */
	public boolean getKeyNotReady(int index) {
		return !keyReady[index];
	}
	/**
	 * This function sets the state that the character is in (0 = idle 1 = Attacking 2 = Hurt 3 = crouch 4 = jump 5 = walk forward 6 = walk backward 7 = block 8 = low block)
	 * @param state The state that the character is set to
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * This function gets the state that the character is in.
	 * @return The state that the character is in
	 */
	public int getState() {
		return state;
	}

	/**
	 * This function sets the characters x and y values to its current x or y value
	 * added to its current speed multiplied by time.
	 * @param time The time that has passed
	 */
	public void update(double time)
	{
		setX(getX() + xSpeed * time);
		setY(getY() + ySpeed * time);

	}

	/**
	 * This function sets the characters walk speed
	 * @param walkSpeed The walk speed value that walk speed will be set to
	 */
	public void setWalkSpeed(double walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	/**
	 * This function gets the characters walk speed
	 * @return The characters walk speed
	 */
	public double getWalkSpeed(){
		return walkSpeed;
	}
	/**
	 * Changes the horizontal speed.
	 * 
	 * @param xSpeed the speed in the x direction, where a positive value
	 * will move to the right, and a negative value will move to the left
	 */
	public void changeXSpeed(double xSpeed) {
		this.xSpeed += xSpeed;
	}
	/**
	 * Sets the horizontal speed.
	 * 
	 * @param xSpeed the speed in the x direction, where a positive value
	 * will move to the right, and a negative value will move to the left
	 */
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}
	/**
	 * Sets the vertical speed.
	 * 
	 * @param ySpeed the speed in the y direction, where a positive value
	 * will move down, and a negative value will move up
	 */
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}
	/**
	 * Gets the center of the sprite horizontally
	 * @return Returns the center of the sprite horizontally
	 */
	public int getCentreX() {
		return (int) (this.getX()+this.getWidth()/2);
	}
	/**
	 * Gets the centre of the sprite vertically
	 * @return Returns the center of the sprite vertically
	 */
	public int getCentreY() {
		return (int) (this.getY()+this.getHeight()/2);
	}
	/**
	 * Changes the vertical speed.
	 * 
	 * @param ySpeed the speed in the y direction, where a positive value
	 * will move down, and a negative value will move up
	 */
	public void changeYSpeed(double ySpeed) {
		this.ySpeed += ySpeed;
	}

	/**
	 * Returns the horizontal speed
	 * 
	 * @return Returns the horizontal speed, where a positive value
	 * indicates it is moving to the right, and a negative value indicates
	 * it is moving to the left
	 */
	public double getXSpeed() {
		return xSpeed;
	}

	/**
	 * Returns the vertical speed
	 * 
	 * @return Returns the vertical speed, where a positive value
	 * indicates it is moving up, and a negative value indicates
	 * it is moving down.
	 */
	public double getYSpeed() {
		return ySpeed;
	}

	/**
	 * Sets whether the character is facing right (1 = facing right, -1 = facing left)
	 * @param facingRight Whether the character is facing right
	 */
	public void setFacingRight (int facingRight) 
	{
		this.facingRight = facingRight;
	}

	/**
	 * Gets whether the character is facing right
	 * @return Whether the character is facing right
	 */
	public int getFacingRight () 
	{
		return facingRight;
	}

	/**
	 * Sets whether the character is able to perform any actions
	 * @param isActionable Returns a boolean where true indicates that
	 * the player is able to perform actions and false indicates that the player is
	 * unable to perform actions
	 */
	public void setIsActionable(boolean isActionable) 
	{
		this.isActionable = isActionable;
	}

	/**
	 * Returns whether the character is able to perform any actions (move/attack).
	 * 
	 * @return Returns a boolean where true indicates that the player is able to
	 * performs actions and false indicates that the player is unable to perform actions.
	 */
	public boolean getIsActionable() 
	{
		return isActionable;
	}

	/**
	 * Sets whether the character is currently crouching
	 * @param isCrouching Whether the character is currently crouching
	 */
	public void setIsCrouching (boolean isCrouching) {
		this.isCrouching = isCrouching;
	}
	/**
	 * Gets whether the character is currently crouching
	 * @return Whether the character is currently crouching
	 */
	public boolean getIsCrouching () {
		return isCrouching;
	}
	public int getHitstun() {
		return hitstun;
	}
	public void setHitstun(int hitstun) {
		this.hitstun = hitstun;
	}
	public int[] getFrameTotals() {
		return frameTotals;
	}
	public int[] getFrameCounters() {
		return frameCounters;
	}
	public void resetFrameCounters(){frameCounters = new int[7];}
	public void resetAttackInt(){attackInt = -1;}
	public RumInputController getInputController() {
		return inputController;
	}
	public ArrayList<Integer> getInputTime() {
		return inputTime;
	}
	public ArrayList<String> getInputs() {
		return inputs;
	}
	public int getAttackInt() {
		return attackInt;
	}
	public void setAttackInt(int attackInt) {
		if (this.attackInt == -1){this.attackInt = attackInt;}
	}
	public byte[] getMovement() {
		return movement;
	}
	public void resetMovement(){movement = new byte[4];}
	/**
	 * This method draws the hit boxes and hurt boxes of the characters
	 */
	@Override
	public void render(GraphicsContext gc) {
		
		gc.setFill(this.getFill());
		Ellipse temp;
		for (Ellipse hitBox : hitBoxes) {
			temp = hitBox;
			gc.setFill(Color.RED);
			gc.strokeOval((temp.getCenterX() - temp.getRadiusX()) * widthAdjust, (temp.getCenterY() - temp.getRadiusY()) * heightAdjust, temp.getRadiusX() * 2 * widthAdjust, temp.getRadiusY() * 2 * heightAdjust);
		}
		for (Rectangle hurtBox : hurtBoxes) {
			gc.setFill(Color.AQUAMARINE);
			gc.strokeRect(hurtBox.getX() * widthAdjust, hurtBox.getY() * heightAdjust, hurtBox.getWidth() * widthAdjust, hurtBox.getHeight() * heightAdjust);
		}
		
	}

}