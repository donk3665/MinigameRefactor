package main;

import javafx.scene.image.Image;

/**
 * This class is responsible for animating the animation that
 * each character does before round 1 starts in the fighting game
 * @author Daniel
 *
 */
public class Animation {
	private int animationTime;
	Image [] frames;
	/**
	 * This function gets the total length of the animation in frames
	 * @return The total length of the animation in frames
	 */
	public int getAnimationTime() {
		return animationTime;
	}
	/**
	 * This function sets the total length of the animation in frames
	 * @param animationTime The total length of the animation in frames that it will be set to
	 */
	public void setAnimationTime(int animationTime) {
		this.animationTime = animationTime;
	}
	/**
	 * This function gets a desired frame of the round start animation
	 * @param index The frame of the animation that will be gotten
	 * @return The frame of the round start animation at index
	 */
	public Image getFrame(int index) {
		return frames[index];
	}
	/**
	 * This function sets an Image array to be held in this class which is the 
	 * round start animation
	 * @param frames The image array that will be held
	 */
	public void setFrames(Image[] frames) {
		this.frames = frames;
	}
}
