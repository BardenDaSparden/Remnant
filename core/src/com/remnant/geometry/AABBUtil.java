package com.remnant.geometry;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.remnant.geometry.Shape.ShapeType;
import com.remnant.math.Math2D;
import com.remnant.math.Transform;
import com.remnant.physics.PhysicsBody;

public class AABBUtil {
	
	public static void getAABB(PhysicsBody body, AABB bounds) {
		Transform transform = body.getTransform();
		Shape shape = body.getShape();
		if(shape.getType() == ShapeType.CIRCLE){
			Circle circle = (Circle) shape;
			bounds.width = circle.radius * 2;
			bounds.height = circle.radius * 2;
			bounds.x = transform.getTranslation().x;
			bounds.y = transform.getTranslation().y;
		} else if(shape.getType() == ShapeType.POLYGON) {
			Vector2f position = transform.getTranslation();
			float rotation = transform.getRotation();
			Vector2f scale = transform.getScale();
			
			ArrayList<Vector2f> vertices = ((Polygon) body.getShape()).getVertices();
			Vector2f min = new Vector2f(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Vector2f max = new Vector2f(Integer.MIN_VALUE, Integer.MIN_VALUE);
			
			//Temporarily transform mesh vertices to calculate aabb boundaries
			for(int i = 0; i < vertices.size(); i++){
				Vector2f vertex = new Vector2f(vertices.get(i));
				Math2D.rotate(vertex, rotation);
				vertex.x *= scale.x;
				vertex.y *= scale.y;
				if(vertex.x < min.x)
					min.x = vertex.x;
				if(vertex.x > max.x)
					max.x = vertex.x;
				if(vertex.y < min.y)
					min.y = vertex.y;
				if(vertex.y > max.y)
					max.y = vertex.y;
			}
			
			bounds.width = max.x - min.x;
			bounds.height = max.y - min.y;
			bounds.x = position.x;
			bounds.y = position.y;
		}
	}
}

