package com.remnant.weapon.projectile;

public class DamageAreaFactory {

	public static DamageArea createDamageArea(int playerID, int weaponID, float x, float y, int radius, int dmgAmount){
		DamageArea damage = new DamageArea(radius, dmgAmount);
		damage.playerID = playerID;
		damage.weaponID = weaponID;
		damage.getBody().getTransform().getTranslation().set(x, y);
		return damage;
	}
	
}
