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

public class CastleKey extends ARPGCollectableAreaEntity {
	private Sprite key;
	private ARPGItem type= ARPGItem.CASTLEKEY;
	
	public CastleKey(Area area, DiscreteCoordinates position) {
		super(area, position);
		key = new RPGSprite("zelda/goldKey",1,1,this,new RegionOfInterest(0,0,16,16));
	}
	
	@Override
	public void draw (Canvas canvas) {
		key.draw(canvas);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
			((ARPGInteractionVisitor)v).interactWith(this);
	}
	protected ARPGItem getItem() {
		return type;
	}
	


}
