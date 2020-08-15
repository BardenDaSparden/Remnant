package com.remnant.player;

import org.joml.Vector2f;

import com.remnant.geometry.Circle;
import com.remnant.map.Pickup;
import com.remnant.physics.ContactEvent;
import com.remnant.physics.ContactListener;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.PhysicsObject;
import com.remnant.physics.RayContactEvent;

public class PickupCollider implements PhysicsObject {	

	PhysicsBody body;
	Player player;
	
	public PickupCollider(int range){
		body = new PhysicsBody(new Circle(range));
		body.setSensor(true);
		body.addListener(new ContactListener() {
			
			@Override
			public void onRayContact(RayContactEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onContactEnd(ContactEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onContactBegin(ContactEvent e) {
				if(e.other instanceof Pickup){
					Pickup pickup = (Pickup) e.other;
					pickup.doAction(player);
				}
			}
		});
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public void setPosition(Vector2f position){
		body.setPosition(position.x, position.y);
	}
	
	public void setPosition(float x, float y){
		body.setPosition(x, y);
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
