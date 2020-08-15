package com.remnant.graphics;

import com.remnant.math.Transform;

public interface Renderable {

	public Transform getTransform();
	public Mesh getMesh();
	public Material getMaterial();
	
}
