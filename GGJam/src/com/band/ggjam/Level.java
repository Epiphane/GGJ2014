package com.band.ggjam;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;


public class Level {
	private OrthographicCamera camera;
	private TiledMap map;
	private ArrayList<MapObject> polygonCollisions;
	private OrthogonalTiledMapRenderer renderer;
	
	/** 
	 * Width and height of the level in tiles
	 */
	private int width, height;
	
	private Particle particle;
	private Wave wave;
	private int[] goal;
	
	/** You're either controlling a particle, or a wave.  This tells you which. */
	public boolean controllingParticle;
	
	private GameState gameState;
	
	private SpriteBatch batch;
	
	public Level(String mapName, GameState gameState) {
		map = new TmxMapLoader().load("levels/"+mapName);
		
		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");
		
		this.gameState = gameState;

		camera = new OrthographicCamera(width * GGJam.TILE_SIZE, height * GGJam.TILE_SIZE);
		camera.setToOrtho(false, width / GGJam.DISPLAY_TILE_SCALE, height / GGJam.DISPLAY_TILE_SCALE);

		// Create renderer
		renderer = new OrthogonalTiledMapRenderer(map, 1 / (GGJam.TILE_SIZE * GGJam.MULTIPLIER_FOR_GOOD_CALCULATIONS));
		renderer.setView(camera);
		
		camera.update();
		
		// Get wall objects from layer #1
		polygonCollisions = new ArrayList<MapObject>();
		for(MapObject object : map.getLayers().get(1).getObjects()) {
			if(object instanceof PolygonMapObject) {
				polygonCollisions.add(object);
			}
		}
		
		// Get all other objects from layer #2
		for(MapObject object : map.getLayers().get(2).getObjects()) {
			MapProperties properties = object.getProperties();
			if(object.getName().equals("Goal")) {
				goal = new int[] { (Integer) properties.get("x"), (Integer) properties.get("y") };
			}
			else if(object.getName().equals("Particle")) {
				particle = new Particle((Integer) properties.get("x"), (Integer) properties.get("y"), this);
			}
		}
		
		batch = new SpriteBatch(1);
		
		controllingParticle = true;
	}
	
	/**
	 * Can the main character move to the point X, Y?
	 * @param x location of point we want to move TO
	 * @param y
	 * @return CAN WE MOVE THERE OR NOT
	 */
	public boolean canMove(float x, float y, float w, float h) {
		float x0 = (x    );// * GGJam.TILE_SIZE;
		float y0 = (y    );// * GGJam.TILE_SIZE;
		float x1 = (x + w);// * GGJam.TILE_SIZE;
		float y1 = (y + h);// * GGJam.TILE_SIZE;
		

//		System.out.println("Checking ["+x0+","+y0+","+x1+","+y1+"]");
		
		for(MapObject objectToCheck : polygonCollisions) {
			// Check if our 4 corners intersect with any platform polygons
			Polygon polyToCheck = ((PolygonMapObject) objectToCheck).getPolygon();
			if(polyToCheck.contains(x0, y0) || polyToCheck.contains(x0, y1) || polyToCheck.contains(x1, y0) || polyToCheck.contains(x1, y1)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void render() {
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		if(particle != null) particle.draw(batch);
		if(wave != null) wave.draw(batch);
		batch.end();
	}

	public void tick(Input input) {
		if (input.buttonStack.shouldSwitch()) {
			controllingParticle = !controllingParticle;
		}
		
		if (controllingParticle)
			if(particle != null) particle.tick(input);
		else
			if(wave != null) wave.tick(input);
	}

	public void dispose() {
	}
}
