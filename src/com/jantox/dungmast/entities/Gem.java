package com.jantox.dungmast.entities;

import com.jantox.dungmast.DungeonGame;
import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Gem extends Entity {
	
	int life = 0;
	int type;

	public Gem(Vector2D pos) {
		super(pos);
		
		sprite = Assets.loadSprite("gems.png");
		
		type = rand.nextInt(3);
		sprite.setAnimation(type * 4, type * 4 + 3, 7);
		for(int i = 0; i < 7; i++)
			sprite.update();
	}

	@Override
	public void handleCollision(Entity e) {
		
	}
	
	public void update() {
		sprite.update();
		
		Player p = map.getPlayer();
		if(this.distanceSquared(p) <= 50 * 50) {
			if(distanceSquared(p) <= 10) {
				this.expired = true;
				if(type == 0) {
					DungeonGame.coins ++;
				} else if(type == 1) {
					DungeonGame.coins += 5;
				} else if(type == 2) {
					DungeonGame.coins += 10;
				}
			}
			
			Vector2D vel = new Vector2D(p.pos.x - pos.x, p.pos.y - pos.y);
			vel.normalize();
			vel.multiply(2);
			pos.add(vel);
		}
		
		life++;
		if(life >= 1500)
			this.expired = true;
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16), true);
	}

	
	
}
