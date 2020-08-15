package com.remnant.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
	
	int width;
	int height;
	
	protected Vector3f translation;
	protected float rotation;
	Quaternionf rotQuaternion;
	Vector3f scale;
	
	boolean bCalcProjection = false;
	boolean bCalcView = false;
	
	Matrix4f projectionMatrix;
	Matrix4f viewMatrix;
	
	public Camera(){
		this(2, 2);
	}
	
	public Camera(int width, int height){
		this.width = width;
		this.height = height;
		
		translation = new Vector3f();
		rotation = 0;
		rotQuaternion = new Quaternionf();
		scale = new Vector3f(1, 1, 1);
		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		calculateViewMatrix();
		calculateProjectionMatrix();
	}
	
	public Camera set(Camera c){
		width = c.width;
		height = c.height;
		translation.set(c.translation);
		rotation = c.rotation;
		rotQuaternion.set(c.rotQuaternion);
		scale.set(c.scale);
		bCalcProjection = true;
		bCalcView = true;
		return this;
	}
	
	public Camera identity(){
		width = 2;
		height = 2;
		translation.set(0, 0, 0);
		rotation = 0;
		rotQuaternion.identity();
		scale.set(0, 0, 0);
		return this;
	}
	
	void calculateViewMatrix(){
		viewMatrix.identity();
		
		rotQuaternion.identity();
		rotQuaternion.rotateAxis(-rotation, 0, 0, -1);
		
		viewMatrix.scale(scale);
		viewMatrix.rotate(rotQuaternion);
		viewMatrix.translate(translation.negate(new Vector3f()));
		bCalcView = false;
	}
	
	void calculateProjectionMatrix(){
		float w = width / 2f;
		float h = height / 2f;
		projectionMatrix.identity();
		projectionMatrix.setOrtho(-w, w, -h, h, -1, 1);
		bCalcProjection = false;
	}
	
	public void setSize(int w, int h){
		this.width = w;
		this.height = h;
		bCalcProjection = true;
	}
	
	public void translate(float x, float y){
		translation.x += x;
		translation.y += y;
		bCalcView = true;
	}
	
	public void setTranslation(float x, float y){
		translation.x = x;
		translation.y = y;
		bCalcView = true;
	}
	
	public Vector2f getTranslation(){
		return new Vector2f(translation.x, translation.y);
	}
	
	public void rotate(float radians){
		rotation += radians;
		bCalcView = true;
	}
	
	public void setRotation(float radians){
		rotation = radians;
		bCalcView = true;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public Vector2f getScale(){
		return new Vector2f(scale.x, scale.y);
	}
	
	public void scale(float scaleFactor){
		scale.x += scaleFactor;
		scale.y += scaleFactor;
		bCalcView = true;
	}
	
	public void setScale(float scaleFactor){
		scale.x = scaleFactor;
		scale.y = scaleFactor;
		bCalcView = true;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Matrix4f getViewMatrix(){
		if(bCalcView)
			calculateViewMatrix();
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix(){
		if(bCalcProjection)
			calculateProjectionMatrix();
		return projectionMatrix;
	}
}
