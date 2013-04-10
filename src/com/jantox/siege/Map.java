package com.jantox.siege;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.jantox.siege.colsys.Circle;
import com.jantox.siege.colsys.CollisionSystem;
import com.jantox.siege.colsys.Line;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Decoration;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Living;
import com.jantox.siege.entities.NPC;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.Tree;
import com.jantox.siege.entities.Entity.entity_type;
import com.jantox.siege.entities.drones.AGC;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.entities.monsters.MonsterFactory;
import com.jantox.siege.entities.monsters.SpawnerFactory;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Camera;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.particles.Particle;
import com.jantox.siege.particles.ParticleEngine;
import com.jantox.siege.scripts.Assets;

public class Map {
	
	private static Random rand = new Random(System.currentTimeMillis());
	
	private Vector2D size;
	
	private List<Entity> entities;
	
	private ParticleEngine pengine;
	private Minimap minimap;
	private SpawnerFactory spawner;
	
	private Sprite game_interface;
	
	private Player player;
	
	public ControlPoint[] cps = new ControlPoint[5];
	
	Sprite grass;
	
	public Vector2D pspawn;
	
	public Store currentstore;
	
	public static int AGC_COUNT = 0;
	public static int SENTRY_COUNT = 0;
	
	public Map(Player p) {
		entities = new ArrayList<Entity>();
		
		this.player = p;
		if(player == null) {
			System.out.println("shouting");
		}
		
		pengine = new ParticleEngine();
		minimap = new Minimap(this);
		spawner = new SpawnerFactory(this);
	}
	
	public void init() {		
		this.spawn(new AGC(null, new Vector2D(500, 500)));
		this.spawn(new AGC(null, new Vector2D(1000, 500)));
		this.spawn(new AGC(null, new Vector2D(500, 1000)));
		this.spawn(new AGC(null, new Vector2D(1000, 1000)));
		this.spawn(new AGC(null, new Vector2D(750, 750)));
		
		grass = Assets.loadSprite("grass.png");
		game_interface = Assets.loadSprite("interface_game.png");
		
		this.setPlayer(player);
		
		currentstore = null;
		
		int cpc = 0;
		for(Entity e : entities) {
			if(e instanceof ControlPoint) {
				cps[cpc++] = (ControlPoint) e;
			}
		}
	}
	
