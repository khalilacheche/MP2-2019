package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Bridge extends AreaEntity {
	private Sprite sprite;
	private Logic signal;
	/**Bridge constructor
	 * 
	 * @param area
	 * @param position
	 * @param signal to make the bridge appear
	 */
	public Bridge(Area area, DiscreteCoordinates position,Logic signal) {
		super(area, Orientation.UP, position);
		sprite =  new RPGSprite("zelda/bridge",3.7f,3.5f,this,new RegionOfInterest(0,0,64,48),new Vector(-0.9f, -1f));
		this.signal=signal;
	}
	
	@Override
	public void draw(Canvas canvas) {
		if(signal.isOn())//Draw only when signal is On
			sprite.draw(canvas);
		
	}
	
//////////////////////////////Interactable / Interactor ////////////////////////////////////////////////////////////////
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return signal.isOff();
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		
	}



}
