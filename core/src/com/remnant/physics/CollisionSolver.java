package com.remnant.physics;

import java.text.DecimalFormat;
import java.util.ArrayList;


import org.joml.Vector2f;

import com.remnant.geometry.AABB;
import com.remnant.geometry.AABBUtil;
import com.remnant.geometry.Circle;
import com.remnant.geometry.Polygon;
import com.remnant.geometry.PolygonUtil;
import com.remnant.geometry.Shape;
import com.remnant.geometry.Shape.ShapeType;
import com.remnant.math.Transform;

public class CollisionSolver {
	
	protected Solver[][] solvers = new Solver[2][2];
	
	//TEMP for DEBUGGING
	public static ArrayList<AABBIntersection> boundsToDraw = new ArrayList<AABBIntersection>();
	
	public CollisionSolver(){
		solvers[0][0] = new PolygonPolygonSolver();
		solvers[0][1] = new PolygonCircleSolver();
		solvers[1][0] = new CirclePolygonSolver();
		solvers[1][1] = new CircleCircleSolver();
	}
	
	public CollisionInfo solveCollision(PhysicsBody bodyA, PhysicsBody bodyB){
		Shape shapeA = bodyA.shape;
		Shape shapeB = bodyB.shape;
		int idxA = (shapeA.getType() == ShapeType.POLYGON) ? 0 : 1;
		int idxB = (shapeB.getType() == ShapeType.POLYGON) ? 0 : 1;
		return solvers[idxA][idxB].solve(bodyA, bodyB);
	}
}

interface Solver{
	public CollisionInfo solve(PhysicsBody bodyA, PhysicsBody bodyB);
}

class PolygonPolygonSolver implements Solver{
	
	public CollisionInfo solve(PhysicsBody bodyA, PhysicsBody bodyB){
		CollisionInfo collisionInfo = new CollisionInfo();
		
		System.out.println("Polygon - Polygon");
		
		AABB boundsA = new AABB(0, 0);
		AABB boundsB = new AABB(0, 0);
		
		AABBUtil.getAABB(bodyA, boundsA);
		AABBUtil.getAABB(bodyB, boundsB);
		boolean intersection = boundsA.intersects(boundsB);
		AABBIntersection interA = new AABBIntersection(boundsA, intersection);
		AABBIntersection interB = new AABBIntersection(boundsB, intersection);
		
		if(!CollisionSolver.boundsToDraw.contains(interA))
			CollisionSolver.boundsToDraw.add(interA);
		if(!CollisionSolver.boundsToDraw.contains(interB))
			CollisionSolver.boundsToDraw.add(interB);
		
		if(!intersection)
			return collisionInfo;
		
		return collisionInfo;
	}
	
}

class PolygonCircleSolver implements Solver{
	
	ArrayList<Vector2f> axes;
	DecimalFormat format = new DecimalFormat("#.###");
	
	public PolygonCircleSolver(){
		axes = new ArrayList<Vector2f>();
	}
	
	public CollisionInfo solve(PhysicsBody bodyA, PhysicsBody bodyB){	
		CollisionInfo collisionInfo = new CollisionInfo();
		
		AABB boundsA = new AABB(0, 0);
		AABB boundsB = new AABB(0, 0);
		
		AABBUtil.getAABB(bodyA, boundsA);
		AABBUtil.getAABB(bodyB, boundsB);
		boolean intersection = boundsA.intersects(boundsB);
		
		AABBIntersection interA = new AABBIntersection(boundsA, intersection);
		AABBIntersection interB = new AABBIntersection(boundsB, intersection);
		
		if(!CollisionSolver.boundsToDraw.contains(interA))
			CollisionSolver.boundsToDraw.add(interA);
		if(!CollisionSolver.boundsToDraw.contains(interB))
			CollisionSolver.boundsToDraw.add(interB);
		
		if(!intersection)
			return collisionInfo;
		
		Transform tA = bodyA.transform;
		Transform tB = bodyB.transform;
		
		Vector2f aToB = new Vector2f(tB.getTranslation().x - tA.getTranslation().x, tB.getTranslation().y - tA.getTranslation().y);
		aToB.normalize();
		
		Polygon polygon = (Polygon) bodyA.shape;
		Circle circle = (Circle) bodyB.shape;
		ArrayList<Vector2f> vertices = polygon.getVertices();
		
		PolygonUtil.transformPolygon(vertices, tA);
		
		for(int i = 0; i < vertices.size(); i++){
			Vector2f v0 = vertices.get(i);
			Vector2f v1 = vertices.get((i + 1) % vertices.size());
			Vector2f normal = new Vector2f(v1.x - v0.x, v1.y - v0.y).perpendicular().normalize();
			if(normal.dot(aToB) >= 0)
				axes.add(normal);
		}
		
		Vector2f closest = getClosestVertex(vertices, tB.getTranslation());
		Vector2f n = new Vector2f(closest.x - tB.getTranslation().x, closest.y - tB.getTranslation().y).negate().normalize();
		axes.add(n);
		
		Vector2f mtvAxis = new Vector2f();
		float minOverlap = Float.MAX_VALUE;
		collisionInfo.collision = true;
		for(int i = 0; i < axes.size(); i++){
			Vector2f axis = axes.get(i);
			Interval p1 = projectPolygon(vertices, axis);
			Interval p2 = projectCircle(tB.getTranslation(), circle.radius, axis);
			if(!p1.overlap(p2)){
				collisionInfo.collision = false;
			} else {
				float overlap = p1.getOverlap(p2);
				if(overlap < minOverlap){
					minOverlap = overlap;
					mtvAxis.set(axis);
				}
			}
		}
		
		if(collisionInfo.collision){
			collisionInfo.normal.set(mtvAxis).negate(new Vector2f());
			collisionInfo.depth = minOverlap + 0.01f;
		}
		
		PolygonUtil.inverseTransformPolygon(vertices, tA);
		axes.clear();
		
		return collisionInfo;
	}
	
