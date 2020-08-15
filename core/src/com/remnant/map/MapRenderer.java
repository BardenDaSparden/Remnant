package com.remnant.map;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.TypedRenderer;

public class MapRenderer extends TypedRenderer {

	static final int LAYER = 1;
	static final String NAME = "Map Renderer";
	
	Texture white;
	Map map = null;
	
	PickupRenderer pickupRenderer;
	TerrainRenderer terrainRenderer;
	
	public MapRenderer() {
		super(NAME, LAYER);
		white = new Texture("images/white.png");
		pickupRenderer = new PickupRenderer(LAYER + 10);
		terrainRenderer = new TerrainRenderer(LAYER + 1);
		add(pickupRenderer);
		add(terrainRenderer);
	}

	public void setMap(Map map){
		this.map = map;
	}
	
	void drawColliders_diffuse(){
		ArrayList<Collider> colliders = map.colliders;
		for(int i = 0; i < colliders.size(); i++){
			Collider collider = colliders.get(i);
			Vector2f position = collider.body.getTransform().getTranslation();
			float orientation = collider.body.getTransform().getRotation();
			float size = collider.getSize();
			batch.draw(position.x, position.y, size, size, orientation, 1, 1, white, new Vector4f(0.85f, 0.75f, 0, 0.5f));
		}
	}
	
	void drawColliders_emissive(){
//		ArrayList<Collider> colliders = map.colliders;
//		for(int i = 0; i < colliders.size(); i++){
//			Collider collider = colliders.get(i);
//			Vector2f position = collider.body.getTransform().getTranslation();
//			float orientation = collider.body.getTransform().getRotation();
//			float size = collider.getSize();
//			batch.draw(position.x, position.y, size, size, orientation, 1, 1, white, new Vector4f(1, 1, 1, 0.75f));
//		}
	}
	
	@Override
	public void prepare() {
		if(map == null)
			return;
		
		pickupRenderer.clear();		
		ArrayList<Pickup> pickups = map.pickups;
		for(int i = 0; i < pickups.size(); i++){
			pickupRenderer.add(pickups.get(i));
		}
		
		terrainRenderer.clear();
		ArrayList<Terrain> terrain = map.terrainObjects;
		for(int i = 0; i < terrain.size(); i++){
			terrainRenderer.add(terrain.get(i));
		}
	}

	@Override
	public void drawPositions(double alpha) {
		if(map == null)
			return;
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			if(map.isOpened)
				drawColliders_diffuse();
		batch.end();
		//pickupRenderer.drawPositions(alpha);
	}

	@Override
	public void drawDiffuse(double alpha) {
		if(map == null)
			return;
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			if(map.isOpened)
				drawColliders_diffuse();
		batch.end();
		//pickupRenderer.drawDiffuse(alpha);
	}

	@Override
	public void drawNormals(double alpha) {
		
	}

	@Override
	public void drawIllumination(double alpha) {
		//pickupRenderer.drawIllumination(alpha);
	}

	@Override
	public void drawEmissive(double alpha) {
		if(map == null)
			return;
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			if(map.isOpened)
				drawColliders_emissive();
		batch.end();
		//pickupRenderer.drawEmissive(alpha);
	}

}
