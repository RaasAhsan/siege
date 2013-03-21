package com.jantox.siege.entities;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.jantox.siege.Item;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;

public class Log extends Entity implements Item {
	
	public Log(Vector2D pos) {
		super(pos);
		
		this.colmask = new Circle(this.pos, 6);
		this.requestCollisions(entity_type.PLAYER);
		
		try {
			this.sprite = new Sprite(32, 32, ImageIO.read(this.getClass().getResourceAsStream("/res/log.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		sprite.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16), true);
	}
	
	@Override
	public void handleCollision(Entity e) {
		Entity.map.getPlayer().pickup(this);
	}

	@Override
	public int onItemUse() {
		
		
		return 1;
	}

	@Override
	public void onHeld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotHeld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRest() {
		return 5;
	}

}
