package com.remnant.gamestate;

import java.util.ArrayList;

import com.remnant.engine.Engine;
import com.remnant.engine.GameSystem;
import com.remnant.map.HealthPickup;
import com.remnant.map.Pickup;
import com.remnant.map.PickupRenderer;
import com.remnant.map.WeaponPickup;

public class PickupSystem extends GameSystem {

	public PickupSystem(Engine game) {
		super(game);
	}

	ArrayList<Pickup> allPickups = new ArrayList<Pickup>();
	PickupRenderer renderer;
	
	@Override
	public void load() {
		renderer = new PickupRenderer(2);
		//engine.renderer.add(renderer);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void unload() {
		engine.renderer.remove(renderer);
		for(int i = 0; i < allPickups.size(); i++){
			Pickup p = allPickups.get(i);
			unregister(p);
		}
		allPickups.clear();
	}

	void register(Pickup pickup){
		//engine.add(pickup);
		//engine.physics.add(pickup);
		allPickups.add(pickup);
		renderer.add(pickup);
	}
	
	void unregister(Pickup pickup){
		engine.remove(pickup);
		engine.physics.remove(pickup);
		allPickups.remove(pickup);
		renderer.remove(pickup);
	}
	
	public void createHealthPickup(float x, float y){
		HealthPickup pickup = new HealthPickup(super.engine, 30);
		pickup.setPosition(x, y);
		register(pickup);
		
	}
	
	public void createWeaponPickup(float x, float y, int weaponID){
		WeaponPickup pickup = new WeaponPickup(super.engine, 1200);
		pickup.setPosition(x, y);
		pickup.setWeaponID(weaponID);
		register(pickup);
	}
	
}
