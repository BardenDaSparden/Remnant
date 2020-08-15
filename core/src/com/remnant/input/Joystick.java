package com.remnant.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class Joystick implements InputDevice {

	static final int ANALOG_LEFT_X = 0;
	static final int ANALOG_LEFT_Y = 1;
	static final int ANALOG_TRIGGER = 2;
	static final int ANALOG_RIGHT_X = 3;
	static final int ANALOG_RIGHT_Y = 4;
	static final int NUM_AXES = 5;
	
	static final int BUTTON_A = 0;
	static final int BUTTON_B = 1;
	static final int BUTTON_X = 2;
	static final int BUTTON_Y = 3;
	static final int BUTTON_LEFT_BUMPER = 4;
	static final int BUTTON_RIGHT_BUMPER = 5;
	static final int BUTTON_BACK = 6;
	static final int BUTTON_START = 7;
	static final int BUTTON_LEFT_ANALOG_CLICK = 8;
	static final int BUTTON_RIGHT_ANALOG_CLICK = 9;
	static final int BUTTON_DPAD_UP = 10;
	static final int BUTTON_DPAD_RIGHT = 11;
	static final int BUTTON_DPAD_DOWN = 12;
	static final int BUTTON_DPAD_LEFT = 13;
	static final int NUM_BUTTONS = 18;
	
	int id;
	FloatBuffer axesBuffer;
	ByteBuffer buttonsBuffer;
	
	ArrayList<JoystickListener> listeners;
	
	float[] previousAxes;
	float[] axes;
	boolean[] previousButtons;
	boolean[] buttons;
	boolean isDetected;
	
	public Joystick(int joystickIdx){
		id = joystickIdx;
		isDetected = glfwJoystickPresent(GLFW_JOYSTICK_1 + id);
		axesBuffer = BufferUtils.createFloatBuffer(NUM_AXES);
		buttonsBuffer = BufferUtils.createByteBuffer(NUM_BUTTONS);
		listeners = new ArrayList<JoystickListener>();
		previousAxes = new float[NUM_AXES];
		axes = new float[NUM_AXES];
		previousButtons = new boolean[NUM_BUTTONS];
		buttons = new boolean[NUM_BUTTONS];
	}
	
	@Override
	public void register(long windowHandle) {
		
	}

	@Override
	public void release() {
		
	}

	@Override
	public void update() {
		if(isDetected){
			for(int i = 0; i < NUM_AXES; i++)
				previousAxes[i] = axes[i];
			for(int i = 0; i < NUM_BUTTONS; i++)
				previousButtons[i] = buttons[i];
			
			axesBuffer = glfwGetJoystickAxes(GLFW_JOYSTICK_1 + id);
			buttonsBuffer = glfwGetJoystickButtons(GLFW_JOYSTICK_1 + id);
			
			for(int i = 0; i < NUM_AXES; i++)
				axes[i] = axesBuffer.get();
			
			for(int i = 0; i < NUM_BUTTONS; i++)
				buttons[i] = (buttonsBuffer.get() == 1) ? true : false;
			
			for(int i = 0; i < NUM_BUTTONS; i++){
				boolean previous = previousButtons[i];
				boolean current = buttons[i];
				if(previous && !current)
					onButtonRelease(i);
				if(!previous && current)
					onButtonPress(i);
			}
		}
	}

	public void addListener(JoystickListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(JoystickListener listener){
		listeners.remove(listener);
	}
	
	protected void onButtonPress(int button){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onButtonPress(button);
	}
	
	protected void onButtonRelease(int button){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).onButtonRelease(button);
	}
	
	public boolean isDetected(){
		return isDetected;
	}
	
	public boolean isButtonDown(int button){
		return buttons[button];
	}
	
	public boolean isLeftTriggerDown(float deadzone){
		float raw = axes[ANALOG_TRIGGER];
		
		//Right Trigger Down
		if(raw < 0)
			return false;
		
		return Math.abs(axes[ANALOG_TRIGGER]) >= deadzone;
	}
	
	public boolean isRightTriggerDown(float deadzone){
		float raw = axes[ANALOG_TRIGGER];
		
		//Right Trigger Down
		if(raw > 0)
			return false;
		
		return Math.abs(axes[ANALOG_TRIGGER]) >= deadzone;
	}
	
	public float getLeftX(){
		return axes[ANALOG_LEFT_X];
	}
	
	public float getLeftY(){
		return axes[ANALOG_LEFT_Y];
	}
	
	public float getRightX(){
		return axes[ANALOG_RIGHT_X];
	}
	
	public float getRightY(){
		return axes[ANALOG_RIGHT_Y];
	}
	
}
