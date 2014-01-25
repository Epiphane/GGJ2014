package com.band.ggjam;

import java.util.ArrayList;


public class Level {
	private ArrayList<Particle> particles;
	private ArrayList<Wave> waves;
	
	private Particle activeParticle;
	private Wave activeWave;
	/** You're either controlling a particle, or a wave.  This tells you which. */
	public boolean controllingParticle;
	
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
		particles = new ArrayList<Particle>();
		waves = new ArrayList<Wave>();
		
		controllingParticle = true;
	}
	
	public void addWave(int x, int y) {
		waves.add(new Wave(x, y));
	}
	
	public void addParticle(int x, int y) {
		particles.add(new Particle(x,y));
	}
	
	public void setActiveParticle(int particleNum) {
		activeParticle = particles.get(particleNum);
		controllingParticle = true;
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
				gameState.draw(Art.tiles[tiles[i][j]][0], i*GGJam.TILE_SIZE, j*GGJam.TILE_SIZE);
			}
		}
	}

	public void tick(Input input) {
		if (controllingParticle) {
			activeParticle.tick(input);
		} else {
			activeWave.tick(input);
		}
	}

	public void dispose() {
		
	}
	
}
