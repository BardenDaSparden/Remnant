//package com.remnant.player;
//
//import org.joml.Vector2f;
//import org.joml.Vector4f;
//
//import com.remnant.graphics.BitmapFont;
//import com.remnant.graphics.BlendState;
//import com.remnant.graphics.Camera;
//import com.remnant.graphics.FontLoader;
//import com.remnant.graphics.Texture;
//import com.remnant.graphics.TypedUIRenderer;
//import com.remnant.math.Color;
//import com.remnant.weapons.Ammo;
//
//public class PlayerHUDRenderer extends TypedUIRenderer {
//	
//	static final int LAYER = 9998;
//	
//	int width;
//	int height;
//	PlayerController controller;
//	
//	Camera camera;
//	
//	final int PADDING_X = 30;
//	final int PADDING_Y = 30;
//	
//	Texture armorMeterOutline;
//	Texture armorMeterFill;
//	Vector2f armorMeterPosition;
//	
//	Texture healthMeterOutline;
//	Texture healthMeterFill;
//	Vector2f healthMeterPosition;
//	
//	Texture ammoMeterOutline;
//	Texture ammoMeterFill;
//	Vector2f ammoMeterPosition;
//	
//	BitmapFont font0;
//	
//	Vector4f color50;
//	Vector4f labelBlue;
//	Vector4f labelGreen;
//	Vector4f labelRed;
//	
//	public PlayerHUDRenderer(int width, int height, PlayerController controller){
//		super(LAYER);
//		this.width = width;
//		this.height = height;
//		this.controller = controller;
//		camera = new Camera(width, height);
//		armorMeterOutline = new Texture("images/ui/armor_meter_outline.png");
//		armorMeterFill = new Texture("images/ui/armor_meter_fill.png");
//		healthMeterOutline = new Texture("images/ui/health_meter_outline.png");
//		healthMeterFill = new Texture("images/ui/health_meter_fill.png");
//		ammoMeterOutline = new Texture("images/ui/ammo_meter_outline.png");
//		ammoMeterFill = new Texture("images/ui/ammo_meter_fill.png");
//		armorMeterPosition = new Vector2f(-width / 2.0f + armorMeterOutline.getWidth() / 2.0f + PADDING_X, -height / 2.0f + armorMeterOutline.getHeight() / 2.0f + PADDING_Y + 40);
//		healthMeterPosition = new Vector2f(-width / 2.0f + healthMeterOutline.getWidth() / 2.0f + PADDING_X, -height / 2.0f + healthMeterOutline.getHeight() / 2.0f + PADDING_Y);
//		ammoMeterPosition = new Vector2f(width / 2.0f - ammoMeterOutline.getWidth() / 2.0f - PADDING_X, -height / 2.0f + ammoMeterOutline.getHeight() / 2.0f + PADDING_Y);
//		
//		font0 = FontLoader.getFont("fonts/ITT.fnt");
//		
//		labelGreen = new Vector4f(0.4f, 0.78f, 0.36f, 0.5f);
//		labelBlue = new Vector4f(0.42f, 0.62f, 0.82f, 0.5f);
//		labelRed = new Vector4f(0.87f, 0.37f, 0.37f, 0.5f);
//		color50 = new Vector4f(1, 1, 1, 0.50f);
//	}
//	
//	@Override
//	public void draw() {
//		
//		float armorScale = controller.armor / 100.0f;
//		float healthScale = controller.health / 100.0f;
//		
//		Ammo ammo = controller.primaryWeapon.getAmmo();
//		float ammoScale = (float)ammo.getClipCurrent() / (float)ammo.getClipMax();
//		
//		cameraStack.push();
//		cameraStack.current().set(camera);
//			batch.begin(BlendState.ALPHA_TRANSPARENCY);
//				batch.draw(healthMeterPosition.x - (healthMeterFill.getWidth() * (1.0f - healthScale) / 2.0f), healthMeterPosition.y, healthMeterFill.getWidth(), healthMeterFill.getHeight(), 0, healthScale, 1, healthMeterFill, color50);
//				batch.draw(healthMeterPosition.x, healthMeterPosition.y, healthMeterOutline.getWidth(), healthMeterOutline.getHeight(), 0, 1, 1, healthMeterOutline, Color.WHITE);
//				font0.draw(healthMeterPosition.x + healthMeterOutline.getWidth() / 2 + 8, healthMeterPosition.y + healthMeterOutline.getHeight() / 2, controller.health + "", batch, labelBlue);
//				batch.draw(armorMeterPosition.x - (armorMeterFill.getWidth() * (1.0f - armorScale) / 2.0f), armorMeterPosition.y, armorMeterFill.getWidth(), armorMeterFill.getHeight(), 0, armorScale, 1, armorMeterFill, color50);
//				batch.draw(armorMeterPosition.x, armorMeterPosition.y, armorMeterOutline.getWidth(), armorMeterOutline.getHeight(), 0, 1, 1, armorMeterOutline, Color.WHITE);
//				font0.draw(armorMeterPosition.x + armorMeterOutline.getWidth() / 2.0f + 8, armorMeterPosition.y + armorMeterOutline.getHeight() / 2.0f, controller.armor + "", batch, labelGreen);
//				batch.draw(ammoMeterPosition.x + (ammoMeterFill.getWidth() * (1.0f - ammoScale) / 2.0f), ammoMeterPosition.y, ammoMeterFill.getWidth(), ammoMeterFill.getHeight(), 0, ammoScale, 1, ammoMeterFill, color50);
//				batch.draw(ammoMeterPosition.x, ammoMeterPosition.y, ammoMeterOutline.getWidth(), ammoMeterOutline.getHeight(), 0, 1, 1, ammoMeterOutline, Color.WHITE);
//				font0.draw(ammoMeterPosition.x - ammoMeterOutline.getWidth() / 2.0f - 32, ammoMeterPosition.y + ammoMeterOutline.getHeight() / 2.0f, ammo.getClipCurrent() + "", batch, labelRed);
//				font0.draw(ammoMeterPosition.x - ammoMeterOutline.getWidth() / 2.0f - 32, ammoMeterPosition.y + ammoMeterOutline.getHeight() / 2.0f + PADDING_Y, ammo.getCurrent() + "", batch, labelRed);
//			batch.end();
//		cameraStack.pop();
//	}
//
//}
