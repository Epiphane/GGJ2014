package com.band.ggjam;

import java.util.ArrayList;

public class WaveTail extends Entity implements Comparable {

	/** Which direction to shrink when you die */
	Point direction;

	/** Only used on initial initialization */
	int index;
	int tileX, tileY;
	int artIndex = 0;

	public static int WAVE_SPEED = Wave.WAVE_SPEED;
	public static int MOVE_TICKS = Wave.MOVE_TICKS * 2;
	public int deathTicks = 0;
	public int moveTicks = 0;

	public WaveTail(int x, int y, Level level, Point direction, int lastDirection) {
		super(x, y, Art.wave[0][0], level);
		tileX = (int) (x / GGJam.TILE_SIZE);
		tileY = (int) (y / GGJam.TILE_SIZE);
		if (direction != null) {
			setDirection(direction, lastDirection);
		}
		
		moveTicks = MOVE_TICKS;
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
				//this.setRotation(0);
			}
			if (direction.y == 1) {
				//this.setRotation(90);
			}
		} else {
			if (lastDirection == 0 && currDirection == 2) {
				artIndex = 1;
				setRotation(90);
			} else if (lastDirection == 2 && currDirection == 4) {
				artIndex = 1;
				setRotation(0);
			} else if (lastDirection == 4 && currDirection == 6) {
				artIndex = 1;
				setRotation(-90);
			} else if (lastDirection == 6 && currDirection == 0) {
				artIndex = 1;
				setRotation(180);
			} else if (lastDirection == 0 && currDirection == 6) {
				artIndex = 2;
				setRotation(90);
			} else if (lastDirection == 2 && currDirection == 0) {
				artIndex = 2;
				setRotation(0);
			} else if (lastDirection == 4 && currDirection == 2) {
				artIndex = 2;
				setRotation(-90);
			} else if (lastDirection == 6 && currDirection == 4) {
				artIndex = 2;
				setRotation(180);
			}
		}
		
		return Utility.directionFromOffset(direction);
	}

	public void die() {
		deathTicks = MOVE_TICKS;
	}

	public void tick() {
		if (deathTicks > 0) {
			deathTicks--;

			if (direction == null) {
				System.out.println("NULL DIRECTION YO");
			}

			if (deathTicks == 0) {
				dead = true;
			}
		} else if (moveTicks > 0) {
			//System.out.println(moveTicks);
			moveTicks--;
		}
		setRegion(Art.wave[3 - moveTicks / 2][artIndex]);
		
	}

	@Override
	public int compareTo(Object arg0) {
		return ((WaveTail) arg0).index - index;
	}

	public boolean canPass(Entity other) {
		return (other instanceof Laser);
	}
}
