package com.barden.remnant;

import java.util.ArrayList;

public class Map {

	Lighting lighting;
	
	GameWorld world;
	
	String name;
	
	Layer terrainLayer;
	
	ArrayList<Layer> layers;
	
	public Map(Lighting lighting, GameWorld world){
		this.lighting = lighting;
		this.world = world;
		
		name = "Internal Map";
		
		terrainLayer = new Layer();
		terrainLayer.setName("Terrain");
		
		layers = new ArrayList<Layer>();
		layers.add(terrainLayer);
	}
	
	public void load(){
		
		createTerrain(0, 0);
		createTerrain(256, 256);
		createTerrain(-256, -256);
		createTerrain(256, 0);
		createTerrain(-256, 0);
		createTerrain(0, 256);
		createTerrain(0, -256);
		createTerrain(256, -256);
		createTerrain(-256, 256);
		
		lighting.createLight(512, 0, 1, 1, 1);
		lighting.createLight(-512, 0, 1, 1, 1);
		lighting.createLight(0, 512, 1, 1, 1);
		lighting.createLight(0, -512, 1, 1, 1);
		
		for(int i = 0; i < layers.size(); i++){
			Layer layer = layers.get(i);
			layer.load(world);
		}
	}
	
	public void unload(){
		for(int i = 0; i < layers.size(); i++){
			Layer layer = layers.get(i);
			layer.unload(world);
		}
	}
	
	public Map createTerrain(float x, float y){
		Terrain t = new Terrain(world);
		t.setPosition(x, y);
		terrainLayer.add(t);
		return this;
	}
	
	public ArrayList<Layer> getLayers(){
		return layers;
	}
	
	public Layer createLayer(String name){
		Layer layer = new Layer();
		layer.setName(name);
		layers.add(layer);
		return layer;
	}

	public void removeLayer(Layer layer){
		layers.remove(layer);
	}
	
	public String getName(){
		return name;
	}
	
}

class LightLayer extends Layer {
	
}
