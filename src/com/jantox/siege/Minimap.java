package com.jantox.siege;

import java.awt.Color;
import java.util.List;

import com.jantox.siege.entities.AGC;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Decoration;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Fence;
import com.jantox.siege.entities.Gate;
import com.jantox.siege.entities.Gem;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.SentryGun;
import com.jantox.siege.entities.Tree;
import com.jantox.siege.entities.monsters.Skeleton;
import com.jantox.siege.entities.monsters.Zombie;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;

public class Minimap {

	private Vector2D pos;
	private Map map;
	
	private BitmapFont numbers;
	
	public Minimap(Map map) {
		this.map = map;
		
		this.pos = new Vector2D(535, 15);
		
		numbers = new BitmapFont();
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer) {
		renderer.setColor(new Color(67, 140, 34, 80));
		renderer.fillCircle(pos, new Vector2D(150, 150));
		renderer.setColor(Color.BLACK);
		renderer.drawCircle(pos, new Vector2D(150, 150));
		
		Vector2D ppos = new Vector2D(pos.getX() + 75, pos.getY() + 75);
		
		renderer.setColor(Color.WHITE);
		renderer.fillRect(ppos.getX(), ppos.getY(), 2, 2);
		
		Player p = map.getPlayer();
		List<Entity> entities = map.getEntities(p, 500);
		
		for(Entity e : entities) {
			renderer.setColor(Color.BLACK);
			if(!(e instanceof ControlPoint)) {
				if(e instanceof Zombie || e instanceof Skeleton) {
					renderer.setColor(Color.RED);
				} else if(e instanceof Fence) {
					renderer.setColor(new Color(139, 69, 69));
				} else if(e instanceof Gate) {
					renderer.setColor(new Color(139, 60, 60));
				} else if(e instanceof Tree) {
					renderer.setColor(new Color(0, 100, 0));
				} else if(e instanceof SentryGun || e instanceof AGC) {
					renderer.setColor(new Color(0, 0, 200));
				} else if(e instanceof Projectile || e instanceof Gem) {
					continue;
				}
				
				Vector2D to = e.pos.copy();
				to.subtract(p.getPosition());
				
				to.normalize();
				to.multiply(Math.sqrt(e.distanceSquared(p)) / 5);
				
				to.add(ppos);
				
				if(to.distanceSquared(ppos) < 75 * 75) {
					if(e instanceof Decoration) {
						if(((Decoration)e).getType() == Decoration.FOOTPATH) {
							renderer.setColor(Color.gray);
							renderer.fillRect(to.getX(), to.getY(), 7, 7);
						} else
							renderer.fillRect(to.getX(), to.getY(), 2, 2);
					} else {
						renderer.fillRect(to.getX(), to.getY(), 2, 2);
					}
				}
			} else {
				Vector2D to = e.pos.copy();
				to.subtract(p.getPosition());
				
				to.normalize();
				to.multiply(Math.sqrt(e.distanceSquared(p)) / 5);
				
				to.add(ppos);
				if(to.distanceSquared(ppos) <= 65 * 65) {
					String cps = String.valueOf(((ControlPoint)e).getControlPointID());
					this.numbers.render(renderer, new Vector2D(to.x - 16, to.y - 16), cps);
				}
			}
		}
		
		/*for(int i = 0; i < 10000; i++) {
			renderer.setColor(Color.BLACK);
			Vector2D v = new Vector2D(pos.x + rand.nextGaussian() * 25 + 75, pos.y + rand.nextGaussian() * 25 + 75);
			renderer.drawLine(v, v, false);
		}*/
	}
	
}
