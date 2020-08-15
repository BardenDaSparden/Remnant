package com.remnant.input;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
	
	static final int KEYBOARD = 0;
	static final int MOUSE = 1;
	static final int JOYSTICK_1 = 2; // Joypad 1
	static final int JOYSTICK_2 = 3; // Joypad 2
	static final int JOYSTICK_3 = 4; // Joypad 3
	static final int JOYSTICK_4 = 5; // Joypad 4
	static final int NUM_DEVICES = 2;
	
	InputDevice[] devices;
	
	public Input(){
		devices = new InputDevice[NUM_DEVICES];
		devices[KEYBOARD] = new Keyboard();
		devices[MOUSE] = new Mouse();
		//devices[JOYSTICK_1] = new Joystick(0);
		//devices[JOYSTICK_2] = new Joystick(1);
		//devices[JOYSTICK_3] = new Joystick(2);
		//devices[JOYSTICK_4] = new Joystick(3);
	}
	
	public void register(long window){
		for(int i = 0; i < NUM_DEVICES; i++){
			InputDevice device = devices[i];
			device.register(window);
		}
	}
	
	public void unregister(){
		for(int i = 0; i < NUM_DEVICES; i++){
			InputDevice device = devices[i];
			device.release();
		}
	}
	
	public void update(){
		for(int i = 0; i < NUM_DEVICES; i++){
			InputDevice device = devices[i];
			device.update();
		}
	}
	
	InputDevice getDevice(int deviceID){
		if(deviceID > NUM_DEVICES)
			throw new RuntimeException("Device ID doesn't exist");
		return devices[deviceID];
	}
	
	public Mouse getMouse(){
		return (Mouse) getDevice(MOUSE);
	}
	
	public Keyboard getKeyboard(){
		return (Keyboard) getDevice(KEYBOARD);
	}
	
	public Joystick getJoystick(int joystickID){
		return (Joystick) getDevice(1 + joystickID);
	}
	
	public void setWindowFocus(boolean focused){
		glfwWindowHint(GLFW_FOCUSED, (focused) ? GLFW_TRUE : GLFW_FALSE);
	}
}
