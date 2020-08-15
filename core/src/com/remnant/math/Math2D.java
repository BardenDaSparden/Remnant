package com.remnant.math;

import org.joml.Vector2f;

public class Math2D {

	public static final Vector2f ORIGIN = new Vector2f(0, 0);
	
	public static float random(float low, float high){
		return (low) + (float)((high - low) * Math.random());
	}
	
	public static float clamp(float low, float high, float value){
		if(value < low)
			value = low;
		if(value > high)
			value = high;
		return value;
	}
	
	public static float cos(float value){
		return (float)Math.cos(value);
	}
	
	public static float sin(float value){
		return (float)Math.sin(value);
	}
	
	public static float min(float v1, float v2){
		return (float)Math.min(v1, v2);
	}
	
	public static float max(float v1, float v2){
		return (float)Math.max(v1, v2);
	}
	
	public static boolean inRange(float value, float min, float max){
		return (min <= value) && (value <= max);
	}
	
	public static float distanceSquared(Vector2f p1, Vector2f p2){
		float dx = p2.x - p1.x;
		float dy = p2.y - p1.y;
		return (dx * dx) + (dy * dy);
	}
	
	public static float distance(Vector2f p1, Vector2f p2){
		return (float)Math.sqrt(distanceSquared(p1, p2));
	}
	
	public static void rotate(Vector2f point, float rotation){
		float c = cos(rotation);
		float s = sin(rotation);
		float rx = point.x * c - point.y * s;
		float ry = point.x * s + point.y * c;
		point.set(rx, ry);
	}
	
	public static void rotate(Vector2f origin, Vector2f point, float rotation){
		point.sub(origin);
		float c = cos(rotation);
		float s = sin(rotation);
		float rx = point.x * c - point.y * s;
		float ry = point.x * s + point.y * c;
		point.set(rx, ry);
		point.add(origin);
	}
	
	public static float approach(float start, float end, float w){
		return start + (end - start) * w;
	}
	
}
