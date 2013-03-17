package com.jantox.dungmast.entities;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.Sprite;
import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.particles.TextParticle;

public class Chest extends Entity {
	
	public Chest(Vector2D pos) {
		super(pos);
		
		this.requestCollisions(entity_type.PLAYER);
		this.colmask = new Circle(pos, 4);
		
		try {
			this.sprite = new Sprite(32, 32, ImageIO.read(this.getClass().getResourceAsStream("/res/chest.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCollision(Entity e) {
		if(e instanceof Player) {
			this.expired = true;
			
			map.spawn(new TextParticle(120, pos.copy(), new Vector2D(0, -0.25), new Font("Lucida Console", Font.PLAIN, 11), Color.YELLOW, "+5 logs"));
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 26), true);
	}

}
