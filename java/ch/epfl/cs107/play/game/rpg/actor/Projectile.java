package ch.epfl.cs107.play.game.rpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.FlyableEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;


abstract public class Projectile extends MovableAreaEntity implements Interactor,FlyableEntity {
	
	private int maxDistance;
	private int traveledDistance; 
	private int speed;
	private boolean hasFinishedRun;//indicates if the projectile ahsFinishedRun
	private Animation idleAnimation;
	
	/**
	 */
	public Projectile(Area area, Orientation orientation, DiscreteCoordinates position,int maxDistance , int speed) {
		super(area, orientation, position);
		
		this.speed=speed;
		this.maxDistance=maxDistance;
		traveledDistance=0;
		hasFinishedRun=false;
	}

	
	protected void finishRun() {
		hasFinishedRun=true;
	}
	protected void setAnimation(Animation anim) {
		idleAnimation = anim;
	}

	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(!hasFinishedRun) {
			move(speed);
			++traveledDistance;
			if(traveledDistance>maxDistance) // End run when the maxDistance was travelled
				finishRun();
		}else {
			getOwnerArea().unregisterActor(this);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		idleAnimation.draw(canvas);
	}

////////////////////////////////Interactable / Interactor ////////////////////////////////////////////////////////////////	
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
		return !hasFinishedRun;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}
	
	@Override
	public boolean isCellInteractable() {
		return false;
	}
	
	@Override
	public boolean isViewInteractable() {
		return false;
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
