package com.remnant.map;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;

import com.remnant.assets.SoundClip;
import com.remnant.engine.Engine;
import com.remnant.player.Player;
import com.remnant.weapon.WeaponID;

public class WeaponPickup extends Pickup {

	static final int PICKUP_RANGE = 40;
	static final float HOVER_AMOUNT = 0.16f;
	
	double t;
	
	final int respawnTime;
	Timer respawnTimer;
	
	SoundClip pickupSound;
	
	int weaponID = -1;
	
	public WeaponPickup(Engine engine, int respawnTime) {
		super(engine, PICKUP_RANGE);
		this.respawnTime = respawnTime;
		respawnTimer = new Timer(respawnTime);
		respawnTimer.onComplete(new TimerCallback() {
			
			@Override
			public void onTimerComplete() {
				respawnTimer.restart();
				respawnTimer.stop();
				isSpawned = true;
				body.setEnable(true);
				light.isActive = true;
			}
		});
		
		light.color.set(0.7f, 0.55f, 0.5f);
		light.radius = 512;
		light.castShadows = false;
	}

	public void setWeaponID(int weaponID){
		this.weaponID = weaponID;
		if(weaponID == WeaponID.PISTOL){
			pickupSound = new SoundClip("resources/sounds/pistol-pickup.wav");
		} else if(weaponID == WeaponID.ASSAULT_RIFLE){
			pickupSound = new SoundClip("resources/sounds/assault-rifle-pickup.wav");
		} else if(weaponID == WeaponID.COMBAT_RIFLE){
			pickupSound = new SoundClip("resources/sounds/combat-rifle-pickup.wav");
		} else if(weaponID == WeaponID.RIFLE){
			pickupSound = new SoundClip("resources/sounds/rifle-pickup.wav");
		} else if(weaponID == WeaponID.ROCKET_LAUNCHER){
			pickupSound = new SoundClip("resources/sounds/rocket-pickup.wav");
		}
	}
	
	public void update(){
		super.update();
		respawnTimer.tick();
		
		float offsetY = (float)Math.sin(t * Math.PI * 2.0f) * HOVER_AMOUNT;
		super.body.getTransform().translate(0, offsetY);
		t += 0.0075f;
	}
	
	@Override
	public void doAction(Player player) {
		if(!isSpawned)
			return;
		
		player.setPrimaryWeapon(weaponID);
		
		if(pickupSound != null)
			pickupSound.play();
		
		isSpawned = false;
		light.isActive = false;
		respawnTimer.start();
		body.setEnable(false);
	}

	
	
}
