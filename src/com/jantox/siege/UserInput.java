package com.jantox.siege;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import com.jantox.siege.entities.Entity;

public class UserInput implements KeyListener, MouseMotionListener, MouseListener {
	
	public static boolean up, down, left, right, control;
	public static boolean space;
	
	public static int x, y;
	
	public static boolean left_mouse;
	
	public boolean inputs[] = new boolean[16];
	
	private HashMap<Integer, Integer> map;
	
	private DungeonGame dg;
	
	public UserInput(DungeonGame dg) {
		this.dg = dg;
		
		map = new HashMap<Integer, Integer>();
		
		this.map(0, KeyEvent.VK_UP);
		this.map(1, KeyEvent.VK_RIGHT);
		this.map(2, KeyEvent.VK_DOWN);
		this.map(3, KeyEvent.VK_LEFT);
		this.map(4, KeyEvent.VK_C);
	}
	
	public void map(int val, int key) {
		map.put(key, val);
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		Entity.rand.nextInt();
		if(ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_W) {
			up = true;
		} else if(ke.getKeyCode() == KeyEvent.VK_DOWN  || ke.getKeyCode() == KeyEvent.VK_S) {
			down = true;
		} else if(ke.getKeyCode() == KeyEvent.VK_LEFT  || ke.getKeyCode() == KeyEvent.VK_A) {
			left = true;
		} else if(ke.getKeyCode() == KeyEvent.VK_RIGHT  || ke.getKeyCode() == KeyEvent.VK_D) {
			right = true;
		} else if(ke.getKeyCode() == KeyEvent.VK_SPACE) {
			space = true;
			if(dg.map.currentstore != null) {
				dg.map.currentstore.buy();
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_C) {
			control = !control;
		}
		
		if(ke.getKeyCode() >= KeyEvent.VK_1 && ke.getKeyCode() <= KeyEvent.VK_9) {
			int selected = ke.getKeyCode() - KeyEvent.VK_1;
			if(Entity.map.getPlayer().getInventory().selected != selected) {
				Entity.map.getPlayer().getInventory().selected = selected;
				Entity.map.getPlayer().getInventory().onHeldItem();
			}
		}
		
		if(ke.getKeyCode() == KeyEvent.VK_P){
			DungeonGame.paused = !DungeonGame.paused;
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_UP  || ke.getKeyCode() == KeyEvent.VK_W) {
			up = false;
		} else if(ke.getKeyCode() == KeyEvent.VK_DOWN  || ke.getKeyCode() == KeyEvent.VK_S) {
			down = false;
		} else if(ke.getKeyCode() == KeyEvent.VK_LEFT  || ke.getKeyCode() == KeyEvent.VK_A) {
			left = false;
		} else if(ke.getKeyCode() == KeyEvent.VK_RIGHT  || ke.getKeyCode() == KeyEvent.VK_D) {
			right = false;
		} else if(ke.getKeyCode() == KeyEvent.VK_SPACE) {
			space = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		Entity.rand.nextInt();
		x = me.getX();
		y = me.getY();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		Entity.rand.nextInt();
		x = me.getX();
		y = me.getY();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Entity.rand.nextInt();
		left_mouse = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		left_mouse = false;
	}

}
