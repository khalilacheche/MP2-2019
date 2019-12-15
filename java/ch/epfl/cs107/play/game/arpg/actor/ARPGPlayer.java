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
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.ARPGPlayerStatusGUI;
import ch.epfl.cs107.play.game.arpg.DeathScreenGUI;
import ch.epfl.cs107.play.game.arpg.WinScreenGUI;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.game.rpg.Inventory;;

public class ARPGPlayer extends Player implements Inventory.Holder{
	
	
	private enum InteractionType{
		SWORD,
		IDLE,
		USEKEY,
		TALK;
	}
	private enum State{
		IDLE,
		SwordAttack,
		BowAttack,
		StaffAttack;
	}
	
	private final static int ANIMATION_DURATION =4;
	private final static float MAX_HEALTH=6;
	
	private Animation[] idleAnimations;
	private Animation[] swordHitAnimations;
	private Animation[] bowHitAnimations;
	private Animation[] staffHitAnimations;
	private Animation currentAnimation;
	
	private ARPGPlayerStatusGUI status;
	private DeathScreenGUI deathScreen; 
	private WinScreenGUI winScreen;
	private ARPGInventory inventory;
	private ARPGPlayerHandler handler;
	
	private Dialog dialog;
	private InteractionType currentInteraction;
	private ARPGItem currentItem;
	private State currentState;
	private Logic signal;


	private boolean wantsViewInteraction;
	private boolean isInventoryShown;
	private boolean wantsToSell;
	private boolean wantsToBuy;
	private boolean isTalking;
	private boolean isInShop;
	private boolean canMove;	

	private int itemIndex=0;
	private float health;
	
	public boolean wantsRestart=false; 
	public boolean responded=false;
	
	private class ARPGPlayerHandler implements ARPGInteractionVisitor{

		@Override
		public void interactWith(CollectableAreaEntity entity) {
			if(entity instanceof Heart)
				addHealth(1);
			if(entity instanceof Coin)
				inventory.addMoney(50);
			if(entity instanceof CastleKey) {
				inventory.addItem(((CastleKey)entity).getItem());
				startDialog("found_castle_key");
			}
			if(entity instanceof ChestKey) {
				inventory.addItem(((ChestKey)entity).getItem());
				startDialog("found_chest_key");
			}
			if(entity instanceof Staff) {
				startDialog("found_staff");
				inventory.addItem(((Staff)entity).item);
			}
			getOwnerArea().unregisterActor(entity);
		}

		@Override
		public void interactWith(CastleDoor door) {
			if(currentInteraction == InteractionType.USEKEY) {
				if(currentItem==ARPGItem.CASTLEKEY) {
					door.setSignal(Logic.TRUE);
				}else if (currentItem==ARPGItem.CHESTKEY) {
					startDialog("wrong_key");
				}
			}else if(currentInteraction == InteractionType.SWORD) {
				startDialog("find_key");				
			}else {
				if(door.isOpen()){
					setIsPassingADoor(door);
					door.setSignal(Logic.FALSE);
					}
			}
		}
		
		@Override
		public void interactWith(Door door) {
			if(currentInteraction == InteractionType.IDLE)
				setIsPassingADoor(door);
		}
		
		@Override
		public void interactWith(CaveDoor door) {
			if(currentInteraction == InteractionType.IDLE)
				setIsPassingADoor(door);
		}
		
		@Override
		public void interactWith(Grass grass) {
			if(currentInteraction==InteractionType.SWORD)
				grass.cut();
		}
		
		@Override
		public void interactWith(ARPGMonster monster) {
			if(currentInteraction == InteractionType.SWORD)
				monster.receiveAttack(ARPGMonster.ARPGAttackType.PHYSICAL, 1);
		}
		
		@Override
		public void interactWith(Chest chest) {
			if(currentInteraction == InteractionType.USEKEY) {
				if(currentItem==ARPGItem.CHESTKEY) {
					startDialog("found_bow");
					addItem(chest.takeContent());
					inventory.removeItem(ARPGItem.CHESTKEY);
				}else{
					startDialog("wrong_key");					
				}
				
			}else
				startDialog("find_key");
		}
		
