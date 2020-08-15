package com.remnant.ui;

public class HBox extends Container {

	FlowLayout layout;
	
	public HBox(){
		layout = new FlowLayout();
		layout.setDirection(FlowLayout.HORIZONTAL);
		layout.setAlignment(FlowLayout.LEADING);
		setLayout(layout);
	}
	
	public void setAlignment(int alignment){
		layout.setAlignment(alignment);
	}
	
	public void setSpacing(int spacingX, int spacingY){
		layout.setSpacing(spacingX, spacingY);
	}
	
}
