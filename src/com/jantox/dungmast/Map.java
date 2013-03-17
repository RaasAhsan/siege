package com.jantox.dungmast;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.colsys.CollisionSystem;
import com.jantox.dungmast.colsys.Line;
import com.jantox.dungmast.entities.AGC;
import com.jantox.dungmast.entities.Barricade;
import com.jantox.dungmast.entities.SentryGun;
import com.jantox.dungmast.entities.ControlPoint;
import com.jantox.dungmast.entities.Decoration;
import com.jantox.dungmast.entities.Entity;
import com.jantox.dungmast.entities.NPC;
import com.jantox.dungmast.entities.Player;
import com.jantox.dungmast.entities.Projectile;
import com.jantox.dungmast.entities.Spawner;
import com.jantox.dungmast.entities.Tree;
import com.jantox.dungmast.entities.Entity.entity_type;
import com.jantox.dungmast.math.Camera;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.particles.Particle;
import com.jantox.dungmast.particles.ParticleEngine;
import com.jantox.dungmast.scripts.Assets;

public class Map {
	
	private static Random rand = new Random(2343);
	
	private Vector2D size;
	
	private List<Entity> entities;
	
	private ParticleEngine pengine;
	private Minimap minimap;
	private ControlMap controlmap;
	private MasterSpawner spawner;
	
	private Player player;
	
	ControlPoint[] cps = new ControlPoint[5];
	
	Sprite grass;
	
	public Vector2D pspawn;
	
	private Store currentstore;
	
	public Map() {
		entities = new ArrayList<Entity>();
		
		pengine = new ParticleEngine();
		minimap = new Minimap(this);
		controlmap = new ControlMap(this);
		spawner = new MasterSpawner(this);
	}
	
	public void init() {		
		this.spawn(new AGC(new Vector2D(500, 500)));
		this.spawn(new AGC(new Vector2D(1000, 500)));
		this.spawn(new AGC(new Vector2D(500, 1000)));
		this.spawn(new AGC(new Vector2D(1000, 1000)));
		this.spawn(new AGC(new Vector2D(750, 750)));
		
		grass = Assets.loadSprite("grass.png");
		
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
				if(e.distanceSquared(player) <= 50 * 50) {
					currentstore = ((NPC)e).getStore();
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
										if(x instanceof ControlPoint) 
											continue;
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
		
		if(UserInput.control) {
			controlmap.update();
		}
		
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
		
		
		/*renderer.setColor(Color.WHITE);
		
		renderer.drawRect(new Rectangle(UserInput.x - 5, UserInput.y - 3, 10, 6));
		renderer.drawLine(new Vector2D(-2, UserInput.y), new Vector2D(UserInput.x - 5, UserInput.y), false);
		renderer.drawLine(new Vector2D(UserInput.x + 5, UserInput.y), new Vector2D(UserInput.x + 1000, UserInput.y), false);
		renderer.drawLine(new Vector2D(UserInput.x, -2), new Vector2D(UserInput.x, UserInput.y - 3), false);
		renderer.drawLine(new Vector2D(UserInput.x, UserInput.y + 3), new Vector2D(UserInput.x, 1000), false);*/
		
		if(currentstore == null) {
			if(UserInput.control) {
				controlmap.render(renderer);
			} else {
				int hx = 10;
				player.heart.setAnimation(2, 2, 0);
				player.heart.update();
				int i = 0;
				for(; i < player.getHealth() / 10; i++) {
					renderer.drawSprite(player.heart, new Vector2D(hx - 5, 5), false);
					hx += 16;
				}
				if(i != 10) {
					player.heart.setAnimation(0,0,0);
					player.heart.update();
					for(; i < 10; i++) {
						renderer.drawSprite(player.heart, new Vector2D(hx - 5, 5), false);
						hx += 16;
					}
				}
				hx = 10;
				for(int j = 0; j < 10; j++) {
					renderer.drawSprite(player.bodyarmor, new Vector2D(hx - 5, 25), false);
					hx += 16;
				}
				renderer.drawText("Gems: " + DungeonGame.coins, new Vector2D(5, 55));
				
				player.getInventory().render(renderer);
				
				minimap.render(renderer);
				
				renderer.setColor(new Color(0, 0,0,40));
				renderer.fillRect(2, 385, 190, 110);
				renderer.fillRect(525, 385, 170, 110);
				
				int cy = 405;
				for(int j = 0; j < 5; j++) {
					ControlPoint cp = cps[j];
					
					String status = "";
					Color c = Color.GREEN;
					if(cp.getOwnership() == 10000 && cp.getOwner() == 0) {
						status = "SAFE";
						c = Color.GREEN;
					} else if(cp.getOwnership() == 0 && cp.getOwner() == 1) {
						status = "TAKEN";
						c = Color.RED;
					} else if(cp.getOwnership() != 10000 && cp.getOwner() == 0) {
						status = "LOSING";
						c = Color.YELLOW;
					} else if(cp.getOwnership() != 0 && cp.getOwner() == 1) {
						status = "CAPTURING";
						c = Color.YELLOW;
					} else if(cp.getOwner() == -1) {
						status = "NEUTRAL";
						c = Color.BLUE;
					}
					
					renderer.setColor(c);
					renderer.drawText("Control Point " + (j+1) + ": " + status, new Vector2D(5, cy));
					
					cy += 20;
				}
				
				renderer.setColor(Color.BLACK);
				renderer.drawText("AGC Drones: " + agc, new Vector2D(534, 405));
				renderer.drawText("Sentry Guns: " + sg, new Vector2D(534, 425));
				renderer.drawText("Barricades: " + bc, new Vector2D(534, 445));
			}
		} else {
			currentstore.render(renderer);
		}
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
		this.player = p;
		player.setPosition(pspawn);
		entities.add(player);
	}
	
	public void setSize(Vector2D size) {
		this.size = size;
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
			if(test.getEntityType() == entity_type.ZOMBIE || test.getEntityType() == entity_type.SKELETON) {
				if(me.distanceSquared(test) < dist) {
					e = test;
					dist = me.distanceSquared(test);
				}
			}
		}
		
		return e;
	}

}