package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;

public class RifleProjectile extends HitscanProjectile {

	public RifleProjectile(Engine engine) {
		super(engine, ProjectileID.RIFLE);
		super.damage = 16;
	}

	@Override
	protected void onExpire() {
		// TODO Auto-generated method stub
		
	}
	
}
