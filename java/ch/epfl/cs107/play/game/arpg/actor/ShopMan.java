package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;

import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ShopMan extends AreaEntity implements Interactor, Inventory.Holder {
    private ARPGInventory inventory;
    private Sprite sprite;
    private ShopManHandler handler;
    private boolean isInventoryShown;
    private int itemIndex = 0;
    private ARPGItem currentItem;
    private Dialog dialog;
    private boolean showDialog;


    public ShopMan(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new RPGSprite("assistant.fixed", 1.f, 2.f, this, new RegionOfInterest(0, 0, 16, 32),new Vector(0,1));
        inventory = new ARPGInventory(1000, this);
        inventory.addMoney(10000);
        inventory.addItem(ARPGItem.ARROW, 10);
        inventory.addItem(ARPGItem.BOMB, 3);
        isInventoryShown = false;
        handler = new ShopManHandler();
        dialog = new Dialog(XMLTexts.getText("sell_text_1"), "zelda/dialog", getOwnerArea());
        showDialog = false;


    }

    @Override
    public void showInventory(boolean bool) {
        isInventoryShown = bool;


    }

    private void updateItem() {

        //Update the currentItem field
        currentItem = inventory.getItem(Math.abs(itemIndex));

        //Update the inventory
        inventory.setCurrentItem(currentItem);
    }
    @Override
    public List < DiscreteCoordinates > getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    @Override
    public boolean sell(InventoryItem item) {
        if (!inventory.contains(item))
            return false;
        else {
            inventory.removeItem(item);
            inventory.addMoney(item.getPrice());
            return true;
        }
    }

    @Override
    public boolean buy(InventoryItem item) {
        if (inventory.getMoney() < item.getPrice()) {
            return false;
        } else {
            inventory.addItem(item);
            inventory.addMoney(-item.getPrice());
            return true;
        }
    }



    @Override
    public boolean takeCellSpace() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if (isInventoryShown) {
            inventory.draw(canvas);
        }
        if (showDialog) {
            dialog.draw(canvas);
        }

    }

    @Override
    public void update(float deltaTime) {
        updateItem();
        Keyboard keyboard = this.getOwnerArea().getKeyboard();
        Button keyA = keyboard.get(Keyboard.A);
        if (keyA.isReleased()) {
            if ((keyboard.get(Keyboard.SHIFT)).isDown() == true)
                itemIndex++;
            else
                itemIndex--;
        }
        showDialog = false;

        super.update(deltaTime);
    }

    @Override
    public List < DiscreteCoordinates > getFieldOfViewCells() {
        // TODO Auto-generated method stub
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    private class ShopManHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            if (player.getIsInShop()) {
                showDialog = false;
                showInventory(true);
                player.showInventory(true);

                if (player.getWantsToBuy()) {
                    if (player.buy(currentItem)) {
                        sell(currentItem);
                    }

                }
                if (player.getWantsToSell()) {
                    if (buy(player.getCurrentItem()))
                        player.sell(player.getCurrentItem());

                }

            } else {
                showInventory(false);
                player.showInventory(false);
            }

        }
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "ShopMan";
    }

}