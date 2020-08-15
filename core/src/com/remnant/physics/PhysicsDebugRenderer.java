package com.remnant.physics;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.geometry.AABB;
import com.remnant.geometry.AABBUtil;
import com.remnant.geometry.Edge;
import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.FontLoader;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.math.Math2D;

public class PhysicsDebugRenderer extends TypedUIRenderer {

	static final int LAYER = 10003;
	
	int width;
	int height;
	Camera camera;
	Texture white;
	BitmapFont font;
	
	Physics physics;
	int gridX = -1;
	int gridY = -1;
	boolean isDrawing = false;
	
	public PhysicsDebugRenderer(int width, int height) {
		super(LAYER);
		camera = new Camera(width, height);
		white = new Texture("images/white.png");
		font = FontLoader.getFont("fonts/ITT.fnt");
	}
	
	public void setPhysics(Physics physics){
		this.physics = physics;
	}
	
	public void setPosition(float x, float y){
		Grid grid = physics.grid;
		gridX = grid.getGridRow(y);
		gridY = grid.getGridCol(x);
	}
	
	void drawCell(int cellX, int cellY){
		Grid grid = physics.grid;
		Vector2f pos = new Vector2f(grid.gridMin.x, grid.gridMin.y);
		Vector2f offset = new Vector2f(grid.cellSize / 2.0f, grid.cellSize / 2.0f);
		pos.add(offset);
		//int lineWidth = 2;
		pos.x += (cellX * grid.cellSize);
		pos.y += (cellY * grid.cellSize);
		//batch.draw(pos.x - offset.x, pos.y, lineWidth, grid.cellSize, 0, 1, 1, white, new Vector4f(1, 1, 1, 0.15f));
		//batch.draw(pos.x + offset.x, pos.y, lineWidth, grid.cellSize, 0, 1, 1, white, new Vector4f(1, 1, 1, 0.15f));
		//batch.draw(pos.x, pos.y + offset.y, grid.cellSize, lineWidth, 0, 1, 1, white, new Vector4f(1, 1, 1, 0.15f));
		//batch.draw(pos.x, pos.y - offset.y, grid.cellSize, lineWidth, 0, 1, 1, white, new Vector4f(1, 1, 1, 0.15f));
		font.drawCentered(pos.x, pos.y + 10, grid.cells.get(cellY).get(cellX).size() + "", batch, new Vector4f(1, 1, 1, 0.2f));
	}
	
	void drawAABB(AABB aabb, Vector4f color){
		batch.draw(aabb.x, aabb.y, aabb.width, aabb.height, 0, 1, 1, white, color);
	}
	
	void drawIntersection(AABBIntersection intersection){
		Vector4f color = new Vector4f();
		AABB bounds = intersection.bounds;
		boolean collision = intersection.intersection;
		color = (collision) ? color.set(0, 1, 0, 0.1f) : color.set(1, 0, 0, 0.1f);
		drawAABB(bounds, color);
	}
	
	float calcWidth(Vector2f start, Vector2f end){
		return Math2D.distance(start, end);
	}
	
	float calcHeight(){
		return 1;
	}
	
	float calcAngle(Vector2f start, Vector2f end){
		return (float)Math.atan2((end.y - start.y), (end.x - start.x));
	}

	public void drawLine(Vector2f start, Vector2f end, Vector4f color){
		float x = (end.x + start.x) / 2.0f;
		float y = (end.y + start.y) / 2.0f;
		float w = calcWidth(start, end);
		float h = calcHeight();
		float r = calcAngle(start, end);
		batch.draw(x, y, w, h, r, 1, 1, white, color);
	}
	
	public void toggle(){
		isDrawing = !isDrawing;
	}
	
	@Override
	public void draw() {
		if(isDrawing){
			if(gridX != -1 && gridY != -1){
				batch.begin(BlendState.ALPHA_TRANSPARENCY);
					drawCell(gridY, gridX);
					drawCell(gridY + 1, gridX);
					drawCell(gridY - 1, gridX);
					drawCell(gridY, gridX + 1);
					drawCell(gridY + 1, gridX + 1);
					drawCell(gridY - 1, gridX + 1);
					drawCell(gridY, gridX - 1);
					drawCell(gridY + 1, gridX - 1);
					drawCell(gridY - 1, gridX - 1);
				batch.end();
			}
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				ArrayList<AABBIntersection> intersections = CollisionSolver.boundsToDraw;
				for(int i = 0; i < intersections.size(); i++){
					AABBIntersection inter = intersections.get(i);
					drawIntersection(inter);
				}
				intersections.clear();
				AABB aabb = new AABB(32, 32);
				Vector4f color = new Vector4f(0, 1, 1, 0.25f);
				for(int i = 0; i < physics.objects.size(); i++){
					PhysicsObject object = physics.objects.get(i);
					AABBUtil.getAABB(object.getBody(), aabb);
					
					if(object.getBody().isSensor){
						color.set(1, 1, 0.25f, 0.15f);
					}
					
					drawAABB(aabb, color);
				}
				color.set(1, 0, 1, 0.5f);
				for(ContactPair pair : physics.contactPairs){
					Vector2f posA = pair.A.getTransform().getTranslation();
					Vector2f posB = pair.B.getTransform().getTranslation();
					drawLine(posA, posB, color);
				}
				
				color.set(0, 1, 0, 1);
				for(Edge edge : RaycastSolver.edges){
					drawLine(edge.start, edge.end, color);
				}
				RaycastSolver.edges.clear();
			batch.end();
		}
	}

}
