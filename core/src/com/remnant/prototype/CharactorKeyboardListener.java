package com.remnant.prototype;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;

import java.util.ArrayList;
import java.util.List;

import com.remnant.input.KeyboardListener;

public class CharactorKeyboardListener implements KeyboardListener{

	int ACTION_LEFT = GLFW_KEY_LEFT;
	int ACTION_LEFT_RELEASE = GLFW_KEY_LEFT;
	
	int ACTION_RIGHT = GLFW_KEY_RIGHT;
	int ACTION_RIGHT_RELEASE = GLFW_KEY_RIGHT;
	
	int ACTION_UP = GLFW_KEY_UP;
	int ACTION_UP_RELEASE = GLFW_KEY_UP;
	
	int ACTION_DOWN = GLFW_KEY_DOWN;
	int ACTION_DOWN_RELEASE = GLFW_KEY_DOWN;
	
	int ACTION_JUMP = GLFW_KEY_X;
	int ACTION_JUMP_RELEASE = GLFW_KEY_X;
	
	int ACTION_FIRE = GLFW_KEY_C;
	int ACTION_FIRE_RELEASE = GLFW_KEY_C;
	
	int ACTION_ALTFIRE = GLFW_KEY_F;
	int ACTION_ALTFIRE_RELEASE = GLFW_KEY_F;
	
	int ACTION_SPRINT = GLFW_KEY_Z;
	int ACTION_SPRINT_RELEASE = GLFW_KEY_Z;
	
	int ACTION_ALTLOOK = GLFW_KEY_D;
	int ACTION_ALTLOOK_RELEASE = GLFW_KEY_D;
	
	int ACTION_PRINT_DEBUG_LOG = GLFW_KEY_Y;
	
	int ACTION_UNLOCK_MORPHBALL = GLFW_KEY_1;
	
	boolean listening = true;
	
	List<ActionEventListener> actionListeners;	
	
	public CharactorKeyboardListener(){
		actionListeners = new ArrayList<ActionEventListener>();
	}
	
	@Override
	public void onKeyPress(int key) {
		if(!listening)
			return;
		
		if(key == ACTION_LEFT)
			notifyAction(CharactorActions.LEFT);
		
		if(key == ACTION_RIGHT)
			notifyAction(CharactorActions.RIGHT);
		
		if(key == ACTION_DOWN)
			notifyAction(CharactorActions.DOWN);
		
		if(key == ACTION_UP)
			notifyAction(CharactorActions.UP);
		
		if(key == ACTION_JUMP)
			notifyAction(CharactorActions.JUMP);
		
		if(key == ACTION_FIRE)
			notifyAction(CharactorActions.FIRE);
		
		if(key == ACTION_ALTFIRE)
			notifyAction(CharactorActions.ALTFIRE);
		
		if(key == ACTION_SPRINT)
			notifyAction(CharactorActions.SPRINT);
		
		if(key == ACTION_ALTLOOK)
			notifyAction(CharactorActions.ALTLOOK);
		
		if(key == ACTION_PRINT_DEBUG_LOG)
			notifyAction(CharactorActions.PRINT_DEBUG_LOG);
		
		if(key == ACTION_UNLOCK_MORPHBALL)
			notifyAction(CharactorActions.UNLOCK_MORPHBALL);
	}

	@Override
	public void onKeyRelease(int key) {
		if(!listening)
			return;
		
		if(key == ACTION_LEFT_RELEASE)
			notifyAction(CharactorActions.LEFT_RELEASE);
		
		if(key == ACTION_RIGHT_RELEASE)
			notifyAction(CharactorActions.RIGHT_RELEASE);
		
		if(key == ACTION_DOWN_RELEASE)
			notifyAction(CharactorActions.DOWN_RELEASE);
		
		if(key == ACTION_UP_RELEASE)
			notifyAction(CharactorActions.UP_RELEASE);
		
		if(key == ACTION_JUMP_RELEASE)
			notifyAction(CharactorActions.JUMP_RELEASE);
		
		if(key == ACTION_FIRE_RELEASE)
			notifyAction(CharactorActions.FIRE_RELEASE);
		
		if(key == ACTION_ALTFIRE_RELEASE)
			notifyAction(CharactorActions.ALTFIRE_RELEASE);
		
		if(key == ACTION_SPRINT_RELEASE)
			notifyAction(CharactorActions.SPRINT_RELEASE);
		
		if(key == ACTION_ALTLOOK_RELEASE)
			notifyAction(CharactorActions.ALTLOOK_RELEASE);
	}

	@Override
	public void onChar(int codepoint) {
		if(!listening)
			return;
	}
	
	public void notifyAction(int action){
		for(int i = 0; i < actionListeners.size(); i++){
			ActionEventListener listener = actionListeners.get(i);
			listener.processAction(action);
		}
	}
	
	public void setActionLeft(int key){}
	public void setActionRight(int key){}
	public void setActionUp(int key){}
	public void setActionDown(int key){}
	public void setActionJump(int key){}
	public void setActionFire(int key){}
	public void setActionAltFire(int key){}
	public void setActionSprint(int key){}
	public void setActionAltLook(int key){}
	
	public void setListening(boolean isListening){
		listening = isListening;
	}
	
	public void addListener(ActionEventListener listener){
		actionListeners.add(listener);
	}
	
	public void removeListener(ActionEventListener listener){
		actionListeners.remove(listener);
	}
}
