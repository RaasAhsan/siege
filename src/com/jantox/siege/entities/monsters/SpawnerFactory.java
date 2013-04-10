package com.jantox.siege.entities.monsters;

import com.jantox.siege.DungeonGame;
import com.jantox.siege.Map;
import com.jantox.siege.math.Vector2D;

public class SpawnerFactory {
	
	public static int SPAWNERS_ALIVE = 0;
	public static int MONSTERS_ALIVE = 0;
	
	public static int MONSTERS_KILLED = 0;
	public static int SPAWNERS_KILLED = 0;
	
	public static int SPAWNER_RATE = 0; // 1 per X + noise second
	public static int MONSTER_RATE = 0; // v per X + noise second
	public static int MONSTER_NUM_RATE = 1; 
	
	public static int POINTS_NEEDED = 0;
	
	private Map map;
	public static long seconds;
	private long ms;
	
	public int spawner_rate;
	
	public SpawnerFactory(Map map) {
		this.map = map;
		seconds = 0;
		ms = System.currentTimeMillis();
		this.startNewWave();
	}
	
	public void update() {
		long now = System.currentTimeMillis();
		if(now - ms >= 1000) {
			if(DungeonGame.breakTime <= 0)
				seconds++;
			ms = System.currentTimeMillis();
			if(DungeonGame.breakTime > 0)
				DungeonGame.breakTime --;
		}
		
		if(DungeonGame.breakTime == 0) {
			if(POINTS_NEEDED > 0) {
				if(seconds >= SPAWNER_RATE) {
					seconds = 0;
					for (int i = 0; i < 100; i++) {
						Vector2D pos = map.cps[2].getCloseTo(200);
						if (map.isFree(pos, 20)) {
							map.spawn(new MonsterFactory(pos));
							break;
						}
					}
				}
			} else {
				this.startNewWave();
			}
		}
	}
	
	public void startNewWave() {
		int wave = ++DungeonGame.wave;
		if(wave > 1) {
			map.clearMonsters();
			DungeonGame.wave_complete = 128;
			DungeonGame.breakTime = 30;
		}
		
		// calculate healths and difficulties of enemies
		MonsterFactory.HEALTH = 1500 + wave * 50;
		Monster.HEALTH = 200 + wave * 50;
		
		POINTS_NEEDED = (int) (10 + wave * 1.5);
		MONSTERS_KILLED = 0;
		
		SPAWNER_RATE = (int) (15);
		MONSTER_RATE = (int) (5);
		
		if(wave% 10 == 0) {
			SpawnerFactory.MONSTER_NUM_RATE++;
		}
		
		seconds = 0;
	}
	
}
