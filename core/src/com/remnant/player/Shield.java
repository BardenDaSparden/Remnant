package com.remnant.player;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;
import org.joml.Vector2f;

import com.remnant.gamestate.EffectsSystem;
import com.remnant.geometry.Circle;
import com.remnant.physics.CollisionInfo;
import com.remnant.physics.ContactEvent;
import com.remnant.physics.ContactListener;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsObject;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.RayContactEvent;
import com.remnant.weapon.projectile.DamageArea;

public class Shield implements PhysicsObject {

	EffectsSystem effects;
	
	boolean isSelf;
	
	PhysicsBody body;
	
	long groupMask;
	
	int health = 100;
	int maxHealth = 100;
	float opacity = 1;
	
	Timer hitTimer;
	boolean wasHit = false;
	
	public Shield(EffectsSystem effects, int radius, boolean isSelf){
		this.effects = effects;
		this.isSelf = isSelf;
		
		if(isSelf)
			groupMask = Masks.SELF_SHIELD;
		else
			groupMask = Masks.OTHER_SHIELD;
		
		body = new PhysicsBody(new Circle(radius));
		body.setSensor(false);
		body.setStatic(true);
		body.setRestitution(0.0f);
		
		hitTimer = new Timer(5);
		hitTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				hitTimer.restart();
				hitTimer.stop();
				opacity = 0;
				wasHit = false;
			}
		});
		
		
		body.addListener(new ContactListener() {
			
			@Override
			public void onContactBegin(ContactEvent e) {
				long group = e.other.getGroupMask();
				if((group & Masks.DAMAGE) == Masks.DAMAGE){
					DamageArea area = (DamageArea) e.other;
					if(!area.isExpired)
						takeDamage(e.info, area.amount);
					area.isExpired = true;
				}
			}

			@Override
			public void onContactEnd(ContactEvent e) { }

			@Override
			public void onRayContact(RayContactEvent e) { }
		});
	}
	
	void takeDamage(CollisionInfo info, int amount){
		if(health - amount > -1){
			health -= amount;
		} else {
			health = 0;
		}
		
		Vector2f contact = info.contact;
		effects.at(contact.x, contact.y, info.normal);
		effects.emitHealthPickupEffect();
		
		hitTimer.restart();
		hitTimer.start();
		opacity = 1;
		wasHit = true;
		
		if(health == 0)
			body.setEnable(false);
	}
	
	public void update(){
		hitTimer.tick();
		if(wasHit){
			opacity = 1.0f - hitTimer.percent();
		}
	}
	
	public void setPosition(float x, float y){
		body.getTransform().setTranslation(x, y);
	}
	
	public void setPosition(Vector2f v){
		body.getTransform().setTranslation(v);
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
		//return Masks.PROJECTILE | Masks.DAMAGE;
		return 0;
	}
	
}
