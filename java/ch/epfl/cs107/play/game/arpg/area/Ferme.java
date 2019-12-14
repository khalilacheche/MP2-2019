package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.Rock;
import ch.epfl.cs107.play.game.arpg.actor.Villager;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Ferme extends ARPGArea{
	public Door[]doors; 
	@Override
	public String getTitle() {
		return "Zelda/Ferme";
	}

	@Override
	protected void createArea() {
		doors = new Door[3];
		doors[0]=new Door("Zelda/Route",new DiscreteCoordinates(1,15),null,this,Orientation.RIGHT,new DiscreteCoordinates(19,15),new DiscreteCoordinates(19,16));
		doors[1]=new Door("Zelda/Village",new DiscreteCoordinates(4,18),null,this,Orientation.DOWN,new DiscreteCoordinates(4,0),new DiscreteCoordinates(5,0));
		doors[2]=new Door("Zelda/Village",new DiscreteCoordinates(14,18),null,this,Orientation.DOWN,new DiscreteCoordinates(13,0),new DiscreteCoordinates(14,0));
		for(int i=0;i<doors.length;++i) {
			registerActor(doors[i]);
		}
		registerActor(new CastleKey(this,new DiscreteCoordinates(6,6)));
		registerActor(new Background (this));
		registerActor(new Foreground(this));
		registerActor(new Rock(this,new DiscreteCoordinates(13,0)));
		registerActor(new Rock(this,new DiscreteCoordinates(14,0)));
		registerActor(new Villager(this,Orientation.UP,new DiscreteCoordinates(6,9),"1",false));
		
		
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}
	

}
