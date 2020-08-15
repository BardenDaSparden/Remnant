//package com.remnant.player;
//
//import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
//import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
//import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
//import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
//import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
//
//import org.joml.Vector2f;
//
//import com.remnant.graphics.LightingSystem;
//import com.remnant.input.GLFWInput;
//import com.remnant.input.GLFWKeyboard;
//import com.remnant.input.GLFWMouse;
//import com.remnant.input.KeyboardListener;
//import com.remnant.input.MouseListener;
//import com.remnant.math.Math2D;
//import com.remnant.sound.SoundClip;
//import com.remnant.weapons.NullWeapon;
//import com.remnant.weapons.Weapon;
//
//public class PlayerController {
//
//	static final int MOVE_FORWARD = GLFW_KEY_W;
//	static final int MOVE_BACKWARD = GLFW_KEY_S;
//	static final int MOVE_LEFT = GLFW_KEY_A;
//	static final int MOVE_RIGHT = GLFW_KEY_D;
//	static final int ACTION_RELOAD = GLFW_KEY_R;
//	
//	Player player;
//	PlayerCamera playerCamera;
//	GLFWInput input;
//	GLFWKeyboard keyboard;
//	GLFWMouse mouse;
//	
//	boolean isListening = false;
//	KeyboardListener keyboardListener;
//	MouseListener mouseListener;
//	
//	final float FORWARD_FORCE = 15000;
//	final float STRAFE_FORCE = 12500;
//	final float BACKWARDS_FORCE = 10000;
//	
//	final int STEP_LENGTH = 40;
//	int stepTime = 0;
//	boolean isMoving = false;
//	SoundClip stepSound;
//	
//	Vector2f movementDirection = new Vector2f();
//	float movementForce = 0;
//	Vector2f force = new Vector2f();
//	//float mouseDX = 0;
//	
//	protected int health = 100;
//	protected int armor = 25;
//	
//	Vector2f primaryWeaponPosition;
//	Vector2f primaryWeaponOffset;
//	
//	Weapon primaryWeapon;
//	Weapon secondaryWeapon;
//	
//	public PlayerController(Player player, PlayerCamera playerCamera, GLFWInput input, LightingSystem lighting){
//		this.player = player;
//		this.playerCamera = playerCamera;
//		this.input = input;
//		keyboard = input.getKeyboard();
//		keyboardListener = new KeyboardListener() {
//			
//			@Override
//			public void onKeyRelease(int key) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onKeyPress(int key) {
//				if(key == ACTION_RELOAD){
//					primaryWeapon.reload();
//				}
//			}
//			
//			@Override
//			public void onChar(char ch) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		
//		mouseListener = new MouseListener() {
//			@Override
//			public void onScroll(float dy) {
//				swap();
//			}
//			
//			@Override
//			public void onMove(float x, float dy) {
////				mouseDX = x;
////				orient();
//			}
//			
//			@Override
//			public void onButtonRelease(int button) {
//				
//			}
//			
//			@Override
//			public void onButtonPress(int button) {
////				if(button == 0){
////					primaryWeapon.fire();
////				}
//			}
//		};
//		
//		mouse = input.getMouse();
//		
//		primaryWeaponPosition = new Vector2f(0, 0);
//		primaryWeaponOffset = new Vector2f(25, 0);
//		
//		stepSound = new SoundClip("resources/sounds/hydraulic_step_quiet.wav");
//		stepSound.setGain(0.65f);
//		
//		primaryWeapon = new NullWeapon();
//		secondaryWeapon = new NullWeapon();
//		
//		toggleInputListening();
//		
//	}
//	
//	public void setPlayer(Player player){
//		this.player = player;
//	}
//	
//	void move(){
//		force.x = movementDirection.x * movementForce;
//		force.y = movementDirection.y * movementForce;
//		player.body.applyForceToCenter(force);
//	}
//	
////	void move(){
////		Math2D.rotate(Math2D.ORIGIN, movementDirection, player.body.getTransform().getRotation());
////		force.x = movementDirection.x * movementForce;
////		force.y = movementDirection.y * movementForce;
////		player.body.applyForceToCenter(force);
////	}
//	
////	void orient(){
////		float torque = -(float)mouseDX * 360 * 10;
////		player.body.applyTorque(torque);
////		mouseDX = 0;
////	}
//	
//	void swap(){
//		primaryWeapon.toggleVisibility();
//		secondaryWeapon.toggleVisibility();
//		Weapon temp = primaryWeapon;
//		primaryWeapon = secondaryWeapon;
//		secondaryWeapon = temp;
//	}
//	
//	void fire(){
//		primaryWeapon.fire();
//	}
//	
//	void reload(){
//		primaryWeapon.reload();
//	}
//	
//	public void setStartingWeapons(Weapon primary, Weapon secondary){
//		primaryWeapon = primary;
//		secondaryWeapon = secondary;
//	}
//	
//	public void update(){
//		
//		movementForce = 0;
//		movementDirection.set(0, 0);
//		isMoving = false;
//		
//		if(isListening){
//			if(keyboard.isKeyDown(MOVE_FORWARD)){
//				movementDirection.y = 1;
//				movementForce = FORWARD_FORCE;
//				isMoving = true;
//			}
//			
//			if(keyboard.isKeyDown(MOVE_BACKWARD)){
//				movementDirection.y = -1;
//				movementForce = BACKWARDS_FORCE;
//				isMoving = true;
//			}
//			
//			if(keyboard.isKeyDown(MOVE_LEFT)){
//				movementDirection.x = -1;
//				movementForce = STRAFE_FORCE;
//				isMoving = true;
//			}
//			
//			if(keyboard.isKeyDown(MOVE_RIGHT)){
//				movementDirection.x = 1;
//				movementForce = STRAFE_FORCE;
//				isMoving = true;
//			}
//			
//			if(mouse.isButtonDown(0)){
//				primaryWeapon.fire();
//			}
//		}
//		
//		if(isMoving){
//			stepTime++;
//			if(stepTime >= STEP_LENGTH){
//				stepTime = 0;
//			}
//			if(stepTime == STEP_LENGTH / 2){
//				stepSound.play();
//			}
//		} else {
//			if(stepTime - 1 > -1)
//				stepTime--;
//		}
//		
//		Vector2f bodyPosition = player.body.getTransform().getTranslation();
//		Vector2f cursorPos = new Vector2f((float)input.getMouse().getX(), (float)input.getMouse().getY());
//		
//		playerCamera.setOffset(cursorPos.x / 2.0f, cursorPos.y / 2.0f);
//		
//		float angleToCursor = (float)Math.atan2(cursorPos.y, cursorPos.x) - (float)Math.PI / 2.0f;
//		
//		move();
//		
//		if(isListening){
//			player.getBody().setRotation(angleToCursor);
//		}
//		
//		primaryWeaponPosition.set(primaryWeaponOffset.x, primaryWeaponOffset.y);
//		Math2D.rotate(primaryWeaponPosition, player.body.getTransform().getRotation());
//		primaryWeaponPosition.add(bodyPosition);
//		
//		primaryWeapon.setPosition(primaryWeaponPosition.x, primaryWeaponPosition.y);
//		primaryWeapon.setOrientation(player.body.getTransform().getRotation());
//	}
//	
//	public void toggleInputListening(){
//		isListening = !isListening;
//		if(isListening){
//			keyboard.addListener(keyboardListener);
//			mouse.addListener(mouseListener);
//		} else {
//			keyboard.removeListener(keyboardListener);
//			mouse.removeListener(mouseListener);
//		}
//	}
//	
//}
