package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CaveFlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.ChestKey;
import ch.epfl.cs107.play.game.arpg.actor.LadderDoor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Cave2 extends ARPGArea {
	private And dropKeySignal;
	private boolean hasDroppedKey;
	@Override
	public String getTitle() {
		return "Zelda/Cave.2";
	}

	@Override
	protected void createArea() {
		registerActor(new LadderDoor("Zelda/Cave.1",new DiscreteCoordinates(4,3),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(7,2)));
		CaveFlameSkull skull1 =new CaveFlameSkull(this,Orientation.LEFT,new DiscreteCoordinates(2,7));
		CaveFlameSkull skull2 =new CaveFlameSkull(this,Orientation.LEFT,new DiscreteCoordinates(13,7));
		dropKeySignal = new And((Logic)skull1,(Logic)skull2);
		registerActor(skull1);
		registerActor(skull2);
		hasDroppedKey=false;
		registerActor(new Background (this));
	}
	
	@Override
	public void update(float deltaTime) {
		if(dropKeySignal.isOn() && !hasDroppedKey) {
			//TODO: Drop ChestKey
			registerActor(new ChestKey(this,new DiscreteCoordinates(7,7)));
			hasDroppedKey=true;
		}
		super.update(deltaTime);
			
	}
	

	@Override
	public float getCameraScaleFactor() {
		return 12;
	}

}
