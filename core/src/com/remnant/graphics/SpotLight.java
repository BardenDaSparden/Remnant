package com.remnant.graphics;

import org.joml.Vector3f;

public class SpotLight extends Light {

	protected float cosCutoff;
	protected Vector3f direction;
	
	public SpotLight(float x, float y, float r, float cutoffInRads){
		super(x, y, r);
		cosCutoff = (float)Math.cos(cutoffInRads);
		direction = new Vector3f(0, 1, 1).normalize();
	}
	
	public void setCutoff(float angleInRads){
		cosCutoff = (float)Math.cos(angleInRads);
	}
	
	public void setDirection(Vector3f direction){
		this.direction.set(direction);
	}
	
}
