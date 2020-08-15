package com.remnant.player;

import org.joml.Vector2f;

import com.remnant.graphics.Camera;
import com.remnant.math.CubicInterpolator;
import com.remnant.math.Interpolator;

public class PlayerCamera extends Camera {

	
	float distanceFromCamera;
	Player player;
	Vector2f poi;
	Interpolator inter = new CubicInterpolator(new Vector2f(0.1f, 0.75f), new Vector2f(0.5f, 0.9f));
	
	public PlayerCamera(int width, int height, Player player){
		super(width, height);
		poi = new Vector2f(0, 0);
		this.player = player;
		distanceFromCamera = height / 2 - 150;
	}
	
	public void update(){
		Vector2f playerPos = player.getBody().getTransform().getTranslation();
		float playerOrientation = player.getBody().getTransform().getRotation();
		Vector2f cameraPos = getTranslation();
		Vector2f cameraOffset = new Vector2f();
		cameraOffset.x = (float)Math.cos(playerOrientation + (float)Math.PI / 2.0f) * distanceFromCamera;
		cameraOffset.y = (float)Math.sin(playerOrientation + (float)Math.PI / 2.0f) * distanceFromCamera;
		
		poi.set(playerPos).add(cameraOffset);
		
		float w = 1f;
		float x = inter.interpolate(-cameraPos.x, poi.x, w);
		float y = inter.interpolate(-cameraPos.y, poi.y, w);
		super.setTranslation(-x, -y);
		super.setRotation(-playerOrientation);
	}
	
	public Vector2f getCameraPOI(){
		return null;
	}
	
}
