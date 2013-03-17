package com.jantox.dungmast;

import java.awt.Color;
import java.util.List;

import com.jantox.dungmast.entities.AGC;
import com.jantox.dungmast.entities.ControlPoint;
import com.jantox.dungmast.entities.Decoration;
import com.jantox.dungmast.entities.Entity;
import com.jantox.dungmast.entities.Fence;
import com.jantox.dungmast.entities.Gate;
import com.jantox.dungmast.entities.Gem;
import com.jantox.dungmast.entities.Player;
import com.jantox.dungmast.entities.Projectile;
import com.jantox.dungmast.entities.SentryGun;
import com.jantox.dungmast.entities.Skeleton;
import com.jantox.dungmast.entities.Tree;
import com.jantox.dungmast.entities.Zombie;
import com.jantox.dungmast.math.Vector2D;

public class Minimap {

	private Vector2D pos;
	private Map map;
	
	public Minimap(Map map) {
		this.map = map;
		
		this.pos = new Vector2D(535, 15);
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
			} else if(e instanceof ControlPoint) {
				renderer.setColor(new Color(255, 255, 0));
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
		}
		
		/*for(int i = 0; i < 10000; i++) {
			renderer.setColor(Color.BLACK);
			Vector2D v = new Vector2D(pos.x + rand.nextGaussian() * 25 + 75, pos.y + rand.nextGaussian() * 25 + 75);
			renderer.drawLine(v, v, false);
		}*/
	}
	
}
