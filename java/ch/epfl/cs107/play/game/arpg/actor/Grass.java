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
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity{
	private Animation fadeout;
	private Animation idle;
	private Animation current;
	private Logic signal;
	private static final double PROBABILITY_TO_DROP_ITEM=0.5f;
	private static final double PROBABILITY_TO_DROP_HEART=0.5f;
	
	private boolean wasExecuted=false;
	

	public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
		signal = Logic.FALSE;
		
		Sprite[] sprites = new Sprite[1];
		sprites[0] = new Sprite("zelda/grass",1,1,this,new RegionOfInterest(0,0,16,16));
		idle = new Animation(5, sprites);
		
		sprites = new Sprite[4];
		for(int i=0;i<4;++i) {
			sprites[i]= new Sprite("zelda/grass.sliced",1,1,this,new RegionOfInterest(i*32,0,32,32));
		}
		fadeout = new Animation(2, sprites,false);
		current=idle;
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(signal.isOn()) {
			current=fadeout;
			dropLoot();
			if(fadeout.isCompleted())
				getOwnerArea().unregisterActor(this);
			else fadeout.update(deltaTime);				
			
		}
		
	}
	
	 
	 private void dropLoot() {
		 if(!wasExecuted) {
			 double rand = RandomGenerator.getInstance().nextDouble();
			 if(rand>PROBABILITY_TO_DROP_ITEM) {
				 rand = RandomGenerator.getInstance().nextDouble();
				 if(rand >PROBABILITY_TO_DROP_HEART) {
					 Heart heart = new Heart(getOwnerArea(), getCurrentMainCellCoordinates());
					 getOwnerArea().registerActor(heart);
				 }else {
					 Coin coin = new Coin(getOwnerArea(), getCurrentMainCellCoordinates());
					 getOwnerArea().registerActor(coin);
				 }
			 }	 
		 }
		wasExecuted=true;
		
	 }

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
		return true;
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
		if(!current.isCompleted())
			current.draw(canvas);
	}

	public void cut() {//// TODO: add fire method
		this.signal=Logic.TRUE;
	}
}