	public void update() {
		currentstore = null;
		for(Entity e : entities) {
			if(e instanceof NPC) {
				if(e.distanceSquared(player) <= ((Circle)e.getMask()).getRadius() * ((Circle)e.getMask()).getRadius()) {
					currentstore = ((NPC)e).getStore();
					currentstore.update();
				}
			}
		}
		
		spawner.update();
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.isExpired()) {
				entities.remove(i);
				if(e instanceof Dropper) {
					((Dropper)e).drop(rand.nextInt());
				}
			} else {
				e.update();
				if (e.requests()) {
					ArrayList<entity_type> req = e.getRequestedCollisions();
					for (int j = 0; j < entities.size(); j++) {
						Entity x = entities.get(j);
						if (x != e) {
							if(x instanceof Decoration) {
								if(((Decoration)x).getType() == Decoration.COLBOX)
								if(CollisionSystem.collides(x.getMask(), e.getMask())) {
									e.handleCollision(x);
								}
							}
							if (req.contains(x.getEntityType()) || req.contains(entity_type.ALL)) {
								if (CollisionSystem.collides(e.getMask(), x.getMask())) {
									if(e instanceof Projectile) {
										if(x instanceof Decoration) {
											if(((Decoration)x).getType() == Decoration.FOOTPATH)
												continue;
										}
									}
									e.handleCollision(x);
								}
							}
						}
					}
				}
			}
		}
		
		pengine.update();
		
		// sort wthe entities by depth
		Collections.sort(entities, new Comparator<Entity>() {

			@Override
			public int compare(Entity a, Entity b) {
				if (a.getDepth() > b.getDepth()) {
					return -1;
				} else if (a.getDepth() < b.getDepth()) {
					return 1;
				}
				return 0;
			}

		});
	}
	
	public void render(Renderer renderer) {
		renderer.fill(new Rectangle(0, 0, 700, 500), false);
		
		int ex = this.getCamera().pos.getX();
		int ey = this.getCamera().pos.getY();
		for(int i = -256; i < 2048; i += 64) {
			for(int j = -256; j < 2048; j += 64) {
				if(j + 64 > ex && j - 64 < ex + 700 && i + 64 > ey && i - 64 < ey + 500)
					renderer.drawSprite(grass, new Vector2D(j, i), true);
			}
		}
		
		for(Entity e : entities) {
			if(e instanceof ControlPoint)
				e.render(renderer);
		}
		
		int agc = 0, sg = 0, bc =0;
		for(Entity e : entities) {
			if(!(e instanceof ControlPoint))
				e.render(renderer);
			if(e instanceof AGC) 
				agc++;
			else if(e instanceof SentryGun) 
				sg++;
			else if(e instanceof Barricade)
				bc ++;
		}
		
		renderer.setColor(Color.red);
		renderer.setFont(new Font("Lucida Console", Font.BOLD, 13));
		
		pengine.render(renderer);
		
		renderer.drawSprite(game_interface, new Vector2D(), false);
		minimap.render(renderer);
		
		if(currentstore == null) {
			int hx = 10;
			player.heart.setAnimation(2, 2, 0);
			player.heart.update();
			int i = 0;
			for (; i < player.getHealth() / 10; i++) {
				renderer.drawSprite(player.heart, new Vector2D(hx - 5, 5),
						false);
				hx += 16;
			}
			if (i != 10) {
				player.heart.setAnimation(0, 0, 0);
				player.heart.update();
				for (; i < 10; i++) {
					renderer.drawSprite(player.heart, new Vector2D(hx - 5, 5),
							false);
					hx += 16;
				}
			}
			hx = 10;
			for (int j = 0; j < 10; j++) {
				renderer.drawSprite(player.bodyarmor, new Vector2D(hx - 5, 25),
						false);
				hx += 16;
			}

			player.getInventory().render(renderer);

			renderer.setColor(new Color(0, 0, 0, 120));
			// renderer.fillRect(4, 466, 692, 30);

			renderer.setFont(new Font("Lucida Console", Font.BOLD, 11));

			int cx = 87;
			for (int j = 0; j < 5; j++) {
				ControlPoint cp = cps[j];

				renderer.setColor(Color.WHITE);
				renderer.drawText("Control Point " + (j + 1), new Vector2D(
						cx + 6, 478));

				renderer.setColor(new Color(205, 50, 30));
				renderer.fillRect(cx, 481, 110, 10);
				renderer.setColor(new Color(50, 150, 30));
				renderer.fillRect(cx, 481, cp.getOwnership() / (10000 / 110),
						10);

				renderer.setColor(Color.BLACK);
				renderer.drawRect(new Rectangle(cx, 481, 110, 10));

				cx += 122;
			}

			/*
			 * renderer.setColor(Color.BLACK); renderer.drawText("AGC Drones: "
			 * + agc, new Vector2D(534, 405)); renderer.drawText("Sentry Guns: "
			 * + sg, new Vector2D(534, 425)); renderer.drawText("Barricades: " +
			 * bc, new Vector2D(534, 445));
			 */
		} else {
			currentstore.render(renderer);
		}
		
		renderer.setColor(Color.YELLOW);
		renderer.drawText("" + DungeonGame.tcoins, new Vector2D(36, 487));
		
		//renderer.setColor(Color.BLACK);
		//renderer.drawText("FPS: " + DungeonGame.fps, new Vector2D(5, 75));
	}
	
	public void seed(int seed) {
		rand = new Random(seed);
		Entity.rand = new Random(seed);
	}
	
	public void spawn(Entity e) {
		if(e instanceof Particle)
			pengine.add((Particle)e);
		else
			entities.add(e);
	}
	
	public void spawnResource(String form, Vector2D location, int amount) {
		if(form.equals("forest")) {
			spawnForest(location, amount);
		}
	}
	
	private void spawnForest(Vector2D location, int amount) {
		int radius = 45;
		for(int i = 0; i < amount; i++) {
			Vector2D loc = location.copy();
			loc.x += rand.nextGaussian() * radius;
			loc.y += rand.nextGaussian() * radius;
			if(this.isFree(loc, 20)) {
				this.spawn(new Tree(loc));
			}
		}
	}
	
	public boolean isFree(Vector2D pos, float radius) {
		for(Entity e : entities) {
			if(CollisionSystem.collides(e.getMask(), new Circle(pos, radius))) {
				return false;
			}
		}
		return true;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player p) {
		player.setPosition(pspawn);
		entities.add(player);
	}
	
	public void setSize(Vector2D size) {
		this.size = size;
		System.out.println(size.x + " " + size.y);
	}
	
	public Vector2D getSize() {
		return size;
	}

	public ArrayList<Entity> getEntities(entity_type et) {
		ArrayList<Entity> ale = new ArrayList<Entity>();
		
		for(Entity e : entities) {
			if(et != entity_type.ALL) {
				if(e.getEntityType() == et) {
					ale.add(e);
				}
			} else {
				ale.add(e);
			}
		}
		
		return ale;
	}

	public Entity getClosestEntity(Entity me, entity_type type) {
		Entity e = null;
		double dist = 1000000000;
		
		for(Entity test : entities) {
			if(test.getEntityType() == type) {
				if(me.distanceSquared(test) < dist) {
					e = test;
					dist = me.distanceSquared(test);
				}
			}
		}
		
		return e;
	}

	public List<Entity> getEntities() {
		return entities;
	}
	
	public List<Entity> getEntities(Entity e, float radius) {
		List<Entity> ale = new ArrayList<Entity>();
		
		for(Entity ent : entities) {
			if(ent != e) {
				if(e.distanceSquared(ent) <= radius * radius) {
					ale.add(ent);
				}
			}
		}
		
		return ale;
	}
	
	public Camera getCamera() {
		return player.getCamera();
	}
	
	public void spawnForge(Vector2D pos) {
		this.spawn(new NPC(new Vector2D(pos.x, pos.y), NPC.ELI));
		this.spawn(new NPC(new Vector2D(pos.x + 97, pos.y + 100), NPC.UPGRADE_STATION));
	}
	
	public void spawnArmory(Vector2D pos) {
		this.spawn(new NPC(new Vector2D(pos.x, pos.y), NPC.BRENDAN));
	}

	public List<Entity> getEntities(Vector2D cpos, int radius) {
		List<Entity> ale = new ArrayList<Entity>();
		
		for(Entity ent : entities) {
			if(cpos.distanceSquared(ent.pos) <= radius * radius) {
				ale.add(ent);
			}
		}
		
		return ale;
	}

	public boolean isFree(Vector2D pos, int radius, Entity test) {
		for(Entity e : entities) {
			if(e != test)
				if(CollisionSystem.collides(e.getMask(), new Circle(pos, radius))) {
					return false;
				}
		}
		return true;
	}

	public ControlPoint getRandomControlPoint() {
		int index = Entity.rand.nextInt(5);
		
		List<ControlPoint> acp = new ArrayList<ControlPoint>();
		for(Entity e: entities) {
			if(e instanceof ControlPoint)
				acp.add((ControlPoint) e);
		}
		return acp.get(index);
	}

	public Entity getClosestMonster(Entity me) {
		Entity e = null;
		double dist = 1000000000;
		
		for(Entity test : entities) {
			if(test.getEntityType() == entity_type.ZOMBIE || test.getEntityType() == entity_type.SKELETON || test.getEntityType() == entity_type.SPAWNER) {
				if(me.distanceSquared(test) < dist) {
					e = test;
					dist = me.distanceSquared(test);
				}
			}
		}
		
		return e;
	}

}