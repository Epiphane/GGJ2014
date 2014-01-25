package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle extends Entity {
	public static int PARTICLE_SPEED = 5;
	
	private TextureRegion[][] spriteSheet;

	public Particle(int x, int y, Level level) {
		super(x, y, Art.particle[0][0], level);
		spriteSheet = Art.particle;
	}

	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.walkDirection();

		tryMove(offset.x * PARTICLE_SPEED, offset.y * PARTICLE_SPEED);
	}
	
	public boolean collide(float x, float y, float w, float h) {
//		if((x <= this.x - getWidth() || x + w >= this.x) && 
//				(y <= this.y - getHeight() || y + h >= this.y)){
//			System.out.println(" Snake: "+x+", "+y+" - "+w+"x"+h);
//			System.out.println(" Me: "+this.x+", "+this.y+" - "+getWidth()+"x"+getHeight());
//			return true;
//		}
		return false;
	}
}