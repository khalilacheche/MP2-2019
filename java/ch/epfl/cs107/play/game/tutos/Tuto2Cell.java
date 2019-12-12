package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior.Tuto2CellType;

public class Tuto2Cell extends Cell {
	public Tuto2CellType type;

	public Tuto2Cell(int x, int y) {
		super(x, y);
	}
	public Tuto2Cell(int x, int y, Tuto2CellType type) {
		super(x, y);
		this.type=type;
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
	}

	@Override
	protected boolean canLeave(Interactable entity) {
		return true;
	}

	@Override
	protected boolean canEnter(Interactable entity) {
		return type.isWalkable;
	}
	public boolean isDoor() {
		return type.name()=="DOOR";
	}

}
