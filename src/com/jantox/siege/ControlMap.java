package com.jantox.siege;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.jantox.siege.colsys.Circle;
import com.jantox.siege.colsys.CollisionSystem;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Decoration;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Fence;
import com.jantox.siege.entities.Gate;
import com.jantox.siege.entities.Gem;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.Tree;
import com.jantox.siege.entities.drones.AGC;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.entities.monsters.Skeleton;
import com.jantox.siege.entities.monsters.MonsterFactory;
import com.jantox.siege.entities.monsters.Zombie;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class ControlMap {

	private Map map;
	
	private BitmapFont numbers;
	private Sprite icons;
	private Sprite flag;
	private Sprite agcselect;
	
	private Keyboard input;
	
	private List<Entity> drones;
	private List<Entity> rdrones;
	
	private Entity selected = null;
	
	public ControlMap(Map map, Keyboard input) {
		this.map = map;
		numbers = new BitmapFont();
		icons = Assets.loadSprite("cmap_icons.png");
		flag = Assets.loadSprite("flag.png");
		agcselect = Assets.loadSprite("agc_select.png");
		drones = new ArrayList<Entity>();
		rdrones = new ArrayList<Entity>();
		this.input = input;
	}
	
	public void update() {
		drones.clear();
		rdrones.clear();
		
		for(Entity e : map.getEntities()) {
			if(!(e instanceof ControlPoint)) {
				Vector2D epos = e.pos.copy();
				Vector2D center = new Vector2D(map.getSize().x / 2, map.getSize().y / 2);
				
				epos.subtract(center);
				epos.normalize();
				
				epos.multiply(Math.sqrt(center.distanceSquared(e.pos)) / 4.5);
				
				if(e instanceof AGC) {
					rdrones.add(e);
					drones.add(new AGC(new Vector2D(epos.x + 350, epos.y + 250)));
				}
			}
		}
	}

	public void render(Renderer renderer) {
		renderer.setColor(Color.BLACK);
		renderer.drawRect(new Rectangle(20, 20, 660, 460));
		renderer.setColor(new Color(125,200,80));
		renderer.fillRect(21, 21, 659, 459);
		List<Entity> entities = map.getEntities();
		
		for(Entity e : entities) {
			if(!(e instanceof ControlPoint)) {
				renderer.setColor(Color.BLACK);
				
				Vector2D epos = this.convertToCMapPos(e.pos);
				
				if(e instanceof Zombie || e instanceof Skeleton) {
					icons.setAnimation(3,3,0);
				} else if(e instanceof Fence) {
					renderer.setColor(new Color(139, 69, 69));
					renderer.fill(new Rectangle(epos.getX(), epos.getY(), 2, 2), false);
					continue;
				} else if(e instanceof AGC) {
					icons.setAnimation(1,1,0);
				} else if(e instanceof SentryGun) {
					icons.setAnimation(2,2,0);
				} else if(e instanceof MonsterFactory) {
					icons.setAnimation(4, 4, 0);
				} else if(e instanceof Barricade) {
					icons.setAnimation(0, 0, 0);
				} else {
					continue;
				}
				icons.update();
				
				renderer.drawSprite(icons, new Vector2D(epos.x - 16, epos.y - 16), false);
				if(e instanceof AGC) {
					if(e == selected)
						renderer.drawSprite(agcselect, new Vector2D(epos.x - 16, epos.y - 16), false);
				}
			} else {
				Vector2D epos = this.convertToCMapPos(e.pos);
				
				String cps = String.valueOf(((ControlPoint)e).getControlPointID());
				this.numbers.render(renderer, new Vector2D(epos.x - 16, epos.y - 16), cps);
			}
		}
		
		for(Entity e : rdrones) {
			if(selected == e) {
				Vector2D cpos = convertToCMapPos(((AGC)e).next_pos);
				cpos.x -= 16;
				cpos.y -= 16;
				renderer.drawSprite(flag, cpos, false);
			}
		}
	}
	
	public Vector2D convertFromCMapPos(Vector2D mouse) {
		Vector2D pos = mouse.copy();
		
		pos.subtract(new Vector2D(350, 250));
		pos.normalize();
		pos.multiply(Math.sqrt(new Vector2D(350, 250).distanceSquared(mouse)));
		pos.multiply(4.25);
		pos.add(new Vector2D(map.getSize().x / 2, map.getSize().y / 2));
		
		return pos;
	}
	
	public Vector2D convertToCMapPos(Vector2D e) {
		Vector2D epos = e.copy();
		Vector2D center = new Vector2D(map.getSize().x / 2, map.getSize().y / 2);
		
		epos.subtract(center);
		epos.normalize();
		
		epos.multiply(Math.sqrt(center.distanceSquared(e)) / 4.25);
		
		epos.add(new Vector2D(350, 250));
		
		return epos;
	}

	public void click() {
		Vector2D mouse = new Vector2D(map.getPlayer().input.x, map.getPlayer().input.y);
		if(map.getPlayer().input.left_mouse) {
			int i = 0;
			for(Entity e : drones) {
				if(CollisionSystem.collides(new Circle(mouse, 5), new Circle(e.pos, 5))) {
					if(selected == null) {
						System.out.println("hello");
						selected = ((AGC)this.rdrones.get(i));
						break;
					}
				}
				i++;
			}
			if(selected != null) {
				((AGC)selected).next_pos = convertFromCMapPos(mouse);
			}
		} else if(map.getPlayer().input.right_mouse) {
			if(selected != null) {
				selected = null;
			}
		}
	}
	
}
