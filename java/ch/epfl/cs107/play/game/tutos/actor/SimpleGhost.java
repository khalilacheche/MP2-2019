package ch.epfl.cs107.play.game.tutos.actor;
import java.awt.Color;

import ch.epfl.cs107.play.game.actor.*;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.areagame.actor.*;

public class SimpleGhost extends Entity {
	
	Sprite sprite;
	float hp;
	float moveSpeed=0.5f;
	
	private TextGraphics hpText;
	
	public SimpleGhost(Vector position, String spriteName) {
		super(position);
		
		this.sprite = new Sprite(spriteName,1,1.f,this);
		this.hp=10;
		this.hpText= new TextGraphics (Integer.toString((int)hp), 0.4f, Color.BLUE);
		this.hpText.setParent(this);
		this.hpText.setAnchor(new Vector(-0.3f,0.1f));
		
	}

	@Override
	public void draw(Canvas canvas) {
		this.sprite.draw(canvas);
		this.hpText.draw(canvas);
		
	}
	
	
	@Override
	public void update(float deltaTime) {
		this.hp -=deltaTime;
		this.hpText.setText(Integer.toString((int)hp));
	}

	public void moveUp() {

		setCurrentPosition(getPosition().add(0.f,moveSpeed));
	}
	public void moveDown() {
		setCurrentPosition(getPosition().add(0.f,-moveSpeed));
	}
	public void moveRight() {
		setCurrentPosition(getPosition().add(moveSpeed,0.f));
	}
	public void moveLeft() {
		setCurrentPosition(getPosition().add(-moveSpeed,0.f));
	}
	
	
	
	
	
	public boolean isWeak() {
		return hp<=0;
	}
	public void strengthen() {
		hp +=10;
	}

}
