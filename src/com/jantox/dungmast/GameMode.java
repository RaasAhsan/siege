package com.jantox.dungmast;

import java.awt.Color;
import java.awt.Font;

import com.jantox.dungmast.math.Vector2D;

public abstract class GameMode {
	
	DungeonGame game;
	
	public GameMode(DungeonGame game) {
		this.game = game;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void render(Renderer renderer);

	public static class Domination extends GameMode {
		
		int homepoints = 0;
		int enemypoints = 0;
		
		int ticks = 0;

		public Domination(DungeonGame game) {
			super(game);
		}

		@Override
		public void update() {
			ticks++;
			if(ticks >= 600) {
				ticks = 0;
				for(int i = 0; i < 5; i++) {
					if(game.map.cps[i].getOwner() == 0) {
						homepoints ++;
					} else if(game.map.cps[i].getOwner() == 1) {
						enemypoints++;
					}
				}
			}
		}

		@Override
		public void render(Renderer renderer) {
			renderer.setFont(new Font("Arial", 0, 40));
			renderer.setColor(Color.GREEN);
			
			renderer.drawText("" + homepoints, new Vector2D(290, 40));
			
			renderer.setColor(Color.black);
			renderer.drawText("  -  ", new Vector2D(327, 40));
			
			renderer.setColor(Color.RED);
			renderer.drawText(enemypoints + "", new Vector2D(400, 40));
		}

		@Override
		public void init() {
			
		}
		
	}
	
	public static class Survival extends GameMode {

		public Survival(DungeonGame game) {
			super(game);
		}

		@Override
		public void update() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void render(Renderer renderer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void init() {
			for(int i = 0; i < 5; i++) {
				game.map.cps[i].setOwner(0);
			}
		}
		
	}
	
}
