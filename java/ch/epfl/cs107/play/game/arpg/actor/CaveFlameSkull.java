package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class CaveFlameSkull extends FlameSkull implements Logic {
	
	/** CaveFlameSkull Constructor
	 * 
	 */
	public CaveFlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position,true);
	}
	
	///////////////////////// Logic //////////////////////////////////////////////////
	@Override
	public boolean isOn() {
		return isDead();
	}
	@Override
	public boolean isOff() {
		return !isDead();
	}
	@Override
	public float getIntensity() {
		return isDead()?1:0;
	}

}
