package com.band.ggjam;


public class Level {
	private Entity particle, wave;
	private GameState gameState;

	public int[][] tiles = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 0, 0, 1},
				{1, 1, 1, 1, 1}
		};
	
	public Level(int dimX, int dimy, GameState gameState) {
		this.gameState = gameState;
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
	
	public void render() {
		for(int i = 0; i < tiles.length; i ++) {
			for(int j = 0; j < tiles[0].length; j ++) {
				gameState.draw(Art.tiles[0][tiles[i][j]], i*GGJam.TILE_SIZE, j*GGJam.TILE_SIZE);
			}
		}
	}

	public void tick(Input input) {
		
	}

	public void dispose() {
		
	}
	
}
