package com.band.ggjam;

import java.util.ArrayList;

public class Level {

	public int[][] tiles = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 1, 1, 1}
		};
	public Entity mainChar;
	
	public Level(int dimX, int dimy, Entity mainChar) {
		this.mainChar = mainChar;
	}
	
	/**
	 * Can the main character move to the point X, Y?
	 * @param x location of point we want to move TO
	 * @param y
	 * @return CAN WE MOVE THERE OR NOT
	 */
	public boolean canMove(int x, int y) {
		return tiles[x][y] == 0;
	}
	
	
	
}
