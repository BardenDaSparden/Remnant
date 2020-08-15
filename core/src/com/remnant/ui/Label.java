package com.remnant.ui;

public class Label extends UINode{

	public static final int ALIGNMENT_LEFT = 0;
	public static final int ALIGNMENT_CENTER = 1;
	
	String text;
	int alignment = ALIGNMENT_CENTER;
	
	public Label(String text){
		setText(text);
		setSizeMode(USE_PREF_WIDTH, USE_PREF_HEIGHT);
	}
	
	public void setText(String text){
		this.text = text;
		int w = style.font.getWidth(text);
		int h = style.font.getHeight(text);
		setPrefSize(w, h);
	}
	
	public String getText(){
		return text;
	}
	
	public int getAligment(){
		return alignment;
	}
	
	public void setAlignment(int alignment){
		this.alignment = alignment;
	}
	
	public void setStyle(UIStyle style){
		super.setStyle(style);
		this.style.font = style.font;
		this.style.textColor.set(style.textColor);
		setText(text);
	}
	
}
