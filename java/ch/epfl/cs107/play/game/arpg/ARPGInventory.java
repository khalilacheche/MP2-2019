package ch.epfl.cs107.play.game.arpg;

import java.util.Map.Entry;
import java.util.Set;

import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;

public class ARPGInventory extends Inventory{
	private final int MAX_COINS=999;
	private int moneyAmount;
	
    /**
     * Default ARPGInventory constructor
     * @param capacity (float): The inventory's maximum weight
     */
	public ARPGInventory(float capacity) {
		super(capacity);
		moneyAmount=0;
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
	
	
	
}
