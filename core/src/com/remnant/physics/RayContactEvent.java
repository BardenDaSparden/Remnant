package com.remnant.physics;

public class RayContactEvent {

	public PhysicsObject other;
	public RaycastInfo info;
	public long mask;
	
	public RayContactEvent(PhysicsObject object, RaycastInfo info, long mask){
		this.other = object;
		this.info = info;
		this.mask = mask;
	}
	
}
