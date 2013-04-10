package com.jantox.siege;

import com.jantox.siege.entities.Entity;

public class PotionEffect {

	public int potionid;
	public int timeleft;
	
	public PotionEffect(int pid) {
		this.potionid = pid;
		if(pid == 0) {
			this.timeleft = 10;
			Entity.map.getPlayer().heal(20);
		} else if(pid == 1) {
			this.timeleft = 1200;
		} else if(pid == 2) {
			this.timeleft = 1200;
		} else if(pid == 3) {
			this.timeleft = 1200;
		}
	}
	
	public void update() {
		timeleft --;
	}
	
}
