package com.remnant.weapon;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;

import com.remnant.assets.SoundClip;

public class AssaultRifle extends Weapon {

	static final int ID = WeaponID.ASSAULT_RIFLE;
	static final int PROJECTILE_ID = WeaponID.ASSAULT_RIFLE;
	
	final int TRIGGER_DELAY = 5;
	Timer triggerTimer;
	boolean canFire = true;
	
	SoundClip fireSound;
	
	public AssaultRifle(){
		super(ID, PROJECTILE_ID);
		markers.offset.set(30, 0);
		markers.barrelOffset.set(1, 21);
		firingType = Weapon.FIRE_TYPE_AUTO;
		triggerTimer = new Timer(TRIGGER_DELAY);
		triggerTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				triggerTimer.restart();
				canFire = true;
			}
		});
		fireSound = new SoundClip("resources/sounds/fire2.wav");
		fireSound.setGain(0.35f);
	}
	
	@Override
	public void update() {
		triggerTimer.tick();
	}

	@Override
	public boolean canFire() {
		return canFire;
	}

	@Override
	public void fire(int playerID, boolean isPrimary) {
		net_fire(playerID, isPrimary);
		fireSound.play();
		canFire = false;
		triggerTimer.start();
	}
	
}
