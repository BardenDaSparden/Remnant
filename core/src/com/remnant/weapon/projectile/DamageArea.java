package com.remnant.weapon.projectile;

import com.remnant.geometry.Circle;
import com.remnant.physics.ContactEvent;
import com.remnant.physics.ContactListener;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsObject;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.RayContactEvent;

public class DamageArea implements PhysicsObject{

	public int playerID;
	public int weaponID;
	public float x;
	public float y;
	public int r;
	public int amount;
	public boolean isExpired = false;
	public int life = 0;
	public int maxLife = 1;
	
	PhysicsBody body;

	public DamageArea(int radius, int amount){
		this.r = radius;
		this.amount = amount;
		body = new PhysicsBody(new Circle(r));
		body.setSensor(true);
		body.addListener(new ContactListener() {

			@Override
			public void onContactBegin(ContactEvent e) {
				long group = e.other.getGroupMask();
				if((group & Masks.WALL) == Masks.WALL){
					DamageArea.this.isExpired = true;
				}
				
			}

			@Override
			public void onContactEnd(ContactEvent e) { }

			@Override
			public void onRayContact(RayContactEvent e) { }
		});
	}
	
	public void update(){
		life++;
		if(life >= maxLife){
			isExpired = true;
		}
	}
	
	@Override
	public PhysicsBody getBody() {
		return body;
	}

	@Override
	public long getGroupMask() {
		return Masks.DAMAGE;
	}

	@Override
	public long getCollisionMask() {
		return Masks.PLAYER | Masks.WALL | Masks.OTHER_SHIELD | Masks.SELF_SHIELD;
	}
	
}
