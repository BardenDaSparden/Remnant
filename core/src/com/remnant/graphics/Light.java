package com.remnant.graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Light{
	
	protected Vector3f position;
	public Vector3f color;
	public Vector3f falloff;
	public float radius;
	public boolean castShadows;
	public boolean isActive = true;
	
	public Light(float x, float y, float radius){
		this(x, y, 50, radius);
	}
	
	public Light(float x, float y, float z, float radius){
		position = new Vector3f();
		position.set(x, y, z);
		color = new Vector3f();
		color.set(1, 1, 1);
		this.radius = radius;
		castShadows = false;
	}
	
	public void activate(){
		isActive = true;
	}
	
	public void deactivate(){
		isActive = false;
	}
	
	public void setActive(boolean v){
		isActive = v;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public void setPosition(float x, float y, float z){
		position.set(x, y, z);
	}
	
	public Light setPosition(Vector2f p){
		position.set(p.x, p.y, 0);
		return this;
	}
	
	public Light setPosition(Vector3f p){
		position.set(p);
		return this;
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
	public float getRadius(){
		return radius;
	}
	
}