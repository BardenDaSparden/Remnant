package com.remnant.physics;

import org.joml.Vector2f;
import com.remnant.math.Transform;

public class ContactPair {
	
	PhysicsObject objectA;
	PhysicsObject objectB;
	
	PhysicsBody A;
	PhysicsBody B;
	
	CollisionInfo info;
	
	public ContactPair(PhysicsObject objectA, PhysicsObject objectB){
		this.objectA = objectA;
		this.objectB = objectB;
		
		A = objectA.getBody();
		B = objectB.getBody();
		info = new CollisionInfo();
	}
	
	protected void solve(CollisionSolver solver){
		info = solver.solveCollision(A, B);
		A.setCollisionInfo(info);
		B.setCollisionInfo(info);
	}
	
	protected void resolve(float dt){
		
		
		Transform tA = A.getTransform();
		Transform tB = B.getTransform();
		
		Vector2f aPos = tA.getTranslation();
		Vector2f bPos = tB.getTranslation();
		
		
		Vector2f contact = info.contact;
		Vector2f rA = new Vector2f(contact.x - aPos.x, contact.y - aPos.y);
		Vector2f rB = new Vector2f(contact.x - bPos.x, contact.y - bPos.y);
		Vector2f vA = new Vector2f(A.velocity);
		Vector2f vB = new Vector2f(B.velocity);
		
		Vector2f relativeVelocity = new Vector2f(vA).sub(vB);
		float velocityAlongNormal = relativeVelocity.dot(info.normal);
		
		//separating velocities
		if(velocityAlongNormal < 0)
			return;
		
		
		float totalInvMass = (A.getInvMass() + B.getInvMass());
		float rACrossN = Vector2f.cross(rA, info.normal);
		float rBCrossN = Vector2f.cross(rB, info.normal);
		float totalInvAngMass = (((rACrossN * rACrossN) * A.getInvAngularMass()) + ((rBCrossN * rBCrossN) * B.getInvAngularMass()));
		
		float e = (float)Math.max(A.e, B.e);
		float j = (-1 - e) * velocityAlongNormal;
		//j /= (totalInvMass + totalInvAngMass);
		j /= (totalInvMass);
		Vector2f impulse = new Vector2f(info.normal).mul(j);
		
		A.applyImpulse(impulse, info.contact);
		B.applyImpulse(new Vector2f(impulse).negate(), info.contact);
		
	}
	
	public void correctPosition(){
		if(info.collision){
			
			float percent = 0.6f;
			float slop = 0.001f;
			
			Vector2f n = new Vector2f(info.normal);
			Vector2f correction = n.mul( (float)Math.max(info.depth, 0) / (A.getInvMass() + B.getInvMass()) * percent );
			
			Transform tA = A.getTransform();
			Transform tB = B.getTransform();
			
			tA.translate(-A.getInvMass() * correction.x, -A.getInvMass() * correction.y);
			tB.translate(B.getInvMass() * correction.x, B.getInvMass() * correction.y);
			
		}
	}
	
	boolean collision(){
		return info.collision;
	}
	
	public boolean hasContactBegun(){
		boolean contactStartedA = !A.previousCollisionInfo.collision && A.collisionInfo.collision;
		boolean contactStartedB = !B.previousCollisionInfo.collision && B.collisionInfo.collision;
		return contactStartedA || contactStartedB;
	}
	
	public boolean hasContactEnded(){
		boolean contactEndedA = A.previousCollisionInfo.collision && !A.collisionInfo.collision;
		boolean contactEndedB = B.previousCollisionInfo.collision && !B.collisionInfo.collision;
		return contactEndedA || contactEndedB;
	}
	
	public PhysicsObject getSelf(){
		return objectA;
	}
	
	public PhysicsObject getOther(){
		return objectB;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof ContactPair))
			return false;
		else{
			ContactPair pair = (ContactPair) obj;
			return ((A == pair.A) && (B == pair.B))  || ((A == pair.B) && (B == pair.A));
		}
	}
	
}
