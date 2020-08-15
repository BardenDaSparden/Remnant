package com.remnant.graphics;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Vertex {

	Vector2f position;
	Vector2f texcoord;
	Vector4f color;
	
	public Vertex(){
		this(new Vector2f(0, 0), new Vector2f(0, 0), new Vector4f(1, 1, 1, 1));
	}
	
	public Vertex(Vector2f position, Vector2f texcoord, Vector4f color){
		this.position = position;
		this.texcoord = texcoord;
		this.color = color;
	}
	
	public Vertex set(Vector2f position, Vector2f texcoord, Vector4f color){
		position.set(position);
		texcoord.set(texcoord);
		color.set(color);
		return this;
	}
	
	public Vertex setPosition(float x, float y){
		position.set(x, y);
		return this;
	}
	
	public Vertex setTexcoord(float u, float v){
		texcoord.set(u, v);
		return this;
	}
	
	public Vertex setColor(float r, float g, float b, float a){
		color.set(r, g, b, a);
		return this;
	}
	
	public Vector2f position(){
		return position;
	}
	
	public Vector2f texcoord(){
		return texcoord;
	}
	
	public Vector4f color(){
		return color;
	}
	
}
