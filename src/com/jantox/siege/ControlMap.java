package com.jantox.siege;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

import com.jantox.siege.entities.AGC;
import com.jantox.siege.entities.Barricade;
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
import com.jantox.siege.entities.monsters.Spawner;
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
	
	public ControlMap(Map map) {
		this.map = map;
		numbers = new BitmapFont();
		icons = Assets.loadSprite("cmap_icons.png");
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer) {
		renderer.setColor(Color.BLACK);
		renderer.drawRect(new Rectangle(20, 20, 660, 460));
		renderer.setColor(new Color(0,0,0,200));
		renderer.fillRect(21, 21, 659, 459);
		List<Entity> entities = map.getEntities();
		
		for(Entity e : entities) {
			if(!(e instanceof ControlPoint)) {
				renderer.setColor(Color.BLACK);
				
				Vector2D epos = e.pos.copy();
				Vector2D center = new Vector2D(map.getSize().x / 2, map.getSize().y / 2);
				
				epos.subtract(center);
				epos.normalize();
				
				epos.multiply(Math.sqrt(center.distanceSquared(e.pos)) / 4.5);
				
				if(e instanceof Zombie || e instanceof Skeleton) {
					icons.setAnimation(3,3,0);
				} else if(e instanceof Fence) {
					renderer.setColor(new Color(139, 69, 69));
					renderer.fill(new Rectangle(epos.getX() + 350, epos.getY() + 250, 2, 2), false);
					continue;
				} else if(e instanceof AGC) { // e instanceof SentryGun
					icons.setAnimation(1,1,0);
				} else if(e instanceof SentryGun) { // e instanceof SentryGun
					icons.setAnimation(2,2,0);
				} /*else if(e instanceof Spawner) {
					renderer.setColor(new Color(255, 255, 255));
				}*/ else if(e instanceof Barricade) {
					icons.setAnimation(0, 0, 0);
				} else {
					continue;
				}
				icons.update();
				
				renderer.drawSprite(icons, new Vector2D(epos.x + 350 - 16, epos.y + 250 - 16), false);
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
