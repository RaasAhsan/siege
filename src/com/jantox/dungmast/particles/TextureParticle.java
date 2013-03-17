package com.jantox.dungmast.particles;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.Sprite;
import com.jantox.dungmast.entities.Entity;
import com.jantox.dungmast.math.Vector2D;

public class TextureParticle extends Particle {

	Sprite texture;
	
	public TextureParticle(int life, Vector2D pos, Vector2D vel, double gravity, Sprite sprite) {
		super(life, pos, vel, gravity);
		this.texture = sprite;
	}
	
	public void update() {
		super.update();
	}

	@Override
	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(texture, new Vector2D(pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2), true);
	}

}
