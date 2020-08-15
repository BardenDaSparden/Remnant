package com.remnant.player;

import java.util.ArrayList;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.geometry.Circle;
import com.remnant.geometry.Shape;
import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.FontLoader;
import com.remnant.graphics.TypedRenderer;
import com.remnant.math.Color;
import com.remnant.math.Interpolator;
import com.remnant.math.LinearInterpolator;
import com.remnant.math.Transform;

public class PlayerRenderer extends TypedRenderer {
	
	static final int LAYER = 102;
	static final String NAME = "Player Renderer";
	
	ArrayList<Player> players;
	Player self;
	
	Texture white;
	Texture diffuse;
	Texture shadow;
	Texture normal;
	Texture illum;
	
	Texture shield;
	Texture shieldLightmap;
	Texture shieldEmissive;
	//Vector4f shieldColor = new Vector4f(0.87f, 0.45f, 0.97f, 0.25f); PURPLE
	Vector4f shieldColor = new Vector4f(0.35f, 0.87f, 0.87f, 0.25f);
	Vector4f shieldColor1 = new Vector4f(0.35f, 0.87f, 0.87f, 0.25f);
	Vector4f shieldColor2 = new Vector4f(0.87f, 0.87f, 0.35f, 0.25f);
	Vector4f shieldColor3 = new Vector4f(0.87f, 0.35f, 0.25f, 0.25f);
	
	Interpolator inter = new LinearInterpolator();
	
	Transform interpolatedTransform = new Transform();
	
	BitmapFont nameFont;
	
	float size = 1f;
	float size2 = 1.25f;
	
	public PlayerRenderer(){
		super(NAME, LAYER);
		//this.self = self;
		players = new ArrayList<Player>();
		white = new Texture("images/white.png");
		diffuse = new Texture("images/player-body.png");
		shadow = new Texture("images/player_shadow.png");
		normal = new Texture("images/normal.png");
		illum = new Texture("images/black.png");
		shield = new Texture("images/shield.png");
		shieldEmissive = new Texture("images/shield-lightmap.png");
		shieldLightmap = new Texture("images/shield-lightmap2.png");
		nameFont = FontLoader.getFont("fonts/imagine_16.fnt");
		//players.add(self);
	}
	
	public void add(Player player){
		players.add(player);
	}
	
	public void remove(Player player){
		players.remove(player);
	}
	
