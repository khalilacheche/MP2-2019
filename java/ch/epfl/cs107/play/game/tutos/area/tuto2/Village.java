package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;

public class Village extends Tuto2Area{

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Zelda/Village";
	}

	@Override
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		
	}

	@Override
	public float getCameraScaleFactor() {
		// TODO Auto-generated method stub
		return 15;
	}

}
