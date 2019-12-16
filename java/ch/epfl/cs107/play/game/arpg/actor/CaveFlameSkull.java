package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class CaveFlameSkull extends FlameSkull implements Logic {
	public CaveFlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position,true);
	}
	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return isDead();
	}
	@Override
	public boolean isOff() {
		// TODO Auto-generated method stub
		return !isDead();
	}
	@Override
	public float getIntensity() {
		// TODO Auto-generated method stub
		return isDead()?1:0;
	}

}
