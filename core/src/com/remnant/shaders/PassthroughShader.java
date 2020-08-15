package com.remnant.shaders;

import com.remnant.graphics.Camera;
import com.remnant.graphics.Material;
import com.remnant.graphics.Shader;
import com.remnant.math.Transform;

public class PassthroughShader extends Shader {

	static final String VERTEX_PATH = "shaders/passthrough.vs";
	static final String FRAGMENT_PATH = "shaders/passthrough.fs";
	static PassthroughShader instance = null;
	
	private PassthroughShader(){
		super(VERTEX_PATH, FRAGMENT_PATH);
	}
	
	public static PassthroughShader get(){
		if(instance == null)
			instance = new PassthroughShader();
		return instance;
	}

	@Override
	public void setMatrices(Camera camera, Transform transform) {
		
	}

	@Override
	public void setMaterial(Material material) {
		
	}

	@Override
	public void updateUniforms() {
		
	}
}
