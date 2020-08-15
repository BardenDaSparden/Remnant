package com.remnant.physics;

public class ContactEvent {

	public PhysicsObject self;
	public PhysicsObject other;
	public CollisionInfo info;
	
	public ContactEvent(PhysicsObject A, PhysicsObject B, CollisionInfo info){
		self = A;
		other = B;
		this.info = info;
	}
	
}
