package com.remnant.weapon;

import com.remnant.weapon.projectile.ProjectileID;

public class HeavyAssaultRifle extends Weapon {

	static final int ID = WeaponID.HEAVY_ASSAULT_RIFLE;
	static final int PROJECTILE_ID = ProjectileID.HEAVY_ASSAULT_RIFLE;
	
	public HeavyAssaultRifle(){
		super(ID, PROJECTILE_ID);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canFire() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fire(int playerID, boolean isPrimary) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