		@Override
		public void interactWith(Villager villager) {
			if(currentInteraction == InteractionType.TALK) {
				if(!isTalking)
					startDialog(villager.getKey());
				
			}else if ( currentInteraction == InteractionType.SWORD) {
				if(!isTalking)
					startDialog("ouch");
			
			}else if (currentInteraction == InteractionType.USEKEY) {
				if(!isTalking)
					startDialog(currentItem==ARPGItem.CASTLEKEY? "found_castle_key":"found_chest_key");
			}
		}	
		
		@Override
		public void interactWith(DialogTrigger trigger) {
			startDialog(trigger.getDialogKey());
			getOwnerArea().unregisterActor(trigger);
		}
		
		@Override
		public void interactWith(ShopMan shop) {
			if(currentInteraction==InteractionType.TALK) {
				isInShop=true;
			}
			wantsViewInteraction=false;	
		}
		
		@Override
		public void interactWith(King king) {
			signal = king;
			if(currentInteraction==InteractionType.TALK)
				if(!isTalking)
					startDialog(king.getKey());
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
		inventory.addMoney(1000);
		//inventory.addItem(ARPGItem.ARROW,10);
		//inventory.addItem(ARPGItem.BOMB,3);
		//inventory.addItem(ARPGItem.STAFF);
		//inventory.addItem(ARPGItem.BOW);
		inventory.addItem(ARPGItem.CASTLEKEY);
		inventory.addItem(ARPGItem.SWORD);
		deathScreen= new DeathScreenGUI();
		winScreen=new WinScreenGUI();
		status=new ARPGPlayerStatusGUI();
		status.setItem(ARPGItem.SWORD);
		currentAnimation=idleAnimations[getOrientation().ordinal()];
		currentState = State.IDLE;
		isInventoryShown=false;
		wantsToSell=false;		
		wantsToBuy=false;
		isTalking=false;
		isInShop=false; 
		canMove=true;
		signal = Logic.FALSE;
		dialog = new Dialog(XMLTexts.getText("empty"),"zelda/dialog",getOwnerArea());
	}
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		wantsToBuy=false; 
		wantsToSell=false; 
		resetViewInteraction();
		canMove=canMove&&!isInShop&&!isTalking;
		
		//System.out.println(getCurrentMainCellCoordinates());
		updateItem();
		status.setHealth(health);
		status.setMoney(inventory.getMoney());
		
		currentAnimation.update(deltaTime);
		handlePlayerState(deltaTime);
		

