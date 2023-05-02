package interfaces;

import javafx.scene.canvas.GraphicsContext;

/**
 * Drawable Interface
 * 
 * This interface imposes the ability for an object to be drawn on the object that implements it. The 
 * drawable interface is meant to be used when drawing on a Graphics Context object in JavaFX.
 * 
 * It contains one method, render, that needs to be implemented when creating a drawable object.
 * <br><br>Updated: March 2021.
 * 
 * @author Christina Kemp
 *
 */
public interface Drawable {
	/**
	 * Draws the object.
	 * 
	 * @param gc
	 *            The graphics context to be drawn onto.
	 */
	abstract public void render(GraphicsContext gc);

}
