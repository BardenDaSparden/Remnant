package com.remnant.geometry;

import org.joml.Vector2f;

import com.remnant.math.Math2D;

public class Rectangle extends Polygon {

	public Rectangle(float width, float height){
		this(width, height, 0);
	}
	
	public Rectangle(float width, float height, float rot){
		float w = width / 2.0f;
		float h = height / 2.0f;
		Vector2f v0 = new Vector2f(-w, -h);
		Vector2f v1 = new Vector2f(w, -h);
		Vector2f v2 = new Vector2f(w, h);
		Vector2f v3 = new Vector2f(-w, h);
		Math2D.rotate(v0, rot);
		Math2D.rotate(v1, rot);
		Math2D.rotate(v2, rot);
		Math2D.rotate(v3, rot);
		vertices.add(v0);
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		calculateArea();
	}
	
}
