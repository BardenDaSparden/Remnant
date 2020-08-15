package com.remnant.shaders;

import com.remnant.graphics.Camera;
import com.remnant.graphics.Material;
import com.remnant.graphics.Shader;
import com.remnant.math.Transform;

public class CompositeShader extends Shader {

	static final String VERTEX_PATH = "shaders/composite.vs";
	static final String FRAGMENT_PATH = "shaders/composite.fs";
	static CompositeShader instance = null;
	
	private CompositeShader() {
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public static CompositeShader get(){
		if(instance == null)
			instance = new CompositeShader();
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
