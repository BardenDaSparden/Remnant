package com.barden.remnant;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;

public class EditorInputProcessor implements InputProcessor {

	boolean leftMBDown = false;
	boolean rightMBDown = false;
	boolean middleMBDown = false;
	
	int scroll = 0;
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Buttons.LEFT){
			leftMBDown = true;
		}
		
		if(button == Buttons.RIGHT){
			rightMBDown = true;
		}
		
		if(button == Buttons.MIDDLE){
			middleMBDown = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button == Buttons.LEFT){
			leftMBDown = false;
		}
		
		if(button == Buttons.RIGHT){
			rightMBDown = false;
		}
		
		if(button == Buttons.MIDDLE){
			middleMBDown = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		scroll = amount;
		return false;
	}
	
	public int getScroll(){
		return scroll;
	}
}
