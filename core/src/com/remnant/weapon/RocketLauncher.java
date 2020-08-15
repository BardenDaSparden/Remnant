package com.remnant.weapon;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;

import com.remnant.assets.SoundClip;
import com.remnant.weapon.projectile.ProjectileID;

public class RocketLauncher extends Weapon {

	static final int ID = WeaponID.ROCKET_LAUNCHER;
	static final int PROJECTILE_ID = ProjectileID.ROCKET_LAUNCHER;
	
	final int TRIGGER_DELAY = 70;
	Timer triggerTimer;
	boolean canFire = true;
	
	SoundClip fireSound;
	
	public RocketLauncher(){
		super(ID, PROJECTILE_ID);
		markers.offset.set(30, 0);
		markers.barrelOffset.set(40, 40);
		firingType = Weapon.FIRE_TYPE_SEMI;
		triggerTimer = new Timer(TRIGGER_DELAY);
		triggerTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				triggerTimer.restart();
				canFire = true;
			}
		});
		fireSound = new SoundClip("resources/sounds/rocket-fire.wav");
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
		canFire = false;
		fireSound.play();
		triggerTimer.start();
	}
	
	
	
}
