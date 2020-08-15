package com.remnant.math;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface Interpolator {

	public float interpolate(float start, float end, float weight);
	public Vector2f interpolate(Vector2f start, Vector2f end, float weight);
	public Vector3f interpolate(Vector3f start, Vector3f end, float weight);
	public Vector4f interpolate(Vector4f start, Vector4f end, float weight);
	
}
