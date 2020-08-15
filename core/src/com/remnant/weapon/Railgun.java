package com.remnant.weapon;

import com.remnant.weapon.projectile.ProjectileID;

public class Railgun extends Weapon {

	static final int ID = WeaponID.RAILGUN;
	static final int PROJECTILE_ID = ProjectileID.RAILGUN;
	
	public Railgun(){
		super(ID, PROJECTILE_ID);
		firingType = Weapon.FIRE_TYPE_CHARGE;
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
