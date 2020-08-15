package com.remnant.map;

import com.remnant.engine.Engine;
import com.remnant.engine.GameObject;
import com.remnant.geometry.Rectangle;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.PhysicsObject;

public class Collider extends GameObject implements PhysicsObject {

	String type;
	private float size;
	PhysicsBody body;
	long groupMask;
	long collisionMask;
	
	public Collider(Engine engine, ColliderDefinition properties) {
		super(engine);
		this.type = properties.type;
		this.setSize(properties.size);
		if(properties.type.equalsIgnoreCase("square")){
			body = new PhysicsBody(new Rectangle(properties.size, properties.size));
			body.setRotation(properties.orientation);
		} else if(properties.type.equalsIgnoreCase("r-triangle")){
			
		}
		groupMask = properties.groupMask;
		collisionMask = Masks.NONE;
		body.setStatic(true);
		body.setRotation(properties.orientation);
	}

	@Override
	public PhysicsBody getBody() {
		return body;
	}

	@Override
	public long getGroupMask() {
		return groupMask;
	}

	@Override
	public long getCollisionMask() {
		return collisionMask;
	}

	@Override
	protected void onExpire() {
		
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

}
