package ch.epfl.cs107.play.game.rpg;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.window.Canvas;



/**
 * Inventory class
 *
 */
public class Inventory implements Graphics {
	
	/**
	 * Inventory.Holder implemented by inventory holder
	 *
	 */
	public interface Holder{
	     /**showInventory
	     * @param bool : inventory is show if true 
	     */
	    public void showInventory(boolean bool);
	     /**
	     * @return name of the holder
	     */
	    public String getName();
	     /** sell the given item
	     * @param item to sell 
	     * @return sell is completed or not 
	     */
	    public boolean sell(InventoryItem item); 
	     /**buy the given item
	     * @param item to buy
	     * @return buy is successful or not
	     */
	    public boolean  buy(InventoryItem item); 
	    
	    /**checks if Holder possess the item
	     * @param item to check
	     * @return doesPosessItem
	     */
	    public boolean possess(InventoryItem item);
	}
	
	protected Holder holder; 
	protected float capacity;
	protected float weight;
	protected Map<InventoryItem,Integer > items;
	
	
	
	
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
	 
	 
	 /** Checks if the inventory contains a specific item or not
	 * @param: item (InventoryItem): The item to check
	 * @return contains (boolean)  
	 */
	public boolean contains(InventoryItem item) {
		return items.containsKey(item);
	}
    /**
     * Default Inventory constructor
     * @param capacity (float): The inventory's maximum weight
     */
	protected Inventory(float capacity,Holder holder){
		this.capacity=capacity;
		this.weight=0;
		items= new HashMap<>();
		this.holder = holder;
	}

	@Override
	public void draw(Canvas canvas) {
		
		}

	
}