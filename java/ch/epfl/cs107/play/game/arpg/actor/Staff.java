package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Staff extends ARPGCollectableAreaEntity {
	ARPGItem item=ARPGItem.STAFF;
	Animation animation;
	
	public Staff(Area area, DiscreteCoordinates position) {
		super(area, position);
		Sprite[] sprites = new Sprite[8];
		for(int i=0;i<8;++i) {
			sprites[i]= new RPGSprite("zelda/staff",1.75f,1.75f,this,new RegionOfInterest(i*32,0,32,32),new Vector(-0.35f,0f));
		}
		animation = new Animation(3, sprites,true);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	@Override
	public void update(float deltaTime) {
		animation.update(deltaTime);
	}
	@Override
	public void draw (Canvas canvas) {
		animation.draw(canvas);
	}
}
