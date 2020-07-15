package com.barden.remnant;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject implements IPhysics, IDrawable {

	GameWorld world;
	
	boolean isActive = true;
	
	boolean isExpired = false;
	
	public GameObject(GameWorld world){
		this.world = world;
	}
	
	public abstract Body getBody();
	public abstract Drawable getDrawable();
	
	public void update(){}
	
	public boolean isExpired() {
		return isExpired;
	}
	
	public void expire(){
		isExpired = true;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public void setActive(boolean active) {
		isActive = active;
	}
	
	public String getType(){
		return "GameObject";
	}
	
	public String getName(){
		return this.toString();
	}
	
}
