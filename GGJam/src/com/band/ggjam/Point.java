package com.band.ggjam;

public class Point {
	public int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Translates the current point by the coordinates of the input point
	 * @param translateBy A point that will be added to the current point
	 * @return 
	 */
	public Point addPoint(Point translateBy) {
		x += translateBy.x;
		y += translateBy.y;
		
		return this;
	}
}
