package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CaveDoor extends Door {
	
	private Sprite closed;
	private Sprite open;
	/**
	 * Cave door
	 * @param destination
	 * @param otherSideCoordinates
	 * @param signal
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public CaveDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
			Orientation orientation, DiscreteCoordinates position) {
		super(destination, otherSideCoordinates, signal, area, orientation, position);
		closed = new RPGSprite("zelda/cave.close",1,1,this,new RegionOfInterest(0,0,16,16));
		open = new RPGSprite("zelda/cave.open",1,1,this,new RegionOfInterest(0,0,16,16));
	}
	
	//Door can be walked on only if it is open
	@Override
	public boolean takeCellSpace() {
		return !isOpen();
	}
	
	@Override
	public void draw (Canvas canvas) {
		super.draw(canvas);
		if(isOpen())
			open.draw(canvas);
		else
			closed.draw(canvas);
	}
	@Override
	public boolean isViewInteractable() {
		return true;
	}
	@Override
	public boolean isCellInteractable() {
		return isOpen();
	}
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);
    }
	
	

}
