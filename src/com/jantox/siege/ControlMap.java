package com.jantox.siege;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

import com.jantox.siege.entities.AGC;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Fence;
import com.jantox.siege.entities.Gate;
import com.jantox.siege.entities.Gem;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.SentryGun;
import com.jantox.siege.entities.Skeleton;
import com.jantox.siege.entities.Spawner;
import com.jantox.siege.entities.Tree;
import com.jantox.siege.entities.Zombie;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;

public class ControlMap {

	Map map;
	BitmapFont numbers;
	
	public ControlMap(Map map) {
		this.map = map;
		numbers = new BitmapFont();
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer) {
		renderer.setColor(Color.BLACK);
		renderer.drawRect(new Rectangle(20, 20, 660, 460));
		renderer.setColor(new Color(0,0,0,80));
		renderer.fillRect(20, 20, 660, 460);
		List<Entity> entities = map.getEntities();
		
		for(Entity e : entities) {
			if(!(e instanceof ControlPoint)) {
				renderer.setColor(Color.BLACK);
				if(e instanceof Zombie || e instanceof Skeleton) {
					renderer.setColor(Color.RED);
				} else if(e instanceof Fence) {
					renderer.setColor(new Color(139, 69, 69));
				} else if(e instanceof Gate) {
					renderer.setColor(new Color(139, 60, 60));
				} else if(e instanceof Tree) {
					renderer.setColor(new Color(0, 150, 0));
				} else if(e instanceof SentryGun || e instanceof AGC) {
					renderer.setColor(new Color(0, 0, 200));
				} else if(e instanceof Spawner) {
					renderer.setColor(new Color(255, 255, 255));
				} else if(e instanceof Projectile || e instanceof Gem) {
					continue;
				}
				
				Vector2D epos = e.pos.copy();
				Vector2D center = new Vector2D(map.getSize().x / 2, map.getSize().y / 2);
				
				epos.subtract(center);
				epos.normalize();
				
				epos.multiply(Math.sqrt(center.distanceSquared(e.pos)) / 4.5);
				
				renderer.fill(new Rectangle(350 + epos.getX(), 250 + epos.getY(), 3, 3), false);
			} else {
				Vector2D epos = e.pos.copy();
				Vector2D center = new Vector2D(map.getSize().x / 2, map.getSize().y / 2);
				
				epos.subtract(center);
				epos.normalize();
				
				epos.multiply(Math.sqrt(center.distanceSquared(e.pos)) / 4.5);
				
				String cps = String.valueOf(((ControlPoint)e).getControlPointID());
				this.numbers.render(renderer, new Vector2D( 350 + epos.x - 16, 250 + epos.y - 16), cps);
			}
		}
	}
	
}
