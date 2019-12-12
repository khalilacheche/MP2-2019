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
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class LogMonster extends ARPGMonster {
	
	
	private Animation currentAnimation;
	private Animation[] idleAnimations;
	private Animation sleeping;
	private Animation wakingUp;
	
	private enum State{
		IDLE,
		SLEEPING,
		WAKINGUP,
		ATTACKING,
		FALLINGASLEEP;
	}
	private State currentState;
	private static List<ARPGAttackType> vulnerabilities = new ArrayList<ARPGAttackType>(Arrays.asList(
			ARPGAttackType.FIRE,ARPGAttackType.PHYSICAL));
	private static final int ANIMATION_DURATION = 3;
	private static final int FACING_CELLS=8;
	private final static int MAX_INACTION_TIME=24;
	private static final double PROBABILITY_OF_INACTIVE = 0.1;
	private static float PLAYER_DAMAGE=2;
	private static float MAX_HEALTH=1;
	private static float MIN_SLEEPING_DURATION=4;
	private static float MAX_SLEEPING_DURATION=7;
	private double sleepTime;
	private int inactionTime;
	private LogMonsterHandler handler;
	
	
	private class LogMonsterHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(ARPGPlayer player) {
			if(currentState==State.ATTACKING)
				player.addHealth(-PLAYER_DAMAGE);
			else {
				currentState=State.ATTACKING;
				System.out.println("assba");
			}
		}
	}
	

	public LogMonster(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, vulnerabilities,MAX_HEALTH);
		
		
		
		idleAnimations= RPGSprite.createAnimations(ANIMATION_DURATION, 
				RPGSprite.extractSprites("zelda/logMonster",
				4, 2, 2,
				this , 32, 32, new Orientation[] {Orientation.DOWN ,
				Orientation.UP , Orientation.RIGHT, Orientation.LEFT})
		);
		
		
		
		Sprite[] sprites = new Sprite[4];
		for(int i=0;i<4;++i) {
			sprites[i]= new Sprite("zelda/logMonster.sleeping",2,2,this,new RegionOfInterest(0,32*i,32,32));
		}
		sleeping = new Animation(ANIMATION_DURATION, sprites);
		
		
		sprites = new Sprite[3];
		for(int i=0;i<3;++i) {
			sprites[i]= new Sprite("zelda/logMonster.wakingUp",2,2,this,new RegionOfInterest(0,32*i,32,32));
		}
		wakingUp= new Animation(ANIMATION_DURATION, sprites,false);
		
		handler = new LogMonsterHandler();
		
		inactionTime=0;
		
		currentState=State.IDLE;
		sleepTime=MIN_SLEEPING_DURATION;
		currentAnimation=idleAnimations[this.getOrientation().ordinal()];

	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		if(currentState == State.ATTACKING)
			return Collections.singletonList
					(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		List<DiscreteCoordinates> list= new ArrayList<DiscreteCoordinates>();
		for(int i=0;i<FACING_CELLS;++i) {
			list.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i+1)));
		}
		return list;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
		
	}
	
	@Override
	public boolean wantsViewInteraction() {
		return currentState==State.ATTACKING || currentState==State.IDLE;
	}
	@Override
	public boolean wantsCellInteraction() {
		return false;
	}
	

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}
	public void update(float deltaTime) {
		
		
		switch(currentState) {
			case IDLE : 
				if(!this.isDisplacementOccurs() && inactionTime <=0) {
					if(RandomGenerator.getInstance().nextDouble()<0.2) 
						moveOrientate();
					
					else
			             moveOrientate();
					currentAnimation=idleAnimations[this.getOrientation().ordinal()];
					if(RandomGenerator.getInstance().nextDouble()<PROBABILITY_OF_INACTIVE)
						inactionTime=RandomGenerator.getInstance().nextInt(MAX_INACTION_TIME);
                    
			        
				}
				else {
					if(this.isDisplacementOccurs()) {
			        	currentAnimation.update(deltaTime);
			        }
			        else  {
			        	inactionTime-=deltaTime;
			        	System.out.println(inactionTime);
			        	currentAnimation.reset();
			        }
				}
			break ;
			case ATTACKING : 
				move(ANIMATION_DURATION); 
				if(this.isDisplacementOccurs()) {
		        	currentAnimation = idleAnimations[this.getOrientation().ordinal()]; 
		        	currentAnimation.update(deltaTime);
		        }
		        else  {
		        	if(!this.getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells())) {
		        		currentState=State.FALLINGASLEEP;
		        	}
		        }
			break ; 
			case FALLINGASLEEP: 
				sleepTime = MIN_SLEEPING_DURATION+(MAX_SLEEPING_DURATION-MIN_SLEEPING_DURATION)*RandomGenerator.getInstance().nextDouble();
				setState(State.SLEEPING,this.sleeping); 
			break; 
			case SLEEPING:
				if(sleepTime >0) {
					sleepTime -=deltaTime; 
					this.currentAnimation.update(deltaTime);		
			}

				if(sleepTime<=0)
					setState(State.WAKINGUP,this.wakingUp);
			break;
			case WAKINGUP:
				this.currentAnimation.update(deltaTime);
				if(this.currentAnimation.isCompleted()) {
					setState(State.IDLE,idleAnimations[this.getOrientation().ordinal()]); 
				}
			break ; 
			
			}
		if(deathAnimation.isCompleted()) {
        	this.getOwnerArea().unregisterActor(this);
        	this.dropLoot(new Coin(this.getOwnerArea(),this.getCurrentMainCellCoordinates()));

        }
       
        if(isDead()) {
        	this.deathAnimation.update(deltaTime);	
		}
	
	super.update(deltaTime);
	
}
	/*@Override
	public void update(float deltaTime) {
		
		super.update(deltaTime);
		
		if(isDead()) {
			deathAnimation.update(deltaTime);
			if(deathAnimation.isCompleted()) {
				dropLoot(new Coin(getOwnerArea(),getCurrentMainCellCoordinates()));
				getOwnerArea().unregisterActor(this);
			}
				
			return;
		}
		
		if(inactionTime<MAX_INACTION_TIME) {
			++inactionTime;
			currentAnimation.reset();
			return;
		}
		
		switch(currentState){
			case SLEEPING:
				currentAnimation=sleeping;
				if(sleepTime>0) {
					sleepTime-=deltaTime;
				}else{
					currentState=State.WAKINGUP;
				}
				break;
			case WAKINGUP:
				currentAnimation=wakingUp;
				if(wakingUp.isCompleted())
					currentState=State.IDLE;
				break;
			case FALLINGASLEEP:
				sleepTime = MIN_SLEEPING_DURATION+(MAX_SLEEPING_DURATION-MIN_SLEEPING_DURATION)*RandomGenerator.getInstance().nextDouble();
				currentState=State.SLEEPING;
				break;
			case ATTACKING:
				move(ANIMATION_DURATION);
				currentAnimation=idleAnimations[this.getOrientation().ordinal()];
				if(!isDisplacementOccurs()|| isTargetReached()) {
					currentState=State.FALLINGASLEEP;
				}
				break;
			case IDLE:
				moveOrientate();
				currentAnimation=idleAnimations[this.getOrientation().ordinal()];
				inactionTime= 20+RandomGenerator.getInstance().nextInt(MAX_INACTION_TIME);
				break;
		}
		currentAnimation.update(deltaTime);
	
	
	}*/
	public void setState(State state,Animation animation) {
		this.currentState = state; 
		this.currentAnimation = animation;
		
	}
	
	private void moveOrientate() {
		if(RandomGenerator.getInstance().nextDouble()<=0.9f) {
			move(ANIMATION_DURATION*4);
		}
		else {
			this.orientate( Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
		}		
	}
	

	@Override
	public void draw(Canvas canvas) {
		if(isDead())
			super.draw(canvas);
		else currentAnimation.draw(canvas);
		
	}


}
