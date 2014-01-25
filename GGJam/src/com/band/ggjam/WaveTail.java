package com.band.ggjam;

public class WaveTail extends Entity implements Comparable {

	/** Which direction to shrink when you die */
	Point direction;
	
	/** Only used on initial initialization */
	int index;
	int tileX, tileY;

	private static int WAVE_SPEED = Wave.WAVE_SPEED;
	private static int DEATH_TICKS = Wave.MOVE_TICKS;
	private int deathTicks;
	
	public WaveTail(int x, int y, Level level, Point direction) {
		super(x, y, Art.wave[0][0], level);
		tileX = (int) (x / GGJam.TILE_SIZE);
		tileY = (int) (y / GGJam.TILE_SIZE);
		this.direction = direction;
	}
	
	public WaveTail(int x, int y, Level level, int index) {
		this(x, y, level, null);
		this.index = index;
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

	@Override
	public int compareTo(Object arg0) {
		return ((WaveTail) arg0).index - index;
	}
}
