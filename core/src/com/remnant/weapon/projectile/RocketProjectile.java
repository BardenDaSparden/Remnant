package com.remnant.weapon.projectile;

import org.joml.Vector2f;

import com.remnant.assets.SoundClip;
import com.remnant.engine.Engine;
import com.remnant.geometry.Circle;
import com.remnant.physics.CollisionInfo;
import com.remnant.physics.ContactEvent;
import com.remnant.physics.ContactListener;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.PhysicsObject;
import com.remnant.physics.RayContactEvent;

public class RocketProjectile extends Projectile implements PhysicsObject {

	PhysicsBody body;
	final float SPEED = 50000;
	SoundClip explodeSound;
	
	public RocketProjectile(Engine engine) {
		super(engine, ProjectileID.ROCKET_LAUNCHER);
		super.damage = 75;
		super.maxLife = 200;
		body = new PhysicsBody(new Circle(4));
		body.setRestitution(0);
		body.addListener(new ContactListener() {

			@Override
			public void onContactBegin(ContactEvent e) {
				RocketProjectile p = (RocketProjectile) e.self;
				CollisionInfo info = e.info;
				Vector2f position = new Vector2f(body.getTransform().getTranslation());
				effects.at(position.x, position.y, info.normal).emitRocketExplosion();
				p.isExpired = true;
			}

			@Override
			public void onContactEnd(ContactEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRayContact(RayContactEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void init(float x, float y, Vector2f dir){
		body.getTransform().getTranslation().set(x, y);
		body.applyForceToCenter(new Vector2f(dir.x * SPEED, dir.y * SPEED));
	}
	
	@Override
	public PhysicsBody getBody() {
		return body;
	}

	@Override
	public long getGroupMask() {
		return Masks.PROJECTILE;
	}

	@Override
	public long getCollisionMask() {
		return Masks.WALL | Masks.OTHER_SHIELD;
	}

	@Override
	protected void onExpire() {
		
	}
}
