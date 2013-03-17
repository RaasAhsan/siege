package com.jantox.dungmast.entities;

import com.jantox.dungmast.math.Vector2D;

public abstract class Resource extends Entity {
	
	protected int amount;
	protected int growTime;
	
	protected int deathTime;
	
	public Resource(Vector2D pos, int amount, int growtime) {
		super(pos);
		
		this.amount = amount;
		this.growTime = growtime;
	}
	
	@Override
	public void update() {
		if(status == 0) {
			deathTime++;
			if(deathTime >= growTime) {
				if(rand.nextInt() % 137 == 0)
					this.grow();
			}
		}
	}
	
	public boolean useResource() {
		if(status != 0) {
			if(rand.nextInt(1000) % 3 == 0) {
				status = 0;
				if(this instanceof Tree) {
					int times = rand.nextInt(2) + 1;
					for(int i = 0; i < times; i++) {
						Entity.map.getPlayer().pickup(new Log(new Vector2D(0,0)));
					}
				}
			}
		}
		return false;
	}
	
	public boolean isUsable() {
		return (status == 1);
	}
	
	public void grow() {
		status = 1;
		deathTime = 0;
	}

}
