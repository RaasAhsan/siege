package com.jantox.siege.entities.monsters;

import java.awt.Color;

import com.jantox.siege.colsys.Circle;
import com.jantox.siege.entities.AGC;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Living;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.SentryGun;
import com.jantox.siege.entities.Entity.entity_type;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.particles.ColorParticle;
import com.jantox.siege.scripts.Assets;

public class EnemyShot extends Entity {
	
	private Vector2D dir;
	private int damage;
	
	int life = 0;
	
	public EnemyShot(Vector2D pos, Vector2D dir, double angle, int type) {
		super(pos.copy());
		this.dir = dir;
		
		this.colmask = new Circle(this.pos, 15);
		
		this.requestCollisions(entity_type.ALL);
		
		this.sprite = Assets.loadSprite("blast.png");
		dir.x *= 4;
		dir.y *= 4;
		this.damage = 10;
		
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
		
		if(life >= 100) 
			this.expired = true;
		
		colmask.update(pos);
	}

	@Override
	public void handleCollision(Entity e) {
		if(e instanceof Living) {
			if(e instanceof Player || e instanceof AGC || e instanceof SentryGun) {
				((Living)e).damage(damage);
			}
		}
		if(!(e instanceof Projectile) && !(e instanceof EnemyShot) && !(e instanceof Zombie) && !(e instanceof Skeleton) && !(e instanceof ControlPoint)) {
			this.expired = true;
			for(int i = 0; i < 3; i++) {
				Vector2D vel = new Vector2D(rand.nextGaussian()/6, -3/100);
				Vector2D np = pos.copy();
				np.x += rand.nextGaussian() * 5;
				np.y -= 16 + rand.nextGaussian() * 5;
				if(e instanceof SentryGun || e instanceof AGC || e instanceof Player)
					map.spawn(new ColorParticle(rand.nextInt(10) + 5, Color.RED, np.copy(), vel, new Vector2D(2,2), 0.2));
				else
					map.spawn(new ColorParticle(rand.nextInt(20) + 10, Color.YELLOW, np.copy(), vel, new Vector2D(2,2), -0.2));
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16 - 20), true);
	}
	
}
