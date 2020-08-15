package com.remnant.map;

import java.util.ArrayList;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.TypedRenderer;
import com.remnant.math.Color;

public class TerrainRenderer extends TypedRenderer {

	static final String NAME = "Terrain";
	
	WallRenderer wallRenderer;
	
	ArrayList<Terrain> terrain;
	
	public TerrainRenderer(int layer) {
		super(NAME, layer);
		wallRenderer = new WallRenderer();
		terrain = new ArrayList<Terrain>();
		add(wallRenderer);
	}

	public void add(Terrain t){
		String type = t.type;
		if(type.equals(Terrain.WALL)){
			wallRenderer.add(t);
		} else {
			terrain.add(t);
		}
		
	}
	
	public void remove(Terrain t){
		String type = t.type;
		if(type.equals(Terrain.WALL)){
			wallRenderer.remove(t);
		} else {
			terrain.remove(t);
		}
	}
	
	public void clear(){
		terrain.clear();
		wallRenderer.clear();
	}
	
	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getDiffuse(), object.material.getBaseColor());
		}
		batch.end();
	}

	@Override
	public void drawDiffuse(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getDiffuse(), object.material.getBaseColor());
		}
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getNormal(), Color.WHITE);
		}
		batch.end();
	}

	@Override
	public void drawIllumination(double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawEmissive(double alpha) {
		batch.begin(BlendState.ADDITIVE);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getEmissive(), Color.WHITE);
		}
		batch.end();
	}
}

class WallRenderer extends TypedRenderer {

	static final int LAYER = 1000;
	static final String NAME = "Terrain-Wall";
	
	ArrayList<Terrain> terrain;
	
	Texture white;
	
	public WallRenderer() {
		super(NAME, LAYER);
		terrain = new ArrayList<Terrain>();
		white = new Texture("images/white.png");
	}
	
	public void add(Terrain t){
		terrain.add(t);
	}
	
	public void remove(Terrain t){
		terrain.remove(t);
	}
	
	public void clear(){
		terrain.clear();
	}
	
	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getDiffuse(), object.material.getBaseColor());
		}
		batch.end();
	}

	@Override
	public void drawDiffuse(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getDiffuse(), object.material.getBaseColor());
		}
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			batch.draw(object.transform, object.mesh, object.material.getNormal(), Color.WHITE);
		}
		batch.end();
	}

	@Override
	public void drawIllumination(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			
			//TODO add material flags foreach texture type
			if(object.material.getEmissive().getFilePath().equals(new Texture("images/black.png").getFilePath()))
				continue;
			batch.draw(object.transform, object.mesh, object.material.getEmissive(), Color.WHITE_QUARTER_OPAQUE);
		}
		batch.end();
	}

	@Override
	public void drawEmissive(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < terrain.size(); i++){
			Terrain object = terrain.get(i);
			
			//TODO add material flags foreach texture type
			if(object.material.getEmissive().getFilePath().equals(new Texture("images/black.png").getFilePath()))
				continue;
			batch.draw(object.transform, object.mesh, object.material.getEmissive(), Color.WHITE);
		}
		batch.end();
	}
	
	
	
}