package com.jantox.siege.entities;

import com.jantox.siege.colsys.Circle;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Tree extends Resource {

	public Tree(Vector2D pos) {
		super(pos, 3, 1500);
		
		this.colmask = new Circle(this.pos, 7);
		
		this.sprite = Assets.loadSprite("tree.png");
		
		status = 1;
		sprite.setAnimation(status, status, 0);
		sprite.update();
	}

	@Override
	public void update() {
		super.update();
		
		sprite.setAnimation(status, status, 0);
		
		sprite.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 17, pos.y - 54), true);
	}

	@Override
	public void handleCollision(Entity e) {
		
	}

}
