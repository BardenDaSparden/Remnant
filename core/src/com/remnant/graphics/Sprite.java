package com.remnant.graphics;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.math.Transform;

public class Sprite implements Renderable {
	
	static final int NUM_VERTICES = 6;
	
	Transform transform;
	Mesh mesh;
	Material material;
	
	public Sprite(int width, int height){
		transform = new Transform();
		mesh = new Mesh(NUM_VERTICES);
		material = new Material(new TextureRegion(new Texture("images/white.png")),
								new TextureRegion(new Texture("images/normal.png")), 
								new TextureRegion(new Texture("images/black.png")));
		transform.setScale(width, height);
		
		float w = 0.5f;
		float h = 0.5f;
		mesh.get(0).setPosition(-w, -h).setTexcoord(0, 0).setColor(1, 1, 1, 1);
		mesh.get(1).setPosition(w, -h).setTexcoord(1, 0).setColor(1, 1, 1, 1);
		mesh.get(2).setPosition(w, h).setTexcoord(1, 1).setColor(1, 1, 1, 1);
		
		mesh.get(3).setPosition(w, h).setTexcoord(1, 1).setColor(1, 1, 1, 1);
		mesh.get(4).setPosition(-w, h).setTexcoord(0, 1).setColor(1, 1, 1, 1);
		mesh.get(5).setPosition(-w, -h).setTexcoord(0, 0).setColor(1, 1, 1, 1);
	}
	
	public Sprite setPosition(float x, float y){
		transform.setTranslation(x, y);
		return this;
	}
	
	public Sprite setSize(float width, float height){
		transform.setScale(width, height);
		return this;
	}
	
	public Sprite setRotation(float angleInRads){
		transform.setRotation(angleInRads);
		return this;
	}
	
	public Sprite setDiffuse(TextureRegion diffuse){
		material.setDiffuse(diffuse);
		return this;
	}
	
	public Sprite setNormal(TextureRegion normal){
		material.setNormal(normal);
		return this;
	}
	
	public Sprite setIllumination(TextureRegion illumination){
		material.setIllumination(illumination);
		return this;
	}
	
	public Sprite setTextures(TextureRegion diffuse, TextureRegion normal, TextureRegion illum){
		material.setDiffuse(diffuse).setNormal(normal).setIllumination(illum);
		return this;
	}
	
	public void setMeshColor(Vector4f color){
		mesh.get(0).color.set(color);
		mesh.get(1).color.set(color);
		mesh.get(2).color.set(color);
		mesh.get(3).color.set(color);
		mesh.get(4).color.set(color);
		mesh.get(5).color.set(color);
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public Material getMaterial() {
		return material;
	}
}