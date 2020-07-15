package com.barden.remnant;

import com.badlogic.gdx.math.Vector2;

public class Transform {

	Vector2 position;
	
	float rotation;
	
	Vector2 scale;
	
	public Transform(){
		position = new Vector2(0, 0);
		rotation = 0.0f;
		scale = new Vector2(1, 1);
	}
	
	public Transform set(Transform other){
		position.set(other.position.x, other.position.y);
		rotation = other.rotation;
		scale.set(other.scale.x, other.scale.y);
		return this;
	}
	
	public void set(float x, float y){
		set(x, y, rotation);
	}
	
	public void set(float x, float y, float angle){
		position.set(x, y);
		rotation = angle;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Transform setPosition(Vector2 pos){
		position.set(pos.x, pos.y);
		return this;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public Transform setRotation(float angle){
		rotation = angle;
		return this;
	}
	
	public Vector2 getScale(){
		return scale;
	}
	
	public Transform setScale(float x, float y){
		scale.x = x;
		scale.y = y;
		return this;
	}
	
	public Transform setScale(Vector2 s){
		scale.x = s.x;
		scale.y = s.y;
		return this;
	}
	
	public void mul(Vector2 v, Vector2 dest){
		
		float c = (float)Math.cos(rotation);
		float s = (float)Math.sin(rotation);
		
		dest.set(v);
		
		dest.x = v.x * c - v.y * s;
		dest.y = v.x * s + v.y * c;
		
		dest.x *= scale.x;
		dest.y *= scale.y;
		
		dest.x += position.x;
		dest.y += position.y;
	}
	
}
