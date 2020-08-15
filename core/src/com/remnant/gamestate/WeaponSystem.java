package com.remnant.gamestate;

import org.joml.Vector2f;

import com.remnant.engine.Engine;
import com.remnant.engine.GameSystem;
import com.remnant.math.Math2D;
import com.remnant.net.packet.WeaponFireResponse;
import com.remnant.player.Player;
import com.remnant.weapon.Weapon;
import com.remnant.weapon.WeaponRenderer;
import com.remnant.weapon.projectile.DamageSystem;
import com.remnant.weapon.projectile.ProjectileRenderer;
import com.remnant.weapon.projectile.ProjectileSystem;

public class WeaponSystem extends GameSystem {
	
	public WeaponSystem(Engine game) {
		super(game);
		
	}

	DamageSystem damageSystem;
	ProjectileSystem projectileSystem;
	
	WeaponRenderer weaponRenderer;
	ProjectileRenderer projectileRenderer;
	
	BaseMPState multiplayerState = null;
	public void setGameState(BaseMPState state){
		multiplayerState = state;
	}
	
	//TODO refactor method, just copied from GAMESTATE
	Vector2f offset = new Vector2f(0, 0);
	public void net_playerShoot(WeaponFireResponse response){
		Player player = multiplayerState.playerSystem.getPlayerByID(response.playerID);
		
		//System.out.println(player != null);
		
		float orientation = player.getBody().getTransform().getRotation();
		Vector2f translation = new Vector2f(player.getBody().getTransform().getTranslation());
		
		Weapon weapon = (response.isPrimary) ? player.getPrimaryWeapon() : player.getSecondaryWeapon();
		
		if(response.isPrimary){
			offset.set(weapon.getMarkerData().offset.x + weapon.getMarkerData().barrelOffset.x, weapon.getMarkerData().offset.y + weapon.getMarkerData().barrelOffset.y);
			Math2D.rotate(offset, orientation);
			offset.add(translation);
		} else {
			offset.set(-weapon.getMarkerData().offset.x + weapon.getMarkerData().barrelOffset.x, -weapon.getMarkerData().offset.y + weapon.getMarkerData().barrelOffset.y);
			Math2D.rotate(offset, orientation);
			offset.add(translation);
		}
		
		projectileSystem.create(player.getID(), weapon.getID(), weapon.getProjectileID(), offset, orientation);
	}
	
	@Override
	public void load() {
		damageSystem = new DamageSystem(engine.physics);
		
		weaponRenderer = new WeaponRenderer();
		projectileRenderer = new ProjectileRenderer();
		projectileSystem = new ProjectileSystem(engine.client, projectileRenderer, engine.physics, engine);
		projectileSystem.setEffectSystem(multiplayerState.effectsSystem);
		projectileSystem.setDamageSystem(damageSystem);
		
		engine.renderer.add(weaponRenderer);
		engine.renderer.add(projectileRenderer);
	}

	@Override
	public void update() {
		projectileSystem.update();
		damageSystem.update();
	}

	@Override
	public void unload() {
		engine.renderer.remove(weaponRenderer);
		engine.renderer.remove(projectileRenderer);
	}
	
}
