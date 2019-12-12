package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public abstract class ARPGArea extends Area {
	
	
	public ARPGBehavior behavior;

	protected abstract void createArea();

	
	 @Override
	    public boolean begin(Window window, FileSystem fileSystem) {
	        if (super.begin(window, fileSystem)) {
	            // Set the behavior map
	        	behavior = new ARPGBehavior(window,getTitle());
	        	setBehavior(behavior);
	        	createArea();
	            return true;
	        }
	        return false;
	    }
	 @Override
	 public void end() {
		 
	 }
	 

}
