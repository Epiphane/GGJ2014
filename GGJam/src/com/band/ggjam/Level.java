package com.band.ggjam;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Level {
	private OrthographicCamera camera;
	private TiledMap map;
	private ArrayList<MapObject> polygonCollisions;
	private OrthogonalTiledMapRenderer renderer;
	
	/** 
	 * Width and height of the level in tiles
	 */
	private int width, height;
	
	private ArrayList<Particle> particles;
	private ArrayList<Wave> waves;
	
	private Particle activeParticle;
	private Wave activeWave;
	/** You're either controlling a particle, or a wave.  This tells you which. */
	public boolean controllingParticle;
	
	private GameState gameState;
	
	private SpriteBatch batch;
	
	public Level(String mapName, int dimX, int dimy, GameState gameState) {
		this.gameState = gameState;

		camera = new OrthographicCamera(GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
		camera.setToOrtho(false, GGJam.GAME_WIDTH / (GGJam.TILE_SIZE * GGJam.DISPLAY_TILE_SCALE), GGJam.GAME_HEIGHT / (GGJam.TILE_SIZE * GGJam.DISPLAY_TILE_SCALE));
		
		map = new TmxMapLoader().load("levels/"+mapName);

//		placeCharacters();
		
		// Set size
		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");
		
		// Create renderer
		renderer = new OrthogonalTiledMapRenderer(map, 1 / (GGJam.TILE_SIZE * GGJam.MULTIPLIER_FOR_GOOD_CALCULATIONS));
		renderer.setView(camera);
		
		batch = renderer.getSpriteBatch();
		
		camera.update();
		
		polygonCollisions = new ArrayList<MapObject>();
		for(MapObject object : map.getLayers().get(1).getObjects()) {
			if(object instanceof PolygonMapObject) {
				polygonCollisions.add(object);
			}
		}
		batch = new SpriteBatch(100);
		
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
	
	public void setActiveWave(int waveNum) {
		activeWave = waves.get(waveNum);
		controllingParticle = false;
	}
	
	/**
	 * Can the main character move to the point X, Y?
	 * @param x location of point we want to move TO
	 * @param y
	 * @return CAN WE MOVE THERE OR NOT
	 */
	public boolean canMove(int x, int y) {
		return true;
		//return tiles[x][y] == 0;
	}
	
	public void render() {
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		activeParticle.draw(batch);
		activeWave.draw(batch);
		batch.end();
	}

	public void tick(Input input) {
		if (input.buttonStack.shouldSwitch()) {
			controllingParticle = !controllingParticle;
		}
		
		if (controllingParticle) {
			activeParticle.tick(input);
		} else {
			activeWave.tick(input);
		}
	}

	public void dispose() {
	}
}
