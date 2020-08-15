package com.remnant.geometry;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.remnant.math.Math2D;
import com.remnant.math.Transform;

public class PolygonUtil {
	
	public static void transformPolygon(ArrayList<Vector2f> vertices, Transform transform){
		for(int i = 0; i < vertices.size(); i++){
			Vector2f vertex = vertices.get(i);
			Math2D.rotate(vertex, transform.getRotation());
			vertex.x *= transform.getScale().x;
			vertex.y *= transform.getScale().y;
			vertex.add(transform.getTranslation());
		}
	}
	
	public static void inverseTransformPolygon(ArrayList<Vector2f> vertices, Transform transform){
		for(int i = 0; i < vertices.size(); i++){
			Vector2f vertex = vertices.get(i);
			vertex.sub(transform.getTranslation());
			vertex.x *= 1.0f / transform.getScale().x;
			vertex.y *= 1.0f / transform.getScale().y;
			Math2D.rotate(vertex, -transform.getRotation());
		}
	}
	
}