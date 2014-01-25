package com.band.ggjam;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InGameState extends GameState {
	private Level currentLevel;
	
	public InGameState() {		
		camera = new OrthographicCamera(GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
		spriteBatch = new SpriteBatch(100);
		
		currentLevel = new Level(40, 20, this);
		
		// TODO: un-hard-code this
		currentLevel.addWave(10, 10);
		currentLevel.setActiveWave(0);
		
		currentLevel.addParticle(5, 5);
		currentLevel.setActiveParticle(0);
	}
	
	@Override
	public void render() {
		spriteBatch.begin();
		currentLevel.render();
		spriteBatch.end();
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

