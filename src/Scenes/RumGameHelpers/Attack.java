package Scenes.RumGameHelpers;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 * This class contains all info pertaining to any given
 * attack. This class includes the range of an attack, the
 * range in which the character executing the attack can be 
 * hit, the attributes of an attack when it is blocked or 
 * hit, and the images of an attack.
 * 
 * @author Daniel
 *
 */
public class Attack {

	private double damage;
	private int shieldStun;
	private int hitStun;
	private double[][][] hitBoxes;
	private double[][][] hurtBoxes;
	private Image[] images;
	private int frameTime;
	private double knockBackX;
	private double knockBackY;
	private double[][] speedChanges;
	private int shieldDamage;
	private int blockingType;
	private double selfKnockBack;
	private byte multihit;
	AudioClip sfx;
	
	/**
	 * This function returns the sound effect for the attack
	 * @return Returns an AudioClip of the sound effect
	 */
	public AudioClip getSfx() {
		return sfx;
	}
	/**
	 * This function sets a sound effect to be held in the Attack class
	 * @param sfx The sound effect that will be held
	 */
	public void setSfx(AudioClip sfx) {
		this.sfx=sfx;
		sfx.setVolume(0.4);
	}

	/**
	 * This function sets a horizontal knockback value to be held in the Attack class
	 * @param knockBackX The knockback value that will be held
	 */
	public void setKnockBackX(double knockBackX) {
		this.knockBackX=knockBackX;
	}
	/**
	 * This function sets a vertical knockback value to be held in the Attack class
	 * @param d The knockback value that will be held
	 */
	public void setKnockBackY(double d) {
		this.knockBackY=d;
	}
	/**
	 * This function returns the horizontal knockback for the attack
	 * @return Returns a double for how much horizontal knockback the attack does
	 */
	public double getKnockBackX() {
		return knockBackX;
	}
	/**
	 * This function returns the vertical knockback for the attack
	 * @return Returns a double for how much vertical knockback the attack does
	 */
	public double getKnockBackY() {
		return knockBackY;
	}
	/**
	 * This function sets an int to be its frame time (the frame an attack is currently at)
	 * @param frameTime The int that will be the current frameTime
	 */
	public void setFrameTime(int frameTime) {
		this.frameTime=frameTime;
	}
	/**
	 * This function sets a shieldStun value to be held in the Attack class
	 * @param shieldStun The shieldStun value that will be held
	 */
	public void setShieldStun(int shieldStun) {
		this.shieldStun=shieldStun;
	}
	/**
	 * This function sets a damage value to be held in the Attack class
	 * @param damage The damage value that will be held
	 */
	public void setDamage(int damage) {
		this.damage=damage;
	}
	/**
	 * This function sets a hitstun value to be held in the Attack class
	 * @param hitStun The hitstun value that will be held
	 */
	public void setHitStun(int hitStun) {
		this.hitStun=hitStun;
	}
	/**
	 * This function sets an array of images to be held in the Attack class
	 * @param images The array of images that will be held
	 */
	public void setImages(Image[] images) {
		this.images=images;
	}
	/**
	 * This function returns an image for a given frame the attack
	 * @param index The frame of the attack that is returned
	 * @return Returns an image of the attack at frame: index
	 */
	public Image getImages(int index) {
		return images[index];
	}
	/**
	 * This function sets a 3d double array to be held in the Attack class
	 * @param hitBoxes The 3d double array that will be held
	 */
	public void setHitboxes(double[][][]hitBoxes) {
		this.hitBoxes=hitBoxes;
	}
	/**
	 * This function sets a 3d double array to be held in the Attack class
	 * @param hurtBoxes The 3d double array that will be held
	 */
	public void setHurtboxes(double[][][]hurtBoxes) {
		this.hurtBoxes=hurtBoxes;
	}
	/**
	 * This function sets a 2d double array to be held in the Attack class
	 * @param speedChanges The 2d double array that will be held
	 */
	public void setSpeedChanges(double[][] speedChanges) {
		this.speedChanges=speedChanges;
	}
	/**
	 * This function returns the damage for the attack
	 * @return Returns a double for how much damage the attack does
	 */
	public double getDamage() {
		return damage;
	}
	/**
	 * This function returns the shieldstun(how long the opponent freezes when they block an attack) for the attack
	 * @return Returns an int for how much shieldstun the attack does
	 */
	public int getShieldStun() {
		return shieldStun;
	}
	/**
	 * This function returns the hitstun(how long the opponent freezes when they're attacked) for the attack
	 * @return Returns an int for how much hitstun the attack does
	 */
	public int getHitStun() {
		return hitStun;
	}
	/**
	 * This function returns an array of hitboxes for the attack
	 * @param index The index of the array that is returned
	 * @return Returns a double array of the size of each hittbox of the attack
	 */
	public double[][] getHitBoxes(int index) {
		return hitBoxes[index];
	}
	/**
	 * This function returns an array of hurtboxes for the attack
	 * @param index The index of the hurtbox that is returned
	 * @return Returns a double array of the size of each hurtbox of the attack
	 */
	public double[][] getHurtBoxes(int index) {
		return hurtBoxes[index];
	}
	/**
	 * This function returns the horizontal speed change for the attack at a given frame
	 * @param index The frame whose horizontal speed change will be viewed
	 * @return Returns a double for how much horizontal speed change the attack does
	 */
	public double getSpeedChangeX(int index) {
		return speedChanges[index][0];
	}
	/**
	 * This function returns the vertical speed change for the attack at a given frame
	 * @param index The frame whose vertical speed change will be viewed
	 * @return Returns a double for how much vertical speed change the attack does
	 */
	public double getSpeedChangeY(int index) {
		return speedChanges[index][1];
	}
	/**
	 * This function returns the current frame for the attack
	 * @return Returns an int for the current frame of the attack
	 */
	public int getFrameTime() {
		return frameTime;
	}
	/**
	 * This function sets a shield damage (how much damage an attack is donw when blocked) value to be held
	 * @param shieldDamage The shield damage value that will be held
	 */
	public void setShieldDamage(int shieldDamage) {
		this.shieldDamage = shieldDamage;
	}
	/**
	 * This function returns the shield damage (how much damage an attack is done when blocked) for the attack
	 * @return Returns an int for how much shield damage the attack does
	 */
	public int getShieldDamage() {
		return shieldDamage;
	}
	/**
	 * This function sets a blocking type (whether the attack needs to be blocked high or low) value to be held
	 * @param blockingType The blocking type value that will be held
	 */
	public void setBlockingType(int blockingType) {
		this.blockingType = blockingType;
	}
	
	/**
	 * This function returns the blocking type of the attack (whether the attack needs to be blocked high or low)
	 * @return Returns an int for the blocking type of the attack
	 */
	public int getBlockingType() {
		return blockingType;
	}
	/**
	 * This function sets a self knockback value to be held
	 * @param selfKnockBack The self knockback value that will be held
	 */
	public void setSelfKnockBack(double selfKnockBack) {
		this.selfKnockBack = selfKnockBack;
	}
	
	/**
	 * This function returns the self knockback for the attack
	 * @return Returns a double for how much self knockback the attack does
	 */
	public double getSelfKnockBack() {
		return selfKnockBack;
	}
	/**
	 * This function sets a multihit value to be held
	 * @param multihit The multihit value that will be held
	 */
	public void setMultihit(byte multihit) {
		this.multihit = multihit;
	}
	/**
	 * This function returns whether an attack hits multiple times (0 = no, 1 = yes)
	 * @return Returns a byte for whether an attack hits multiple times
	 */
	public byte getMultihit(){
		return multihit;
	}


}
