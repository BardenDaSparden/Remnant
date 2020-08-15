package com.remnant.player;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.FontLoader;
import com.remnant.graphics.TextureRegion;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.math.Color;

public class PlayerHUDRenderer extends TypedUIRenderer {
	
	static final int LAYER = 9998;
	
	int width;
	int height;
	PlayerController controller;
	Camera camera;
	
	Texture hudOverlay;
	
	Vector2f healthMeterPos = new Vector2f();
	Vector2f shieldMeterPos = new Vector2f();
	Vector2f motionSensorPos = new Vector2f();
	Vector2f weaponInfoPos = new Vector2f();
	
	BitmapFont font;
	
	Texture shieldMeterOutline;
	Texture shieldMeterFill;
	TextureRegion shieldMeterFillClipped;
	
	Texture healthMeterOutline;
	Texture healthMeterFill;
	
	Texture motionSensor;
	
	Texture weaponInfoOutline;
	
	Texture white;
	
	Vector4f shieldColor1 = new Vector4f(0.35f, 0.87f, 0.87f, 0.5f);
	Vector4f shieldColor2 = new Vector4f(0.87f, 0.87f, 0.35f, 0.5f);
	Vector4f shieldColor3 = new Vector4f(0.87f, 0.35f, 0.25f, 0.5f);
	
	public PlayerHUDRenderer(int width, int height, PlayerController controller){
		super(LAYER);
		this.width = width;
		this.height = height;
		this.controller = controller;
		
		font = FontLoader.getFont("fonts/imagine_24.fnt");
		
		shieldMeterOutline = new Texture("images/shield-meter-outline.png");
		shieldMeterFill = new Texture("images/shield-meter-fill.png");
		shieldMeterFillClipped = new TextureRegion(shieldMeterFill);
		
		healthMeterOutline = new Texture("images/health-meter-outline.png");
		healthMeterFill = new Texture("images/health-meter-fill-piece.png");
		
		motionSensor = new Texture("images/ui/motion-sensor.png");
		
		weaponInfoOutline = new Texture("images/weapon-info-outline.png");
		
		white = new Texture("images/white.png");
		
		camera = new Camera(width, height);
		hudOverlay = new Texture("images/ui/hud-overlay.png");
		
		healthMeterPos.x = 0;
		healthMeterPos.y = height / 2 - 50;
		
		shieldMeterPos.x = 0;
		shieldMeterPos.y = healthMeterPos.y - healthMeterOutline.getHeight() / 2 - 10;
		
		motionSensorPos.x = -width / 2.0f + motionSensor.getWidth() / 2.0f + 50;
		motionSensorPos.y = -height / 2.0f + motionSensor.getHeight() / 2.0f + 50;
		
		weaponInfoPos.x = width / 2.0f - weaponInfoOutline.getWidth() / 2.0f - 50;
		weaponInfoPos.y = height / 2.0f - weaponInfoOutline.getHeight() / 2.0f - 50;
	}
	
	void drawHealthMeter(){
		int health = controller.player.health;
		
		final float HEALTH_PER_PIECE = (float)controller.player.maxHealth / 22.0f;
		int numPieces = (int)((float)health / HEALTH_PER_PIECE);
		//float rem = health % HEALTH_PER_PIECE;
		
		Vector2f spacing = new Vector2f(2, 0);
		Vector2f healthFillOffset = new Vector2f(30 + spacing.x + 1 + healthMeterFill.getWidth() / 2.0f, 0);
		Vector4f healthColor = new Vector4f();
		if(health < 26){
			healthColor.set(shieldColor3);
		} else if(health < 51){
			healthColor.set(shieldColor2);
		} else {
			healthColor.set(shieldColor1);
		}
		
		batch.draw(healthMeterPos.x, healthMeterPos.y, healthMeterOutline.getWidth(), healthMeterOutline.getHeight(), 0, 1, 1, healthMeterOutline, Color.WHITE_QUARTER_OPAQUE);
		
		for(int i = 0; i < numPieces; i++){
			batch.draw(healthMeterPos.x + healthFillOffset.x + (i * healthMeterFill.getWidth() + i * spacing.x), healthMeterPos.y, healthMeterFill.getWidth(), healthMeterFill.getHeight(), 0, 1, 1, healthMeterFill, healthColor);
		}
		
		for(int i = 0; i < numPieces; i++){
			batch.draw(healthMeterPos.x - healthFillOffset.x - (i * healthMeterFill.getWidth() + i * spacing.x), healthMeterPos.y, healthMeterFill.getWidth(), healthMeterFill.getHeight(), 0, 1, 1, healthMeterFill, healthColor);
		}
		
		batch.draw(healthMeterPos.x, healthMeterPos.y, 60, 26, 0, 1, 1, white, shieldColor1);
		String s = health+"";
		font.draw(healthMeterPos.x - font.getWidth(s) / 2 + 1, healthMeterPos.y + font.getHeight(s) / 2 + 1, s, batch, Color.BLACK_HALF_OPAQUE);
	}
	
	void drawShieldMeter(){
		//int shield = controller.player.shield.health;
		//float scale = (float)shield / (float)controller.player.shield.maxHealth;
		//scale *= scale;
		//float invScale = 1.0f - scale;
		//float offsetX = shieldMeterFill.getWidth() / 2.0f * invScale; 
		
		//shieldMeterFillClipped = new TextureRegion(shieldMeterFill, offsetX, 0, shieldMeterFill.getWidth() * scale, shieldMeterFill.getHeight());
		
		batch.draw(shieldMeterPos.x, shieldMeterPos.y, shieldMeterOutline.getWidth(), shieldMeterOutline.getHeight(), 0, 1, 1, shieldMeterOutline, Color.WHITE_QUARTER_OPAQUE);
		batch.draw(shieldMeterPos.x, shieldMeterPos.y, shieldMeterFillClipped.getWidth(), shieldMeterFillClipped.getHeight(), 0, 1, 1, shieldMeterFillClipped, Color.WHITE_QUARTER_OPAQUE);
	}
	
	void drawMotionSensor(){
		batch.draw(motionSensorPos.x, motionSensorPos.y, motionSensor.getWidth(), motionSensor.getHeight(), 0, 1, 1, motionSensor, Color.WHITE_HALF_OPAQUE);
	}
	
	void drawWeaponInfo(){
		batch.draw(-weaponInfoPos.x, weaponInfoPos.y, weaponInfoOutline.getWidth(), weaponInfoOutline.getHeight(), 0, -1, 1, weaponInfoOutline, Color.WHITE_HALF_OPAQUE);
		batch.draw(weaponInfoPos.x, weaponInfoPos.y, weaponInfoOutline.getWidth(), weaponInfoOutline.getHeight(), 0, 1, 1, weaponInfoOutline, Color.WHITE_HALF_OPAQUE);
	}
	
	void drawGrenadeInfo(){
		
	}
	
	void drawGameInfo(){
		
	}
	
	@Override
	public void draw() {
		
		cameraStack.push();
		cameraStack.current().set(camera);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				drawHealthMeter();
				drawShieldMeter();
				drawMotionSensor();
				drawWeaponInfo();
				//batch.draw(0, 0, width, height, 0, 1, 1, hudOverlay, Color.WHITE_HALF_OPAQUE);
			batch.end();
		cameraStack.pop();
	}

}
