package game.tutos.area.tuto1;
import ch.epfl.cs107.play.game.tutos.area.*;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;

public class Village extends SimpleArea{

	@Override
	public String getTitle() {
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
		return 10.f;
	}
	
}