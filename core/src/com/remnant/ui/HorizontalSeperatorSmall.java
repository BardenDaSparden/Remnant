package com.remnant.ui;

public class HorizontalSeperatorSmall extends UINode{

	public HorizontalSeperatorSmall(float percentX){
		setPrefSize(0, 2);
		setPercentSize(percentX, 1);
		setSizeMode(PERCENT, USE_PREF_HEIGHT);
		style.backgroundColor.set(0, 0, 0, 0.5f);
	}
	
}
