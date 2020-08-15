package com.remnant.math;

import org.joml.Vector2f;

import com.remnant.ui.EditorAttribute;

@EditorAttribute(name = "Transform")
public class Transform {

	Vector2f translation;
	float rotation;
	Vector2f scale;
	
	public Transform(){
		translation = new Vector2f();
		rotation = 0.0f;
		scale = new Vector2f(1, 1);
	}
	
	public Transform set(Transform t){
		translation.set(t.translation);
		rotation = t.rotation;
		scale.set(t.scale);
		return this;
	}
	
	public Vector2f getTranslation(){
		return translation;
	}
	
	public Transform setTranslation(Vector2f t){
		return setTranslation(t.x, t.y);
	}
	
	public Transform setTranslation(float x, float y){
		translation.set(x, y);
		return this;
	}
	
	public Transform translate(float x, float y){
		translation.x += x;
		translation.y += y;
		return this;
	}
	
	public Transform translate(Vector2f t){
		translation.add(t);
		return this;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public Transform setRotation(float rads){
		this.rotation = rads;
		return this;
	}
	
	public Transform rotate(float rads){
		rotation += rads;
		return this;
	}
	
	public Vector2f getScale(){
		return scale;
	}
	
	public Transform setScale(Vector2f s){
		return setScale(s.x, s.y);
	}
	
	public Transform setScale(float sx, float sy){
		scale.set(sx, sy);
		return this;
	}
	
	public Transform scale(float x, float y){
		scale.x += x;
		scale.y += y;
		return this;
	}
	
	public Transform scale(Vector2f s){
		scale.add(s);
		return this;
	}
	
	public void transform(Vector2f v){
		transform(v, v);
	}
	
	public void transform(Vector2f v, Vector2f dest){
		float x = v.x;
		float y = v.y;
		float c = (float)Math.cos(rotation);
		float s = (float)Math.sin(rotation);
		dest.x = (scale.x) * ((x * c) + (y * -s)) + translation.x;
		dest.y = (scale.y) * ((x * s) + (y *  c)) + translation.y;
	}
	
}
