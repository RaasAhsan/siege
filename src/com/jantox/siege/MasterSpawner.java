package com.jantox.siege;

import com.jantox.siege.entities.monsters.Spawner;

public class MasterSpawner {
	
	public static int CURRENT_MONSTERS = 0;
	public static int CURRENT_SPAWNERS = 0;
	public static int MONSTERS_KILLED = 0;
	public static int SPAWNERS_DESTROYED = 0;
	
	public static final int MAX_SPAWNERS = 10;
	public static final int MAX_MONSTERS = 50;

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
			
			if(seconds % 15 == 0 && MasterSpawner.CURRENT_SPAWNERS < MAX_SPAWNERS) {
				map.spawn(new Spawner(map.cps[2].getCloseTo(300)));
				System.out.println("Spawners Active: " + CURRENT_SPAWNERS);
			}
		}
		
		if(seconds >= 60) {
			seconds = 0;
			
			// this is the spawner algorithm
		}
	}
	
}
