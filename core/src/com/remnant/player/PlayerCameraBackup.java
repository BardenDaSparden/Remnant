//package com.remnant.player;
//
//import org.joml.Vector2f;
//
//import com.remnant.graphics.Camera;
//import com.remnant.math.CubicInterpolator;
//import com.remnant.math.Interpolator;
//import com.remnant.math.Math2D;
//
//public class PlayerCamera extends Camera {
//
//	Player player;
//	Vector2f offset;
//	Vector2f offsetInter;
//	Vector2f temp1;
//	Vector2f temp2;
//	Vector2f origin;
//	Interpolator inter = new CubicInterpolator(new Vector2f(0.1f, 0.75f), new Vector2f(0.5f, 0.9f));
//	
//	public PlayerCamera(int width, int height, Player player){
//		super(width, height);
//		setSize(width, height);
//		this.player = player;
//		//offset =  new Vector2f(26, height / 2 - 163.5f);
//		offset = new Vector2f(0, 0);
//		offsetInter = new Vector2f(0, 0);
//		temp1 = new Vector2f(0, 0);
//		temp2 = new Vector2f(0, 0);
//		origin = new Vector2f(0, 0);
//	}
//	
//	public void update(){
//		offsetInter.x = inter.interpolate(offsetInter.x, offset.x, 0.05f);
//		offsetInter.y = inter.interpolate(offsetInter.y, offset.y, 0.05f);
//		
//		//float orientation = player.getBody().getTransform().getRotation();
//		Vector2f position = player.getBody().getTransform().getTranslation();
//		temp1.set(0, 0);
//		temp2.set(offsetInter);
//		//Math2D.rotate(origin, temp2, orientation);
//		Math2D.rotate(origin, temp2, 0);
//		temp1.x = (position.x) + temp2.x;
//		temp1.y = position.y + temp2.y;
//		setTranslation(-temp1.x, -temp1.y);
//		//setRotation(-orientation);
//	}
//	
//	public Vector2f getCameraPOI(){
//		return null;
//	}
//	
//	public void setOffset(float x, float y){
//		offset.x = x;
//		offset.y = y;
//	}
//	
//}
