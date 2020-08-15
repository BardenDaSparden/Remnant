package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;

public class SniperProjectile extends HitscanProjectile {

	public SniperProjectile(Engine engine) {
		super(engine, ProjectileID.SNIPER);
		super.damage = 90;
	}

	@Override
	protected void onExpire() {
		// TODO Auto-generated method stub
		
	}

}
