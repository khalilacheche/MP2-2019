package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Orb  extends AreaEntity implements Logic{
	
	private static final int ANIMATION_DURATION = 4;
	private Animation idle;
	private Animation amorced;
	private Animation currentAnimation;
	private boolean wasHit;
	
	public Orb(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
		wasHit=false;
		Sprite[] sprites = new Sprite[6];
		for(int i=0;i<6;++i) {
			sprites [i] = new RPGSprite("zelda/orb",1,1,this,new RegionOfInterest(32*i,0,32,32),new Vector(0,0.5f));
		}
		idle= new Animation (ANIMATION_DURATION,sprites);
		sprites = new Sprite[6];
		for(int i=0;i<6;++i) {
			sprites [i] = new RPGSprite("zelda/orb",1,1,this,new RegionOfInterest(32*i,32,32,32),new Vector(0,0.5f));
		}
		amorced= new Animation (ANIMATION_DURATION,sprites);
	}
	
	protected void amorceSignal () {
		wasHit =true;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		currentAnimation = wasHit? amorced: idle;
		currentAnimation.update(deltaTime);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		
	}

	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return wasHit;
	}

	@Override
	public boolean isOff() {
		// TODO Auto-generated method stub
		return !wasHit;
	}

	@Override
	public float getIntensity() {
		return 0;
	}

}
