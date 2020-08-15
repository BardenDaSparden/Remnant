package com.remnant.weapon.projectile;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector2f;

import com.esotericsoftware.kryonet.Client;
import com.remnant.assets.SoundClip;
import com.remnant.engine.Engine;
import com.remnant.gamestate.EffectsSystem;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsObject;
import com.remnant.physics.Physics;
import com.remnant.physics.RaycastInfo;

public class ProjectileSystem {
	
	Client client;
	
	final int MAX_RANGE = 2000;
	Engine engine;
	ProjectileRenderer renderer;
	Physics physics;
	EffectsSystem effects;
	DamageSystem damageSystem;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	Random random = new Random();
	SoundClip fireSound;
	
	String[] fireSoundPaths = {
			"resources/sounds/fire2.wav"
	};
	
	SoundClip[] fireSounds = new SoundClip[fireSoundPaths.length];
	
	public ProjectileSystem(Client client, ProjectileRenderer renderer, Physics scene, Engine engine){
		this.client = client;
		this.renderer = renderer;
		this.physics = scene;
		this.engine = engine;
		float INITIAL_GAIN = 0.15f;
		for(int i = 0; i < fireSounds.length; i++){
			fireSounds[i] = new SoundClip(fireSoundPaths[i]);
			fireSounds[i].setGain(INITIAL_GAIN);
		}
	}
	
	public void setEffectSystem(EffectsSystem effects){
		this.effects = effects;
	}
	
	public void setDamageSystem(DamageSystem damage){
		this.damageSystem = damage;
	}
	
	SoundClip getFireSound(){
		return fireSounds[random.nextInt(fireSounds.length)];
	}
	
	public void create(int playerID, int weaponID, int projectileID, Vector2f position, float orientation){
		Projectile p = ProjectileFactory.createProjectile(engine, projectileID);
		p.effects = effects;
		
		Vector2f direction = new Vector2f();
		orientation += Math.PI / 2.0f;
		direction.x = (float)Math.cos(orientation);
		direction.y = (float)Math.sin(orientation);
		
		if(p instanceof HitscanProjectile){
			
			HitscanProjectile projectile = (HitscanProjectile) p;
			
			RaycastInfo raycastInfo = physics.raycast(position, direction, MAX_RANGE, Masks.PROJECTILE, Masks.WALL | Masks.OTHER_SHIELD);
			Vector2f end = new Vector2f();
			
			if(raycastInfo.hit){
				end = raycastInfo.contact;
			} else {
				end.x = position.x + direction.x * MAX_RANGE;
				end.y = position.y + direction.y * MAX_RANGE;
			}
			
			projectile.start = position;
			projectile.end.set(end);
			damageSystem.create(playerID, weaponID, end.x, end.y, 3, projectile.damage);
			effects.at(end.x - direction.x * 0.5f, end.y - direction.y * 0.5f, raycastInfo.normal).emitImpactEffect();
			effects.at(position.x, position.y, direction).emitImpactEffect();
		} else if(p instanceof RocketProjectile){
			RocketProjectile projectile = (RocketProjectile) p;
			projectile.init(position.x, position.y, direction);
			physics.add(projectile);
			effects.at(position.x, position.y, direction).emitImpactEffect();
		}
		
		renderer.add(p);
		projectiles.add(p);
	}
	
	public void update(){
		for(int i = 0; i < projectiles.size(); i++){
			Projectile projectile = projectiles.get(i);
			projectile.update();
			if(projectile.isExpired){
				if(projectile instanceof PhysicsObject){
					PhysicsObject p = (PhysicsObject) projectile;
					physics.remove(p);
				}
				
				projectiles.remove(i);
				renderer.remove(projectile);
			}
		}
	}
	
}
