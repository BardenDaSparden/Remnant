package com.remnant.weapon;

import com.remnant.weapon.projectile.ProjectileID;

public class Sniper extends Weapon {

	static final int ID = WeaponID.SNIPER;
	static final int PROJECTILE_ID = ProjectileID.SNIPER;
	
	public Sniper(){
		super(ID, PROJECTILE_ID);
		firingType = Weapon.FIRE_TYPE_SEMI;
		markers.offset.set(30, 2);
		markers.barrelOffset.set(0, 20);
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
