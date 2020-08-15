package com.remnant.weapon;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.TypedRenderer;
import com.remnant.math.Color;
import com.remnant.math.Interpolator;
import com.remnant.math.LinearInterpolator;
import com.remnant.math.Math2D;
import com.remnant.math.Transform;
import com.remnant.player.Player;

public class WeaponRenderer extends TypedRenderer {
	
	static final int LAYER = 101;
	static final String NAME = "Weapon Renderer";
	
	ArrayList<Player> players = new ArrayList<Player>();
	
	Texture pistolBody;
	Texture assaultBody;
	Texture combatBody;
	Texture rifleBody;
	Texture sniperBody;
	Texture rocketLauncherBody;
	
	Texture normal;
	Texture illum;
	
	Vector2f rightWeaponOffset = new Vector2f(20, 4);
	Vector2f offset = new Vector2f();
	
	Interpolator inter = new LinearInterpolator();
	Transform interpolatedTransform = new Transform();
	
	public WeaponRenderer(){
		super(NAME, LAYER);
		pistolBody = new Texture("images/pistol.png");
		assaultBody = new Texture("images/rifle.png");
		combatBody = new Texture("images/combat-rifle.png");
		rifleBody = new Texture("images/assault-rifle-2.png");
		sniperBody = new Texture("images/sniper.png");
		rocketLauncherBody = new Texture("images/rocket-launcher.png");
		normal = new Texture("images/normal.png");
		illum = new Texture("images/white.png");
	}
	
	public void add(Player player){
		players.add(player);
	}
	
	public void remove(Player player){
		players.remove(player);
	}
	
	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	void drawWeapon(double alpha, Player player, Weapon weapon, boolean flip){
		Transform previous = player.getBody().getTransform();
		Transform current = player.getBody().getPreviousTransform();
		interpolatedTransform.setRotation(inter.interpolate(previous.getRotation(), current.getRotation(), (float)alpha));
		interpolatedTransform.setScale(inter.interpolate(previous.getScale(), current.getScale(), (float)alpha));
		interpolatedTransform.setTranslation(inter.interpolate(previous.getTranslation(), current.getTranslation(), (float)alpha));
		Texture texture = null;
		
		if(weapon.ID == WeaponID.PISTOL){
			texture = pistolBody;
		} else if(weapon.ID == WeaponID.ASSAULT_RIFLE){
			texture = assaultBody;
		} else if(weapon.ID == WeaponID.COMBAT_RIFLE){
			texture = combatBody;
		} else if(weapon.ID == WeaponID.RIFLE){
			texture = rifleBody;
		} else if(weapon.ID == WeaponID.SNIPER){
			texture = sniperBody;
		} else if(weapon.ID == WeaponID.ROCKET_LAUNCHER){
			texture = rocketLauncherBody;
		}
		
		offset.set(weapon.markers.offset);
		Math2D.rotate(offset, interpolatedTransform.getRotation());
		
		if(!flip)
			offset.set(-offset.x, -offset.y);
		
		batch.draw(current.getTranslation().x + offset.x, current.getTranslation().y + offset.y, texture.getWidth(), texture.getHeight(), current.getRotation(), 1f, 1f, texture, Color.WHITE);
	}
	
	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < players.size(); i++){
			Player player = players.get(i);
			Weapon primary = player.getPrimaryWeapon();
			Weapon secondary = player.getSecondaryWeapon();
			drawWeapon(alpha, player, primary, true);
			//drawWeapon(alpha, player, secondary, false);
		}
		batch.end();
	}

	@Override
	public void drawDiffuse(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < players.size(); i++){
			Player player = players.get(i);
			Weapon primary = player.getPrimaryWeapon();
			Weapon secondary = player.getSecondaryWeapon();
			drawWeapon(alpha, player, primary, true);
			//drawWeapon(alpha, player, secondary, false);
		}
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
//		batch.begin(BlendState.ALPHA_TRANSPARENCY);
//		for(int i = 0; i < players.size(); i++){
//			Player player = players.get(i);
//			Weapon weapon = player.getPrimaryWeapon();
//			Transform transform = player.getBody().getTransform();
//			Texture texture = null;
//			
//			if(weapon.ID == WeaponID.PISTOL){
//				texture = pistolBody;
//			} else if(weapon.ID == WeaponID.ASSAULT_RIFLE){
//				texture = assaultBody;
//			}
//			
//			texture = normal;
//			
//			batch.draw(transform.getTranslation().x, transform.getTranslation().y, texture.getWidth(), texture.getHeight(), transform.getRotation(), 1, 1, texture, Color.WHITE);
//		}
//		batch.end();
	}

	@Override
	public void drawIllumination(double alpha) {
//		batch.begin(BlendState.ALPHA_TRANSPARENCY);
//		for(int i = 0; i < players.size(); i++){
//			Player player = players.get(i);
//			Weapon weapon = player.getPrimaryWeapon();
//			Transform transform = player.getBody().getTransform();
//			Texture texture = null;
//			
//			if(weapon.ID == WeaponID.PISTOL){
//				texture = pistolBody;
//			} else if(weapon.ID == WeaponID.ASSAULT_RIFLE){
//				texture = assaultBody;
//			}
//			
//			texture = illum;
//			
//			batch.draw(transform.getTranslation().x, transform.getTranslation().y, texture.getWidth(), texture.getHeight(), transform.getRotation(), 1, 1, texture, Color.WHITE);
//		}
//		batch.end();
	}
	
	@Override
	public void drawEmissive(double alpha){
		
	}
	
}
