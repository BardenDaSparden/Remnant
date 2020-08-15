package com.remnant.map;

import com.remnant.physics.Masks;

public class ColliderDefinition {

	public String type = "Square";
	public float size = 256;
	public float orientation = 0.0f;
	public long groupMask = Masks.WALL;
	
}
