package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.ARPGPlayerStatusGUI;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;

public class ARPGPlayer extends Player implements Inventory.Holder{
	
	
	private enum State{
		IDLE,
		SwordAttack,
		BowAttack,
		StaffAttack;
	}
	
	final static int ANIMATION_DURATION =4;
	boolean wantsViewInteraction;
	int itemIndex=0;
	public ARPGPlayerHandler handler;
	private Animation[] idleAnimations;
	private Animation[] swordHitAnimations;
	private Animation[] bowHitAnimations;
	private Animation[] staffHitAnimations;
	private Animation currentAnimation;
	private ARPGInventory inventory;
	private ARPGPlayerStatusGUI status;
	private ARPGItem currentItem;
	private State currentState;
	private boolean isInventoryShown;
	
	private float health;
	private boolean isInShop;
	private final static float MAX_HEALTH=6;
	private boolean wantsToBuy;
	private boolean wantsToSell;
	
	private class ARPGPlayerHandler implements ARPGInteractionVisitor{
		
		public void interactWith(CollectableAreaEntity entity) {//TODO: change instanceof
			if(entity instanceof Heart)
				addHealth(1);
			if(entity instanceof Coin)
				inventory.addMoney(50);
			if(entity instanceof CastleKey) {
				inventory.addItem(((CastleKey)entity).item);
			}
			getOwnerArea().unregisterActor(entity);
				
		}

		
		public void interactWith(CastleDoor door) {
			if(currentItem==ARPGItem.CASTLEKEY && wantsViewInteraction()) {
				door.setSignal(Logic.TRUE);
			}else if(door.isOpen()){
				setIsPassingADoor(door);
				door.setSignal(Logic.FALSE);
			}
		}
		
		public void interactWith(Door door) {
			setIsPassingADoor(door);
		}
		public void interactWith(CaveDoor door) {
			setIsPassingADoor(door);
		}
		public void interactWith(Grass grass) {
			if(currentItem==ARPGItem.SWORD)
				grass.cut();
		}
		public void interactWith(ARPGMonster monster) {
			if(currentItem==ARPGItem.SWORD && wantsViewInteraction())
				monster.receiveAttack(ARPGMonster.ARPGAttackType.PHYSICAL, 1);
		}
		public void interactWith(Chest chest) {
			chest.open();
		}
		
		
	
	}



