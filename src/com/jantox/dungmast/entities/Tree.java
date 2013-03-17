package com.jantox.dungmast.entities;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

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
