package com.remnant.weapon;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;

import com.remnant.assets.SoundClip;
import com.remnant.assets.AudioClip;
import com.remnant.engine.Engine;
import com.remnant.weapon.projectile.ProjectileID;

public class CombatRifle extends Weapon {

	static final int ID = WeaponID.COMBAT_RIFLE;
	static final int PROJECTILE_ID = ProjectileID.COMBAT_RIFLE;
	
	int playerID;
	boolean isPrimary;
	
	final int TRIGGER_TIME = 20;
	final int BURST_INTERVAL = 4;
	final int BURST_AMOUNT = 3;
	Timer triggerTimer;
	Timer burstTimer;
	
	int shotCount = 0;
	boolean canFire = true;
	boolean doBurst = false;
	
	//SoundClip fireSound;
	
	AudioClip fireSound2;
	
	public CombatRifle(){
		super(ID, PROJECTILE_ID);
		firingType = Weapon.FIRE_TYPE_SEMI;
		markers.offset.set(30, 0);
		markers.barrelOffset.set(1, 22);
		triggerTimer = new Timer(TRIGGER_TIME);
		triggerTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				triggerTimer.restart();
				canFire = true;
			}
		});
		burstTimer = new Timer(BURST_INTERVAL);
		burstTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				if(shotCount < BURST_AMOUNT){
					shotCount++;
					doBurst = true;
					burstTimer.restart();
					burstTimer.start();
				} else {
					burstTimer.restart();
					burstTimer.stop();
					shotCount = 0;
					doBurst = false;
				}
			}
		});
		triggerTimer.start();
		
		//fireSound = new SoundClip("resources/sounds/fire2.wav");
		//fireSound.setGain(0.3f);
		
		fireSound2 = Engine.Loader().loadAudio("resources/sounds/CombatRifleFire.ogg");
		fireSound2.setGain(0.3f);
	}
	
	@Override
	public void update() {
		triggerTimer.tick();
		burstTimer.tick();
		
		if(!canFire && doBurst){
			net_fire(playerID, isPrimary);
			fireSound2.play();
			doBurst = false;
		}
	}

	@Override
	public boolean canFire() {
		return canFire;
	}

	@Override
	public void fire(int playerID, boolean isPrimary) {
		this.playerID = playerID;
		this.isPrimary = isPrimary;
		canFire = false;
		burstTimer.start();
		triggerTimer.start();
	}
}
