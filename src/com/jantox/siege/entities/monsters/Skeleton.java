package com.jantox.siege.entities.monsters;

import java.util.ArrayList;

import com.jantox.siege.colsys.Circle;
import com.jantox.siege.colsys.CollisionSystem;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Gate;
import com.jantox.siege.entities.Gem;
import com.jantox.siege.entities.Living;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.Entity.entity_type;
import com.jantox.siege.entities.drones.AGC;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Skeleton extends Living {
	
	Vector2D vel;
	private Vector2D gotoline;
	private boolean gotodone = false;
	
	int breakTime = 0;
	
	ControlPoint p;
	
	Entity attack = null;
	int breaktime = 0;
	
	long timedone = 0;
	
	public Skeleton(Vector2D pos, Vector2D gotoline) {
		super(pos);
		this.gotoline = gotoline;
		
		this.colmask = new Circle(this.pos, 11);
		this.health = 300;
		
		this.sprite = Assets.loadSprite("skeleton.png");
		
		p = map.getRandomControlPoint();
		
		this.requestCollisions(entity_type.PLAYER, entity_type.GATE, entity_type.CONTROL_POINT, entity_type.BARRICADE);
	}

	@Override
	public void handleCollision(Entity e) {
		if(e instanceof Player) {
			Player p = (Player) e;
			if(breakTime == 0) {
				breakTime = 20;
				p.damage(3);
			}
			pos.subtract(vel);
		} else if(e instanceof Gate) {
			if(!((Gate)e).isBroken()) {
				if (breakTime == 0) {
					((Gate) e).damage(5);
					breakTime = 1;
					pos.subtract(vel);
				}
			}
		} else if(e instanceof Barricade) {
			((Living)e).damage(5);
		} 
		colmask.update(pos);
	}

	@Override
	public void update() {
		super.update();
		
		if(breaktime > 0)
			breaktime--;
		
		timedone++;
		
		if(timedone > 60 * 45) {
			if(!gotodone) {
				this.expired = true;
				SpawnerFactory.CURRENT_MONSTERS--;
			}
		}
		
		if(breakTime > 0)
			breakTime --;
		
		if(gotodone) {
			if(p != null) {
				if(p.distanceSquared(this) >= 20) {
					vel = new Vector2D(p.getPosition().getX() - pos.getX(), p
							.getPosition().getY() - pos.getY());
					vel.normalize();
				}
			}
		} else {
			vel = new Vector2D(gotoline.x - pos.getX(), gotoline.y - pos.getY());
			vel.normalize();
			
			if(gotoline.distanceSquared(pos) <= 20 * 20) {
				gotodone = true;
			}
		}
		
		ArrayList<Entity> ale = map.getEntities(entity_type.ALL);
		for(Entity e : ale) {
			if(e != this && !(e instanceof ControlPoint) && !(e instanceof Gate)) {
				if(CollisionSystem.collides(e.getMask(), this.getMask())) {
					Vector2D toa = new Vector2D(pos.x - e.pos.x, pos.y - e.pos.y);
					
					Vector2D nd = toa.copy();
					nd.normalize();
					nd.multiply(4);
					
					vel = nd;
				}
			}
			
			if (e instanceof SentryGun || e instanceof AGC || e instanceof Player) {
				if(this.distanceSquared(e) <= 100 * 100) {
					attack = e;
					break;
				}
			}
		}
		vel.x /= 3;
		vel.y /= 3;

		if(attack != null)
			if(((Living)attack).isDead()) {
				attack = null;
			}
		
		if(attack != null) {
			if(breaktime <= 0) {
				breaktime = 100;
				
				Vector2D avel = new Vector2D(attack.pos.x, attack.pos.y);
				Vector2D cvel = new Vector2D(this.pos.x, this.pos.y - 20);

				double ang = cvel.angleTo(avel);

				double cx = Math.cos(ang);
				double cy = Math.sin(ang);
				Vector2D vel = new Vector2D(cx, cy);

				Vector2D np = new Vector2D(this.pos.x, this.pos.y);
				map.spawn(new EnemyShot(np, vel, ang, Projectile.BLAST));
			}
		}
		
		pos.add(vel);
		colmask.update(pos);
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 15, pos.y - 49), true);
	}

	@Override
	public void drop(int chance) {
		map.spawn(new Gem(new Vector2D(pos.x + rand.nextGaussian() * 10, pos.y + rand.nextGaussian() * 10)));
	}

}
