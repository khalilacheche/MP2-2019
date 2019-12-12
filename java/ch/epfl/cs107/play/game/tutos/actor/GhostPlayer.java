package ch.epfl.cs107.play.game.tutos.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class GhostPlayer extends MovableAreaEntity {
	private final static int ANIMATION_DURATION = 1;
	Sprite sprite;
	public boolean inDoor=false;
	public GhostPlayer(Area area, Orientation orientation, DiscreteCoordinates position,String spritename) {
		super(area, orientation, position);
		 sprite= new Sprite(spritename,1,1.f,this);
		// TODO Auto-generated constructor stub
	}

	public void enterArea(Area area, DiscreteCoordinates position) {
		area.registerActor(this);
		setCurrentPosition(position.toVector());
		resetMotion();
		inDoor=false;
		setOwnerArea(area);
	}
	public void leaveArea(Area area) {
		area.unregisterActor(this);
		inDoor=false;
	}


	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	@Override
	public void update(float deltaTime) {
		Keyboard keyboard =getOwnerArea().getKeyboard();
		Button keyUp = keyboard.get(Keyboard.UP) ;
		Button keyDown = keyboard.get(Keyboard.DOWN) ;
		Button keyLeft = keyboard.get(Keyboard.LEFT) ;
		Button keyRight = keyboard.get(Keyboard.RIGHT) ;
		
		if(keyLeft.isDown()) {
			if(this.getOrientation()==Orientation.LEFT) {
				this.move(ANIMATION_DURATION);
			}else {
				this.orientate(Orientation.LEFT);
			}
		}
		if(keyRight.isDown()) {
			if(this.getOrientation()==Orientation.RIGHT) {
				this.move(ANIMATION_DURATION);
			}else {
				this.orientate(Orientation.RIGHT);
			}
		}
		if(keyUp.isDown()) {
			if(this.getOrientation()==Orientation.UP) {
				this.move(ANIMATION_DURATION);
			}else {
				this.orientate(Orientation.UP);
			}
		}
		if(keyDown.isDown()) {
			if(this.getOrientation()==Orientation.DOWN) {
				this.move(ANIMATION_DURATION);
			}else {
				this.orientate(Orientation.DOWN);
			}
		}
		
		super.update(deltaTime);
		List<DiscreteCoordinates> pos= getCurrentCells();
			inDoor=(((Tuto2Area)getOwnerArea()).isDoor(pos.get(0)));
	}
	
	

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		sprite.draw(canvas);
	}

}
