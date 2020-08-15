package com.remnant.graphics;

import org.joml.Vector2f;

import com.remnant.assets.Texture;

public class TextureLight extends Light {
	
	TextureRegion region;
	Vector2f scale;
	
	public TextureLight(float worldX, float worldY, Texture texture){
		super(worldX, worldY, 0);
		this.region = new TextureRegion(texture);
		scale = new Vector2f(1, 1);
	}
	
	public TextureRegion getTexture(){
		return region;
	}
	
	public Vector2f getScale(){
		return scale;
	}
	
}
