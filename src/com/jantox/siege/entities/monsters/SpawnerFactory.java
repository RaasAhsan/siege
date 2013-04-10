package com.jantox.siege.entities.monsters;

import com.jantox.siege.Map;

public class SpawnerFactory {
	
	public static int CURRENT_MONSTERS = 0;
	public static int CURRENT_SPAWNERS = 0;
	public static int MONSTERS_KILLED = 0;
	public static int SPAWNERS_DESTROYED = 0;
	
	public static final int MAX_SPAWNERS = 5;
	public static final int MAX_MONSTERS = 35;
	
	public static final int MAX_TIME_BETWEEN_SPAWNERS = 40;
	public static final int MIN_TIME_BETWEEN_SPAWNERS = 15;
	
	public static int SPAWN_NUMBER = 1;

	private Map map;
	
	public static long seconds;
	private long ms;
	
	private double spawntime = 40;
	
	public SpawnerFactory(Map map) {
		this.map = map;
		seconds = 10;
		ms = System.currentTimeMillis();
	}
	
	public void update() {
		long now = System.currentTimeMillis();
		if(now - ms >= 1000) {
			seconds++;
			ms = System.currentTimeMillis();
		}
		
		if(seconds >= spawntime) {
			seconds = 0;
			if(SpawnerFactory.CURRENT_SPAWNERS < MAX_SPAWNERS) {
				int sp = SPAWN_NUMBER;
				if(SPAWN_NUMBER > 3)
					sp = 3;
				for(int i = 0; i < sp; i++)
					if(SpawnerFactory.CURRENT_SPAWNERS < SpawnerFactory.MAX_SPAWNERS)
						map.spawn(new MonsterFactory(map.cps[2].getCloseTo(200)));
				System.out.println("Spawners Active: " + CURRENT_SPAWNERS);
			}
		}
		
		if(seconds % 10 == 0) {
			// this is the spawner algorithm
			this.spawntime = -0.5 * SpawnerFactory.SPAWNERS_DESTROYED - 0.25
					* Map.AGC_COUNT - 0.25 * Map.SENTRY_COUNT
					+ SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS;
			// should get rid of these so there can be *special* occurences maybe
			if(spawntime > SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS) 
				spawntime = SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS;
			else if(spawntime < SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS) 
				spawntime = SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS;
			
			SpawnerFactory.SPAWN_NUMBER = (int) Math.floor((SpawnerFactory.SPAWNERS_DESTROYED+30) / 30);
		}
	}
	
}
