package com.remnant.ui;

import org.joml.Vector2f;

public class EditorProperties {

	public int numDivisions;
	
	public int cellSize;
	
	public boolean snapToGrid;
	
	public boolean drawGrid;
	
	public Vector2f cameraPosition = new Vector2f(0, 0);
	
	public Vector2f cameraZoom = new Vector2f(1, 1);
	
	public EditorProperties(){
		numDivisions = 0;
		cellSize = 256;
		snapToGrid = false;
		drawGrid = false;
	}
	
}
