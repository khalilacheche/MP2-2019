package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;

import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Projectile;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;

public class MagicWaterProjectile extends Projectile {
	
	private MagicWaterHandler handler;
	private Monster.AttackType attack= Monster.AttackType.MAGIC;
	private static int defaultDistance = 100;
	private static int defaultSpeed = 2;
	
	private class MagicWaterHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(Monster monster) {
			finishRun();
			monster.receiveAttack(attack, 0.5f);
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
			sprites[i]= new RPGSprite("zelda/magicWaterProjectile",1,1,this,new RegionOfInterest(32*i,0,32,32));
		}
		setAnimation(new Animation(speed,sprites));	
	}
	
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position) {
		this(area,orientation,position,defaultDistance,defaultSpeed);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(!isDisplacementOccurs())
			finishRun();
	}
	
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	

}
