package com.remnant.input;

public interface KeyboardListener {

	public void onKeyPress(int key);
	public void onKeyRelease(int key);
	public void onChar(int codepoint);
	
}
