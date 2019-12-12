package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGAttackType;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class DarkLord extends ARPGMonster {
	
	private enum State{
		IDLE,
		TELEPORTING,
		INVOKINGTELEPORT,
		INVOKINGSPELL,
		ATTACKING;
	}
	private final static int ANIMATION_DURATION=4;
	private final static int MIN_SPELL_WAIT_DURATION=30;
	private final static int MAX_SPELL_WAIT_DURATION=20;
	private final static int NEIGHBOR_CELLS_RADIUS=1;
	private final static int TELEPORTATION_RADIUS=3;
	private DarkLordHandler handler;
	private int n=40;
	private int currentSpell=1;
	private Animation[] idleAnimations;
	private Animation[] invokeAnimations;
	private Animation currentAnimation;
	private State currentState;
	static List<ARPGAttackType> vulnerabilities = new ArrayList<ARPGAttackType>(Arrays.asList(
			ARPGAttackType.WATER));
	
	
	class DarkLordHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(ARPGPlayer player) {
			if(currentState==State.IDLE) {
				currentState=State.INVOKINGTELEPORT;
			}
		}
	}
	
	
	
    /**
     * Default DarkLord constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, vulnerabilities);
		Sprite [][] sprites = RPGSprite.extractSprites("zelda/darkLord",
				3, 2, 2,
				this , 32, 32, new Orientation[] {Orientation.UP ,
				Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		idleAnimations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		
		sprites = RPGSprite.extractSprites("zelda/darkLord.spell",
				3, 2, 2,
				this , 32, 32, new Orientation[] {Orientation.UP ,
				Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		invokeAnimations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites,false);
		
		handler=new DarkLordHandler();
		currentState=State.IDLE;
		health=3;
		
		
	}


	@Override
	public void dropLoot() {
		getOwnerArea().registerActor(new CastleKey(getOwnerArea(),getCurrentMainCellCoordinates()));
	}

	
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells(){
		List<DiscreteCoordinates> field = new ArrayList<DiscreteCoordinates>();
		for(int i=-NEIGHBOR_CELLS_RADIUS ;i<NEIGHBOR_CELLS_RADIUS + 1;++i) {
			for(int j=-NEIGHBOR_CELLS_RADIUS;j<NEIGHBOR_CELLS_RADIUS + 1;++j) {
				if(i!=0 || j!=0)
					field.add(getCurrentMainCellCoordinates().jump(i,j));	
			}
		}
		return field;
	}
	
	
	boolean teleport() {
		boolean success=false;
		List<DiscreteCoordinates> cells= chooseRandomCell();
		success = getOwnerArea().leaveAreaCells(this, getCurrentCells());
		setCurrentPosition(cells.get(0).toVector());
		success = success&&getOwnerArea().enterAreaCells(this,cells);
		
		if(!success)
			System.out.println("Error occured while teleporting");
		return success; 	
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(health<=0) {
			currentAnimation=vanish;
			currentAnimation.update(deltaTime);
			if(currentAnimation.isCompleted()) {
				getOwnerArea().unregisterActor(this);
				dropLoot();
			}
			return;	
		}
		++currentSpell;
		
		if(currentSpell%n==0 && currentState==State.IDLE) {
			
			orientateToOpenSpace();
			if(RandomGenerator.getInstance().nextDouble()>0.5)
				currentState=State.ATTACKING;
			else
				currentState=State.INVOKINGSPELL;
		}
		
		
		
		switch(currentState) {
			case IDLE:
				moveOrientate();
				currentAnimation=idleAnimations[this.getOrientation().ordinal()];
				break;
			case TELEPORTING:
				teleport();
				currentAnimation.reset();
				currentState=State.IDLE;
				
				break;
			case INVOKINGTELEPORT:
				currentAnimation= invokeAnimations[this.getOrientation().ordinal()];
				if(currentAnimation.isCompleted())
					currentState=State.TELEPORTING;
				break;
			case INVOKINGSPELL:
				currentAnimation= invokeAnimations[this.getOrientation().ordinal()];
				if(currentAnimation.isCompleted()) {
					castFlameSkull();
					currentState=State.IDLE;
				}
				break;
			case ATTACKING:
				castFireSpell();
				currentState=State.IDLE;
				
				break;
		
		
		}
		currentAnimation.update(deltaTime);
		
	}
	
	
	List<DiscreteCoordinates> chooseRandomCell(){
		List<DiscreteCoordinates> field = new ArrayList<DiscreteCoordinates>();
		int randx;
		int randy;
		do {
			randx= RandomGenerator.getInstance().nextInt(2*TELEPORTATION_RADIUS)-TELEPORTATION_RADIUS;
			randy= RandomGenerator.getInstance().nextInt(2*TELEPORTATION_RADIUS)-TELEPORTATION_RADIUS;
			field=Collections.singletonList(getCurrentMainCellCoordinates().jump(new Vector(randx,randy)));
		}while(!canEnter(field));
		return field;
	} 
	
	void castFireSpell() {
		getOwnerArea().registerActor(new FireSpell(getOwnerArea(),getOrientation(),
				getCurrentMainCellCoordinates().jump(getOrientation().toVector()),
				4));
	
	}
	void castFlameSkull() {
		if(canPlaceActor(getOrientation()));
		getOwnerArea().registerActor(new FlameSkull(getOwnerArea(),getOrientation(),
				getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
	
	}
	
	
	void orientateToOpenSpace() {
		Orientation or=getOrientation();
		while(!canPlaceActor(or)) {
			 or=getRandomOrientation();
		}
		orientate(or);
	}
	
	
	boolean canPlaceActor(Orientation orientation) {
		return canEnter(Collections.singletonList(getCurrentMainCellCoordinates().jump(orientation.toVector())));
	}
	boolean canEnter(List<DiscreteCoordinates> coord) {
		return((ARPGArea)getOwnerArea()).behavior.canEnter(null,coord);
	}
	
	
	
	void moveOrientate() {
		if(RandomGenerator.getInstance().nextDouble()<=0.8f) {
			move(ANIMATION_DURATION);
		}else {
			orientate(getRandomOrientation());
		}		
		orientateToOpenSpace();
	}
	
	
	Orientation getRandomOrientation() {
		return Orientation.fromInt(RandomGenerator.getInstance().nextInt(4));
	}
	

	@Override
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		
	}
	
	@Override
	public boolean takeCellSpace() {
		return true;
	}


	@Override
	public boolean isViewInteractable() {
		return false;
	}
	@Override
	public boolean isCellInteractable() {
		return true;
	}


	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}
	
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}


}
