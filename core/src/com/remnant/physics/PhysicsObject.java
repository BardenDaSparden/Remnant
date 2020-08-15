package com.remnant.physics;

public interface PhysicsObject {

	public PhysicsBody getBody();
	public long getGroupMask();
	public long getCollisionMask();
	
}
