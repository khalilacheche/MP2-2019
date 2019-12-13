package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Chest extends AreaEntity {
	
	private Sprite open;
	private Sprite closed;
	private Logic signal;
	private ARPGItem content= ARPGItem.BOW;
	private Dialog dialog;
	private String keyOpen="chest_open";
	private String keyClosed="chest_closed";
	private boolean isTalking;
	

	public Chest(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
		open = new RPGSprite("zelda/chest.open",1,1,this,new RegionOfInterest(0,0,16,16),new Vector(0, 0.5f));
		closed = new RPGSprite("zelda/chest.closed",1,1,this,new RegionOfInterest(0,0,16,16),new Vector(0, 0.5f));
		signal=Logic.FALSE;
		dialog= new Dialog(XMLTexts.getText(keyClosed),"zelda/dialog",getOwnerArea());
		isTalking=false;
	}
	
	protected ARPGItem takeContent() {
		signal=Logic.TRUE;
		return content;
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
	public void update(float deltaTime) {
		moveDialog();
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}

	@Override
	public void draw(Canvas canvas) {
		if(isTalking) {
			dialog.draw(canvas);
		}
		
		if(signal.isOn())
			open.draw(canvas);
		else
			closed.draw(canvas);
		
	}
	private void moveDialog() {
		if(getOwnerArea().getKeyboard().get(Keyboard.ENTER).isReleased()) {
			if(dialog.push()) {
				isTalking=false;
			}
		}
	}
	protected boolean hasFinishedDialog(){
		return !isTalking;
	}
	protected void showMessage() {
		if(isTalking)
			return;
		isTalking=true;
		if(signal.isOn()) {
			dialog.resetDialog(XMLTexts.getText(keyOpen));
			
		}else {
			dialog.resetDialog(XMLTexts.getText(keyClosed));
			
		}
	}

}
