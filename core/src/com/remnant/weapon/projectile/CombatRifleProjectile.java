package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;

public class CombatRifleProjectile extends HitscanProjectile {

	public CombatRifleProjectile(Engine engine) {
		super(engine, ProjectileID.COMBAT_RIFLE);
		super.damage = 15;
	}

	@Override
	protected void onExpire() {
		// TODO Auto-generated method stub
		
	}

}
