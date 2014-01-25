package com.band.ggjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;


public class Level {
	private Entity particle, wave;
	private GameState gameState;
	
	private SpriteBatch batch;

	public int[][] tiles = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 0, 0, 1},
				{1, 1, 1, 1, 1}
		};
	
	public Level(int dimX, int dimy, GameState gameState) {
		this.gameState = gameState;
		
		particle = new Particle(50,50);
		wave = new Wave(100);
		
		batch = new SpriteBatch(100);
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
		batch.begin();
		for(int i = 0; i < tiles.length; i ++) {
			for(int j = 0; j < tiles[0].length; j ++) {
				batch.draw(Art.tiles[tiles[i][j]][0], i*GGJam.TILE_SIZE, j*GGJam.TILE_SIZE);
			}
		}
		
		particle.draw(batch);
		wave.draw(batch);
		batch.end();
	}

	public void tick(Input input) {
		particle.tick();
		wave.tick();
	}

	public void dispose() {
	}
}
