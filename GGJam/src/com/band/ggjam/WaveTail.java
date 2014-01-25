package com.band.ggjam;

public class WaveTail extends Entity {

	public WaveTail(int x, int y, Level level) {
		super((int) GGJam.TILE_SIZE * x, (int) GGJam.TILE_SIZE * y, Art.wave[0][0], level);
		
	}

}
