package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class FireSpell extends AreaEntity implements Interactor {

	private static final int ANIMATTION_SPEED = 2;
	private static int MAX_LIFE_TIME=50;
	private static int MIN_LIFE_TIME=30;
	private static int PROPAGATION_TIME_FIRE=10;
	private int propagationTime;
	private float force;
	private int lifeTime;
	private Animation idle;
	private FireHandler handler;
	private final static ARPGMonster.ARPGAttackType attack = ARPGMonster.ARPGAttackType.FIRE;
	
	private boolean hasExecuted;
	
	class FireHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith(ARPGPlayer player) {
			player.addHealth(-force);
		}
		@Override
		public void interactWith(ARPGMonster monster) {
			monster.receiveAttack(attack,force);
		}
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
		}
		
	}
	
	public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position,float force) {
		super(area, orientation, position);
		
		
		Sprite[] sprites = new Sprite[7];
		for(int i=0;i<7;++i) {
			sprites[i]= new RPGSprite("zelda/fire",1,1,this,new RegionOfInterest(i*16,0,16,16));
		}
		idle = new Animation(ANIMATTION_SPEED, sprites);
		
		handler=new FireHandler();
		lifeTime= MIN_LIFE_TIME+RandomGenerator.getInstance().nextInt(MAX_LIFE_TIME-MIN_LIFE_TIME);
		this.force=force;
		hasExecuted =false;
		propagationTime=PROPAGATION_TIME_FIRE;
	}



	@Override
	public boolean takeCellSpace() {
		// Entities can step on fire
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		// Water can destroy it
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		// No need for any view interaction(we only need cell interaction for damaging entity or get destroyed by water)
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		idle.update(deltaTime);
		--lifeTime;
		--propagationTime;
		if(lifeTime<=0) {
			extenguish();
		}
		if(propagationTime<=0) {
			if(!hasExecuted) {
				if(force>0 ) {
					FireSpell sibling = new FireSpell(getOwnerArea(),getOrientation(),getFieldOfViewCells().get(0),force-1);
					getOwnerArea().registerActor(sibling);
					
				}
				hasExecuted=true;
			}
			
		}	
	}
	public void extenguish() {
		getOwnerArea().unregisterActor(this);
	}
	

	@Override
	public void draw(Canvas canvas) {
		idle.draw(canvas);
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList
				(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
		
	}

}
