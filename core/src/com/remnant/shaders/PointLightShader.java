package com.remnant.shaders;

import com.remnant.graphics.Camera;
import com.remnant.graphics.Material;
import com.remnant.graphics.Shader;
import com.remnant.math.Transform;

public class PointLightShader extends Shader {

	static final String VERTEX_PATH = "shaders/pointlight.vs";
	static final String FRAGMENT_PATH = "shaders/pointlight.fs";
	static PointLightShader instance = null;
	
	private PointLightShader(){
		super(VERTEX_PATH, FRAGMENT_PATH);
	}
	
	public static PointLightShader get(){
		if(instance == null)
			instance = new PointLightShader();
		return instance;
	}

	@Override
	public void setMatrices(Camera camera, Transform transform) {
		
	}

	@Override
	public void setMaterial(Material material) { }
	
	@Override
	public void updateUniforms() {
		
	}
}
