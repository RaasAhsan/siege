package com.jantox.siege;

import com.jantox.siege.entities.monsters.Spawner;

public class MasterSpawner {
	
	public static int CURRENT_MONSTERS = 0;
	public static int CURRENT_SPAWNERS = 0;
	public static int MONSTERS_KILLED = 0;
	public static int SPAWNERS_DESTROYED = 0;
	
	public static final int MAX_SPAWNERS = 6;
	public static final int MAX_MONSTERS = 30;
	
	public static final int MAX_TIME_BETWEEN_SPAWNERS = 40;
	public static final int MIN_TIME_BETWEEN_SPAWNERS = 20;

	private Map map;
	
	private long seconds;
	private long ms;
	
	private double spawntime = 40;
	
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
		
		if(seconds >= spawntime && MasterSpawner.CURRENT_SPAWNERS < MAX_SPAWNERS) {
			seconds = 0;
			map.spawn(new Spawner(map.cps[2].getCloseTo(225)));
			System.out.println("Spawners Active: " + CURRENT_SPAWNERS);
		}
		
		if(seconds % 10 == 0) {
			
			// this is the spawner algorithm
			this.spawntime = -0.5 * MasterSpawner.SPAWNERS_DESTROYED + MasterSpawner.MAX_TIME_BETWEEN_SPAWNERS;
			if(spawntime > MasterSpawner.MAX_TIME_BETWEEN_SPAWNERS) 
				spawntime = MasterSpawner.MAX_TIME_BETWEEN_SPAWNERS;
			else if(spawntime < MasterSpawner.MIN_TIME_BETWEEN_SPAWNERS) 
				spawntime = MasterSpawner.MIN_TIME_BETWEEN_SPAWNERS;
			System.out.println("Time between Spawners: " + spawntime);
		}
	}
	
}
