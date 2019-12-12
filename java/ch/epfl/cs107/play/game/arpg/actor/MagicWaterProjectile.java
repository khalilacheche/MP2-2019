package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGAttackType;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Projectile;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class MagicWaterProjectile extends Projectile {
	
	MagicWaterHandler handler;
	Animation idleAnimation;
	ARPGAttackType attack= ARPGAttackType.WATER;
	class MagicWaterHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(ARPGMonster monster) {
			finishRun();
			monster.receiveAttack(attack, 0.5f);
				System.out.println(monster +" "+monster.health);
		}
		@Override
		public void interactWith(FireSpell spell) {
			spell.extenguish();
		}
	}

	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int speed) {
		super(area, orientation, position, maxDistance, speed);
		handler=new MagicWaterHandler();
		Sprite[] sprites = new Sprite[4];
		for(int i=0;i<4;++i) {
			sprites[i]= new Sprite("zelda/magicWaterProjectile",1,1,this,new RegionOfInterest(32*i,0,32,32));
		}
		idleAnimation= new Animation(speed,sprites);	
	}
	
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position) {
		this(area,orientation,position,100,2);
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(!isDisplacementOccurs())
			finishRun();
		idleAnimation.update(deltaTime);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		
	}
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	@Override
	public void draw(Canvas canvas) {
		idleAnimation.draw(canvas);
	}

}
