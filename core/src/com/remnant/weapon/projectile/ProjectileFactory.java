package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;

public class ProjectileFactory {
	
	public static Projectile createProjectile(Engine engine, int projID){
		Projectile p = null;
		if(projID == ProjectileID.PISTOL)
			p = new PistolProjectile(engine);
		else if(projID == ProjectileID.ASSAULT_RIFLE)
			p = new AssaultRifleProjectile(engine);
		else if(projID == ProjectileID.COMBAT_RIFLE)
			p = new CombatRifleProjectile(engine);
		else if(projID == ProjectileID.RIFLE)
			p = new RifleProjectile(engine);
		else if(projID == ProjectileID.SNIPER)
			p = new SniperProjectile(engine);
		else if(projID == ProjectileID.ROCKET_LAUNCHER)
			p = new RocketProjectile(engine);
			
		return p;
	}
	
}