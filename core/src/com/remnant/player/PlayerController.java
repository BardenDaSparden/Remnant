package com.remnant.player;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import com.esotericsoftware.kryonet.Client;
import com.remnant.graphics.Lighting;
import com.remnant.input.Input;
import com.remnant.input.Keyboard;
import com.remnant.input.Mouse;
import com.remnant.input.KeyboardListener;
import com.remnant.input.MouseListener;
import com.remnant.math.CubicInterpolator;
import com.remnant.math.Interpolator;
import com.remnant.math.Math2D;
import com.remnant.weapon.Weapon;

public class PlayerController {

	static final int MOVE_FORWARD = GLFW_KEY_W;
	static final int MOVE_BACKWARD = GLFW_KEY_S;
	static final int MOVE_LEFT = GLFW_KEY_A;
	static final int MOVE_RIGHT = GLFW_KEY_D;
	static final int SPRINT = GLFW_KEY_LEFT_SHIFT;
	
	Client client;
	Player player;
	Input input;
	Keyboard keyboard;
	Mouse mouse;
	
	boolean isListening = false;
	KeyboardListener keyboardListener;
	MouseListener mouseListener;
	
	float playerOrientation = 0.0f;
	float newPlayerOrientation = 0.0f;
	
	Interpolator inter = new CubicInterpolator(new Vector2f(0.1f, 0.75f), new Vector2f(0.5f, 0.9f));
	
	
	final int MOVE_FORCE = 45000;
	final int SPRINT_FORCE = 53000;
	boolean isMoving = false;
	float movementDirection = 0;
	float movementForce = 0;
	Vector2f force = new Vector2f();
	
	public PlayerController(Client client, Player player, Input input, Lighting lighting){
		this.client = client;
		this.player = player;
		this.input = input;
		
		keyboard = input.getKeyboard();
		keyboardListener = new KeyboardListener() {
			
			@Override
			public void onKeyRelease(int key) {
				
			}
			
			@Override
			public void onKeyPress(int key) {
				
			}

			@Override
			public void onChar(int codepoint) {
				// TODO Auto-generated method stub
				
			}
		};
		
		mouseListener = new MouseListener() {
			@Override
			public void onScroll(float dy) {
				
			}
			
			@Override
			public void onMove(float x, float dy) {
				if(isListening)
					newPlayerOrientation += (-x) * (float)Math.PI / 512.0f;	
				
			}
			
			@Override
			public void onButtonRelease(int button) {
				
			}
			
			@Override
			public void onButtonPress(int button) {
				if(isListening){
					if(button == Mouse.LEFT_BUTTON){
						Weapon primary = player.getPrimaryWeapon();
						if(primary.getFiringType() == Weapon.FIRE_TYPE_SEMI && primary.canFire()){
							primary.fire(player.ID, true);
						}
					}
				}
			}
		};
		
		mouse = input.getMouse();
		playerOrientation = player.getBody().getTransform().getRotation();
		newPlayerOrientation = playerOrientation;
	}
	
	void move(){
		force.x = (float)Math.cos(movementDirection) * movementForce;
		force.y = (float)Math.sin(movementDirection) * movementForce;
		player.body.applyForceToCenter(force);
	}
	
	public void update(){
		
		float w = 0.05f;
		playerOrientation = inter.interpolate(playerOrientation, newPlayerOrientation, w);
		
		if(isListening){
			player.getBody().setRotation(playerOrientation);
		}
		
		movementForce = 0;
		movementDirection = 0;
		Vector2f movementDirVec = new Vector2f(0, 0);
		isMoving = false;
		
		if(isListening){
			if(keyboard.isKeyDown(MOVE_FORWARD)){
				movementForce = MOVE_FORCE;
				movementDirVec.y = 1;
				isMoving = true;
			}
			
			if(keyboard.isKeyDown(MOVE_BACKWARD)){
				movementDirVec.y = -1;
				movementForce = MOVE_FORCE;
				isMoving = true;
			}
			
			if(keyboard.isKeyDown(MOVE_LEFT)){
				movementDirVec.x = -1;
				movementForce = MOVE_FORCE;
				isMoving = true;
			}
			
			if(keyboard.isKeyDown(MOVE_RIGHT)){
				movementDirVec.x = 1;
				movementForce = MOVE_FORCE;
				isMoving = true;
			}
			
			if(keyboard.isKeyDown(SPRINT)){
				movementForce = SPRINT_FORCE;
			}
			
			if(mouse.isButtonDown(Mouse.LEFT_BUTTON)){
				Weapon weapon = player.getPrimaryWeapon();
				if(weapon.getFiringType() == Weapon.FIRE_TYPE_AUTO && weapon.canFire()){
					weapon.fire(player.ID, true);
				}
			}
			
			if(movementDirVec.x != 0 || movementDirVec.y != 0){
				movementDirVec.normalize();
				Math2D.rotate(movementDirVec, player.getBody().getTransform().getRotation());
				movementDirection = (float)Math.atan2(movementDirVec.y, movementDirVec.x);
			}
		}
		
		move();
	}
	
	public void setListening(boolean value){
		if(value != isListening){
			isListening = value;
			if(isListening){
				keyboard.addListener(keyboardListener);
				mouse.addListener(mouseListener);
			} else {
				keyboard.removeListener(keyboardListener);
				mouse.removeListener(mouseListener);
			}
		}
	}
}
