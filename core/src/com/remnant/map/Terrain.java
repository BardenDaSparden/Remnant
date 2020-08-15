package com.remnant.map;

import com.remnant.assets.Texture;
import com.remnant.engine.Engine;
import com.remnant.engine.GameObject;
import com.remnant.graphics.Material2;
import com.remnant.graphics.Mesh;
import com.remnant.graphics.MeshUtil;
import com.remnant.math.Transform;

public class Terrain extends GameObject {

	public static final String FLOOR = "Floor";
	public static final String WALL = "Wall";
	public static final String CEILING = "Ceiling";
	
	String type = FLOOR;
	
	Transform transform;
	
	Mesh mesh;
	
	Material2 material;
	
	int size = 256;
	
	public Terrain(Engine engine) {
		super(engine);
		transform = new Transform();
		mesh = MeshUtil.createRectangle(256, 256);
		material = new Material2();
		material.setEmissive(null);
	}

	@Override
	protected void onExpire() {
		
	}
	
	public Transform getTransform(){
		return transform;
	}
	
	public Mesh getMesh(){
		return mesh;
	}
	
	public Material2 getMaterial(){
		return material;
	}
	
	public int getSize(){
		return size;
	}
	
	
	public void setDiffuse(String texturePath){
		material.setDiffuse(new Texture(texturePath));
	}
	
	public void setDiffuse(Texture t){
		material.setDiffuse(t);
	}
	
	public void setSize(int size){
		mesh = MeshUtil.createRectangle(size, size);
	}
}
