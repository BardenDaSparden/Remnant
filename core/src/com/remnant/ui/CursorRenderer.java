package com.remnant.ui;

import org.joml.Vector2f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.math.Color;

public class CursorRenderer extends TypedUIRenderer {

	static final int LAYER = 11000;
	
	Texture white;
	Camera camera;
	Texture baseCursorTexture;
	Texture createCursorTexture;
	Cursor cursor = null;
	
	public CursorRenderer(int width, int height) {
		super(LAYER);
		white = new Texture("images/white.png");
		camera = new Camera(width, height);
		baseCursorTexture = new Texture("images/cursor-base.png");
		createCursorTexture = new Texture("images/cursor-create.png");
	}
	
	public void setCursor(Cursor cursor){
		this.cursor = cursor;
	}
	
	void drawPoint(Vector2f pos, float size){
		batch.draw(pos.x, pos.y, size, size, 0, 1, 1, white, Color.WHITE);
	}
	
	void drawCursor(){
		Texture t = null;
		
		if(cursor.getStyle() == Cursor.CursorStyle.DEFAULT){
			t = baseCursorTexture;
		} else if(cursor.getStyle() == Cursor.CursorStyle.CREATE){
			t = createCursorTexture;
		}
		
		float w = t.getWidth() / 2.0f;
		float h = t.getHeight() / 2.0f;
		
		batch.draw(cursor.position.x + w + 1, cursor.position.y - h -  1, t.getWidth(), t.getHeight(), 0, 1, 1, t, Color.WHITE);
	}
	
	@Override
	public void draw() {
		cameraStack.push();
		cameraStack.current().set(camera);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				if(cursor.isVisible)
					drawCursor();
			batch.end();
		cameraStack.pop();
	}
	
}
