package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.rpg.InventoryItem;



/**  Action RPG Inventory Item
 */

public enum ARPGItem implements InventoryItem {
	
	CASTLEKEY("key",0,0,"goldKey"),
	ARROW("arrow",2,10,"arrow.icon"),
	STAFF("staff_water",3,50,"staff_water.icon"),
	BOMB("bomb",5,10,"bomb"),
	BOW("bow",2,10,"bow.icon"),
	SWORD("sword",4,10,"sword.icon"),
	CHESTKEY("key",0,0,"key");

	String name;
	float weight;
	int price;
	String spriteName;
	
	ARPGItem(String n,float w, int p, String spn){
		name=n;
		weight=w;
		price=p;	
		spriteName="zelda/"+spn;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return weight;
	}

	@Override
	public int getPrice() {
		// TODO Auto-generated method stub
		return price;
	}
	
	public String getSpriteName() {
		return spriteName;
	} 
	
}
