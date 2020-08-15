package com.remnant.weapon;

import com.esotericsoftware.kryonet.Client;

public class WeaponFactory {
	
	public static Weapon createWeapon(Client client, int weaponID){
		Weapon weapon = null;
		
		if(weaponID == WeaponID.PISTOL){
			weapon = new Pistol();
		} else if(weaponID == WeaponID.ASSAULT_RIFLE){
			weapon = new AssaultRifle();
		} else if(weaponID == WeaponID.COMBAT_RIFLE){
			weapon = new CombatRifle();
		} else if(weaponID == WeaponID.RIFLE){
			weapon = new Rifle();
		} else if(weaponID == WeaponID.SNIPER){
			weapon = new Sniper();
		} else if(weaponID == WeaponID.RAILGUN){
			weapon = new Railgun();
		} else if(weaponID == WeaponID.ROCKET_LAUNCHER){
			weapon = new RocketLauncher();
		} else if(weaponID == WeaponID.HEAVY_ASSAULT_RIFLE){
			weapon = new HeavyAssaultRifle();
		} else if(weaponID == WeaponID.SHOTGUN){
			weapon = new Shotgun();
		}
		
		weapon.setClient(client);
		
		return weapon;
	}
	
}