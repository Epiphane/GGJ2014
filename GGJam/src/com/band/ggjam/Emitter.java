package com.band.ggjam;

public class Emitter extends Entity {
	public static int SHOT_TIMER = 2;
	
	int shotDelay;
	private int frame;
	
	public Emitter(int x, int y, Level level) {
		super(x, y, Art.entities[0][0], level);
		
		frame = 0;
	}
	
	public void tick() {
		frame += 2;
		if(frame >= Laser.LASER_FRAMES) frame = 0;
		if(shotDelay++ > SHOT_TIMER) {
			if(currentLevel.canMove(this, x + getWidth(), y, 0, 0)) currentLevel.add(new Laser(x, y, currentLevel, Laser.LASER_SPEED, 0, frame), true);
			if(currentLevel.canMove(this, x - getWidth(), y, 0, 0)) currentLevel.add(new Laser(x, y, currentLevel, -Laser.LASER_SPEED, 0, frame), true);
			if(currentLevel.canMove(this, x, y - getHeight(), 0, 0)) currentLevel.add(new Laser(x, y, currentLevel, 0, -Laser.LASER_SPEED, frame), true);
			if(currentLevel.canMove(this, x, y + getHeight() + 10, 0, 0)) currentLevel.add(new Laser(x, y, currentLevel, 0, Laser.LASER_SPEED, frame), true);
			shotDelay = 0;
//			currentLevel.add(new Laser(x, y, currentLevel, Laser.LASER_SPEED, Laser.LASER_SPEED, frame), true);
//			currentLevel.add(new Laser(x, y, currentLevel, -Laser.LASER_SPEED, -Laser.LASER_SPEED, frame), true);
//			currentLevel.add(new Laser(x, y, currentLevel, Laser.LASER_SPEED, -Laser.LASER_SPEED, frame), true);
//			currentLevel.add(new Laser(x, y, currentLevel, -Laser.LASER_SPEED, Laser.LASER_SPEED, frame), true);
		}
	}
}
