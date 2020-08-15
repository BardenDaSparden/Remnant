 
package com.remnant.map;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.TypedRenderer;
import com.remnant.math.Color;
import com.remnant.weapon.WeaponID;

public class PickupRenderer extends TypedRenderer {

	static final String NAME = "Pickup Renderer";
	
	Texture pistolBody;
	Texture assaultBody;
	Texture combatBody;
	Texture rifleBody;
	Texture sniperBody;
	Texture rocketLauncherBody;
	
	Texture healthPickup;
	
	ArrayList<Pickup> allPickups = new ArrayList<Pickup>();
	
	float s = 1f;
	
	public PickupRenderer(int layerIndex) {
		super(NAME, layerIndex);
		
		pistolBody = new Texture("images/pistol.png");
		assaultBody = new Texture("images/rifle.png");
		combatBody = new Texture("images/combat-rifle.png");
		rifleBody = new Texture("images/assault-rifle-2.png");
		sniperBody = new Texture("images/sniper.png");
		rocketLauncherBody = new Texture("images/rocket-launcher.png");
		
		healthPickup = new Texture("images/pickup-health-diffuse.png");
	}

	public void add(Pickup pickup){
		allPickups.add(pickup);
	}
	
	public void remove(Pickup pickup){
		allPickups.remove(pickup);
	}
	
	public void clear(){
		allPickups.clear();
	}
	
	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	void drawPickup(Pickup pickup, Texture t, float w){
		Vector2f position = pickup.getBody().getTransform().getTranslation();
		float orientation = pickup.getBody().getTransform().getRotation();
		if(pickup instanceof WeaponPickup){
			orientation += (float)Math.PI / 2.0f;
			s = 1;
		} else {
			s = 1;
		}
		float opacity = (pickup.isSpawned) ? 1 : 0;
		Vector4f color = new Vector4f(Color.WHITE);
		color.w = opacity * w;
		
		batch.draw(position.x, position.y, t.getWidth(), t.getHeight(), orientation, s, s, t, color);
	}
	
	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			for(int i = 0; i < allPickups.size(); i++){
				Pickup pickup = allPickups.get(i);
				if(pickup instanceof HealthPickup)
					drawPickup(pickup, healthPickup, 1);
				else if(pickup instanceof WeaponPickup){
					WeaponPickup p = (WeaponPickup) pickup;
					Texture texture = null;
					if(p.weaponID == WeaponID.PISTOL){
						texture = pistolBody;
					} else if(p.weaponID == WeaponID.ASSAULT_RIFLE){
						texture = assaultBody;
					} else if(p.weaponID == WeaponID.COMBAT_RIFLE){
						texture = combatBody;
					} else if(p.weaponID == WeaponID.RIFLE){
						texture = rifleBody;
					} else if(p.weaponID == WeaponID.SNIPER){
						texture = sniperBody;
					} else if(p.weaponID == WeaponID.ROCKET_LAUNCHER){
						texture = rocketLauncherBody;
					}
					drawPickup(pickup, texture, 1);
				}
			}
		batch.end();
	}
	
	@Override
	public void drawDiffuse(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			for(int i = 0; i < allPickups.size(); i++){
				Pickup pickup = allPickups.get(i);
				if(pickup instanceof HealthPickup)
					drawPickup(pickup, healthPickup, 0.5f);
				else if(pickup instanceof WeaponPickup){
					WeaponPickup p = (WeaponPickup) pickup;
					Texture texture = null;
					if(p.weaponID == WeaponID.PISTOL){
						texture = pistolBody;
					} else if(p.weaponID == WeaponID.ASSAULT_RIFLE){
						texture = assaultBody;
					} else if(p.weaponID == WeaponID.COMBAT_RIFLE){
						texture = combatBody;
					} else if(p.weaponID == WeaponID.RIFLE){
						texture = rifleBody;
					} else if(p.weaponID == WeaponID.SNIPER){
						texture = sniperBody;
					} else if(p.weaponID == WeaponID.ROCKET_LAUNCHER){
						texture = rocketLauncherBody;
					}
					drawPickup(pickup, texture, 1);
				}
			}
			
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawIllumination(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < allPickups.size(); i++){
			Pickup pickup = allPickups.get(i);
			if(pickup instanceof HealthPickup)
				drawPickup(pickup, healthPickup, 0.15f);
		}
		batch.end();
	}

	@Override
	public void drawEmissive(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < allPickups.size(); i++){
			Pickup pickup = allPickups.get(i);
			if(pickup instanceof HealthPickup)
				drawPickup(pickup, healthPickup, 1f);
			else if(pickup instanceof WeaponPickup){
				WeaponPickup p = (WeaponPickup) pickup;
				Texture texture = null;
				if(p.weaponID == WeaponID.PISTOL){
					texture = pistolBody;
				} else if(p.weaponID == WeaponID.ASSAULT_RIFLE){
					texture = assaultBody;
				} else if(p.weaponID == WeaponID.COMBAT_RIFLE){
					texture = combatBody;
				} else if(p.weaponID == WeaponID.RIFLE){
					texture = rifleBody;
				} else if(p.weaponID == WeaponID.SNIPER){
					texture = sniperBody;
				} else if(p.weaponID == WeaponID.ROCKET_LAUNCHER){
					texture = rocketLauncherBody;
				}
				drawPickup(pickup, texture, 0.6f);
			}
		}
	batch.end();
	}
	
}
