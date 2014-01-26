package com.band.ggjam;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class GameState {
	// Keeps track of the tile offset
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	
	private final String[] chars = {"ABCDEFGHIJKLMNOPQ","RSTUVWXYZ01234567","89.,!?:;\"'+-=/\\< "};
	protected SpriteBatch spriteBatch;
	
	protected Music menuTheme;
	protected Music snakeMusic;
	protected Music particleMuisc;
	
	private GGJam appListener;
	
	/**
	 * Make sure that the sprites are removed
	 */
	public void removed() {
		spriteBatch.dispose();
	}
	
	/**
	 * Create projection matrix for displaying and a large sprite batch
	 * @param appListener
	 */
	public final void init(GGJam appListener) {
		this.appListener = appListener;
	}

	/**
	 * Change between screens e.g. TitleScreen and InGameScreen
	 * 
	 * @param screen
	 */
	protected void setScreen(GameState screen) {
		appListener.setScreen(screen);
	}
	
	/**
	 * Draw a TextureRegion onto the screen
	 * 
	 * @param region
	 * @param x
	 * @param y
	 */
	public void draw(TextureRegion region, int x, int y) {
		int width = region.getRegionWidth();
		if(width < 0) width *= -1;
		assert(spriteBatch != null);
		spriteBatch.draw(region, x, y, width, region.getRegionHeight());
	}
	
	/**
	 * Draw a string onto the screen. Commented out until we get a good font file
	 * 
	 * @param string
	 * @param x
	 * @param y
	 */
	public void drawString(String string, int x, int y) {
		string = string.toUpperCase();
		for(int i = 0; i < string.length(); i ++) {
			char ch = string.charAt(i);
			for(int ys = 0; ys < chars.length; ys ++) {
				int xs = chars[ys].indexOf(ch);
				if(xs >= 0) {
					draw(Art.guys[xs][ys], x + i * 20, y);
				}
			}
		}
	}
	
	public void drawStringTopRight(String string, int x, int y) {
		string = string.toUpperCase();
		x -= string.length() * 20;
		for(int i = 0; i < string.length(); i ++) {
			char ch = string.charAt(i);
			for(int ys = 0; ys < chars.length; ys ++) {
				int xs = chars[ys].indexOf(ch);
				if(xs >= 0) {
					draw(Art.guys[xs][ys], x + i * 20, y);
				}
			}
		}
	}
	
	public void drawTitle(String string) {
		drawStringTopRight(string, GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
	}
	
	public abstract void render();
	
	public abstract void tick(Input input);
	
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public abstract void dispose();

	public void beatLevel(String nextLevel) {
		System.out.println("You should never ever get this message. Like, ever. EVER.");
	}

	public void restartLevel() {
		System.out.println("You should never ever get this message. Like, ever. EVER.");
	}
}
