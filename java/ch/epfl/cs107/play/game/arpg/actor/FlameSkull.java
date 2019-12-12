package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGAttackType;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.FlyableEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

public class FlameSkull extends ARPGMonster implements FlyableEntity {
	
	
	static List<ARPGAttackType> vulnerabilities = new ArrayList<ARPGAttackType>(Arrays.asList(
			ARPGAttackType.WATER,ARPGAttackType.PHYSICAL));
	private final int ANIMATION_DURATION=8;
	private FlameSkullHandler handler;
	private Animation currentAnimation;
	private Animation[] idleAnimations;
	protected ARPGAttackType attack =ARPGAttackType.FIRE;
	private boolean isImmortal;
	
	private final float MIN_LIFE_TIME=1f;
	private final static float MAX_LIFE_TIME=4f;
	
	private float lifeTime;
	
	
	class FlameSkullHandler implements ARPGInteractionVisitor{
		
		@Override
		public void interactWith(ARPGMonster monster) {
			monster.receiveAttack(attack,1f);
		}
		@Override
		public void interactWith(ARPGPlayer player) {
			player.addHealth(-1f);
		}
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
		}
		
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}
		
		
	}
	
	
	
    /**
     * Default FlameSkull constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param isImmortal (boolean): if true, then skull only dies by hits
     */
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position,boolean isImmortal) {
		super(area, orientation, position, vulnerabilities);
		Sprite [][] sprites = RPGSprite.extractSprites("zelda/flameSkull",
				3, 2, 2,
				this , 32, 32, new Orientation[] {Orientation.UP ,
				Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		idleAnimations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		handler= new FlameSkullHandler();
		health=1;
		lifeTime = MAX_LIFE_TIME;
		this.isImmortal=isImmortal;
	}
    /**
     * Default FlameSkull constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
	 *Immortal is false by default
     */
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
		this(area, orientation,position,false);
	}





	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
		
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(isDead) {			
			currentAnimation=vanish;
			currentAnimation.update(deltaTime);
			if(currentAnimation.isCompleted()) {
				dropLoot();
				getOwnerArea().unregisterActor(this);
			}
			return;
		}
		if(!isImmortal)
			lifeTime-=deltaTime;
			
		currentAnimation=idleAnimations[this.getOrientation().ordinal()];
		moveOrientate();
		if(isDisplacementOccurs()) {
			currentAnimation.update(deltaTime);
			
		}else {
			currentAnimation.reset();
		}
		if(lifeTime<=0) {
			isDead=true;
		}
		
		
	}
	
	
	private void moveOrientate() {
		if(RandomGenerator.getInstance().nextDouble()<=0.9f) {
			move(ANIMATION_DURATION);
		}
		else {
			this.orientate( Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
		}		
	}
	

	
	@Override
	public void dropLoot() {
		
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
		return true;
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
	public boolean wantsCellInteraction() {
		return true;
	}
	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

}
