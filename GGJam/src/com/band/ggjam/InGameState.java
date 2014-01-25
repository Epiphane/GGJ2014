package com.band.ggjam;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class InGameState extends GameState {
	private Level currentLevel;
	
	public InGameState() {		
		camera = new OrthographicCamera(GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
		
		currentLevel = new Level(40, 20, this);
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		currentLevel.tick(input);
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

}

