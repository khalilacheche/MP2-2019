package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;;

public class ARPGPlayerStatusGUI implements Graphics {

	
	int money;
	float health;
	ARPGItem item;
	RegionOfInterest[] digitsRoi;
	 
	
	
	private static final float DEPTH = 1001;

	@Override
	public void draw(Canvas canvas) {

		drawGearDisplay(canvas);
		drawHealthBar(canvas);
		drawCoinDisplay(canvas);
	}
	
	
	void drawCoinDisplay(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector(width/2, height/2));
		ImageGraphics coinDisplay = new ImageGraphics(
				ResourcePath.getSprite("zelda/coinsDisplay"),
				3f, 1.25f, new RegionOfInterest(0, 0, 64, 32),
				anchor.add(new Vector(0, 0.f)), 1, DEPTH);
		
		coinDisplay.draw(canvas);
		ImageGraphics coins;
		int digit=money;
		for(int i=2;i>=0;--i) {
			coins= new ImageGraphics(
					ResourcePath.getSprite("zelda/digits"),
					0.75f, 0.75f, digitsRoi[digit%10],
					anchor.add(new Vector(1.1f+i*0.5f, 0.25f)), 1, DEPTH);
			coins.draw(canvas);
			digit=digit/10;
		}
	}
	
	
	void drawGearDisplay(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector(width/2, height/2));
		
		ImageGraphics gearDisplay = new ImageGraphics(
				ResourcePath.getSprite("zelda/gearDisplay"),
				1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32),
				anchor.add(new Vector(0, height - 1.75f)), 1, DEPTH);
		
		gearDisplay.draw(canvas);
		
		ImageGraphics gear;
		if(item==ARPGItem.BOMB)
			gear= new ImageGraphics(
					ResourcePath.getSprite(item.getSpriteName()),
					0.75f, 0.75f, new RegionOfInterest(0, 0, 16, 16),
					anchor.add(new Vector(0.4f, height - 1.40f)), 1, DEPTH);
		else
			gear= new ImageGraphics(
					ResourcePath.getSprite(item.getSpriteName()),
					0.75f, 0.75f, new RegionOfInterest(0, 0, 32, 32),
					anchor.add(new Vector(0.4f, height - 1.40f)), 1, DEPTH);
			
		
		gear.draw(canvas);
		
	}
	void drawHealthBar(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector(width/2, height/2));
		ImageGraphics heart;
		int q=0;
		
		for(int i=1;i<=ARPGPlayer.getMaxHealth();++i) {
			
			if(i<=health) {
				heart= new ImageGraphics(
						ResourcePath.getSprite("zelda/heartDisplay"),
						0.75f, 0.75f, new RegionOfInterest(32, 0, 16, 16),
						anchor.add(new Vector(1f+0.7f*i, height - 1.40f)), 1, DEPTH);
				q=i;
			}else {
				if(i==q+1&& health-q>0.5f && health>0) {
					heart= new ImageGraphics(
							ResourcePath.getSprite("zelda/heartDisplay"),
							0.75f, 0.75f, new RegionOfInterest(16, 0, 16, 16),
							anchor.add(new Vector(1f+0.7f*i, height - 1.40f)), 1, DEPTH);
				}else {
					heart= new ImageGraphics(
							ResourcePath.getSprite("zelda/heartDisplay"),
							0.75f, 0.75f, new RegionOfInterest(0, 0, 16, 16),
							anchor.add(new Vector(1f+0.7f*i, height - 1.40f)), 1, DEPTH);
				}
			}
			heart.draw(canvas);
		}
		
		
	}
	
	public void setItem(ARPGItem item) {
		this.item = item;
	}
	public void setHealth(float health) {
		this.health=health;
	}
	public void setMoney(int money) {
		this.money=money;
	}
	
	public ARPGPlayerStatusGUI(){
		digitsRoi = new RegionOfInterest[10];
		int index=1;
		digitsRoi[0]= new RegionOfInterest(16, 32, 16, 16);
		for(int y =0;y<3;++y) {
			for(int x=0;x<4;++x) {
				if(index>9)
					return;
				digitsRoi[index]= new RegionOfInterest(16*x, 16*y, 16, 16);
				++index;
			}
		}
	}



}