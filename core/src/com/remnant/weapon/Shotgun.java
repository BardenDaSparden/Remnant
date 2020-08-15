package com.remnant.weapon;

import com.remnant.weapon.projectile.ProjectileID;

public class Shotgun extends Weapon {

	static final int ID = WeaponID.SHOTGUN;
	static final int PROJECTILE_ID = ProjectileID.SHOTGUN;
	
	public Shotgun(){
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
