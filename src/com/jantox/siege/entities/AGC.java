package com.jantox.siege.entities;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jantox.siege.colsys.Circle;
import com.jantox.siege.colsys.CollisionSystem;
import com.jantox.siege.entities.Entity.entity_type;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

// autonomous ground control
public class AGC extends Living {
	
	private Vector2D next_pos;
	private Vector2D velocity;
	
	private Entity attack = null;

	public AGC(Vector2D pos) {
		super(pos);
		
		this.health = 1000;
		this.colmask = new Circle(pos, 7);
		
		this.requestCollisions(entity_type.FENCE, entity_type.GATE, entity_type.BARRICADE);
		
		this.sprite = Assets.loadSprite("agc.png");
		
		direction = 0;
		
		sprite.setAnimation(0,7,5);
		ControlPoint cp = map.getRandomControlPoint();
		if(cp != null)
			next_pos = cp.getCloseTo(70);
		else
			next_pos = new Vector2D(rand.nextInt(6 * 32 + 55 * 32), rand.nextInt(6 * 32 + 55 * 32));
	}

	public void update() {
		this.colmask.update(pos);
		
		if(attack == null)
			velocity = new Vector2D(next_pos.getX() - pos.getX(), next_pos.getY() - pos.getY());
		else
			velocity = new Vector2D(attack.getPosition().getX() - pos.getX(), attack.getPosition().getY() - pos.getY());
		
		velocity.normalize();
		velocity.multiply(0.5);
		
		ArrayList<Entity> ale = map.getEntities(entity_type.ALL);
		for(Entity e : ale) {
			if(e != null) {
				if(e != this && !(e instanceof ControlPoint) && !(e instanceof AGC) && !(e instanceof SentryGun) && !(e instanceof Barricade)) {
					if(CollisionSystem.collides(e.getMask(), this.getMask())) {
						Vector2D toa = new Vector2D(pos.x - e.pos.x, pos.y - e.pos.y);
						
						Vector2D nd = toa.copy();
						nd.normalize();
						nd.multiply(0.5);
						
						velocity = nd;
					}
				} 
				if (e instanceof Zombie || e instanceof Skeleton) {
					if(this.distanceSquared(e) <= 75 * 75) {
						attack = e;
						break;
					}
				}
				attack = null;
			}
		}
		
		pos.add(velocity);
		
		if(this.pos.distanceSquared(next_pos) <= 5 * 5) {
			ControlPoint cp = map.getRandomControlPoint();
			if(cp != null)
				next_pos = cp.getCloseTo(70);
			else
				next_pos = new Vector2D(rand.nextInt(6 * 32 + 55 * 32), rand.nextInt(6 * 32 + 55 * 32));
		}
		
		if(attack == null) {
			double angle = pos.angleTo(next_pos);
			angle = Math.toDegrees(angle);
			angle -= 270 - 22.5;
			while(angle < 0)
				angle += 360;
			
			int sdir = (int) ((angle / 45));
			if(sdir < 0)
				sdir += 8;
			
			sprite.setAnimation(sdir, sdir, 0);
			sprite.update();
		} else {
			((Living)attack).damage(2);
			
			double angle = pos.angleTo(attack.getPosition());
			angle = Math.toDegrees(angle);
			angle -= 270 - 22.5;
			while(angle < 0)
				angle += 360;
			
			int sdir = (int) ((angle / 45));
			if(sdir < 0)
				sdir += 8;
			
			sprite.setAnimation(sdir, sdir, 0);
			sprite.update();
		}
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 31, pos.y - 37), true);
	}
	
	@Override
	public void drop(int chance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCollision(Entity e) {
		ControlPoint cp = map.getRandomControlPoint();
		if(cp != null)
			next_pos = cp.getCloseTo(70);
		else
			next_pos = new Vector2D(rand.nextInt(6 * 32 + 55 * 32), rand.nextInt(6 * 32 + 55 * 32));// TODO Auto-generated method stub
	}
	
}
