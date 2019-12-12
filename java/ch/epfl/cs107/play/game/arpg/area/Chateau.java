package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Chateau extends ARPGArea {
	public Door[]doors; 
	@Override
	public String getTitle() {
		return "Zelda/Chateau";
	}

	@Override
	protected void createArea() {
		doors = new Door[1];
		doors[0]=new Door("Zelda/RouteChateau",new DiscreteCoordinates(9,12),Logic.TRUE,this,Orientation.DOWN,new DiscreteCoordinates(7,0),new DiscreteCoordinates(8,0));
		registerActor(doors[0]);
		registerActor(new Background (this)); 
		registerActor(new Foreground(this));
		
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}
}
