package com.band.ggjam;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class GGJam implements ApplicationListener {
	public static final int GAME_WIDTH = 384;
	public static final int GAME_HEIGHT = 256;
	public final static float DISPLAY_TILE_SCALE = 3f;
	public final static float MULTIPLIER_FOR_GOOD_CALCULATIONS = 3;
	public final static float TILE_SIZE = 32f;
	
	public final static float FRAMERATE = 60f;
	
	private GameState gameState;

	/**
	 * Keeps track of delay in order to run updates possibly multiple times
	 */
	private float accumulatedTime = 0;
	
	/**
	 * Keeps track of all inputs
	 */
	private final Input input = new Input();
	
	@Override
	public void create() {
		Art.load();
		gameState = new InGameState();
		gameState.init(this);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void dispose() {
		gameState.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		accumulatedTime += Gdx.graphics.getDeltaTime();
		while(accumulatedTime > 1.0f / FRAMERATE) {
			gameState.tick(input);
			input.tick();
			accumulatedTime -= 1.0f / FRAMERATE;
		}
		gameState.render();
	}
	
	public void setScreen(GameState newState) {
		if(gameState != null) gameState.removed();
		gameState = newState;
		if(gameState != null) gameState.init(this);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
