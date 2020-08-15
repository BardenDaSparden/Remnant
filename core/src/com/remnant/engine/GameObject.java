package com.remnant.engine;

public abstract class GameObject {

	protected Engine engine;
	
	protected boolean isExpired = false;
	
	protected boolean isActive = true;
	
	public GameObject(Engine engine){
		this.engine = engine;
	}
	
	public void update(){}
	
	protected abstract void onExpire();
	
	public void expire(){
		isExpired = true;
	}
	
	public boolean isExpired(){
		return isExpired();
	}
	
	public void activate(){
		isActive = true;
	}
	
	public void deactivate(){
		isActive = true;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
}
