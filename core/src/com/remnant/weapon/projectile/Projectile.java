package com.remnant.weapon.projectile;

import com.remnant.engine.Engine;
import com.remnant.engine.GameObject;
import com.remnant.gamestate.EffectsSystem;

public abstract class Projectile extends GameObject {

	public int ID;
	public int life = 0;
	public int maxLife = 2;
	public int damage = 3;
	public boolean isExpired = false;
	
	public EffectsSystem effects;
	
	public Projectile(Engine engine, int ID) {
		super(engine);
		this.ID = ID;
	}
	
	public void setEffects(EffectsSystem effects){
		this.effects = effects;
	}
	
	public void update(){
		life++;
		if(life >= maxLife)
			isExpired = true;
	}
	
	public boolean isDead(){
		return life >= maxLife;
	}
	
	protected abstract void onExpire();
	
}
