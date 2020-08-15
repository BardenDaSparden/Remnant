package com.remnant.ui;

public class Button extends AnchorPane {

	//final int Z_INDEX = 20;
	Label label;
	int labelAnchor = AnchorLayout.CENTER;
	
	public Button(String text){
		label = new Label(text);
		setPrefSize(120, 40);
		setSizeMode(PREFERRED, PREFERRED);
		add(label, labelAnchor);
	}
	
	protected void resizeDescending(){
		super.resizeDescending();
		//label.setPosition(posX, posY);
		//label.setPrefSize(super.width, super.height);
	}
	
	public void setText(String str){
		label.setText(str);
		resizeDescending();
	}
	
	public void setStyle(UIStyle style){
		super.setStyle(style);
		label.setStyle(style);
	}
	
	public String getText(){
		return label.getText();
	}

	public void setTextAnchor(int anchor){
		remove(label, labelAnchor);
		add(label, anchor);
		labelAnchor = anchor;
	}
	
}
