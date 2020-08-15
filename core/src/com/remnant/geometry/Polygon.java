package com.remnant.geometry;

import java.util.ArrayList;

import org.joml.Vector2f;

public class Polygon implements Shape {

	protected ArrayList<Vector2f> vertices;
	private float area;
	
	public Polygon(Vector2f...verts){
		vertices = new ArrayList<Vector2f>();
		for(int i = 0; i < verts.length; i++){
			vertices.add(verts[i]);
		}
		calculateArea();
	}
	
	protected void calculateArea(){
		//calc area
	}
	
	public ArrayList<Vector2f> getVertices(){
		return vertices;
	}
	
	@Override
	public float getArea(){
		return area;
	}
	
	@Override
	public ShapeType getType(){
		return ShapeType.POLYGON;
	}
	
}
