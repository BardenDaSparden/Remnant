package com.remnant.weapon;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;

import com.remnant.assets.SoundClip;
import com.remnant.weapon.projectile.ProjectileID;

public class Rifle extends Weapon {

	static final int ID = WeaponID.RIFLE;
	static final int PROJECTILE_ID = ProjectileID.RIFLE;
	
	final int TRIGGER_TIME = 20;
	Timer triggerTimer;
	boolean canFire = true;
	
	SoundClip fireSound;
	
	public Rifle(){
		super(ID, PROJECTILE_ID);
		firingType = Weapon.FIRE_TYPE_SEMI;
		markers.offset.set(30, 0);
		markers.barrelOffset.set(1, 21);
		triggerTimer = new Timer(TRIGGER_TIME);
		triggerTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				triggerTimer.restart();
				canFire = true;
			}
		});
		
		fireSound = new SoundClip("resources/sounds/dmr_fire_1.wav");
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
