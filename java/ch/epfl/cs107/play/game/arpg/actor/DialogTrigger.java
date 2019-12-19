package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class DialogTrigger extends AreaEntity {
	private String dialogKey;
	
	/** Dialog trigger constructor
	 * 
	 */
	public DialogTrigger(Area area ,DiscreteCoordinates position, String key) {
		super(area, Orientation.UP, position);
		dialogKey = key;
	}
	
	
	/**
	 * 
	 * @return dialog name key in XML file
	 */
	protected String getDialogKey() {
		return dialogKey;
	}
	
	
////////////////////////////Interactable / Interactor ////////////////////////////////////////////////////////////////
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}

	@Override
	public void draw(Canvas canvas) {
		
	}

}
