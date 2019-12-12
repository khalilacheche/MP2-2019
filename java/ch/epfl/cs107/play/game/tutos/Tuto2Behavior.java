package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2Behavior extends AreaBehavior {
	public enum Tuto2CellType {
		NULL(0, false),
		WALL(-16777216, false), // #000000 RGB code of black
		IMPASSABLE (-8750470, false), // #7A7A7A , RGB color of gray
		INTERACT(-256, true), // #FFFF00 , RGB color of yellow
		DOOR(-195580, true), // #FD0404 , RGB color of red
		WALKABLE(-1, true),; // #FFFFFF , RGB color of white
		final int type;
		final boolean isWalkable;
		
		Tuto2CellType(int type , boolean isWalkable){
			this.type = type;
			this.isWalkable = isWalkable;
		}
		
		
		static Tuto2CellType toType(int type) {
			for(Tuto2CellType cellType : Tuto2CellType.values()) {
				if(cellType.type==type)
					return cellType;
			}
			return NULL;
		}
		
	}
	public boolean isDoor(DiscreteCoordinates cord) {
		return ((Tuto2Cell)getCell(cord.x,cord.y)).isDoor();
	}

	public Tuto2Behavior(Window window, String name) {
		super(window, name);
		Tuto2CellType  cellType;
		for(int x=0;x<this.getWidth();++x) {
			for(int y=0;y<this.getHeight();++y) {
				 cellType =Tuto2CellType.toType(getRGB(this.getHeight() -1-y, x));
				 setCell(x,y,new Tuto2Cell(x,y,cellType));
			}
		}
	}

}
