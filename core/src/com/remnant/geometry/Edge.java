package com.remnant.geometry;

import org.joml.Vector2f;

public class Edge {
	
	public Vector2f start;
	public Vector2f end;
	public Vector2f normal;
	
	public Edge(Vector2f start, Vector2f end){
		this.start = start;
		this.end = end;
		this.normal = new Vector2f(end.x - start.x, end.y - start.y).perpendicular().normalize();
	}
	
}