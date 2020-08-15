package com.remnant.physics;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.remnant.geometry.Circle;
import com.remnant.geometry.Edge;
import com.remnant.geometry.Polygon;
import com.remnant.geometry.PolygonUtil;
import com.remnant.geometry.Shape.ShapeType;
import com.remnant.math.Transform;

public class RaycastSolver {

	public static ArrayList<Edge> edges = new ArrayList<Edge>();
	IRaycastSolver[] solvers;
	
	public RaycastSolver(){
		solvers = new IRaycastSolver[]{new RayPolygonSolver(), new RayCircleSolver()};
	}
	
	public RaycastInfo solve(Vector2f start, Vector2f normal, float maxDistance, PhysicsBody body){
		int idx = (body.shape.getType() == ShapeType.POLYGON) ? 0 : 1;
		return solvers[idx].solve(start, normal, maxDistance, body);
	}
	
}

interface IRaycastSolver{
	public RaycastInfo solve(Vector2f rayOrigin, Vector2f rayDir, float maxDistance, PhysicsBody body);
}

class RayPolygonSolver implements IRaycastSolver {
	
	final float RAY_DISTANCE_PADDING = 200;
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	@Override
	public RaycastInfo solve(Vector2f rayOrigin, Vector2f rayDir, float maxDistance, PhysicsBody body) {
		RaycastInfo raycastInfo = new RaycastInfo();
		
		boolean inRange = isBodyInRange(rayOrigin, maxDistance, body);
		if(!inRange)
			return raycastInfo;
		
		//System.out.println("Body in range of Raycast!");
		
		calculateAxisFromBody(body);
		for(int i = 0; i < edges.size(); i++){
			Edge edge = edges.get(i);
			if(edge.normal.dot(rayDir) > 0){
				edges.remove(i);
			}
		}
		
		//System.out.println("Num edges to test: " + edges.size());
		
		boolean hit = false;
		Vector2f lastCD = new Vector2f();
		float uMin = 1;
		
		Edge ray = new Edge(rayOrigin, new Vector2f(rayOrigin.x + rayDir.x * maxDistance, rayOrigin.y + rayDir.y * maxDistance));
		for(int i = 0; i < edges.size(); i++){
			Edge edge = edges.get(i);
			Vector2f AB = new Vector2f(edge.end.x - edge.start.x, edge.end.y - edge.start.y);
			Vector2f CD = new Vector2f(ray.end.x - ray.start.x, ray.end.y - ray.start.y);
			Vector2f P = new Vector2f(rayOrigin.x - edge.start.x, rayOrigin.y - edge.start.y);
			float denom = Vector2f.cross(AB, CD);
			float u = Vector2f.cross(P, AB) / denom;
			float t = Vector2f.cross(P, CD) / denom;
			
			//if edge and ray are co-directional
			if(edge.normal.dot(rayDir) > 0)
				continue;
			
			if(t >= 0 && t <= 1){
				if(u >= 0 && u <= 1){
					RaycastSolver.edges.add(edge);
					hit = true;
					lastCD.set(CD);
					raycastInfo.normal.set(edge.normal);
					if(u < uMin)
						uMin = u;
				}
			}
		}
		
		if(hit){
			raycastInfo.hit = true;
			raycastInfo.contact.set(rayOrigin.x + (lastCD.x * uMin), rayOrigin.y + (lastCD.y * uMin));
		}
		
		return raycastInfo;
	}
	
	boolean isBodyInRange(Vector2f rayOrigin, float max, PhysicsBody body){
		max += RAY_DISTANCE_PADDING;
		Vector2f bodyPosition = body.transform.getTranslation();
		float dx = bodyPosition.x - rayOrigin.x;
		float dy = bodyPosition.y - rayOrigin.y;
		float d2 = dx * dx + dy * dy;
		return (d2 < max * max);
	}
	
	void calculateAxisFromBody(PhysicsBody body){
		Transform transform = body.transform;
		Polygon shape = (Polygon) body.shape;
		ArrayList<Vector2f> vertices = shape.getVertices();
		PolygonUtil.transformPolygon(vertices, transform);
		
		edges.clear();
		for(int i = 0; i < vertices.size(); i++){
			Vector2f v0 = new Vector2f(vertices.get(i));
			Vector2f v1 = new Vector2f(vertices.get((i + 1) % vertices.size()));
			edges.add(new Edge(v0, v1));
		}
		
		PolygonUtil.inverseTransformPolygon(vertices, transform);
	}
	
	Interval projectEdgeOnAxis(Edge edge, Vector2f axis){
		float min = edge.start.dot(axis);
		float max = min;
		float d = edge.end.dot(axis);
		if(d < min)
			min = d;
		if(d > max)
			max = d;
		return new Interval(min, max);
	}
	
}

class RayCircleSolver implements IRaycastSolver{
	
	@Override
	public RaycastInfo solve(Vector2f rayOrigin, Vector2f rayDirection, float maxDistance, PhysicsBody body) {
		RaycastInfo info = new RaycastInfo();
		Circle circle = (Circle)body.shape;
		Transform transform = body.transform;
		Vector2f position = transform.getTranslation();
		float radius = circle.radius;
		
		Vector2f dist = new Vector2f();
		dist.x = rayOrigin.x - position.x;
		dist.y = rayOrigin.y - position.y;
		if(dist.lengthSquared() < radius * radius){
			info.hit = false;
			return info;
		}
		
		rayDirection.normalize();
		
		//Ray Origin to Circle Position
		Vector2f AB = new Vector2f(0, 0);
		
		//Circle position to closest point on ray
		Vector2f BD = new Vector2f(0, 0);
		
		//Projection of AB onto ray direction
		Vector2f D = new Vector2f(0, 0);
		
		Vector2f.sub(position, rayOrigin, AB);
		
		//Project AB on ray direction
		float length = AB.length();
		D.x = rayDirection.x * length;
		D.y = rayDirection.y * length;
		D.add(rayOrigin);
		
		Vector2f.sub(D, position, BD);
		
		if(BD.lengthSquared() > radius * radius){
			info.hit = false;
			return info;
		}
		
		Vector2f C = new Vector2f(D);
		float offset = (float)Math.sqrt((radius * radius) - (BD.lengthSquared()));
		C.x += -rayDirection.x * offset;
		C.y += -rayDirection.y * offset;
		
		info.hit = true;
		info.contact = C;
		info.normal = new Vector2f(C.x - position.x, C.y - position.y).normalize();
		
		return info;
	}
	
}
