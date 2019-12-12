package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class LadderDoor extends Door {
	private Sprite sprite;
	public LadderDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
			Orientation orientation, DiscreteCoordinates position) {
		super(destination, otherSideCoordinates, signal, area, orientation, position);
		sprite = new RPGSprite("zelda/ladder",1,1,this,new RegionOfInterest(0,0,32,32));
	}
	
	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}

}
