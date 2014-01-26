package com.band.ggjam;

public class MiniParticle extends Particle {
	private float dx, dy;
	
	public MiniParticle(float x, float y, Level level, float dx, float dy) {
		super((int) x, (int) y, level);
		setSize(getWidth()/5, getHeight()/5);
		
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {

		setPosition(x + dx, y + dy);
	}
}
