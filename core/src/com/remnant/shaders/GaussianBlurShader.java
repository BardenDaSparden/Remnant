package com.remnant.shaders;

import org.joml.Matrix4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.Camera;
import com.remnant.graphics.Material;
import com.remnant.graphics.Shader;
import com.remnant.math.Transform;

public class GaussianBlurShader extends Shader {

	static final String VERTEX_PATH = "shaders/blur.vs";
	static final String FRAGMENT_PATH = "shaders/blur.fs";
	static GaussianBlurShader instance = null;
	
	Matrix4f projection;
	Matrix4f view;
	Texture texture;
	
	private GaussianBlurShader(){
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public static GaussianBlurShader get(){
		if(instance == null)
			instance = new GaussianBlurShader();
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
