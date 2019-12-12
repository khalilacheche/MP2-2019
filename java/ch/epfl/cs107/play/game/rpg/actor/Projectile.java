package ch.epfl.cs107.play.game.rpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.FlyableEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

abstract public class Projectile extends MovableAreaEntity implements Interactor,FlyableEntity {
	
	int maxDistance;
	int traveledDistance; 
	int speed;
	boolean hasFinishedRun;
	

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
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(!hasFinishedRun) {
			move(speed);
			++traveledDistance;
			if(traveledDistance>maxDistance)
				hasFinishedRun=true;
		}else {
			getOwnerArea().unregisterActor(this);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		
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
		return !hasFinishedRun;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		
	}

}
