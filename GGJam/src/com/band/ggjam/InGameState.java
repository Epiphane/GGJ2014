package com.band.ggjam;

import java.io.File;
import java.util.logging.FileHandler;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InGameState extends GameState {
	private Level currentLevel;
	
	protected Music initMusic;
	protected Music swapMusic;
	
	private String levelName;
	
	public InGameState() {	
		levelName = "level1";
		
		camera = new OrthographicCamera(GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
		spriteBatch = new SpriteBatch(100);
		
		// Get and set music variables for Tutorial Level
		FileHandle swapHandler = null;
		FileHandle initHandler = null;
		try{
			initHandler = Gdx.files.internal("audio/tut2.dot.wav");
			swapHandler = Gdx.files.internal("audio/tut2.dot.wav"); //TODO: get rid of this? I hacked it in to stop exception
		}catch(Exception e) {
			System.out.println("Encountered error: " + e);
		}
		initMusic = Gdx.audio.newMusic(initHandler);
		swapMusic = Gdx.audio.newMusic(swapHandler);
		swapMusic.setLooping(true);
		initMusic.setLooping(true);
		swapMusic.setVolume(0);
		initMusic.setVolume(1);

		restartLevel();
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		if(input.buttonStack.find(Input.RESTART) != null) {
			restartLevel();
		}
		else {
			currentLevel.tick(input);
		}
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

	public void beatLevel(String nextLevel) {
		if(nextLevel != null) {
			levelName = nextLevel;
			currentLevel.dispose();
			currentLevel = new Level(nextLevel, this, initMusic, swapMusic);
		}
	}
	
	public void restartLevel() {
		currentLevel = new Level(levelName, this, initMusic, swapMusic);
	}
}

