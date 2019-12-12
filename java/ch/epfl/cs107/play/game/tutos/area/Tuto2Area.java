package ch.epfl.cs107.play.game.tutos.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class Tuto2Area extends Area {

	
	/**
     * Create the area by adding all its actors
     * called by the begin method, when the area starts to play
     */
	protected abstract void createArea();
	private Tuto2Behavior behavior;

	
	 @Override
	    public boolean begin(Window window, FileSystem fileSystem) {
	        if (super.begin(window, fileSystem)) {
	            // Set the behavior map
	        	behavior = new Tuto2Behavior(window,getTitle());
	        	setBehavior(behavior);
	        	createArea();
	            return true;
	        }
	        return false;
	    }
	 @Override
	 public void end() {
		 
	 }
	 
	 public boolean isDoor(DiscreteCoordinates coord) {
		 return behavior.isDoor(coord);
	 }
	 

}
