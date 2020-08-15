package com.remnant.map;

public class TerrainDefinition {

	public String type = "Floor";
	public int size = 256;
	public float orientation = 0.0f;
	public String diffusePath = "images/tiles/floor.png";
	public String normalPath = "images/normal.png";
	public String emissivePath = "images/black.png";
	
	public void get(Terrain object){
		diffusePath = object.getMaterial().getDiffuse().getFilePath();
		normalPath = object.getMaterial().getNormal().getFilePath();
		emissivePath = object.getMaterial().getEmissive().getFilePath();
		size = object.getSize();
		type = object.type;
	}
	
}
