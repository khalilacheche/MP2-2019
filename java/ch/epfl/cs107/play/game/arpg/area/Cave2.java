package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.LadderDoor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Cave2 extends ARPGArea {

	@Override
	public String getTitle() {
		return "Zelda/Cave.2";
	}

	@Override
	protected void createArea() {
		registerActor(new LadderDoor("Zelda/Cave.1",new DiscreteCoordinates(4,3),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(7,2)));
		registerActor(new FlameSkull(this,Orientation.LEFT,new DiscreteCoordinates(2,6),true));
		registerActor(new FlameSkull(this,Orientation.RIGHT,new DiscreteCoordinates(6,6),true));
		registerActor(new Background (this));
	}

	@Override
	public float getCameraScaleFactor() {
		// TODO Auto-generated method stub
		return 12;
	}

}
