package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bridge;
import ch.epfl.cs107.play.game.arpg.actor.DialogTrigger;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.Orb;
import ch.epfl.cs107.play.game.arpg.actor.Waterfall;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
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
		
		
		doors = new Door[4];
		doors[0]=new Door("Zelda/Ferme",new DiscreteCoordinates(18,15),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(0,15),new DiscreteCoordinates(0,16));
		doors[1]=new Door("Zelda/Village",new DiscreteCoordinates(29,18),Logic.TRUE,this,Orientation.DOWN,new DiscreteCoordinates(9,0),new DiscreteCoordinates(10,0));
		doors[2]=new Door("Zelda/RouteChateau",new DiscreteCoordinates(9,1),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(9,19),new DiscreteCoordinates(10,19));
		doors[3]=new Door("Zelda/RouteTemple",new DiscreteCoordinates(1,5),Logic.TRUE,this,Orientation.RIGHT,new DiscreteCoordinates(19,9),new DiscreteCoordinates(19,10),new DiscreteCoordinates(19,11));
		for(int i=0;i<doors.length;++i) {
			registerActor(doors[i]);
		}
		
		Actor orb = new Orb(this,new DiscreteCoordinates(18,8)); 
		registerActor(orb);
		registerActor(new Bridge(this,new DiscreteCoordinates(16,10),(Logic)orb));
		grass = new Grass[18];
		int counter=0;
		for(int i=5;i<8;++i) {
			for(int j=6;j<12;++j) {
				grass[counter]=new Grass(this,Orientation.UP,new DiscreteCoordinates(i,j));
				registerActor(grass[counter]);
				++counter;
			}
		}
		registerActor (new Waterfall(this,new DiscreteCoordinates(17,3)));
		registerActor(new LogMonster(this,Orientation.DOWN,new DiscreteCoordinates(10,10)));
		registerActor(new DialogTrigger(this,new DiscreteCoordinates(15,10),"river"));
		registerActor(new DialogTrigger(this,new DiscreteCoordinates(15,8),"orb"));
		 
	}

	@Override
	public float getCameraScaleFactor() {
		return 15;
	}
	

}
