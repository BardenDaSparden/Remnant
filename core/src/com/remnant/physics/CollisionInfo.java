package com.remnant.physics;

import org.joml.Vector2f;

public class CollisionInfo {

	public boolean collision;
	public Vector2f normal;
	public Vector2f contact;
	public Vector2f contact2;
	public float depth;
	
	public CollisionInfo(){
		collision = false;
		normal = new Vector2f(0, 0);
		contact = new Vector2f(0, 0);
		contact2 = new Vector2f(0, 0);
		depth = 0;
	}
	
}

