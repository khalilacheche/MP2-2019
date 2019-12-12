package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.LadderDoor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Cave1 extends ARPGArea {

	@Override
	public String getTitle() {
		return "Zelda/Cave.1";
	}

	@Override
	protected void createArea() {
		registerActor(new LadderDoor("Zelda/Cave.2",new DiscreteCoordinates(7,3),Logic.TRUE,this,Orientation.RIGHT,new DiscreteCoordinates(4,4)));
		registerActor(new Door("Zelda/Village",new DiscreteCoordinates(25,17),Logic.TRUE,this,Orientation.DOWN,new DiscreteCoordinates(4,0)));
		registerActor(new Background (this)); 
	}

	@Override
	public float getCameraScaleFactor() {
		return 8;
	}

}
