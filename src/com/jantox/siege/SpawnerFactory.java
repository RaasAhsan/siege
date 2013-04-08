package com.jantox.siege;

import com.jantox.siege.entities.monsters.MonsterFactory;

public class SpawnerFactory {
	
	public static int CURRENT_MONSTERS = 0;
	public static int CURRENT_SPAWNERS = 0;
	public static int MONSTERS_KILLED = 0;
	public static int SPAWNERS_DESTROYED = 0;
	
	public static final int MAX_SPAWNERS = 6;
	public static final int MAX_MONSTERS = 30;
	
	public static final int MAX_TIME_BETWEEN_SPAWNERS = 30;
	public static final int MIN_TIME_BETWEEN_SPAWNERS = 10;
	
	public static int SPAWN_NUMBER = 1;

	private Map map;
	
	private long seconds;
	private long ms;
	
	private double spawntime = 40;
	
	public SpawnerFactory(Map map) {
		this.map = map;
		seconds = 15;
		ms = System.currentTimeMillis();
	}
	
	public void update() {
		long now = System.currentTimeMillis();
		if(now - ms >= 1000) {
			seconds++;
			ms = System.currentTimeMillis();
		}
		
		if(seconds >= spawntime && SpawnerFactory.CURRENT_SPAWNERS < MAX_SPAWNERS) {
			seconds = 0;
			for(int i = 0; i < SPAWN_NUMBER; i++)
				map.spawn(new MonsterFactory(map.cps[2].getCloseTo(225)));
			System.out.println("Spawners Active: " + CURRENT_SPAWNERS);
		}
		
		if(seconds % 10 == 0) {
			
			// this is the spawner algorithm
			this.spawntime = -0.75 * SpawnerFactory.SPAWNERS_DESTROYED - 0.5
					* Map.AGC_COUNT - 0.5 * Map.SENTRY_COUNT
					+ SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS;
			// should get rid of these so there can be *special* occurences
			if(spawntime > SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS) 
				spawntime = SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS;
			else if(spawntime < SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS) 
				spawntime = SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS;
			
			SpawnerFactory.SPAWN_NUMBER = (int) Math.floor((SpawnerFactory.SPAWNERS_DESTROYED+15) / 15);
		}
	}
	
}
