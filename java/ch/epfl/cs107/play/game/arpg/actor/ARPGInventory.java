package ch.epfl.cs107.play.game.arpg.actor;

import java.awt.Color;
import java.util.Map.Entry;
import java.util.Set;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGInventory extends Inventory{
	
	private final int MAX_COINS=999;
	private int moneyAmount;
	private ARPGItem item;
	private ImageGraphics itemGraphics;
	private TextGraphics quantity; 
	private ImageGraphics[] slotsBackground = new ImageGraphics[8];
	private ImageGraphics slotBackground =new ImageGraphics(
			ResourcePath.getSprite("zelda/inventory.slot"),
			1,1);
	private ImageGraphics selector = new ImageGraphics(
			ResourcePath.getSprite("zelda/inventory.selector"),
			1, 1,new RegionOfInterest(0,0,64,64));
	private ImageGraphics bomb = new ImageGraphics(
			ResourcePath.getSprite(ARPGItem.BOMB.getSpriteName()),
			1, 1, new RegionOfInterest(0, 0, 16, 16),
			new Vector(0,0), 1, 1005);
	private TextGraphics text = new TextGraphics("x",1,Color.BLACK);
	
    private TextGraphics textInventory = new TextGraphics("Inventory",1,Color.BLACK);

	
	private ImageGraphics inventoryBackground = new ImageGraphics(
			ResourcePath.getSprite("zelda/inventory.background"),
			1, 1);
	private TextGraphics textInstructions; 
	
    /**
     * Default ARPGInventory constructor
     * @param capacity (float): The inventory's maximum weight
     */
	public ARPGInventory(float capacity,Holder holder) {
		super(capacity,holder);
		moneyAmount=0;
		 
	}
	
	protected void setCurrentItem(ARPGItem item) {
		this.item=item; 
	}
	
	
		
	
	 /** Adds one InventoryItem to the inventory
     * @param: item (InventoryItem): The item to add
     * @return: success (boolean): Returns true if the element was successfully added to the inventory  
     */
	 protected boolean addItem(InventoryItem item) {
		 return addItem(item,1);
	}
	 
	 /** Adds InventoryItems to the inventory
	 * @param: item (InventoryItem): The item to add
	 * @param: quantity (int): The quantity to add
	 * @return: success (boolean): Returns true if the elements were successfully added to the inventory  
	 */
	 protected boolean addItem(InventoryItem item,int quantity) {
		 
		 int number = 0;
		 if(items.containsKey(item))
			 number=items.get(item);
		 
		if(item.getWeight()*quantity+weight<=capacity) {
			items.put(item,number +quantity);	
			return true;
		}else {
			System.out.println("Inventory too full!");
			return false;
		}
	}
	 /** Removes one InventoryItem from the inventory
	 * @param: item (InventoryItem): The item to remove
	 * @return: success (boolean): Returns true if the element was successfully removed from the inventory  
	 */
	 protected boolean removeItem(InventoryItem item) {
		 return removeItem(item,1);
	}
	 
	
	 
	 /** Removes InventoryItems from the inventory
	 * @param: item (InventoryItem): The item to remove
	 * @param: quantity (int): The quantity to remove
	 * @return: success (boolean): Returns true if the elements were successfully removed from the inventory  
	 */
	 protected boolean removeItem(InventoryItem item,int quantity) {
		if(!items.containsKey(item))
			return false;
		
		if(items.get(item)>quantity) {
			items.put(item, items.get(item)-1);
		}else {
			items.remove(item);
		}
		return true;
	}
	
    /** Add the money amount to the inventory
     * @param: amount (float): The money amount: Can be postive for adding money, negative for removing
     */
	protected void addMoney(int amount) {
		moneyAmount+=amount;
		if(moneyAmount>MAX_COINS) {
			moneyAmount=MAX_COINS;
		}
		if(moneyAmount <0)
			moneyAmount = 0;
	}
	
	
    /** Getter for the money field
     * @return: amount (int)
     */
	
	public int getMoney() {
		return moneyAmount;
	}
	
    /** Getter for the fortune
     * @return: amount (int): The total player's fortune value (inventory elements price * quantity + money)
     */
	public int getFortune() {
		int sum=moneyAmount;
		for(Entry<InventoryItem,Integer> item : items.entrySet() ) {
			sum+=item.getKey().getPrice()*item.getValue();
		}
		return sum;
	}
	
    /** Getter for the ARPGItem by index
     * @param: index (int): The index of the item we want to retrieve. Can be bigger than item count, Cannot be negative
     */
	public ARPGItem getItem(int index) {
		Set<InventoryItem> inventoryItems = items.keySet();
		Object[] intd = inventoryItems.toArray();
		return (ARPGItem)intd[index%intd.length];
	}
	
	@Override 
	public void draw(Canvas canvas) {
		//System.out.println(holder.getName());
		switch(holder.getName()) {
		case "ARPGPlayer":
				//super.draw(canvas);
			if(!((ARPGPlayer)holder).getIsInShop())
				drawInventory(canvas,null,"Inventory",1/3.65f,(float)4/5,1f); 
			else
				drawInventory(canvas,"TAB : switch items   S:sellItems","Inventory",1/3.65f,(float)4/5,-0.5f); 

			break; 
				
		case "ShopMan":
			drawInventory(canvas,"A : switch items   B:buyItems   ESC:quit","Shop Inventory",0.1f,(float)4/5,0.5f); 
			
		}
	}
	
	private void drawInventory(Canvas canvas,String input,  String inventoryText,float widthText, float heightText,float scale) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		float position = scale==1 ? height/2 : ((scale==0.5f) ?  0 : height/2-0.5f);
		if(scale== -0.5f) {
			scale = 0.5f;
		}
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector((width/2), position));
		inventoryBackground.setAnchor(anchor.add(new Vector(0, 1*scale)));
		inventoryBackground.setDepth(1003);
		inventoryBackground.setHeight((height-1)*scale);
		inventoryBackground.setWidth(width);
		inventoryBackground.draw(canvas);
		textInventory.setFontSize(width/9);
		textInventory.setText(inventoryText);
		textInventory.setAnchor(anchor.add(new Vector(width*widthText, (height*heightText)*scale)));
		textInventory.setDepth(1004);
		textInventory.draw(canvas);

		for(int i=0;i<8;++i) {
			slotsBackground[i] =slotBackground; 
			
			//slotsBackground.setParent(inventoryBackground);
			slotsBackground[i].setHeight((height/6)*scale);
			slotsBackground[i].setWidth(width/6);
			slotsBackground[i].setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11,((i/4)*(height/4)+height/4)*scale)));
			slotsBackground[i].setDepth(1004);
			slotsBackground[i].draw(canvas);
			
			}
		int i =7;
		for(InventoryItem inventoryItem : items.keySet()) {
			if(((ARPGItem)inventoryItem)==item ) {
				slotsBackground[i] = selector;
				slotsBackground[i].setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11,((i/4)*(height/4)+height/4)*scale)));
				slotsBackground[i].setHeight((height/6)*scale);
				slotsBackground[i].setWidth(width/6);
				slotsBackground[i].setDepth(1004);
				slotsBackground[i].draw(canvas);
			}
					
			quantity =  text;
			quantity.setText("x"+items.get(inventoryItem));
			quantity.setFontSize(width/32);
			quantity.setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11+width/16, ((i/4)*(height/4)+height/4)*scale)));
			if(inventoryItem== ARPGItem.BOMB) {
				itemGraphics= bomb;
				itemGraphics.setWidth(width/16);
				itemGraphics.setHeight((height/16)*scale);
				itemGraphics.setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11+width/23, ((i/4)*(height/4)+height/4+height/20)*scale)));
			    }
			else {
				itemGraphics= new ImageGraphics(
					ResourcePath.getSprite(((ARPGItem)inventoryItem).getSpriteName()),
						width/16, (height/16)*scale, new RegionOfInterest(0, 0, 32, 32),
						anchor.add(new Vector(((i%4)+1)*(width/5)-width/11+width/23, ((i/4)*(height/4)+height/4+height/20)*scale)), 1, 1005);
				}
			
			quantity.setDepth(1004);
			quantity.draw(canvas);
			itemGraphics.draw(canvas);
		 --i;
		}
		if(input==null)
			return;
		textInstructions = new TextGraphics(input,width/25,Color.black);
		textInstructions.setAnchor(anchor.add(new Vector(width/8, height/13)));
		textInstructions.setDepth(1004);
		textInstructions.draw(canvas);
	}

	
	
	
	
	
}