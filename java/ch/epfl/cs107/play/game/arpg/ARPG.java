package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Cave1;
import ch.epfl.cs107.play.game.arpg.area.Cave2;
import ch.epfl.cs107.play.game.arpg.area.Chateau;
import ch.epfl.cs107.play.game.arpg.area.Ferme;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.arpg.area.RouteChateau;
import ch.epfl.cs107.play.game.arpg.area.RouteTemple;
import ch.epfl.cs107.play.game.arpg.area.Temple;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {
	
	
	private void createAreas() {
		super.addArea(new Ferme());
		super.addArea(new Village());
		super.addArea(new Route());
		super.addArea(new RouteChateau());
		super.addArea(new Chateau());
		super.addArea(new Cave1());
		super.addArea(new Cave2());
		super.addArea(new RouteTemple());
		super.addArea(new Temple()); 
	}
	@Override
	public String getTitle() {
		return "Mini projet 2";
	}
	
	@Override
	public boolean begin (Window window, FileSystem fileSystem) {
		if(super.begin(window, fileSystem)) {
			createAreas();
			setCurrentArea("Zelda/Ferme",true);
			initPlayer(new ARPGPlayer(getCurrentArea(), Orientation.RIGHT, new DiscreteCoordinates(6,10)));
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void update(float deltaTime) {
	     if(((ARPGPlayer)(this.getPlayer())).responded&&((ARPGPlayer)(this.getPlayer())).wantsRestart) 
			this.begin(this.getWindow(), this.getFileSystem()); 
		else if(((ARPGPlayer)(this.getPlayer())).responded&&!((ARPGPlayer)(this.getPlayer())).wantsRestart)
			this.end();
			
		
	     
		super.update(deltaTime);
	}
	
	 @Override
	    public void end() {
		 System.exit(0);
	    }
	

}
