package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.arpg.ARPGAttackType;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Bomb extends AreaEntity implements Interactor{
	
	
	
	protected final float waitTime =3;
	protected float timer;
	protected int frameDuration = 3;
	protected Logic signal;
	protected ARPGBombHandler handler;
	protected RPGSprite sprite;
	protected Animation idle;
	protected Animation explosion;
	protected Animation current;
	final static ARPGAttackType attack = ARPGAttackType.FIRE;
	boolean hurt =false;
	
	
	public class ARPGBombHandler implements ARPGInteractionVisitor{
		
		@Override
		public void interactWith(ARPGPlayer player) {
			//Player gets burned by bomb
			if(!hurt) {
				player.addHealth(-1f);
				hurt=true;
				System.out.println("ouch");
				
			}
		}
		@Override
		public void interactWith(ARPGMonster monster) {
			monster.receiveAttack(attack,1f);
		}
		
		public void interactWith(Grass grass) {
			//Grass is destroyed by bomb
			grass.cut();	
		}
		public void interactWith(Rock rock) {
			//Rock is destroyed by bomb
			getOwnerArea().unregisterActor(rock);
		}
		public void interactWith(CaveDoor door) {
			//CaveDoor is open by bomb
			door.setSignal(Logic.TRUE);
		}
		
	
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(signal.isOff()) {
			timer-=deltaTime;
			
			frameDuration = (int)waitTime - ( (int)timer > 1 ? (int)timer:1);
			idle.setSpeedFactor(frameDuration);
			idle.update(deltaTime);
			if(timer<=0||signal.isOn()) {
				explode();
			 }
			
		}else {

			if(explosion.isCompleted()) {
				((ARPGArea)getOwnerArea()).unregisterActor(this);
			}else {
				explosion.update(deltaTime);
				
			}
		}
	}



	public Bomb(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
				
		//Initializing variables
		timer=waitTime;
		signal =Logic.FALSE;
		handler = new ARPGBombHandler();
		
		//"Ticking" Animation
		Sprite [] sprites = new Sprite[2];
		
		sprites[0]= new RPGSprite("zelda/bomb",1,1,this,new RegionOfInterest(0,0,16,16));
		sprites[1]= new RPGSprite("zelda/bomb",1,1,this,new RegionOfInterest(16,0,16,16));
		idle = new Animation(frameDuration,sprites,true);
		
		//Explosion Animation
		sprites = new Sprite[7];
		
		for(int i=0;i<7;++i) {
			sprites[i]= new RPGSprite("zelda/explosion",1,1,this,new RegionOfInterest(32*i,0,32,32));
		}
		explosion = new Animation (3,sprites,false);
		
		//Set the current animation to the ticking animation
		current = idle;

		
		
	}
	
	public void explode() {
		signal=Logic.TRUE;
		current=explosion;
	}
	

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		//Actors shouldn't be able to move on the bomb
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		//TODO: make true f we want to make the user able to pick up the bomb again
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		//Draw current animation
		if(!current.isCompleted())
			current.draw(canvas);
		
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() { // Returning the 3x3 square around the bomb
		List<DiscreteCoordinates> field = new ArrayList<DiscreteCoordinates>();
		for(int i=-1;i<2;++i) {
			for(int j=-1;j<2;++j) {				
				field.add(getCurrentMainCellCoordinates().jump(i,j));	
			}
		}
		return field;
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		// We want to interact only when the timer is over
		return signal.isOn();
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

}
