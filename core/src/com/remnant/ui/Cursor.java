package com.remnant.ui;

import org.joml.Vector2f;

public class Cursor {

	public enum CursorStyle{
		DEFAULT, CREATE;
	}
	
	Vector2f position;
	CursorStyle style;
	boolean isVisible = true;
	
	public Cursor(){
		position = new Vector2f();
		style = CursorStyle.DEFAULT;
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public void setPosition(float x, float y){
		position.set(x, y);
	}
	
	public void setPosition(Vector2f p){
		position.set(p.x, p.y);
	}
	
	public void move(float dx, float dy){
		position.x += dx;
		position.y += dy;
	}
	
	public CursorStyle getStyle(){
		return style;
	}
	
	public void setCursorStyle(CursorStyle newStyle){
		style = newStyle;
	}
	
	public void setVisible(boolean visible){
		isVisible = visible;
	}
	
}
