package com.remnant.weapon;

public class WeaponID {

	public static final int PISTOL = 0;
	public static final int ASSAULT_RIFLE = 1;
	public static final int COMBAT_RIFLE = 2;
	public static final int RIFLE = 3;
	public static final int SNIPER = 4;
	public static final int RAILGUN = 5;
	public static final int ROCKET_LAUNCHER = 6;
	public static final int HEAVY_ASSAULT_RIFLE = 7;
	public static final int SHOTGUN = 8;
	public static final int NUM_WEAPONS = 9;
	
	public static String getName(int weaponID){
		String name = "";
		if(weaponID == PISTOL){
			name = "Pistol";
		} else if(weaponID == ASSAULT_RIFLE){
			name = "Assault Rifle";
		} else if(weaponID == COMBAT_RIFLE){
			name = "Combat Rifle";
		} else if(weaponID == RIFLE){
			name = "Rifle";
		} else if(weaponID == SNIPER){
			name = "Sniper";
		} else if(weaponID == RAILGUN){
			name = "Railgun";
		} else if(weaponID == ROCKET_LAUNCHER){
			name = "Rocket Launcher";
		} else if(weaponID == HEAVY_ASSAULT_RIFLE){
			name = "Heavy Assault Rifle";
		} else if(weaponID == SHOTGUN){
			name = "Shotgun";
		}
		return name;
	}
	
}
