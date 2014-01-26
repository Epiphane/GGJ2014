package com.band.ggjam;

public class Switch extends Entity {
	public static int SHOT_TIMER = 4;
	
	public boolean active;
	
	public Switch(int x, int y, Level level, int id) {
		super(x, y, Art.entities[2][0], level);
		
		
	}
	
	public void tick() {
		
	}
	
	public boolean canPass(Entity other) {
		return !(other instanceof Particle);
	}
	
	public boolean collide(float x, float y, float w, float h) {
		active = super.collide(x, y, w, h);
		if(active)
			setRegion(Art.entities[2][1]);
		else
			setRegion(Art.entities[2][0]);
		
		return false;
	}
}
