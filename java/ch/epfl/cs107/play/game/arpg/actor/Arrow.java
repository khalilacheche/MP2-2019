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

public class Arrow extends Projectile {
	
	private ArrowHandler handler;
	private Monster.AttackType attack= Monster.AttackType.PHYSICAL;
	private class ArrowHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
			//finishRun();
		}
		@Override
		public void interactWith(Monster monster) {
			finishRun();
			monster.receiveAttack(attack, 0.1f);
		}
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
			finishRun();
		}
		@Override
		public void interactWith(FireSpell spell) {
			spell.extenguish();
		}
		@Override
		public void interactWith(Orb orb) {
			orb.amorceSignal();
		}
	}

	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int speed) {
		super(area, orientation, position, maxDistance, speed);
		handler=new ArrowHandler();
		Sprite[] sprite = new Sprite[1];
		sprite[0]= new RPGSprite("zelda/arrow",1,1,this,new RegionOfInterest(32*this.getOrientation().ordinal(),0,32,32));
		super.setAnimation(new Animation(speed,sprite));	
	}
	
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
		this(area,orientation,position,100,2);
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
