package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.FlyableEntity;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior {
	
	public enum ARPGCellType {
		NULL(0, false,false),
		WALL(-16777216, false,false), // #000000 RGB code of black
		IMPASSABLE (-8750470, false,true), // #7A7A7A , RGB color of gray
		INTERACT(-256, true,true), // #FFFF00 , RGB color of yellow
		DOOR(-195580, true,true), // #FD0404 , RGB color of red
		WALKABLE(-1, true,true),; // #FFFFFF , RGB color of white
		final int type;
		final boolean isWalkable;
		final boolean isFlyable;
		
		ARPGCellType(int type , boolean isWalkable,boolean isFlyable){
			this.type = type;
			this.isWalkable = isWalkable;
			this.isFlyable=isFlyable;
		}
		
		
		static ARPGCellType toType(int type) {
			for(ARPGCellType cellType : ARPGCellType.values()) {
				if(cellType.type==type)
					return cellType;
			}
			return NULL;
		}
		
	}
	public class ARPGCell extends Cell{
		public ARPGCellType type;
		
		public ARPGCell(int x, int y,ARPGCellType type) {
			super(x, y);
			this.type=type;
		}

		@Override
		public boolean isCellInteractable() {
			return false;
		}

		@Override
		public boolean isViewInteractable() {
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
			
		}

		@Override
		protected boolean canLeave(Interactable entity) {
			return true;
		}

		@Override
		protected boolean canEnter(Interactable entity) {
			
				return (type.isWalkable || (type.isFlyable && entity instanceof FlyableEntity)) && (!hasNonTraversableContent());
			
		}
		
	}
	
	
	public ARPGBehavior(Window window, String name) {
		super(window, name);
		ARPGCellType  cellType;
		for(int x=0;x<this.getWidth();++x) {
			for(int y=0;y<this.getHeight();++y) {
				 cellType =ARPGCellType.toType(getRGB(this.getHeight() -1-y, x));
				 setCell(x,y,new ARPGCell(x,y,cellType));
			}
		}
	}


}