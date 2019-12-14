package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.Chest;
import ch.epfl.cs107.play.game.arpg.actor.ShopMan;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Village extends ARPGArea {
	public Door[]doors; 
	@Override
	public String getTitle() {
		return "Zelda/Village";
	}

	@Override
	protected void createArea() {
		doors = new Door[4];
		doors[0]=new Door("Zelda/Ferme",new DiscreteCoordinates(4,1),null,this,Orientation.RIGHT,new DiscreteCoordinates(4,19),new DiscreteCoordinates(5,19));
		doors[1]=new Door("Zelda/Ferme",new DiscreteCoordinates(14,1),null,this,Orientation.DOWN,new DiscreteCoordinates(13,19),new DiscreteCoordinates(14,19),new DiscreteCoordinates(15,19));
		doors[2]=new Door("Zelda/Route",new DiscreteCoordinates(9,1),null,this,Orientation.DOWN,new DiscreteCoordinates(29,19),new DiscreteCoordinates(30,19));
		doors[3]=new CaveDoor("Zelda/Cave.1",new DiscreteCoordinates(4,1),Logic.FALSE,this,Orientation.UP,new DiscreteCoordinates(25,18));
		for(int i=0;i<doors.length;++i) {
			registerActor(doors[i]);
		}
		registerActor(new ShopMan(this,Orientation.DOWN,new DiscreteCoordinates(17,11)));
		registerActor(new Chest (this, new DiscreteCoordinates(15,18)));
		registerActor(new Background (this));
		registerActor(new Foreground(this));
		
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}


}
