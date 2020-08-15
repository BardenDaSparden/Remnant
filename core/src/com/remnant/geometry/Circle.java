package com.remnant.geometry;

public class Circle implements Shape{

	public float radius;
	
	public Circle(int radius){
		this.radius = radius;
	}
	
	@Override
	public float getArea(){
		return (float)Math.PI * radius * radius;
	}
	
	@Override
	public ShapeType getType(){
		return ShapeType.CIRCLE;
	}
	
}
