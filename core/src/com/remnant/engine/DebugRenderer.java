package com.remnant.engine;

import java.text.DecimalFormat;

import org.joml.Vector4f;

import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.FontLoader;
import com.remnant.graphics.TypedUIRenderer;

public class DebugRenderer extends TypedUIRenderer {

	static final int LAYER = 10000;
	
	int width;
	int height;
	Camera camera;
	
	BitmapFont largeFont;
	BitmapFont smallFont;
	
	Vector4f textColor;
	
	DecimalFormat f = new DecimalFormat("##.###");
	
	String gamestate;
	String version;
	
	String[] gamestateToString = {"Splash", "Main Menu", "Play", "Editor", "Credits", "Sandbox", "HOST MP", "Client MP"};
	
	int nPlayers = 0;
	
	int nPhysicsObjects = 0;
	
	int nStaticObjects = 0;
	
	int nDynamicObjects = 0;
	
	int nIntersections = 0;
	
	int nLights = 0;
	
	int nOccluders = 0;
	
	boolean isConnectedToServer = false;
	
	double updateTime = 0.0;
	
	double renderTime = 0.0;
	
	boolean isDrawing = false;
	
	public DebugRenderer(int width, int height) {
		super(LAYER);
		this.width = width;
		this.height = height;
		camera = new Camera(width, height);
		largeFont = FontLoader.getFont("fonts/imagine_24.fnt");
		smallFont = FontLoader.getFont("fonts/imagine_12.fnt");
		textColor = new Vector4f(1, 1, 1, 0.5f);
	}

	public void setGameState(int stateID){
		gamestate = gamestateToString[stateID];
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public void setRenderTime(double renderTime){
		this.renderTime = renderTime;
	}
	
	public void setUpdateTime(double updateTime){
		this.updateTime = updateTime;
	}
	
	public void setConnectionStatus(boolean v){
		isConnectedToServer = v;
	}
	
	public void setNumPlayers(int playerCount){
		this.nPlayers = playerCount;
	}
	
	public void setNumPhysicsObjects(int n){
		nPhysicsObjects = n;
	}
	
	public void setNumStaticObects(int n){
		nStaticObjects = n;
	}
	
	public void setNumDynamicObects(int n){
		nDynamicObjects = n;
	}
	
	public void setNumPhysicsIntersections(int n){
		nIntersections = n;
	}
	
	public void setNumLights(int n){
		nLights = n;
	}
	
	public void setNumOccluders(int n){
		nOccluders = n;
	}
	
	public void toggle(){
		isDrawing = !isDrawing;
	}
	
	@Override
	public void draw() {
		if(!isDrawing)
			return;
		
		int padding = 35;
		
		cameraStack.push();
		cameraStack.current().set(camera);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("State: " + gamestate) / 2, "State: " + gamestate, batch, textColor);
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Version: " + version) / 2 - 20, "Version: " + version, batch, textColor);
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Connected? " + isConnectedToServer) / 2 - 40, "Connected? " + isConnectedToServer, batch, textColor);
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Players : " + nPlayers) / 2 - 60, "Players : " + nPlayers, batch, textColor);
				
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Phys Objects : " + nPhysicsObjects) / 2 - 80, "Phys Objects : " + nPhysicsObjects, batch, textColor);
				
				smallFont.draw(-width / 2 + padding + 170, height / 2 - padding + smallFont.getHeight("Static : " + nStaticObjects) / 2 - 80, "Static : " + nStaticObjects, batch, textColor);
				smallFont.draw(-width / 2 + padding + 270, height / 2 - padding + smallFont.getHeight("Dynamic : "+ nDynamicObjects) / 2 - 100, "Dynamic : "+ nDynamicObjects, batch, textColor);
				
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Phys Intersections : " + nIntersections) / 2 - 100, "Phys Intersections : " + nIntersections, batch, textColor);
				
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Lights : " + nLights) / 2 - 120, "Lights : " + nLights, batch, textColor);
				
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Occluders : " + nOccluders) / 2 - 140, "Occluders : " + nOccluders, batch, textColor);
				
				
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Update: : " + f.format(updateTime)) / 2 - 160, "Update: : " + f.format(updateTime), batch, textColor);
				
				smallFont.draw(-width / 2 + padding, height / 2 - padding + smallFont.getHeight("Render: : " + f.format(renderTime)) / 2 - 180, "Render: : " + f.format(renderTime), batch, textColor);
			batch.end();
		cameraStack.pop();
	}
}
