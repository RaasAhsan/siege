package com.jantox.siege.entities.monsters;

import com.jantox.siege.SpawnerFactory;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Living;
import com.jantox.siege.entities.Weapon;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Monster extends Living {
	
	enum monster_type { ZOMBIE, SKELETON };
	
	private Weapon weapon;
	private double speed, damage;
	
	public Monster(String gfxfile, int health, double speed, double damage) {
		super(null);
		
		this.sprite = Assets.loadSprite(gfxfile);
		this.health = this.maxhealth = health;
		this.speed = speed;
		this.damage = damage;
		
		SpawnerFactory.CURRENT_MONSTERS++;
	}
	
	public void update() {
		super.update();
		
		if(health < 0) {
			SpawnerFactory.CURRENT_MONSTERS--;
		}
	}

	@Override
	public void render(Renderer renderer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void drop(int chance) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
