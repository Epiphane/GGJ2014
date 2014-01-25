package com.band.ggjam;


public class Laser extends Entity {
	public static final float LASER_SPEED = 4;

	private int frame;
	
	private float dx, dy;
	private boolean toBeDead = false;
	
	public Laser(float x, float y, Level level, float dx, float dy) {
		super(x, y, Art.laser[0][0], level);
		
		this.dx = dx;
		this.dy = dy;
		
		float angle = (float) (Math.atan(dy / dx) * 180 / Math.PI);
		System.out.println(angle);
		setRotation(angle - 90);
		setOrigin(0,0);
	}
	
	public void tick() {
		if(toBeDead)
			dead = true;
		
		tryMove(dx, dy);
	}
	
	public void hitWall(float dx, float dy) {
		toBeDead = true;
		setRegion(Art.laser[0][2]);
	}
}
