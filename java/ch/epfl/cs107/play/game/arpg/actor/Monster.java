package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * ARPGMonster : base class of all monsters
 *
 */
public abstract class Monster extends MovableAreaEntity implements Interactor{
	/**
	 * Attack Types
	 *
	 */
	public enum AttackType {
		FIRE,
		MAGIC,
		PHYSICAL;
	}
	private  final float MAX_HEALTH;
	private Animation deathAnimation;
	private float health;
	private List<AttackType> vulnerabilities;
	protected ImageGraphics healthBar; 

	
	
	
    /**
     * Default ARPGMonster constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param vuln (List<ARPGAttackType>): List of the monster's vulnerabilities.Can be empty, but not null
     */
	
	public Monster(Area area, Orientation orientation, DiscreteCoordinates position,List<AttackType> vuln, float maxHealth) {
		super(area, orientation, position);
		Sprite[] sprites = new Sprite[7];
		for(int i=0;i<7;++i) {
			sprites[i]= new RPGSprite("zelda/vanish",1,1,this,new RegionOfInterest(i*32,0,32,32));
		}
		MAX_HEALTH = health = maxHealth;
		
		deathAnimation = new Animation(2,sprites,false);
		vulnerabilities = vuln;
		healthBar = new ImageGraphics(ResourcePath.getSprite("zelda/monsterHealth"),3.f,0.5f,new RegionOfInterest(2,4,150,36));
		healthBar.setParent(this);
	}
	
	/**
	 * dropLoot when Monster hasDied
	 */
	
	protected abstract void dropLoot();
	
	@Override 
	public void update(float deltaTime) {
		super.update(deltaTime);
		// handle death
		if(isDead()) {			
			deathAnimation.update(deltaTime);
			if(deathAnimation.isCompleted()) {
				dropLoot();
				getOwnerArea().unregisterActor(this);
			}
			return;
		}
	}
	
	/**
	 * @return Monster hasDied or not
	 */
	public boolean isDead() {
		return health<=0;
	}
	
    /** Add the health amount to the monster
     * @param: amount (float): The damage amount: Can be postive for adding health, negative for removing
     */
	private void addHealth(float amount) {
		health+=amount;
	}
	
    /** Monster receives and handles attack
     * @param: attack (ARPGAttackType): The type of the received attack
     * @param: damage (float): The damage amount
     */
	public void receiveAttack(AttackType attack, float damage) {
		if(vulnerabilities.contains(attack)) {
			addHealth(-damage);
			if(!isDead()) {
				healthBar.setWidth(this.health*3/MAX_HEALTH);
		 		this.healthBar.setAnchor(new Vector(-1.f*this.health/MAX_HEALTH,healthBar.getAnchor().y));
			}
		}
	}
	@Override
	public void draw(Canvas canvas) {
		deathAnimation.draw(canvas);
	}
	
	
	

	/**
	 * Get this monster's current occupying cells coordinates
	 * @return Returns by default the main cell occupied by monster
	 */
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	/**
	 * Get this monster's current field of view cells coordinates
	 * @return Returns by default the cell facing the monster's orientation
	 */
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList
				(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}
	
	@Override
	public boolean takeCellSpace() {
		return !isDead();
	}	
	
	@Override
	public boolean isCellInteractable() {
		return true;
	}
	
	@Override
	public boolean isViewInteractable() {
		return true;
	}
	
	protected float getHealth() {
		return health ; 
	}
}