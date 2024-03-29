package com.jantox.siege;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.monsters.SpawnerFactory;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.network.Client;
import com.jantox.siege.scripts.Assets;
import com.jantox.siege.scripts.ScriptReader;

@SuppressWarnings("serial")
public class DungeonGame extends Canvas implements Runnable {
	
	public static int coins = 100;
	public static int tcoins = 100;
	
	public static int wave = 0;
	public static int breakTime = 0; // seconds
	
	private int cbreak = 0;
	
	private Thread thread;
	private Keyboard ui;
	
	private BufferedImage offscreen;
	private Graphics offscrgfx;
	
	public Map map;

	private boolean multiplayer;
	private Client client;

	private int frameCount = 0;
	
	public static float interp;

	public static boolean paused = false;
	
	private Sprite pausemode, wavecomplete;
	
	private GameMode gamemode;
	
	public static int fps = 0;
	public static int wave_complete = 0;
	
	private Sprite youlost;
	
	public DungeonGame(boolean m) {
		this.multiplayer = m;
		
		this.setPreferredSize(new Dimension(700, 500));
		
		offscreen = new BufferedImage((int) 700, (int) 500, BufferedImage.TYPE_INT_RGB);
		offscrgfx = offscreen.getGraphics();
		
		this.addKeyListener((ui = new Keyboard(this)));
		this.addMouseListener(ui);
		this.addMouseMotionListener(ui);
		
		youlost = Assets.loadSprite("youlost.png");
		pausemode = Assets.loadSprite("pause_menu.png");
		wavecomplete = Assets.loadSprite("wave_complete.png");
		
		try {
			Player p = new Player(new Vector2D(1200, 400), ui, new Vector2D(700, 500));
			map = new ScriptReader().readMapScript(p, "map.msf");
			p.init();
			
			map.init();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gamemode = new GameMode.Survival(this);
		gamemode.init();
		
		if(multiplayer) { //initiate client and stuff
			// connect to server
		}
	}
	
	public void addNotify() {
		super.addNotify();
		
		this.requestFocus();
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		final double GAME_HERTZ = 100.0;
		
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		
		final int MAX_UPDATES_BEFORE_RENDER = 2;
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		// If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
		
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		
		while (true) {
			double now = System.nanoTime();
			int updateCount = 0;

			// Do as many game updates as we need to, potentially playing
			// catchup.
			if(!paused)
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
						&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
					update();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

			// If for some reason an update takes forever, we don't want to do
			// an insane number of catchups.
			// If you were doing some sort of game that needed to keep EXACT
			// time, you would get rid of this.
			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}

			// Render. To do so, we need to calculate interpolation for a smooth
			// render.
			float interpolation = Math.min(1.0f,
					(float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
			DungeonGame.interp = interpolation;
			this.render(offscrgfx);
			frameCount++;

			Graphics g = this.getGraphics();
			g.drawImage(offscreen, 0, 0, this.getWidth(), this.getHeight(), 0,
					0, this.getWidth(), this.getHeight(), null);
			g.dispose();
			
			lastRenderTime = now;

			// Update the frames we got.
			int thisSecond = (int) (lastUpdateTime / 1000000000);
			if (thisSecond > lastSecondTime) {
				//System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
	            fps = frameCount;
				frameCount = 0;
				lastSecondTime = thisSecond;
			}

			while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
					&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
				try {
					Thread.sleep(2);
				} catch (Exception e) {
				}

				now = System.nanoTime();
			}
		}
	}
	
	public void update() {
		Entity.ticks ++;
		if(Entity.ticks % 60 == 0) {
			Entity.ticks = 0;
		}
		
		if(wave_complete > 0)
			wave_complete--;
		
		if(Entity.ticks % 3 == 0) {
			if(Math.ceil(DungeonGame.tcoins) < Math.ceil(DungeonGame.coins)) {
				tcoins = (int) (coins + (tcoins - coins) / 2);
			} else if(Math.ceil(DungeonGame.tcoins) > Math.ceil(DungeonGame.coins)) {
				tcoins = (int) (coins + (tcoins - coins) / 2);
			} else if(Math.ceil(tcoins) == Math.ceil(coins)) {
				tcoins = coins;
			}
		}
		
		map.update();
		gamemode.update();
		if(multiplayer) {
			this.updateMultiplayer();
		}
	}
	
	public void updateMultiplayer() {
		
	}
	
	public void render(Graphics g) {
		super.paint(g);
		
		Renderer renderer = Renderer.create(g, map.getPlayer().getCamera());
		map.render(renderer);
		gamemode.render(renderer);
		renderer.setColor(Color.YELLOW);
		renderer.setFont(new Font(Font.SANS_SERIF, 0, 20));
		renderer.drawText("Wave " + wave, new Vector2D(310, 30));
		if(breakTime > 0) {
			renderer.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
			renderer.drawText("Next Wave In: " + breakTime, new Vector2D(280, 60));
		}
		renderer.setColor(Color.RED);
		renderer.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		renderer.drawText("Need Kills: " + SpawnerFactory.POINTS_NEEDED, new Vector2D(553, 185));
		if(wave_complete > 0) {
			renderer.drawSprite(wavecomplete, new Vector2D(), false);
		}
		
		if(paused) {
			renderer.drawSprite(pausemode, new Vector2D(), false);
		}
	}
	
}
