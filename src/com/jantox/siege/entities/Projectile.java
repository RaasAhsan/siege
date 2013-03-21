package com.jantox.siege.entities;

import java.awt.Color;

import com.jantox.siege.Item;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.particles.ColorParticle;
import com.jantox.siege.scripts.Assets;

public class Projectile extends Entity implements Item {
	
	public static final int BLAST = 0;
	public static final int ARROW = 1;
	
	private int type;
	private Vector2D dir;
	private int damage;
	
	int life = 0;
	
	public Projectile(Vector2D pos, Vector2D dir, double angle, int type) {
		super(pos.copy());
		this.dir = dir;
		this.type = type;
		
		this.colmask = new Circle(this.pos, 15);
		
		this.requestCollisions(entity_type.ALL);
		
		if(type == BLAST) {
			this.sprite = Assets.loadSprite("blast.png");
			dir.x *= 4;
			dir.y *= 4;
			this.damage = 50;
		} else if(type == ARROW) {
			this.sprite = Assets.loadSprite("arrow.png");
			dir.x *= 25;
			dir.y *= 25;
			this.damage = 300;
		}
		
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
	
	public void update() {
		pos.x += dir.x;
		pos.y += dir.y;
		
		life++;
		if(this.type == BLAST) {
			if(life >= 100) 
				this.expired = true;
		} else if(this.type == ARROW) {
			if(life >= 50) 
				this.expired = true;
		}
		
		colmask.update(pos);
	}

	@Override
	public void handleCollision(Entity e) {
		if(e instanceof Living) {
			if(e instanceof Skeleton || e instanceof Zombie || e instanceof Spawner) {
				((Living)e).damage(damage);
			}
		}
		if(!(e instanceof Player) && !(e instanceof Projectile) && !(e instanceof SentryGun) && !(e instanceof ControlPoint)) {
			this.expired = true;
			for(int i = 0; i < 3; i++) {
				Vector2D vel = new Vector2D(rand.nextGaussian()/6, -3/100);
				Vector2D np = pos.copy();
				np.x += rand.nextGaussian() * 5;
				np.y -= 16 + rand.nextGaussian() * 5;
				if(e instanceof Zombie || e instanceof Skeleton)
					map.spawn(new ColorParticle(rand.nextInt(10) + 5, Color.RED, np.copy(), vel, new Vector2D(2,2), 0.2));
				else
					map.spawn(new ColorParticle(rand.nextInt(20) + 10, Color.YELLOW, np.copy(), vel, new Vector2D(2,2), -0.2));
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		if(type == BLAST)
			renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16 - 20), true);
		else
			renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16 - 20), true);
	}

	@Override
	public int onItemUse() {
		return 0;
	}

	@Override
	public void onHeld() {
		
	}

	@Override
	public void onNotHeld() {
		
	}

	public int getType() {
		return type;
	}

	@Override
	public int getRest() {
		return 5;
	}

}
