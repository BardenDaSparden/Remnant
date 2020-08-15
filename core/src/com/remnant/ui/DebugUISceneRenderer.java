package com.remnant.ui;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.Camera;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.math.Color;

public class DebugUISceneRenderer extends TypedUIRenderer {

	static final int LAYER = 10000;
	
	Camera camera;
	UIStage stage;
	Texture white;
	
	public DebugUISceneRenderer(int width, int height){
		super(LAYER);
		this.camera = new Camera(width, height);
		white = new Texture("images/white.png");
	}
	
	public void setStage(UIStage stage){
		this.stage = stage;
	}
	
	void drawBox(float x, float y, float width, float height, Vector4f color){
		batch.draw(x, y, width, height, 0, 1, 1, white, color);
	}
	
	void drawNode(UINode node){
		
		if(!node.isVisible)
			return;
		
		if(!node.debugDraw)
			return;
		
		//UIStyle style = node.getStyle();
		
		float x = node.getX();
		float y = node.getY();
		float w = node.getWidth();
		float h = node.getHeight();
		
		Vector4f color = new Vector4f(Color.WHITE_QUARTER_OPAQUE);
		color.w = 0.1f;
		if(node.isMouseOver)
			color.set(0, 1, 0, color.w);
		else
			color.set(color.x, color.y, color.z, color.w);
		drawBox(x, y, w, h, color);
	}
	
	void traverse(UINode node){
		if(node.isParent()){
			drawNode(node);
			for(int i = 0; i < node.getChildren().size(); i++){
				UINode child = node.getChildren().get(i);
				traverse(child);
			}
		} else {
			drawNode(node);
		}
	}
	
	@Override
	public void draw() {
		cameraStack.push();
		cameraStack.current().set(camera);
			if(stage != null)
				traverse(stage.getRoot());
		cameraStack.pop();
	}
} 