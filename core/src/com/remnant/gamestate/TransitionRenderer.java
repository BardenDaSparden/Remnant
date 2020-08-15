package com.remnant.gamestate;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.TypedUIRenderer;

public class TransitionRenderer extends TypedUIRenderer {

	static final int LAYER = 10000;
	
	int width, height;
	Camera camera;
	Texture white;
	Vector4f color;
	
	public TransitionRenderer(int width, int height) {
		super(LAYER);
		this.width = width;
		this.height = height;
		this.camera = new Camera(width, height);
		white = new Texture("images/white.png");
		color = new Vector4f(0, 0, 0, 1);
	}

	public void setOpacity(float opacity){
		color.w = opacity;
	}
	
	@Override
	public void draw() {
		cameraStack.push();
		cameraStack.current().set(camera);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(0, 0, width, height, 0, 1, 1, white, color);
			batch.end();
		cameraStack.pop();
	}
}