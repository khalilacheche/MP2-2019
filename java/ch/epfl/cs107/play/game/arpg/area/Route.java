package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Route extends ARPGArea {
	public Door[]doors;
	public Grass[] grass;
	@Override
	public String getTitle() {
		return "Zelda/Route";
	}
	@Override
	protected void createArea() {
		//TODO: improve door declaration
		
		registerActor(new Background (this));
		registerActor(new Foreground(this));
		
		
		doors = new Door[3];
		doors[0]=new Door("Zelda/Ferme",new DiscreteCoordinates(18,15),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(0,15),new DiscreteCoordinates(0,16));
		doors[1]=new Door("Zelda/Village",new DiscreteCoordinates(29,18),Logic.TRUE,this,Orientation.DOWN,new DiscreteCoordinates(9,0),new DiscreteCoordinates(10,0));
		doors[2]=new Door("Zelda/RouteChateau",new DiscreteCoordinates(9,1),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(9,19),new DiscreteCoordinates(10,19));
		for(int i=0;i<doors.length;++i) {
			registerActor(doors[i]);
		}
		
		grass = new Grass[18];
		int counter=0;
		for(int i=5;i<8;++i) {
			for(int j=6;j<12;++j) {
				grass[counter]=new Grass(this,Orientation.UP,new DiscreteCoordinates(i,j));
				registerActor(grass[counter]);
				++counter;
			}
		}
		/*registerActor(new FlameSkull(this,Orientation.UP,new DiscreteCoordinates(10,10)));
		registerActor(new FireSpell(this,Orientation.LEFT,new DiscreteCoordinates(11,11),4));*/
		registerActor(new LogMonster(this,Orientation.DOWN,new DiscreteCoordinates(10,10)));
		 
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}
	

}
