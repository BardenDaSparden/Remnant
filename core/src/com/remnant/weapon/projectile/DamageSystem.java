package com.remnant.weapon.projectile;

import java.util.ArrayList;

import com.remnant.physics.Physics;

public class DamageSystem {

	Physics physics;
	ArrayList<DamageArea> regions = new ArrayList<DamageArea>();
	
	public DamageSystem(Physics physics){
		this.physics = physics;
	}
	
	public void create(int playerID, int weaponID, float x, float y, int r, int dmg){
		DamageArea region = DamageAreaFactory.createDamageArea(playerID, weaponID, x, y, r, dmg);
		physics.add(region);
		regions.add(region);
	}
	
	public void update(){
		for(int i = 0; i < regions.size(); i++){
			DamageArea region = regions.get(i);
			region.update();
			if(region.isExpired){
				regions.remove(i);
				physics.remove(region);
			}
		}
	}
	
}
