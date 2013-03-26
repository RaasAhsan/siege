package com.jantox.siege.sfx;

import java.util.Random;


public abstract class Sound {
	
	protected int duration;
	protected int p = 0;
	
	Random rand = new Random();
	
	protected Sound(int duration) {
		this.duration = Sounds.SAMPLE_RATE * duration / 1000;
	}
	
	public boolean read(int[] buffer, int len) {
		if (p+len>duration) len = duration-p;
        fill(buffer, len);
        return p <= duration;
	}
	
	protected abstract void fill(int[] buffer, int len);

	public static class Place extends Sound {

		public Place() {
			super(10);
		}
		
		@Override
		protected void fill(int[] buffer, int len) {
			for (int i=0; i<len; i++, p++) {
                buffer[i] += ((i*500/Sounds.SAMPLE_RATE)%2*1000)*(duration-p)/duration;
            }
		}
		
	}
	
	public static class Blast extends Sound {

		public Blast() {
			super(10);
		}
		
		@Override
		protected void fill(int[] buffer, int len) {
			for (int i=0; i<len; i++, p++) {
                buffer[i] += ((i*200/Sounds.SAMPLE_RATE)%2*500)*(duration-p)/duration;
            }
		}
		
	}
	
	public static class Drink extends Sound {
		
		public Drink() {
			super(10);
		}
		
		@Override
		protected void fill(int[] buffer, int len) {
			for (int i=0; i<len; i++, p++) {
                buffer[i] += ((i*Math.cos(i)*i/Sounds.SAMPLE_RATE)%2*500)*(duration-p)/duration;
            }
		}
		
	}
	
	public static class Select extends Sound {

		public Select() {
			super(10);
		}

		@Override
		protected void fill(int[] buffer, int len) {
			for (int i=0; i<len; i++, p++) {
                buffer[i] += ((i*1000/Sounds.SAMPLE_RATE)%2*100)*(duration-p)/duration;
            }
		}
		
	}
	
}
