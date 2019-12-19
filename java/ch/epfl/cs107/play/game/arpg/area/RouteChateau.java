package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.Test;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;

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
	public void update (float deltaTime) {
		super.update(deltaTime);
		if(Test.MODE) {
			if(getKeyboard().get(Keyboard.L).isReleased())
				registerActor(new LogMonster(this,Orientation.DOWN,new DiscreteCoordinates(9,9)));
			if(getKeyboard().get(Keyboard.S).isReleased())
				registerActor(new FlameSkull(this,Orientation.DOWN,new DiscreteCoordinates(8,10)));
			if(getKeyboard().get(Keyboard.B).isReleased())
				registerActor(new Bomb (this, new DiscreteCoordinates(8,8)));
				
		}
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}

}
