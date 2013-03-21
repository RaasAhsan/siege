package com.jantox.siege.scripts;

import com.jantox.siege.entities.Monster;
import com.jantox.siege.entities.Weapon;

public class MonsterScript {

	public String type;
	private String gfxfile;
	private int health;
	private double speed, damage;
	private Weapon held;
	
	public MonsterScript(String type, String gfxfile, int health, double speed, double damage, Weapon held) {
		this.type = type;
		this.gfxfile = gfxfile;
		this.health = health;
		this.speed = speed;
		this.damage = damage;
		this.held = held;
	}
	
	public Monster compile() {
		
		return new Monster(gfxfile, health, speed, damage);
	}

}
