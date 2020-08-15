package com.remnant.geometry;

public interface Shape {
	
	public enum ShapeType {
		CIRCLE, POLYGON
	}
	
	public float getArea();
	public ShapeType getType();
	
}
