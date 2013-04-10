package com.jantox.siege.entities.monsters;

import com.jantox.siege.Map;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.math.Vector2D;

public class SpawnerFactory {
	
	public static int CURRENT_MONSTERS = 0;
	public static int CURRENT_SPAWNERS = 0;
	public static int MONSTERS_KILLED = 0;
	public static int SPAWNERS_DESTROYED = 0;
	
	public static final int MAX_SPAWNERS = 5;
	public static final int MAX_MONSTERS = 35;
	
	public static int MAX_TIME_BETWEEN_SPAWNERS = 30;
	public static int MIN_TIME_BETWEEN_SPAWNERS = 15;
	
	public static int SPAWN_NUMBER = 1;

	private Map map;
	
	public static long seconds;
	private long ms;
	
	private double spawntime = 40;
	
	public SpawnerFactory(Map map) {
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
		
		if(seconds >= spawntime) {
			seconds = 0;
			if(SpawnerFactory.CURRENT_SPAWNERS < MAX_SPAWNERS) {
				int sp = SPAWN_NUMBER;
				if(SPAWN_NUMBER > 3)
					sp = 3;
				for(int i = 0; i < sp; i++) {
					if(SpawnerFactory.CURRENT_SPAWNERS < SpawnerFactory.MAX_SPAWNERS) {
						for(int j = 0;j < 100; j++) {
							Vector2D pos = map.cps[2].getCloseTo(250);
							if(map.isFree(pos, 20)) {
								map.spawn(new MonsterFactory(pos));
								break;
							}
						}
					}
				}
				System.out.println("Spawners Active: " + CURRENT_SPAWNERS);
			}
		}
		
		if(seconds % 10 == 0) {
			// this is the spawner algorithm
			this.spawntime = 0.5 * SpawnerFactory.SPAWNERS_DESTROYED - 0.25
					* Map.AGC_COUNT - 0.25 * Map.SENTRY_COUNT
					+ SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS;
			// should get rid of these so there can be *special* occurences maybe
			if(spawntime > SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS) 
				spawntime = SpawnerFactory.MAX_TIME_BETWEEN_SPAWNERS;
			else if(spawntime < SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS) 
				spawntime = SpawnerFactory.MIN_TIME_BETWEEN_SPAWNERS;
			
			SpawnerFactory.SPAWN_NUMBER = (int) Math.floor((SpawnerFactory.SPAWNERS_DESTROYED+30) / 30);
		}
	}
	
}
