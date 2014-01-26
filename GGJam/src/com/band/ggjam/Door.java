package com.band.ggjam;

public class Door extends Entity {
	public static int SHOT_TIMER = 4;
	
	boolean open = false;
	boolean defaultState = false;
	int[] triggerIDs;
	
	int frame;
	
	public Door(int x, int y, Level level, String trigger) {
		super(x, y, Art.entities[1][0], level);
		
		String[] triggers = trigger.split("/");
		triggerIDs = new int[triggers.length];
		for(int i = 0; i < triggers.length; i ++) {
			triggerIDs[i] = Integer.parseInt(triggers[i]);
		}
		
		frame = 0;
	}
	
	public Door(int x, int y, Level level, String trigger, boolean defaultState) {
		this(x, y, level, trigger);
		
		this.defaultState = defaultState;
	}
	
	public void tick() {
		open = defaultState;
		for(int i = 0; i < triggerIDs.length; i ++) {
			if(currentLevel.switches.get(triggerIDs[i]).active)
				open = !defaultState;
		}
		
		setState();
	}
	
	public boolean collide(float x, float y, float w, float h) {
		return super.collide(x, y, w, h) && !open;
	}
	
	public void setState() {
		if(open)
			setRegion(Art.entities[1][2]);
		else {
			setRegion(Art.entities[1][frame++ / 20]);
			if(frame >= 40) frame = 0;
		}
	}
}
