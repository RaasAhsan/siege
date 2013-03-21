package com.jantox.siege.entities;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jantox.siege.colsys.AABB;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;

public class Gate extends Living {

	public Gate(Vector2D pos) {
		super(pos);
		
		this.colmask = new AABB(this.pos, 32,32);
		this.health = 0;
		this.maxhealth = 10000;
		
		try {
			this.sprite = new Sprite(32, 64, ImageIO.read(this.getClass().getResourceAsStream("/res/gate.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if(health <= 0) {
			sprite.setAnimation(1, 1, 0);
		} else if(health >= maxhealth) {
			sprite.setAnimation(0, 0, 0);
		}
		sprite.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 52), true);
		
		if(health != maxhealth) {
			renderer.setColor(Color.RED);
			renderer.fill(new Rectangle(pos.getX() - 9, pos.getY() - 50, 10000 / 500, 4), true);
			renderer.setColor(Color.GREEN);
			renderer.fill(new Rectangle(pos.getX() - 9, pos.getY() - 50, health / 500, 4), true);
		}
	}

	@Override
	public void handleCollision(Entity e) {
		
	}
	
	public boolean isBroken() {
		return health <= 0;
	}

	@Override
	public void drop(int chance) {
		
	}

}
