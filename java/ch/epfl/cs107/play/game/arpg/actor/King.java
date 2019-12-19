package ch.epfl.cs107.play.game.arpg.actor;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * King Character Class
 *
 */
public class King extends AreaEntity implements Interactor,Logic{
	private RPGSprite sprite;
	private KingHandler handler;
	private boolean hasTalked;
	private boolean signalValue;
	private String key = "won";
	private boolean showTip;
	private TextGraphics text;
	
	private class KingHandler implements ARPGInteractionVisitor {
		@Override 
		public void interactWith(ARPGPlayer player) {
			showTip=true;
			if(player.isTalking()&&!hasTalked)
				hasTalked=true;
			if(!player.isTalking()&&hasTalked)
				signalValue=true;
		}
	}
	
	public King(Area area,DiscreteCoordinates position) {
		super(area, Orientation.DOWN, position);
		sprite= new RPGSprite("zelda/king",1,2,this,new RegionOfInterest(0,64,16,32));
		handler = new KingHandler();
		hasTalked =false;
		signalValue =false;
		showTip=false;
		text=new TextGraphics("press T to talk",0.5f,Color.black);
		text.setParent(this);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return 	Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}
	@Override
	public void update (float deltaTime) {
		showTip=false;
	}

	@Override
	public void draw(Canvas canvas) {
		if(showTip)
			text.draw(canvas);
		sprite.draw(canvas);
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
		
	}

	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return signalValue;
	}

	@Override
	public boolean isOff() {
		// TODO Auto-generated method stub
		return !signalValue;
	}

	@Override
	public float getIntensity() {
		return 0;
	}
	
	/**
	 * @return tagName from XMLFile
	 */
	protected String getKey() {
		return key;
	}

}
