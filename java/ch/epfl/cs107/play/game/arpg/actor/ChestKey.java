package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class ChestKey extends ARPGCollectableAreaEntity {
	private Sprite key;
	private ARPGItem type= ARPGItem.CHESTKEY;
	
	public ChestKey(Area area, DiscreteCoordinates position) {
		super(area, position);
		key = new RPGSprite("zelda/key",1,1,this,new RegionOfInterest(0,0,16,16));
	}
	
	@Override
	public void draw (Canvas canvas) {
		key.draw(canvas);
	}
	
	protected ARPGItem getItem() {
		for(ARPGItem i : ARPGItem.values())
			if(i==type)
				return i;
		return null;
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	


}