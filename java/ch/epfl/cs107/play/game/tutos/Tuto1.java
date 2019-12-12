package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import game.tutos.area.tuto1.Ferme;
import game.tutos.area.tuto1.Village;

public class Tuto1 extends AreaGame {
	private SimpleGhost player;
	
	@Override
	public String getTitle() {
		return "Tuto1";
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
			player = new SimpleGhost(new Vector(18,7),"ghost.1");
			getCurrentArea().registerActor(player);
			return true;
		}
			else return false;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		Keyboard keyboard = getWindow().getKeyboard() ;
		Button keyUp = keyboard.get(Keyboard.UP) ;
		Button keyDown = keyboard.get(Keyboard.DOWN) ;
		Button keyLeft = keyboard.get(Keyboard.LEFT) ;
		Button keyRight = keyboard.get(Keyboard.RIGHT) ;
		
		
		if(player.isWeak()) {
			switchArea();
			System.out.println(getCurrentArea().getTitle());
			//getCurrentArea().setViewCandidate(null);
			
		}
		
		if(keyUp.isDown())
		{
			player.moveUp();
		}
		if(keyDown.isDown())
		{
			player.moveDown();
		}
		if(keyLeft.isDown())
		{
			player.moveLeft();
		}
		if(keyRight.isDown())
		{
			player.moveRight();
		}

	}
	
	public void switchArea() {
		getCurrentArea().unregisterActor(player);
		if(getCurrentArea().getTitle()=="Zelda/Village") {
			setCurrentArea("Zelda/Ferme",true);
		}else {
			setCurrentArea("Zelda/Village",true);
		}
		getCurrentArea().registerActor(player);
		getCurrentArea().setViewCandidate(player);
		player.strengthen();
	}
	

}
