package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.io.XMLTexts;

public class Villager extends MovableAreaEntity{

	private Animation[] idleAnimations;
	private Animation currentAnimation;
	private final static int ANIMATION_DURATION=4;
	private boolean isTalking;
	private Dialog dialog;
	private String key;
	
	public Villager(Area area, Orientation orientation, DiscreteCoordinates position,String key) {
		super(area, orientation, position);
		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, 
				RPGSprite.extractSprites("zelda/character",
				4, 1, 2,
				this , 16, 32,new Orientation[] {Orientation.DOWN ,
				Orientation.UP , Orientation.RIGHT, Orientation.LEFT})
		);
		isTalking = false;
		dialog= new Dialog(XMLTexts.getText(key),"zelda/dialog",getOwnerArea());
		this.key=key;
		//phrases = Arrays.asList(dialogs);
	}
	
	
	

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	
	//Villager does not accept interactions
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	@Override
	public void update(float deltaTime) {
		currentAnimation = idleAnimations[getOrientation().ordinal()];	
		moveDialog();
		super.update(deltaTime);
		
		
	}
	
	
	private void moveDialog() {
		if(getOwnerArea().getKeyboard().get(Keyboard.ENTER).isReleased()) {
			if(dialog.push()) {
				isTalking=false;
			}
		}
	}
	protected boolean hasFinishedDialog() {
		return !isTalking;
	}
	

	@Override
	public void draw(Canvas canvas) {
		if(isTalking)
			dialog.draw(canvas);
		currentAnimation.draw(canvas);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	protected void startTalking() {
		if(!isTalking) {
			isTalking=true;
			dialog.resetDialog(XMLTexts.getText(key));
		}
	}

}
