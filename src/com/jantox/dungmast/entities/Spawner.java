package com.jantox.dungmast.entities;

import java.util.Random;

import com.jantox.dungmast.Map;
import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.colsys.AABB;
import com.jantox.dungmast.colsys.Line;
import com.jantox.dungmast.entities.Entity.entity_type;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Spawner extends Living {
	
	public static final int SPAWN_BREAK = 180;
	
	private float radius;
	
	private long totalSpawned;
	private long timeSinceLast;
	private boolean spawning;
	
	public Spawner(Vector2D pos) {
		super(pos);
		
		this.colmask = new AABB(pos.copy(), 56, 10);
		
		this.maxhealth = this.health = 1200;
		
		this.totalSpawned = 0;
		this.radius = 30;
		this.timeSinceLast = 0;
		this.spawning = true;
		
		this.sprite = Assets.loadSprite("spawner.png");
	}
	
	public void update() {
		super.update();
		
		timeSinceLast++;
		
		if(timeSinceLast > SPAWN_BREAK) {
			if(rand.nextInt() % 37 == 0) {
				if(spawning) {
					Vector2D rand = this.getCloseTo(180);
					
					Vector2D sps = new Vector2D(Entity.rand.nextGaussian(), Entity.rand.nextGaussian());
					sps.normalize();
					sps.multiply(40 + Entity.rand.nextGaussian() * 128);
					sps.add(pos.copy());
					this.spawn(entity_type.PLAYER, sps, rand);
				}
			}
		}
		
		this.sprite.setAnimation(health / 240, health / 240, 0);
		sprite.update();
	}
	
	public void spawn(entity_type type, Vector2D pos, Vector2D gotoline) {
		totalSpawned++;
		timeSinceLast = 0;
		
		if(rand.nextInt() % 2 == 0)
			map.spawn(new Zombie(pos, gotoline));
		else 
			map.spawn(new Skeleton(pos, gotoline));
	}
	
	public Vector2D getCloseTo() {
		Vector2D sp = pos.copy();
		sp.x += rand.nextGaussian() * radius;
		sp.y += rand.nextGaussian() * radius;
		return sp;
	}
	
	public long getTotalSpawned() {
		return totalSpawned;
	}
	
	public long getTimeSinceLastSpawn() {
		return timeSinceLast;
	}
	
	public void setSpawning(boolean b) {
		spawning = b;
	}

	@Override
	public void drop(int chance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 65, pos.y - 65), true);
	}
	
}
