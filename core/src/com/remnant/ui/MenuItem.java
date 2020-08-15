package com.remnant.ui;

import org.joml.Vector4f;

public class MenuItem extends AnchorPane {

	Vector4f defaultColor = new Vector4f(0, 0, 0, 0);
	Vector4f hoverColor = new Vector4f(0.2f, 0.2f, 0.2f, 1);
	
	Label label;
	
	public MenuItem(String itemName){
		style.backgroundColor.set(defaultColor);
		setPrefSize(200, 50);
		setSizeMode(PREFERRED, PREFERRED);
		setPadding(16);
		
		label = new Label(itemName);
		add(label, AnchorLayout.LEFT);
	}
	
	protected void onHoverStart(){
		super.onHoverStart();
		style.backgroundColor.set(hoverColor);
	}
	
	protected void onHoverEnd(){
		super.onHoverEnd();
		style.backgroundColor.set(defaultColor);
	}
	
}
