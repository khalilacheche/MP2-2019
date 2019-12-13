package ch.epfl.cs107.play.game.arpg;

import java.util.Map.Entry;
import java.awt.Color;
import java.util.Set;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
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
	
    /**
     * Default ARPGInventory constructor
     * @param capacity (float): The inventory's maximum weight
     */
	public ARPGInventory(float capacity) {
		super(capacity);
		moneyAmount=0;
	}
	
	public void setCurrentItem(ARPGItem item) {
		this.item=item; 
	}
	
	
    /** Add the money amount to the inventory
     * @param: amount (float): The money amount: Can be postive for adding money, negative for removing
     */
	public void addMoney(int amount) {
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
		super.draw(canvas);
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector(width/2, height/2));

		for(int i=0;i<8;++i) {
			slotsBackground[i] =slotBackground; 
			
			//slotsBackground.setParent(inventoryBackground);
			slotsBackground[i].setHeight(height/6);
			slotsBackground[i].setWidth(width/6);
			slotsBackground[i].setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11,(i/4)*(height/4)+height/4)));
			slotsBackground[i].setDepth(1004);
			slotsBackground[i].draw(canvas);
			
			}
		int i =7;
		for(InventoryItem inventoryItem : items.keySet()) {
			if(((ARPGItem)inventoryItem)==item ) {
				slotsBackground[i] = selector;
				slotsBackground[i].setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11,(i/4)*(height/4)+height/4)));
				slotsBackground[i].setHeight(height/6);
				slotsBackground[i].setWidth(width/6);
				slotsBackground[i].setDepth(1004);
				slotsBackground[i].draw(canvas);
			}
					
			quantity =  text;
			quantity.setText("x"+items.get(inventoryItem));
			quantity.setFontSize(width/32);
			quantity.setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11+width/16, (i/4)*(height/4)+height/4)));
			if(inventoryItem== ARPGItem.BOMB) {
				itemGraphics= bomb;
				itemGraphics.setWidth(width/16);
				itemGraphics.setHeight(height/16);
				itemGraphics.setAnchor(anchor.add(new Vector(((i%4)+1)*(width/5)-width/11+width/23, (i/4)*(height/4)+height/4+height/20)));
			    }
			else {
				itemGraphics= new ImageGraphics(
					ResourcePath.getSprite(((ARPGItem)inventoryItem).getSpriteName()),
						width/16, height/16, new RegionOfInterest(0, 0, 32, 32),
						anchor.add(new Vector(((i%4)+1)*(width/5)-width/11+width/23, (i/4)*(height/4)+height/4+height/20)), 1, 1005);
				}
			quantity.setDepth(1004);
			quantity.draw(canvas);
			itemGraphics.draw(canvas);
		 --i;
		}
		
	}
	
	
	
}