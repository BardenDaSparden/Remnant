package com.barden.remnant;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Material {

	public TextureRegion diffuse;
	public TextureRegion normal;
	public TextureRegion specular;
	public TextureRegion glow;
	
	public Color color;
	
	public Material(){
		diffuse = null;
		normal = null;
		specular = null;
		glow = null;
		
		color = new Color(1, 1, 1, 1);
	}
	
}
