package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;

public class AssaultRifleProjectile extends HitscanProjectile {

	public AssaultRifleProjectile(Engine engine) {
		super(engine, ProjectileID.ASSAULT_RIFLE);
		super.damage = 6;
	}

	@Override
	protected void onExpire() {
		// TODO Auto-generated method stub
		
	}

}
