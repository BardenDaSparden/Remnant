package com.remnant.weapon.projectile;

public class ProjectileID {

	public static final int PISTOL = 0;
	public static final int ASSAULT_RIFLE = 1;
	public static final int COMBAT_RIFLE = 2;
	public static final int RIFLE = 3;
	public static final int SNIPER = 4;
	public static final int RAILGUN = 5;
	public static final int ROCKET_LAUNCHER = 6;
	public static final int HEAVY_ASSAULT_RIFLE = 7;
	public static final int LIGHTNING = 8;
	public static final int SHOTGUN = 9;
	public static final int NUM_PROJECTILES = 10;
	
	public static String getName(int projID){
		String name = "";
		if(projID == PISTOL){
			name = "Pistol";
		} else if(projID == ASSAULT_RIFLE){
			name = "Assault Rifle";
		} else if(projID == COMBAT_RIFLE){
			name = "Combat Rifle";
		} else if(projID == RIFLE){
			name = "Rifle";
		} else if(projID == SNIPER){
			name = "Sniper";
		} else if(projID == RAILGUN){
			name = "Railgun";
		} else if(projID == ROCKET_LAUNCHER){
			name = "Rocket Launcher";
		} else if(projID == HEAVY_ASSAULT_RIFLE){
			name = "Heavy Assault Rifle";
		} else if(projID == LIGHTNING){
			name = "Lightning";
		} else if(projID == SHOTGUN){
			name = "Shotgun";
		}
		return name;
	}
	
}