	@Override
	public void prepare() {
//		t++;
//		t %= 360;
	}

	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			for(int i = 0; i < players.size(); i++){
				Player player = players.get(i);
				Shape shape = player.getBody().getShape();
				float radius = 0;
				if(shape instanceof Circle)
					radius = ((Circle) shape).radius;
				
				Transform previous = player.getBody().getTransform();
				Transform current = player.getBody().getPreviousTransform();
				interpolatedTransform.setRotation(inter.interpolate(previous.getRotation(), current.getRotation(), (float)alpha));
				interpolatedTransform.setScale(inter.interpolate(previous.getScale(), current.getScale(), (float)alpha));
				interpolatedTransform.setTranslation(inter.interpolate(previous.getTranslation(), current.getTranslation(), (float)alpha));
				batch.draw(current.getTranslation().x, current.getTranslation().y, radius * 2, radius * 2, current.getRotation(), 1, 1, white, Color.WHITE);
				
				//if(player.shield.body.isEnabled())
					//batch.draw(current.getTranslation().x, current.getTranslation().y, shield.getWidth(), shield.getHeight(), 0, size2, size2, shield, shieldColor);
			}
		batch.end();
	}

	@Override
	public void drawDiffuse(double alpha) {
		//Draw Players
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			for(int i = 0; i < players.size(); i++){
				Player player = players.get(i);
				Shape shape = player.getBody().getShape();
				float radius = 0;
				if(shape instanceof Circle)
					radius = ((Circle) shape).radius;
				
				Transform previous = player.getBody().getTransform();
				Transform current = player.getBody().getPreviousTransform();
				interpolatedTransform.setRotation(inter.interpolate(previous.getRotation(), current.getRotation(), (float)alpha));
				interpolatedTransform.setScale(inter.interpolate(previous.getScale(), current.getScale(), (float)alpha));
				interpolatedTransform.setTranslation(inter.interpolate(previous.getTranslation(), current.getTranslation(), (float)alpha));
				
				batch.draw(current.getTranslation().x, current.getTranslation().y, radius * 2, radius * 2, current.getRotation(), 1, 1, white, Color.WHITE);
//				if(player.shield.health > 50){
//					shieldColor.set(shieldColor1);
//				} else if(player.shield.health > 25){
//					shieldColor.set(shieldColor2);
//				} else {
//					shieldColor.set(shieldColor3);
//				}
//				shieldColor.w = 0.5f * player.shield.opacity;
//				if(player.shield.body.isEnabled())
//					batch.draw(current.getTranslation().x, current.getTranslation().y, shield.getWidth(), shield.getHeight(), 0, size2, size2, shield, shieldColor);
			}
		batch.end();
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		
		//Draw Player Names
//		for(int i = 0; i < players.size(); i++){
//			Player player = players.get(i);
//			Transform transform = player.getBody().getTransform();
//			String name = player.name;
//			nameFont.drawCentered(transform.getTranslation().x + 30, transform.getTranslation().y + 60, name, batch, Color.WHITE);
//		}
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			for(int i = 0; i < players.size(); i++){
				Player player = players.get(i);
				Shape shape = player.getBody().getShape();
				float radius = 0;
				if(shape instanceof Circle)
					radius = ((Circle) shape).radius;
				Transform transform = player.getBody().getPreviousTransform();
				batch.draw(transform.getTranslation().x, transform.getTranslation().y, radius * 2, radius * 2, transform.getRotation(), 1, 1, normal, Color.WHITE);
			}
		batch.end();
	}

	@Override
	public void drawIllumination(double alpha) {
		//Vector4f color = new Vector4f(1, 1, 1, 0.1f);
//		batch.begin(BlendState.ALPHA_TRANSPARENCY);
//			for(int i = 0; i < players.size(); i++){
//				Player player = players.get(i);
//				
//				Transform previous = player.getBody().getTransform();
//				Transform current = player.getBody().getPreviousTransform();
//				interpolatedTransform.setRotation(inter.interpolate(previous.getRotation(), current.getRotation(), (float)alpha));
//				interpolatedTransform.setScale(inter.interpolate(previous.getScale(), current.getScale(), (float)alpha));
//				interpolatedTransform.setTranslation(inter.interpolate(previous.getTranslation(), current.getTranslation(), (float)alpha));
//				
//				//batch.draw(transform.getTranslation().x, transform.getTranslation().y, diffuse.getWidth(), diffuse.getHeight(), transform.getRotation(), size, size, illum, Color.BLACK);
////				if(player.shield.health > 50){
////					shieldColor.set(shieldColor1);
////				} else if(player.shield.health > 25){
////					shieldColor.set(shieldColor2);
////				} else {
////					shieldColor.set(shieldColor3);
////				}
////				shieldColor.w = 0.75f * player.shield.opacity;
////				if(player.shield.body.isEnabled())
////					batch.draw(current.getTranslation().x, current.getTranslation().y, shield.getWidth(), shield.getHeight(), 0, size2, size2, shieldEmissive, shieldColor);
//			}
//		batch.end();
	}
	
	@Override
	public void drawEmissive(double alpha){
//		batch.begin(BlendState.ADDITIVE);
//		for(int i = 0; i < players.size(); i++){
//			Player player = players.get(i);
//			
//			Transform previous = player.getBody().getTransform();
//			Transform current = player.getBody().getPreviousTransform();
//			interpolatedTransform.setRotation(inter.interpolate(previous.getRotation(), current.getRotation(), (float)alpha));
//			interpolatedTransform.setScale(inter.interpolate(previous.getScale(), current.getScale(), (float)alpha));
//			interpolatedTransform.setTranslation(inter.interpolate(previous.getTranslation(), current.getTranslation(), (float)alpha));
//			
//			
//			//batch.draw(transform.getTranslation().x, transform.getTranslation().y, diffuse.getWidth(), diffuse.getHeight(), transform.getRotation(), size, size, diffuse, Color.WHITE);
////			if(player.shield.health > 50){
////				shieldColor.set(shieldColor1);
////			} else if(player.shield.health > 25){
////				shieldColor.set(shieldColor2);
////			} else {
////				shieldColor.set(shieldColor3);
////			}
////			shieldColor.w = 0.65f * player.shield.opacity;
////			if(player.shield.body.isEnabled())
////				batch.draw(interpolatedTransform.getTranslation().x, interpolatedTransform.getTranslation().y, shield.getWidth(), shield.getHeight(), 0, size2, size2, shieldLightmap, shieldColor);
//		}
//	batch.end();
	}
}
