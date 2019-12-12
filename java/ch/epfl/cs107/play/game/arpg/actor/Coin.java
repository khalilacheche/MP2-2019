package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends ARPGCollectableAreaEntity {
	Animation animation;

	public Coin(Area area, DiscreteCoordinates position) {
		super(area, position);
		Sprite[] sprites = new Sprite[4];
		for(int i=0;i<4;++i) {
			sprites[i]= new Sprite("zelda/coin",1,1,this,new RegionOfInterest(i*16,0,16,16));
		}
		animation = new Animation(3, sprites,true);
	}
	
	@Override
	public void update(float deltaTime) {
		animation.update(deltaTime);
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
	}
}
