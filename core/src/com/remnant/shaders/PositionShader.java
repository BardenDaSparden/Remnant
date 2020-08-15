package com.remnant.shaders;

import com.remnant.graphics.Camera;
import com.remnant.graphics.Material;
import com.remnant.graphics.Shader;
import com.remnant.math.Transform;

public class PositionShader extends Shader {

	static final String VERTEX_PATH = "shaders/position.vs";
	static final String FRAGMENT_PATH = "shaders/position.fs";
	static PositionShader instance = null;
	
	private PositionShader(){
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public static PositionShader get(){
		if(instance == null)
			instance = new PositionShader();
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