		manageKeyboardInputs(getOwnerArea().getKeyboard());
		


		
	}
	@Override
	public void draw(Canvas canvas) {
		if(isInventoryShown) {
			this.inventory.draw(canvas);
		}
		if(isDead())
			this.deathScreen.draw(canvas);
		currentAnimation.draw(canvas);
		status.draw(canvas);
		if(isTalking)
			dialog.draw(canvas);
		if(hasWon()) {
			winScreen.draw(canvas);
		}
		
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////	

//////////////////////////////////////GETTERS////////////////////////////////////////////////////////////////////////	
	
    /**
     * Max health getter
     * @return (List of DiscreteCoordinates). The Player's Maximum Health
     */
	public static float getMaxHealth() {
		return MAX_HEALTH;
	}
	
	protected boolean hasWon() {
		return signal.isOn();
	}
	
	public boolean getIsInShop() {
		return isInShop; 
	}
	
	@Override
	public String getName() {
		return "ARPGPlayer";
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
	
	protected boolean isTalking() {
		return isTalking;
	}
	
	public boolean  isDead() {
		return health<=0; 
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
/////////////////////////////// ACTING FUNCTIONS /////////////////////////////////////////////////////////////////	
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
			currentInteraction = InteractionType.SWORD;
			currentState=State.SwordAttack;
			wantsViewInteraction=true;
			break;
		case CASTLEKEY:
		case CHESTKEY:
			wantsViewInteraction=true;
			currentInteraction = InteractionType.USEKEY;
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
	
	protected boolean addItem(ARPGItem item) {
		return inventory.addItem(item);
	}
	



	@Override
	public boolean sell (InventoryItem item) {
		if(((ARPGItem)item)==ARPGItem.SWORD)
			return false; 
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
		if(item==null)
			return false; 
		if(inventory.getMoney()<item.getPrice()) {
			return false ; 
		}
		else {
			inventory.addItem(item); 
			inventory.addMoney(-item.getPrice());
			return true; 
		}
	}

	
	protected void startDialog(String key) {
		canMove=false;
		isTalking=true;
		dialog.resetDialog(XMLTexts.getText(key));
	}
	
	

    /**
     * Instanciates Arrow in the field view 
     */
	protected boolean throwArrow() {
		Arrow arrow =new Arrow(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		return ((ARPGArea)getOwnerArea()).canEnterAreaCells(arrow,((Interactable)arrow).getCurrentCells())&&getOwnerArea().registerActor(arrow);
	}
	
	/**
     * Instanciates MagicWaterProjectile in the field view 
     */
	protected boolean castWaterSpell() {
		MagicWaterProjectile water =new MagicWaterProjectile(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		return ((ARPGArea)getOwnerArea()).canEnterAreaCells(water,((Interactable)water).getCurrentCells())&&getOwnerArea().registerActor(water);
	}
	
	
	/**
     * Instanciates Bomb in the field view 
     */
	private boolean placeBomb() {
		Bomb bomb =new Bomb(getOwnerArea(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
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
	
	private void handlePlayerState(float deltaTime) {
		switch(currentState) {
		case IDLE:
			currentInteraction = InteractionType.IDLE;
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
	
	private void resetViewInteraction() {
		wantsViewInteraction=false;
	}
	
	
	
	private void manageKeyboardInputs(Keyboard keyboard) {
		if(currentState==State.IDLE&&canMove) {
			moveOrientate(keyboard.get(Keyboard.UP),Orientation.UP);
			moveOrientate(keyboard.get(Keyboard.DOWN),Orientation.DOWN);
			moveOrientate(keyboard.get(Keyboard.LEFT),Orientation.LEFT);
			moveOrientate(keyboard.get(Keyboard.RIGHT),Orientation.RIGHT);
		}
		
		if(keyboard.get(Keyboard.TAB).isReleased()) {
			if((keyboard.get(Keyboard.SHIFT)).isDown()==true)
				itemIndex++;
			else
				itemIndex--;
		}
		if(keyboard.get(Keyboard.T).isReleased()) {
			wantsViewInteraction=true;
			currentInteraction = InteractionType.TALK;
		}
		if(keyboard.get(Keyboard.SPACE).isReleased()) {
			useItem();
		}
		if(keyboard.get(Keyboard.I).isReleased()) {
			showInventory(!isInventoryShown);
		}
		
		
		if(isInShop) {
			if(keyboard.get(Keyboard.ESC).isReleased()) {
				isInShop = false;
				canMove=true;
			}
			if(keyboard.get(Keyboard.B).isReleased()) {
				wantsToBuy = true; 
			}
			
			if(keyboard.get(Keyboard.S).isReleased()) {
				wantsToSell = true; 
				
			}
		}
		if(isTalking) {
			if(keyboard.get(Keyboard.ENTER).isReleased()) {
				if(dialog.push()) {
					isTalking=false;
					canMove=true;
				}
			}
		}
		
		restartGameHandler(); 
		
				
	}
	
	private void restartGameHandler() {
		if(isDead()|| hasWon()) {
			if(this.getOwnerArea().getKeyboard().get(Keyboard.R).isReleased()) {
                wantsRestart=true; 
                responded=true; 
			}
			if(this.getOwnerArea().getKeyboard().get(Keyboard.N).isReleased()) {
				wantsRestart=false; 
				responded=true; 
			}
		}
	}
	
	@Override
    public void showInventory(boolean bool) {
		isInventoryShown = bool;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
//////////////////////////////// Interactable / Interactor ////////////////////////////////////////////////////////////////	

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return 	Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////


	
	



}
