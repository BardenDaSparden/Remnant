package com.remnant.map;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;
import org.joml.Vector2f;

import com.remnant.engine.Engine;
import com.remnant.gamestate.BaseMPState;
import com.remnant.gamestate.GameState;
import com.remnant.player.Player;

public class HealthPickup extends Pickup {

	static final int PICKUP_RANGE = 15;
	int healthAmount = 10;
	final int respawnTime;
	Timer respawnTimer;
	
	public HealthPickup(Engine engine, int respawnTime) {
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
		light.color.set(0.7f, 0.2f, 0.2f);
		light.castShadows = false;
	}

	@Override
	public void update(){
		super.update();
		respawnTimer.tick();
	}
	
	
	public void setHealthAmount(int amt){
		healthAmount = amt;
	}
	
	@Override
	public void doAction(Player player) {
		if(!isSpawned)
			return;
		
		if(player.fullHealth())
			return;
		
		GameState state = engine.getGameState();
		BaseMPState mps = null;
		
		if(state instanceof BaseMPState){
			mps = (BaseMPState) state;
			
			Vector2f position = super.body.getTransform().getTranslation();
			
			mps.effectsSystem.at(position.x, position.y, new Vector2f(0, 1)).emitHealthPickupEffect();
		}
		
		isSpawned = false;
		light.isActive = false;
		respawnTimer.start();
		body.setEnable(false);
		player.health += healthAmount;
		if(player.health > 100)
			player.health = 100;
	}

}
