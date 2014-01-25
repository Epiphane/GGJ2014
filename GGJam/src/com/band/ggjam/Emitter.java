package com.band.ggjam;

public class Emitter extends Entity {
	public static int SHOT_TIMER = 4;
	
	int shotDelay;
	
	public Emitter(int x, int y, Level level) {
		super(x, y, level);
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		if(shotDelay++ > SHOT_TIMER) {
			currentLevel.add(new Laser(x, y, currentLevel, Laser.LASER_SPEED, 0));
			currentLevel.add(new Laser(x, y, currentLevel, -Laser.LASER_SPEED, 0));
			currentLevel.add(new Laser(x, y, currentLevel, 0, -Laser.LASER_SPEED));
			currentLevel.add(new Laser(x, y, currentLevel, 0, Laser.LASER_SPEED));
		}
	}
}
