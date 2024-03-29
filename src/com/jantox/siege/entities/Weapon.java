package com.jantox.siege.entities;

import com.jantox.siege.Item;
import com.jantox.siege.math.Vector2D;

public abstract class Weapon extends Entity implements Item {

	protected Entity owner;
	
	public Weapon(Entity owner) {
		super(owner.getPosition().copy());
		this.owner = owner;
	}

	public void update() {
		this.pos = owner.pos.copy();
	}
	
	public abstract void attack();
	
	@Override
	public void handleCollision(Entity e) {
		
	}

	@Override
	public int onItemUse() {
		this.attack();
		return 1;
	}

	@Override
	public void onHeld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotHeld() {
		// TODO Auto-generated method stub
		
	}
	
}
