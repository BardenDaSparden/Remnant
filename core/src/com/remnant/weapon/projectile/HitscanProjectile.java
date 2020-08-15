package com.remnant.weapon.projectile;

import org.joml.Vector2f;

import com.remnant.engine.Engine;

public abstract class HitscanProjectile extends Projectile {

	public Vector2f start;
	public Vector2f end;
	
	public HitscanProjectile(Engine engine, int ID) {
		super(engine, ID);
		start = new Vector2f();
		end = new Vector2f();
	}

}
