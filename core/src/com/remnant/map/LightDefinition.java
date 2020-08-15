package com.remnant.map;

import org.joml.Vector3f;

import com.remnant.graphics.Light;

public class LightDefinition {

	public Vector3f color = new Vector3f(1, 1, 1);
	public float radius = 512;
	public float z = 200;
	public boolean castShadows = false;

	public void get(Light light){
		color.x = light.color.x;
		color.y = light.color.y;
		color.z = light.color.z;
		radius = light.radius;
		z = light.getPosition().z;
		castShadows = light.castShadows;
	}
	
}
