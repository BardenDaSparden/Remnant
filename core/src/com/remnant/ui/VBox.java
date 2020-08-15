package com.remnant.ui;

public class VBox extends Container {

	FlowLayout layout;
	
	public VBox(){
		layout = new FlowLayout();
		layout.setDirection(FlowLayout.VERTICAL);
		layout.setAlignment(FlowLayout.LEADING);
		setLayout(layout);
	}
	
	public void setAlignment(int alignment){
		layout.setAlignment(alignment);
	}
	
	public void shrink(boolean v){
		layout.useChildrenSize(v);
	}
	
}