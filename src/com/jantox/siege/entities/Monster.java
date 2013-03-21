package com.jantox.siege.entities;

import com.jantox.siege.MasterSpawner;
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
		
		MasterSpawner.CURRENT_MONSTERS++;
	}
	
	public void update() {
		super.update();
		
		if(health < 0) {
			MasterSpawner.CURRENT_MONSTERS--;
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
