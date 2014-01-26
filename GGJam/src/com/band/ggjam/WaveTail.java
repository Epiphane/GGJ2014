package com.band.ggjam;

import java.util.ArrayList;

public class WaveTail extends Entity implements Comparable {

	/** Which direction to shrink when you die */
	Point direction;

	/** Only used on initial initialization */
	int index;
	int tileX, tileY;
	int artIndex = 0;

	private static int WAVE_SPEED = Wave.WAVE_SPEED;
	private static int DEATH_TICKS = Wave.MOVE_TICKS;
	private int deathTicks;

	public WaveTail(int x, int y, Level level, Point direction, int lastDirection) {
		super(x, y, Art.wave[0][0], level);
		tileX = (int) (x / GGJam.TILE_SIZE);
		tileY = (int) (y / GGJam.TILE_SIZE);
		if (direction != null) {
			setDirection(direction, lastDirection);
		}
	}

	public WaveTail(int x, int y, Level level, int index) {
		this(x, y, level, null, -1);
		this.index = index;
	}
	
	//  7 0 1
	//  6 X 2
	//  5 4 3
	//
	public int setDirection(Point direction, int lastDirection) {
		this.direction = direction;

		int currDirection = Utility.directionFromOffset(direction);
		
		if (lastDirection == -1 || lastDirection == currDirection) {
			this.setRotation(Utility.dirToDegree(direction));
			if (direction.x == 1) {
				this.setRotation(0);
			}
			if (direction.y == 1) {
				this.setRotation(90);
			}
		} else {
			if (lastDirection == 0 && currDirection == 2) {
				artIndex = 1;
			} else if (lastDirection == 2 && currDirection == 4) {
				artIndex = 2;
			} else if (lastDirection == 4 && currDirection == 6) {
				artIndex = 3;
			} else if (lastDirection == 6 && currDirection == 0) {
				artIndex = 4;
			} else if (lastDirection == 0 && currDirection == 6) {
				artIndex = 5;
			} else if (lastDirection == 2 && currDirection == 0) {
				artIndex = 6;
			} else if (lastDirection == 4 && currDirection == 2) {
				artIndex = 7;
			} else if (lastDirection == 6 && currDirection == 4) {
				artIndex = 8;
			}
		}
		
		this.setRegion(Art.wave[0][artIndex]);

		return Utility.directionFromOffset(direction);
	}

	public void die() {
		deathTicks = DEATH_TICKS;
	}

	public void tick() {
		if (deathTicks > 0) {
			deathTicks--;

			if (direction == null) {
				System.out.println("NULL DIRECTION YO");
			}

			x += direction.x * WAVE_SPEED;
			y += direction.y * WAVE_SPEED;

			if (direction.x < 0) {
				this.setRegion(Art.wave[(deathTicks) % 4][artIndex]);
			} else if (direction.x > 0) {
				this.setRegion(Art.wave[3 - (deathTicks + 3) % 4][artIndex]);
			} else if (direction.y < 0) {
				this.setRegion(Art.wave[(deathTicks) % 4][artIndex]);
			} else if (direction.y > 0) {
				this.setRegion(Art.wave[3 - (deathTicks + 3) % 4][artIndex]);
			}

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

	public boolean canPass(Entity other) {
		return (other instanceof Laser);
	}
}
