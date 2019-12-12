package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteChateau extends ARPGArea {

	public Door[]doors; 
	@Override
	public String getTitle() {
		return "Zelda/RouteChateau";
	}

	@Override
	protected void createArea() {
		doors = new Door[1]; 
		doors[0]=new Door("Zelda/Route",new DiscreteCoordinates(9,18),Logic.TRUE,this,Orientation.DOWN,new DiscreteCoordinates(9,0),new DiscreteCoordinates(10,0));
		CastleDoor door=new CastleDoor("Zelda/Chateau",new DiscreteCoordinates(7,1),Logic.TRUE,this,Orientation.DOWN,new DiscreteCoordinates(9,13),new DiscreteCoordinates(10,13));
		for(int i=0;i<doors.length;++i) {
			registerActor(doors[i]);
		}
		registerActor(door);
		registerActor(new Background (this));
		registerActor(new Foreground(this));
		registerActor(new DarkLord(this,Orientation.UP,new DiscreteCoordinates(9,10)));
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}

}
