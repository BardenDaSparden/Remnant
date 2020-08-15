package com.remnant.shaders;

import com.remnant.graphics.Camera;
import com.remnant.graphics.Material;
import com.remnant.graphics.Shader;
import com.remnant.math.Transform;

public class SpotLightShader extends Shader {

	static final String VERTEX_PATH = "shaders/spotlight.vs";
	static final String FRAGMENT_PATH = "shaders/spotlight.fs";
	static SpotLightShader instance = null;
	
	private SpotLightShader(){
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public static SpotLightShader get(){
		if(instance == null)
			instance = new SpotLightShader();
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

