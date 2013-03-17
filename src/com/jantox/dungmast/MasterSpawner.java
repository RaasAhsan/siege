package com.jantox.dungmast;

import com.jantox.dungmast.entities.Entity;
import com.jantox.dungmast.entities.Spawner;
import com.jantox.dungmast.math.Vector2D;

public class MasterSpawner {

	private Map map;
	
	private long seconds;
	private long ms;
	
	public MasterSpawner(Map map) {
		this.map = map;
		seconds = 0;
		ms = System.currentTimeMillis();
	}
	
	public void update() {
		long now = System.currentTimeMillis();
		if(now - ms >= 1000) {
			seconds++;
			ms = System.currentTimeMillis();
		}
		
		if(seconds >= 15) {
			map.spawn(new Spawner(map.cps[2].getCloseTo(300)));
			seconds = 0;
		}
	}
	
}
