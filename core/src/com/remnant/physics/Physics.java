package com.remnant.physics;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;

public class Physics {
	
	static final int WORLD_WIDTH = 8000;
	static final int WORLD_HEIGHT = 8000;
	static final int CELL_SIZE = 200;
	
	final float dt;
	
	//All physics objects
	List<PhysicsObject> objects;
	
	//Broad-phase grid
	protected Grid grid;
	
	//Pairs of "Physics" objects with possibilty of collisions
	List<ContactPair> contactPairs;
	boolean bCalcPairs = false;
	
	//Narrow-phase collision solver
	CollisionSolver collisionSolver;
	
	//Raycast interection solver
	RaycastSolver raycastSolver;
	ArrayList<RayContactEvent> tempEvents;
	
	
	public Physics(float timestep){
		dt = timestep;
		objects = new ArrayList<PhysicsObject>();
		grid = new Grid(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);
		contactPairs = new ArrayList<ContactPair>();
		collisionSolver = new CollisionSolver();
		raycastSolver = new RaycastSolver();
		tempEvents = new ArrayList<RayContactEvent>();
	}
	
	public void update(){
		
		grid.clear();
		for(int i = 0; i < objects.size(); i++){
			PhysicsObject object = objects.get(i);
			PhysicsBody body = object.getBody();
			if(!body.isEnabled)
				continue;
			grid.insert(object);
		}
		
		calculatePairs();
		
		for(int i = 0; i < contactPairs.size(); i++){
			ContactPair pair = contactPairs.get(i);
			ContactEvent eSelf = null;// = new ContactEvent(pair.objectA, pair.objectB, pair.info);
			ContactEvent eOther= null;// = new ContactEvent(pair.objectB, pair.objectA, pair.info);
			
			pair.solve(collisionSolver);
			
			if(pair.collision()){
				boolean bResolve = !(pair.A.isSensor || pair.B.isSensor);
				if(bResolve){
					pair.resolve(dt);
					pair.correctPosition();
				}
			}
			
			//If there was contact start, or contact end, create the associated events to notify each body in collision pair
			boolean cStart = pair.hasContactBegun();
			boolean cEnd = pair.hasContactEnded();
			
			if(cStart || cEnd){
				eSelf = new ContactEvent(pair.objectA, pair.objectB, pair.A.collisionInfo);
				eOther = new ContactEvent(pair.objectB, pair.objectA, pair.A.collisionInfo);
			}
			
			
			if(cStart){
				//notify objects of contact
				pair.A.onContactBegin(eSelf);
				pair.B.onContactBegin(eOther);
			} else if(cEnd){
				pair.A.onContactEnd(eSelf);
				pair.B.onContactEnd(eOther);
			}
			
		}
		
		for(int i = 0; i < objects.size(); i++){
			PhysicsObject object = objects.get(i);
			PhysicsBody body = object.getBody();
			if(!body.isEnabled)
				continue;
			body.integrate((float)dt);
		}
	}
	
	ArrayList<PhysicsObject> nearbyObjects = new ArrayList<PhysicsObject>();
	void calculatePairs(){
		contactPairs.clear();
		
		for(int i = 0; i < objects.size(); i++){
			
			PhysicsObject objectA = objects.get(i);
			grid.query(objectA, nearbyObjects);
			
			for(int j = 0; j < nearbyObjects.size(); j++){
				
				PhysicsObject objectB = nearbyObjects.get(j);
				
				PhysicsBody A = objectA.getBody();
				PhysicsBody B = objectB.getBody();
				
				if(objectA == objectB)
					continue;
				
				if(A.isStatic && B.isStatic)
					continue;
				
				if((objectA.getGroupMask() & objectB.getCollisionMask()) == 0)
					continue;
				
				ContactPair pair = new ContactPair(objectA, objectB);
				if(!contactPairs.contains(pair))
					contactPairs.add(pair);
			}
			
			nearbyObjects.clear();
		}
		//bCalcPairs = false;
	}
	
	public RaycastInfo raycast(Vector2f start, Vector2f dir, float maxDistance, long groupMask, long collisionMask){
		tempEvents.clear();
		//TODO (Remnant) add aabb test for effeciency
		for(int i = 0; i < objects.size(); i++){
			PhysicsObject object = objects.get(i);
			
			if((object.getGroupMask() | collisionMask) == 0)
				continue;
			
			if(object.getBody().isSensor)
				continue;
			
			if(!object.getBody().isEnabled)
				continue;
			
			RaycastInfo raycast = raycastSolver.solve(start, dir, maxDistance, object.getBody());
			if(raycast.hit){
				tempEvents.add(new RayContactEvent(object, raycast, groupMask));
			}
		}
		
		RayContactEvent event = null;
		RaycastInfo info = null;
		float minDistSqr = Float.MAX_VALUE;
		
		if(tempEvents.size() > 0){
			for(int i = 0; i < tempEvents.size(); i++){
				RayContactEvent e = tempEvents.get(i);
				RaycastInfo raycast = e.info;
				float dx = raycast.contact.x - start.x;
				float dy = raycast.contact.y - start.y;
				float distSqr = dx * dx + dy * dy;
				if(distSqr < minDistSqr){
					event = e;
					minDistSqr = distSqr;
				}
			}
		}
		
		if(event != null){
			if(minDistSqr > maxDistance * maxDistance)
				event = null;
		}
		
		if(event != null){
			PhysicsBody body = event.other.getBody();
			body.onRayContact(event);
			info = event.info;
		} else {
			info = new RaycastInfo();
		}
		
		return info;
	}
	
	public PhysicsObject add(PhysicsObject object){
		objects.add(object);
		return object;
	}
	
	public void remove(PhysicsObject object){
		objects.remove(object);
	}

	public int getNumObjects(){
		return objects.size();
	}
	
	public int getNumStatic(){
		int count = 0;
		for(int i = 0; i < objects.size(); i++){
			PhysicsObject object = objects.get(i);
			if(object.getBody().isStatic)
				count++;
		}
		return count;
	}
	
	public List<PhysicsObject> getObjects(){
		return objects;
	}	
	
	public int getNumIntersections(){
		return contactPairs.size();
	}
	
	public void clear(){
		objects.clear();
		contactPairs.clear();
	}
	
}
