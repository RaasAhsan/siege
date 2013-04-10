package com.jantox.siege.entities.monsters;

import com.jantox.siege.colsys.AABB;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Gem;
import com.jantox.siege.entities.Living;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class MonsterFactory extends Living {
	
	public static int HEALTH = 1500;
	
	private float radius;
	
	private long totalSpawned;
	private long timeSinceLast;
	private boolean spawning;
	
	long seconds, ms;
	
	public MonsterFactory(Vector2D pos) {
		super(pos);
		
		this.colmask = new AABB(pos.copy(), 56, 10);
		
		this.maxhealth = this.health = HEALTH;
		
		this.totalSpawned = 0;
		this.radius = 30;
		this.timeSinceLast = 0;
		this.spawning = true;
		
		this.sprite = Assets.loadSprite("spawner.png");
		
		SpawnerFactory.SPAWNERS_ALIVE++;
	}
	
	public void update() {
		super.update();
		
		long now = System.currentTimeMillis();
		if(now - ms >= 1000) {
			seconds++;
			ms = System.currentTimeMillis();
		}
		
		if(health <= 0) {
			SpawnerFactory.SPAWNERS_ALIVE--;
			SpawnerFactory.SPAWNERS_KILLED++;
			SpawnerFactory.POINTS_NEEDED-=2;
			this.expired = true;
			for(int i = 0; i < 5; i++) {
				map.spawn(new Gem(new Vector2D(pos.x + rand.nextGaussian() *10, pos.y + rand.nextGaussian() * 10)));
			}
			SpawnerFactory.seconds += 5;
		}
		
		if(seconds == SpawnerFactory.MONSTER_RATE) {
			seconds = 0;
			for(int i = 0; i < SpawnerFactory.MONSTER_NUM_RATE; i++) {
				Vector2D rand = this.getCloseTo(180);

				Vector2D sps = new Vector2D(Entity.rand.nextGaussian(), Entity.rand.nextGaussian());
				sps.normalize();
				sps.multiply(40 + Entity.rand.nextGaussian() * 128);
				sps.add(pos.copy());
				this.spawn(sps, rand);
			}
		}
		
		this.sprite.setAnimation(health / (maxhealth / 5), health / (maxhealth / 5), 0);
		sprite.update();
	}
	
	public void spawn(Vector2D pos, Vector2D gotoline) {
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
