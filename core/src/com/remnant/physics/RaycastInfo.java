package com.remnant.physics;

import org.joml.Vector2f;

public class RaycastInfo {

	public Vector2f normal;
	public Vector2f contact;
	public boolean hit;
	
	public RaycastInfo(){
		normal = new Vector2f(0, 0);
		contact = new Vector2f(0, 0);
		hit = false;
	}
	
}
