package com.band.ggjam;

public class Utility {
	/**
	 * Change from a direction (number from 0-7) to a set of
	 * offset coordinates.
	 */
	public static Point offsetFromDirection(int dir) {
		switch(dir) {
		case 0:
			return new Point(0, 1);
		case 1:
			return new Point(1, 1);
		case 2:
			return new Point(1, 0);
		case 3:
			return new Point(1, -1);
		case 4:
			return new Point(0, -1);
		case 5:
			return new Point(-1, -1);
		case 6:
			return new Point(-1, 0);
		case 7:
			return new Point(-1, 1);
		default:
			System.out.println("What just happened??? Tried to get offset from direction: " + dir);
			break;
		}
		
		return null;
	}
}
