package com.remnant.physics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.remnant.geometry.AABB;
import com.remnant.geometry.AABBUtil;

public class Grid {

	ArrayList<ArrayList<ArrayList<PhysicsObject>>> cells;
	int numRows;
	int numCols;
	int cellSize;
	Vector2f gridMin;
	
	public Grid(int worldWidth, int worldHeight, int cellSize){
		this.cellSize = cellSize;
		numRows = (int)Math.ceil((float)worldHeight / (float)cellSize);
		numCols = (int)Math.ceil((float)worldWidth / (float)cellSize);
		cells = new ArrayList<ArrayList<ArrayList<PhysicsObject>>>();
		for(int i = 0; i < numRows; i++){
			ArrayList<ArrayList<PhysicsObject>> list = new ArrayList<ArrayList<PhysicsObject>>();
			for(int j = 0; j < numCols; j++){
				list.add(new ArrayList<PhysicsObject>());
			}
			cells.add(list);
		}
		gridMin = new Vector2f(-worldWidth / 2.0f, -worldWidth / 2.0f);
	}
	
	int getGridCol(float xPos){
		if(xPos < gridMin.x)
			throw new IllegalArgumentException("X Position is out of range");
		return (int) Math.floor( (xPos - gridMin.x) / cellSize );
	}
	
	int getGridRow(float yPos){
		if(yPos < gridMin.y)
			throw new IllegalArgumentException("Y Position is out of range");
		return (int) Math.floor( (yPos - gridMin.y) / cellSize );
	}
	
	public void insert(PhysicsObject object){
		
		AABB bounds = new AABB(0, 0);
		Vector2f min = new Vector2f();
		Vector2f max = new Vector2f();
		AABBUtil.getAABB(object.getBody(), bounds);
		
		min.x = bounds.x - bounds.width / 2.0f;
		min.y = bounds.y - bounds.height / 2.0f;
		
		max.x = bounds.x + bounds.width / 2.0f;
		max.y = bounds.y + bounds.height / 2.0f;
		
		int rowStart = getGridRow(min.y);
		int rowEnd = getGridRow(max.y);
		int colStart = getGridCol(min.x);
		int colEnd = getGridCol(max.x);
		
		for(int i = rowStart; i <= rowEnd; i++){
			for(int j = colStart; j <= colEnd; j++){
				cells.get(i).get(j).add(object);
			}
		}
		
//		Vector2f position = object.getBody().getTransform().getTranslation();
//		int row = getGridRow(position.y);
//		int col = getGridCol(position.x);
//		
//		cells.get(row).get(col).add(object);
	}
	
	public void query(PhysicsObject object, List<PhysicsObject> objects){
		Vector2f position = object.getBody().getTransform().getTranslation();
		int row = getGridRow(position.y);
		int col = getGridCol(position.x);
		
		int minRow = row;
		int maxRow = row;
		int minCol = col;
		int maxCol = col;
		
		if(row - 1 > -1)
			minRow--;
		if(row + 1 < numRows)
			maxRow++;
		if(col - 1 > -1)
			minCol--;
		if(col + 1 < numCols)
			maxCol++;
		
		for(int i = minRow; i <= maxRow; i++){
			for(int j = minCol; j <= maxCol; j++){
				ArrayList<PhysicsObject> cell = cells.get(i).get(j);
				objects.addAll(cell);
			}
		}
		
	}
	
	public void clear(){
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				cells.get(i).get(j).clear();
			}
		}
	}
	
}
