package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;

public class PistolProjectile extends HitscanProjectile {

	public PistolProjectile(Engine engine) {
		super(engine, ProjectileID.PISTOL);
		super.damage = 15;
	}

	@Override
	protected void onExpire() {
		// TODO Auto-generated method stub
		
	}
	
	
}
