package ch.epfl.cs107.play.game.arpg;


import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Draw this GUI when player hasDied
 *
 */
public class DeathScreenGUI implements Graphics {
     ImageGraphics gameOver = new ImageGraphics(ResourcePath.getSprite("zelda/deathScreen"),
    			1, 1) ; 
	@Override
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
				Vector((width/2), height/2));
		
		anchor.add(new Vector(0, 0));
		gameOver.setHeight(height);
		gameOver.setWidth(width);
		gameOver.setAnchor(anchor);
		gameOver.setDepth(10000);
		gameOver.draw(canvas);
	}

}
