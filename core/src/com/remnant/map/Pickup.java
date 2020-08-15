package com.remnant.map;

import com.remnant.engine.Engine;
import com.remnant.engine.GameObject;
import com.remnant.geometry.Circle;
import com.remnant.graphics.Light;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.PhysicsObject;
import com.remnant.player.Player;

public abstract class Pickup extends GameObject implements PhysicsObject {
	
	PhysicsBody body;
	
	Light light;
	
	int pickupRadius;
	
	protected boolean isSpawned = true;
	
	public Pickup(Engine engine, int pickupRadius){
		super(engine);
		this.pickupRadius = pickupRadius;
		body = new PhysicsBody(new Circle(pickupRadius));
		body.setSensor(true);
		body.setStatic(true);
		light = engine.lighting.createPointLight(0, 0, 256);
	}

	public void setPosition(float x, float y){
		body.setPosition(x, y);
		light.getPosition().set(x, y, light.getPosition().z);
	}
	
	public abstract void doAction(Player player);
	
	@Override
	protected void onExpire() {
		//THIS OBJECT SHOULDNT EXPIRE
	}
	
	@Override
	public PhysicsBody getBody() {
		return body;
	}

	@Override
	public long getGroupMask() {
		return Masks.PICKUP;
	}

	@Override
	public long getCollisionMask() {
		return Masks.PICKUP;
	}
	
}
