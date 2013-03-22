package com.jantox.siege.entities.monsters;

import java.util.Random;

import com.jantox.siege.Map;
import com.jantox.siege.MasterSpawner;
import com.jantox.siege.colsys.AABB;
import com.jantox.siege.colsys.Line;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Living;
import com.jantox.siege.entities.Entity.entity_type;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

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
		
		MasterSpawner.CURRENT_SPAWNERS++;
	}
	
	public void update() {
		super.update();
		
		if(health <= 0) {
			MasterSpawner.CURRENT_SPAWNERS--;
			this.expired = true;
		}
		
		timeSinceLast++;
		
		if(timeSinceLast > SPAWN_BREAK) {
			if(rand.nextInt() % 37 == 0) {
				if(spawning) {
					if(MasterSpawner.CURRENT_MONSTERS < MasterSpawner.MAX_MONSTERS) {
						Vector2D rand = this.getCloseTo(180);
						
						Vector2D sps = new Vector2D(Entity.rand.nextGaussian(), Entity.rand.nextGaussian());
						sps.normalize();
						sps.multiply(40 + Entity.rand.nextGaussian() * 128);
						sps.add(pos.copy());
						this.spawn(entity_type.PLAYER, sps, rand);
						MasterSpawner.CURRENT_MONSTERS++;
						System.out.println("Monsters Alive: " + MasterSpawner.CURRENT_MONSTERS);
					}
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
