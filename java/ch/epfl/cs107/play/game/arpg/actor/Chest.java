package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Chest extends AreaEntity {
	
	private Sprite open;
	private Sprite closed;
	private Logic signal;

	public Chest(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
		open = new RPGSprite("zelda/chest.open",1,1,this,new RegionOfInterest(0,0,16,16));
		closed = new RPGSprite("zelda/chest.closed",1,1,this,new RegionOfInterest(0,0,16,16));
		signal=Logic.FALSE;
	}
	
	
	public void open() {
		signal=Logic.TRUE;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		// Chest takes space
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}

	@Override
	public void draw(Canvas canvas) {
		if(signal.isOn())
			open.draw(canvas);
		else
			closed.draw(canvas);
		
	}

}
