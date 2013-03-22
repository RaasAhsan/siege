package com.jantox.siege.entities;

import com.jantox.siege.Dropper;
import com.jantox.siege.MasterSpawner;
import com.jantox.siege.entities.monsters.Skeleton;
import com.jantox.siege.entities.monsters.Zombie;
import com.jantox.siege.math.Vector2D;

public abstract class Living extends Entity implements Dropper {

	protected int maxhealth;
	protected int health;
	
	public Living(Vector2D pos) {
		super(pos);
		
		health = 1;
	}
	
	public void update() {
		if(this.isDead() && !expired)  {
			expired = true;
			if(this instanceof Zombie || this instanceof Skeleton) {
				System.out.println("Killed a monster!");
				MasterSpawner.CURRENT_MONSTERS--;
				MasterSpawner.MONSTERS_KILLED ++;
			}
			this.drop(rand.nextInt(5000));
			for(int i = 0; i < 50; i++) {
				double val = rand.nextGaussian();
				if(val > 0)
					val = -val;
				//map.spawn(new ColorParticle(140, Color.RED, this.pos, new Vector2D(rand.nextGaussian(), rand.nextGaussian()), new Vector2D(2, 2), 0));
			}
		}
	}
	
	public void heal(int h) {
		health += h;
		if(health > maxhealth)
			health = maxhealth;
	}
	
	public void damage(int d) {
		health -= d;
		if(health < 0)
			health = 0;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
}
