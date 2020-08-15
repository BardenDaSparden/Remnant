package com.remnant.ui;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.CameraStack;
import com.remnant.graphics.TypedUIRenderer;

public class GridOverlay extends TypedUIRenderer {

static final int LAYER = 9998;
	
	int width;
	int height;
	int cellSize;
	int numDivisions;
	float thickness;
	
	float camOffsetX;
	float camOffsetY;
	
	Vector4f emissiveColor;
	Vector4f lineColor;
	Vector4f divisionColor;
	
	Vector4f lc;
	Vector4f dc;
	
	Texture white;
	
	Camera camera;
	
	boolean bDraw = false;
	
	public GridOverlay(int baseWidth, int baseHeight){
		super(LAYER);
		width = baseWidth;
		height = baseHeight;
		cellSize = 128;
		numDivisions = 1;
		thickness = 4.0f;
		
		emissiveColor = new Vector4f(1, 1, 1, 0.2f);
		lineColor = new Vector4f(0.2f, 0.85f, 0.2f, 0.12f);
		
		lc = new Vector4f(lineColor);
		dc = new Vector4f(lineColor);
		
		white = new Texture("images/white.png");
		
		camera = new Camera(1, 1);
	}

	public void setDraw(boolean bDraw){
		this.bDraw = bDraw;
	}
	
	public void setCellSize(int cellSize){
		this.cellSize = cellSize;
	}
	
	public void setNumDivisions(int divisions){
		this.numDivisions = divisions;
	}
	
	public void setCameraOffset(Camera camera){
		Vector2f cameraPos = camera.getTranslation();
		camOffsetX = cameraPos.x;
		camOffsetY = cameraPos.y;
	}
	
	public Vector2f snapPosition(Vector2f position){
		//int divisions = (numDivisions == 0) ? 1 : numDivisions
		float s = cellSize;// / numDivisions + 1;
		Vector2f v = new Vector2f(position);
		v.x = ((int)Math.floor((v.x + cellSize / 2.0f) / s)) * (s);
		v.y = ((int)Math.floor((v.y + cellSize / 2.0f) / s)) * (s);
		
		//Vector4f pos4 = new Vector4f(v.x, v.y, 0, 1);
		//Vector4f gridSpace = new Vector4f();
		//Matrix4f projection = camera.getViewMatrix();
		//projection.transform(pos4, gridSpace);
		
	//	v.x = gridSpace.x;
		//v.y = gridSpace.y;
		
		return v;
	}
	
	void drawOutline(float x, float y, Vector4f color){
		float s = cellSize / 2.0f;
		batch.draw(x - s, y, thickness, cellSize, 0, 1, 1, white, color);
		batch.draw(x, y - s, cellSize, thickness, 0, 1, 1, white, color);
		batch.draw(x + s, y, thickness, cellSize, 0, 1, 1, white, color);
		batch.draw(x, y + s, cellSize, thickness, 0, 1, 1, white, color);
	}
	
	Vector4f c = new Vector4f();
	void drawDivisions(float x, float y, Vector4f color){
		float s = cellSize / 2.0f;
		float offset = cellSize / (float)numDivisions;
		
		c.x = color.x * 0.5f;
		c.y = color.y * 0.5f;
		c.z = color.z * 0.5f;
		c.w = 1;
		
		//Draw Vertical Divisions
		for(int i = 1; i < numDivisions; i++){
			batch.draw(x - s + (offset * i), y, 2f, cellSize, 0, 1, 1, white, c);
		}
		
		//Draw Horizontal Divisions
		for(int i = 1; i < numDivisions; i++){
			batch.draw(x, y - s + (offset * i), cellSize, 2f, 0, 1, 1, white, c);
		}
	}
	
	void drawCell(float x, float y, boolean emission){
		if(emission){
			lc.set(emissiveColor);
			dc.set(emissiveColor);
		} else {
			lc.set(lineColor);
			dc.set(lineColor);
		}
		drawOutline(x, y, lc);
		if(numDivisions > 1){
			drawDivisions(x, y, dc);
		}
	}
	
	public void setCamera(Camera c){
		camera.set(c);
	}
	
	@Override
	public void draw() {
		
		if(!bDraw)
			return;
		
		int s = cellSize;
		int startCellX = ((int)Math.floor((-camOffsetX + s / 2.0f) / s));
		int startCellY = ((int)Math.floor((-camOffsetY + s / 2.0f) / s));
		
		CameraStack stack = super.cameraStack;
		stack.push();
		stack.push();
		stack.current().set(camera);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				int cellX = startCellX - 7;
				int cellY = startCellY - 7;
				for(int i = cellX; i < startCellX + 7; i++){
					for(int j = cellY; j < startCellY + 7; j++){
						drawCell(i * cellSize, j * cellSize, false);
					}
				}
			batch.end();
		stack.pop();
		stack.pop();
	}
	
}
