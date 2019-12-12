package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;

public class Tuto2 extends AreaGame {
	private GhostPlayer player;
	
	@Override
	public String getTitle() {
		return "Tuto2";
	}
	private void createAreas() {
		super.addArea(new Ferme());
		super.addArea(new Village());
	}
	@Override
	public void end (){
		
	}
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (super.begin(window , fileSystem)) {
			createAreas();
			setCurrentArea("Zelda/Village",true);
			player = new GhostPlayer(getCurrentArea(),Orientation.DOWN,new DiscreteCoordinates(5,15),"ghost.1");
			getCurrentArea().registerActor(player);
			getCurrentArea().setViewCandidate(player);
			return true;
		}
			else return false;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		
		if(player.inDoor) {
			switchArea();
			
		}

	}
	
	public void switchArea() {
		player.leaveArea(getCurrentArea());
		if(getCurrentArea().getTitle()=="Zelda/Village") {
			setCurrentArea("Zelda/Ferme",false);
			player.enterArea(getCurrentArea(),new DiscreteCoordinates(2,10));
		}else {
			setCurrentArea("Zelda/Village",false);
			player.enterArea(getCurrentArea(),new DiscreteCoordinates(5,15));
		}
		getCurrentArea().setViewCandidate(player);
	}
	

}
