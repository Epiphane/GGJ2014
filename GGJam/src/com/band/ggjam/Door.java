package com.band.ggjam;

public class Door extends Entity {
	public static int SHOT_TIMER = 4;
	
	boolean open = false;
	
	
	
	public Door(int x, int y, Level level, String trigger) {
		super(x, y, Art.entities[1][0], level);
		
		
	}
	
	public void tick() {
		
	}
	
	public boolean canPass(Entity other) {
		return open;
	}
	
	public void toggleState(boolean open) {
		this.open = open;
		if(open)
			setRegion(Art.entities[1][0]);
		else
			setRegion(Art.entities[1][1]);
	}
}
