package com.remnant.weapon;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;

import com.remnant.assets.AudioClip;
import com.remnant.engine.Engine;
import com.remnant.weapon.projectile.ProjectileID;

public class Pistol extends Weapon {

	static final int ID = WeaponID.PISTOL;
	static final int PROJECTILE_ID = ProjectileID.PISTOL;
	
	final int TRIGGER_DELAY = 15;
	Timer triggerTimer;
	boolean canFire = true;
	
	AudioClip fireSound;
	
	public Pistol(){
		super(ID, PROJECTILE_ID);
		
		markers.offset.set(30, 0);
		markers.barrelOffset.set(4, 25);
		
		triggerTimer = new Timer(TRIGGER_DELAY);
		triggerTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				canFire = true;
				triggerTimer.restart();
				triggerTimer.stop();
			}
		});
		
		fireSound = Engine.Loader().loadAudio("resources/sounds/Pistol-Fire-1.ogg");
		
	}
	
	@Override
	public void update(){
		triggerTimer.tick();
	}
	
	@Override
	public boolean canFire(){
		return canFire;
	}
	
	@Override
	public void fire(int playerID, boolean isPrimary){
		net_fire(playerID, isPrimary);
		fireSound.play();
		canFire = false;
		triggerTimer.start();
	}
	
}
