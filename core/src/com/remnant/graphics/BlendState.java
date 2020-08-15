package com.remnant.graphics;

import static org.lwjgl.opengl.GL11.*;

public enum BlendState {

	NONE(GL_SRC_ALPHA, GL_SRC_ALPHA), ALPHA_TRANSPARENCY(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA), ADDITIVE(GL_SRC_ALPHA, GL_ONE), MULTIPLY(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA), SCREEN(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
	
	int source;
	int destination;
	
	BlendState(int s, int d){
		source = s;
		destination = d;
	}
	
	public int getSource(){
		return source;
	}
	
	public int getDestination(){
		return destination;
	}
	
}
