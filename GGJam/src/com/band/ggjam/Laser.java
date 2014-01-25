package com.band.ggjam;


public class Laser extends Entity {
	public static final float LASER_SPEED = 4;
	public static final int LASER_FRAMES = 10;

	private int frame;
	
	private float dx, dy;
	private boolean toBeDead = false;
	
	public Laser(float x, float y, Level level, float dx, float dy, int frameStart) {
		super(x, y + 10, Art.laser[0][0], level);
		
		this.dx = dx;
		this.dy = dy;
		
		float angle = (float) (Math.atan(dy / dx) * 180 / Math.PI);
		setOrigin(16,6);
		setRotation(angle - 90);
		
		if(dx < 0)
			setRotation(angle - 270);
		
		frame = frameStart;
	}
	
	public void tick() {
		setRegion(Art.laser[0][frame * 2 / LASER_FRAMES]);
		if(++frame >= LASER_FRAMES) frame = 0;
		
		if(toBeDead)
			dead = true;
		
		tryMove(dx, dy);
	}
	
//	public float getWidth() {
//		if(this.getRotation() % 180 != 0)
//			return 1;
//		else 
//			return 1;
//	}
	
	public void hitWall(float dx, float dy) {
		toBeDead = true;
		setRegion(Art.laser[0][2]);
		
		x += dx;
		y +=dy;
	}
	
	public boolean canPass(Entity other) {
		return (other instanceof Laser) || (other instanceof Emitter);
	}
}