	public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		wantsViewInteraction=false;
		handler = new ARPGPlayerHandler();
		health = MAX_HEALTH;
		Sprite [][] sprites = RPGSprite.extractSprites("zelda/player",
				4, 1, 2,
				this , 16, 32, new Orientation[] {Orientation.DOWN ,
				Orientation.RIGHT , Orientation.UP, Orientation.LEFT});
		idleAnimations=RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites);
		
		sprites = RPGSprite.extractSprites("zelda/player.staff_water",
				4, 2, 2,
				this , 32, 32,new Vector(-0.5f, 0) ,new Orientation[] {Orientation.DOWN ,
				Orientation.UP , Orientation.RIGHT, Orientation.LEFT});
		staffHitAnimations=RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites,false);
		
		
		sprites = RPGSprite.extractSprites("zelda/player.sword",
				4, 2, 2,
				this , 32, 32, new Vector(-0.5f, 0),new Orientation[] {Orientation.DOWN ,
				Orientation.UP , Orientation.RIGHT, Orientation.LEFT});
		swordHitAnimations=RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites,false);
		
		
		sprites = RPGSprite.extractSprites("zelda/player.bow",
				4, 2, 2,
				this , 32, 32, new Vector(-0.5f, 0),new Orientation[] {Orientation.DOWN ,
				Orientation.UP , Orientation.RIGHT, Orientation.LEFT});
		bowHitAnimations=RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites,false);
		
		
		inventory= new ARPGInventory(1000,this);
		inventory.addMoney(10);
		inventory.addItem(ARPGItem.ARROW,10);
		inventory.addItem(ARPGItem.BOMB,3);
		inventory.addItem(ARPGItem.BOW);
		inventory.addItem(ARPGItem.STAFF);
		inventory.addItem(ARPGItem.SWORD);
		status=new ARPGPlayerStatusGUI();
		status.setItem(ARPGItem.SWORD);
		currentState = State.IDLE;
		isInventoryShown=false;
		isInShop=false; 
		wantsToBuy=false;
		wantsToSell=false; 
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		wantsToBuy=false; 
		wantsToSell=false; 
		//System.out.println(getCurrentMainCellCoordinates());
		updateItem();
		status.setHealth(health);
		status.setMoney(inventory.getMoney());
		
		manageKeyboardInputs(getOwnerArea().getKeyboard());
		
		switch(currentState) {
		case IDLE:
			currentAnimation=idleAnimations[this.getOrientation().ordinal()];
			if(isDisplacementOccurs()) {
				currentAnimation.update(deltaTime);
				
			}else {
				currentAnimation.reset();
			}
			break;
		case SwordAttack:
			currentAnimation=swordHitAnimations[this.getOrientation().ordinal()];
			if(currentAnimation.isCompleted()) {
				wantsViewInteraction=true;
				currentAnimation.reset();
				currentState=State.IDLE;
			}
			break;
		case BowAttack:
			currentAnimation= bowHitAnimations[this.getOrientation().ordinal()];
			if(currentAnimation.isCompleted()) {
				if(inventory.contains(ARPGItem.ARROW))
				if(throwArrow()) {
					inventory.removeItem(ARPGItem.ARROW);					
				}
				currentAnimation.reset();
				currentState=State.IDLE;
			}
			break;
		case StaffAttack:
			currentAnimation= staffHitAnimations[this.getOrientation().ordinal()];
			if(currentAnimation.isCompleted()) {
				castWaterSpell();
				currentAnimation.reset();
				currentState=State.IDLE;
			}
			break;
		}
		if(currentState!=State.IDLE)
			currentAnimation.update(deltaTime);
		
		

		
	}
	
	
	
	
	
	private void moveOrientate(Button button, Orientation orientation) {
		if(button.isDown()) {
			if(this.getOrientation()==orientation) {
				this.move(ANIMATION_DURATION);
			}else {
				this.orientate(orientation);
			}			
		}
	}
	
	
	
	private void manageKeyboardInputs(Keyboard keyboard) {
		wantsToBuy=false; 
		if(currentState==State.IDLE) {
			moveOrientate(keyboard.get(Keyboard.UP),Orientation.UP);
			moveOrientate(keyboard.get(Keyboard.DOWN),Orientation.DOWN);
			moveOrientate(keyboard.get(Keyboard.LEFT),Orientation.LEFT);
			moveOrientate(keyboard.get(Keyboard.RIGHT),Orientation.RIGHT);
			
		}
		
		wantsViewInteraction=false;//Rappel a zero TODO: impelemnt it better
		
		
		Button keyTab = keyboard.get(Keyboard.TAB) ;
		if(keyTab.isReleased()) {
			if((keyboard.get(Keyboard.SHIFT)).isDown()==true)
				itemIndex++;
			else
				itemIndex--;
		}
		
		Button keySpace = keyboard.get(Keyboard.SPACE) ;
		if(keySpace.isReleased()) {
			useItem();
		}
		
		Button keyO = keyboard.get(Keyboard.O);
		if(keyO.isReleased()) {
			showInventory(!isInventoryShown);
		}
		
		Button keyE = keyboard.get(Keyboard.E);
		if(keyE.isReleased()) {
			isInShop = true; 
		}
		
		Button keyESC = keyboard.get(Keyboard.ESC);
		if(keyESC.isReleased()) {
			isInShop = false; 
		}
		if(isInShop) {
				Button keyB = keyboard.get(Keyboard.B);
				if(keyB.isReleased()) {
					wantsToBuy = true; 
				}
				
				Button keyS = keyboard.get(Keyboard.S);
				if(keyS.isReleased()) {
					wantsToSell = true; 
					
				}
		}
		
		
	}
	@Override
    public void showInventory(boolean bool) {
		isInventoryShown = bool; 
	//	isInShop = inShop;
		
	}
	
	
	public boolean getIsInShop() {
		return isInShop; 
	}
    /**
     * Max health getter
     * @return (List of DiscreteCoordinates). The Player's Maximum Health
     */
	public static float getMaxHealth() {
		return MAX_HEALTH;
	}
	
	
    /** Add the health amount to the ARPGPlayer
     * @param: amount (float): The damage amount: Can be positive for adding health, negative for removing
     */
	protected void addHealth(float amount) {
		health+=amount;
		//Clamping health between max and min value
		if(health>MAX_HEALTH)
			health=MAX_HEALTH;
		if(health<0)
			health=0;
	}


	private void useItem() {
		switch(currentItem) {
		case BOMB:
			if(placeBomb()) {
				inventory.removeItem(ARPGItem.BOMB);
			}
			break;
		case SWORD:
			currentState=State.SwordAttack;
			break;
		case CASTLEKEY:
			wantsViewInteraction=true;
			System.out.println("Hmmm.. this must open a door..");
			break;
		case BOW:
			currentState=State.BowAttack;
			break;
		case ARROW:
			break;
		case STAFF:
			currentState=State.StaffAttack;
			break;
		}
		
	}
	
	
    /**
     * Instanciates Arrow in the field view 
     */
	protected boolean throwArrow() {
		Arrow arrow =new Arrow(getOwnerArea(),getOrientation(),getFieldOfViewCells().get(0));
		return ((ARPGArea)getOwnerArea()).canEnterAreaCells(arrow,((Interactable)arrow).getCurrentCells())&&getOwnerArea().registerActor(arrow);
	}
	
	/**
     * Instanciates MagicWaterProjectile in the field view 
     */
	protected boolean castWaterSpell() {
		MagicWaterProjectile water =new MagicWaterProjectile(getOwnerArea(),getOrientation(),getFieldOfViewCells().get(0));
		return ((ARPGArea)getOwnerArea()).canEnterAreaCells(water,((Interactable)water).getCurrentCells())&&getOwnerArea().registerActor(water);
	}
	
	
	/**
     * Instanciates Bomb in the field view 
     */
	private boolean placeBomb() {
		Bomb bomb =new Bomb(getOwnerArea(),getFieldOfViewCells().get(0));
		return ((ARPGArea)getOwnerArea()).canEnterAreaCells(bomb,((Interactable)bomb).getCurrentCells())&&getOwnerArea().registerActor(bomb);
	}

	private void updateItem() {
		
		//Update the currentItem field
		currentItem = inventory.getItem(Math.abs(itemIndex));
		
		//Update the GUI
		status.setItem(currentItem);
		
		//Update the inventory
		inventory.setCurrentItem(currentItem); 
	}

	

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList
				(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return wantsViewInteraction;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}


	@Override
	public void draw(Canvas canvas) {
		if(isInventoryShown) {
			//System.out.println("show inventory");
			this.inventory.draw(canvas);
		}
		currentAnimation.draw(canvas);
		status.draw(canvas);
		
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ARPGPlayer";
	}


	@Override
	public boolean sell (InventoryItem item) {
		if(!inventory.contains(item))
			return false ; 
		else {
		    inventory.removeItem(item);
		    inventory.addMoney(item.getPrice());
		    return true;
		}
	}
	
	@Override 
	public boolean  buy(InventoryItem item) {
		if(inventory.getMoney()<item.getPrice()) {
			return false ; 
		}
		else {
			inventory.addItem(item); 
			inventory.addMoney(-item.getPrice());
			return true; 
		}
	}
	public boolean getWantsToBuy() {
		return wantsToBuy;
	}
	
	public boolean getWantsToSell() {
		return wantsToSell;
	}
	public ARPGItem getCurrentItem() {
		return currentItem; 
	}

}