	Vector2f getClosestVertex(ArrayList<Vector2f> vertices, Vector2f origin){
		float minDistance = Float.MAX_VALUE;
		Vector2f closest = new Vector2f();
		for(int i = 0; i < vertices.size(); i++){
			Vector2f vertex = vertices.get(i);
			float dx = (origin.x - vertex.x);
			float dy = (origin.y - vertex.y);
			float d2 = (dx * dx) + (dy * dy);
			if(d2 < minDistance){
				minDistance = d2;
				closest = vertex;
			}
		}
		return closest;
	}
	
	Interval projectPolygon(ArrayList<Vector2f> vertices, Vector2f axis){
		float min = vertices.get(0).dot(axis);
		float max = min;
		for(int i = 1; i < vertices.size(); i++){
			Vector2f vertex = vertices.get(i);
			float d = axis.dot(vertex);
			if(d < min)
				min = d;
			if(d > max)
				max = d;
		}
		return new Interval(min, max);
	}
	
	Interval projectCircle(Vector2f center, float radius, Vector2f axis){
		Vector2f v0 = new Vector2f(center.x + axis.x * radius, center.y + axis.y * radius);
		Vector2f v1 = new Vector2f(center.x - axis.x * radius, center.y - axis.y * radius);
		float min = v0.dot(axis);
		float max = min;
		float d = 0;
		
		d = v1.dot(axis);
		if(d < min)
			min = d;
		if(d > max)
			max = d;
		
		return new Interval(min, max);
	}
	
}

class CirclePolygonSolver implements Solver{
	
	PolygonCircleSolver solver;
	
	public CirclePolygonSolver(){
		solver = new PolygonCircleSolver();
	}
	
	public CollisionInfo solve(PhysicsBody bodyA, PhysicsBody bodyB){
		System.out.println("Circle - Polygon");
		return solver.solve(bodyB, bodyA);
	}
}

class CircleCircleSolver implements Solver {
	
	public CollisionInfo solve(PhysicsBody bodyA, PhysicsBody bodyB){
		
		CollisionInfo collisionInfo = new CollisionInfo();
		
		Circle circleA = (Circle) bodyA.shape;
		Circle circleB = (Circle) bodyB.shape;
		
		AABB boundsA = new AABB(0, 0);
		AABB boundsB = new AABB(0, 0);
		
		Transform ta = bodyA.getTransform();
		Transform tb = bodyB.getTransform();
		
		Vector2f aPos = ta.getTranslation();
		Vector2f bPos = tb.getTranslation();
		
		boundsA.width = circleA.radius * 2;
		boundsA.height = circleA.radius * 2;
		boundsA.x = aPos.x;
		boundsA.y = aPos.y;
		
		boundsB.width = circleB.radius * 2;
		boundsB.height = circleB.radius * 2;
		boundsB.x = bPos.x;
		boundsB.y = bPos.y;
		
		boolean intersection = boundsA.intersects(boundsB);
		
		AABBIntersection interA = new AABBIntersection(boundsA, intersection);
		AABBIntersection interB = new AABBIntersection(boundsB, intersection);
		
		if(!CollisionSolver.boundsToDraw.contains(interA))
			CollisionSolver.boundsToDraw.add(interA);
		if(!CollisionSolver.boundsToDraw.contains(interB))
			CollisionSolver.boundsToDraw.add(interB);
		
		if(!intersection)
			return collisionInfo;
		
		float dx = bPos.x - aPos.x;
		float dy = bPos.y - aPos.y;
		float rSum = (circleA.radius + circleB.radius);
		float d = (float)Math.sqrt(dx * dx + dy * dy);
		
		collisionInfo.collision = (d <= rSum - 0.02f);
		
		if(collisionInfo.collision){
			Vector2f normal = new Vector2f(dx, dy);
			normal.normalize();
			Vector2f contact = new Vector2f(aPos.x + normal.x * circleA.radius, aPos.y + normal.y * circleA.radius);
			collisionInfo.normal.set(normal);
			collisionInfo.contact.set(contact);
			collisionInfo.depth = (circleA.radius + circleB.radius) - (d);
		}
		
		return collisionInfo;
	}
	
}

class Interval {
	
	public float min;
	public float max;
	
	public Interval(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public boolean overlap(Interval other){
		return max >= other.min && other.max >= min;
	}
	
	public float getOverlap(Interval other){
		return Math.max(0, Math.min(max, other.max) - Math.max(min, other.min));
	}
	
}

class AABBIntersection {
	
	public AABB bounds;
	public boolean intersection;
	
	public AABBIntersection(AABB bounds, boolean intersection){
		this.bounds = bounds;
		this.intersection = intersection;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof AABBIntersection){
			AABBIntersection intersection = (AABBIntersection) other;
			boolean equal = intersection.bounds.equals(bounds);
			//HACKY AS FUCK
			if(equal){
				if(this.intersection || intersection.intersection){
					this.intersection = true;
					intersection.intersection = true;
				}
			}
			return equal;
		} else {
			return false;
		}
	}
}