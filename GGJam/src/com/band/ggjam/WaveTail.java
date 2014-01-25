package com.band.ggjam;

public class WaveTail extends Entity {

	/** Which direction to shrink when you die */
	Point direction;

	private static int WAVE_SPEED = Wave.WAVE_SPEED;
	private static int DEATH_TICKS = Wave.MOVE_TICKS;
	private int deathTicks;
	
	public boolean dead = false;
	
	public WaveTail(int x, int y, Level level, Point direction) {
		super((int) GGJam.TILE_SIZE * x, (int) GGJam.TILE_SIZE * y, Art.wave[0][0], level);
		this.direction = direction;
	}
	
	public void die() {
		deathTicks = DEATH_TICKS;
	}
	
	public void tick() {
		if (deathTicks > 0) {
			deathTicks--;
			
			x += direction.x * WAVE_SPEED;
			y += direction.y * WAVE_SPEED;
			
			setPosition(x, y);
			
			if (deathTicks == 0) {
				dead = true;
			}
		}
	}

}
