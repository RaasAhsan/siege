package com.jantox.dungmast.entities;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.colsys.AABB;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Fence extends Entity {

	public Fence(Vector2D pos, int anim) {
		super(pos);
		
		this.sprite = Assets.loadSprite("fence.png");
		
		if(anim == 0) { 
			this.colmask = new AABB(this.pos, 32, 12);
		} else if(anim == 1) {
			this.colmask = new AABB(this.pos, 12, 32);
		} else {
			this.colmask = new AABB(this.pos, 32, 32);
		}
		
		sprite.setAnimation(anim, anim,0);
		sprite.update();
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 32, pos.y - 44), true);
	}

	@Override
	public void handleCollision(Entity e) {
		
	}

}
