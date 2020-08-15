package com.remnant.ui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.FontLoader;

public class UIStyle {
	
	public static final UIStyle DARK = new DarkStyle();
	
	Texture backgroundImg = null;
	Vector4f backgroundColor;
	Vector4f borderColor;
	int borderSize;
	Vector4f textColor;
	Vector4f textShadowColor;
	Vector2f textShadowOffset;
	BitmapFont font;
	
	public UIStyle(){
		backgroundColor = new Vector4f(0.18f, 0.18f, 0.18f, 0f);
		borderColor = new Vector4f();
		borderSize = 0;
		textColor = new Vector4f(0.74f, 0.74f, 0.74f, 1);
		textShadowColor = new Vector4f(0, 0, 0, 0);
		textShadowOffset = new Vector2f();
		font = FontLoader.getFont("fonts/open_sans_16.fnt");
	}
	
	public UIStyle set(UIStyle style){
		backgroundColor.set(style.backgroundColor);
		borderColor.set(style.borderColor);
		borderSize = style.borderSize;
		textColor.set(style.textColor);
		textShadowColor.set(style.textShadowColor);
		textShadowOffset.set(style.textShadowOffset);
		font = style.font;
		return this;
	}
	
	public void setBackground(Texture texture){
		backgroundImg = texture;
	}
	
	public Vector4f getBackgroundColor(){
		return backgroundColor;
	}
	
	public Vector4f getBorderColor(){
		return borderColor;
	}
	
	public int getBorderSize(){
		return borderSize;
	}
	
	public Vector4f getTextColor(){
		return textColor;
	}
	
	public Vector4f getTextShadowColor(){
		return textShadowColor;
	}
	
	public Vector2f getTextShadowOffset(){
		return textShadowOffset;
	}

	public BitmapFont getFont(){
		return font;
	}
	
	public void setFont(BitmapFont font){
		this.font = font;
	}
	
}

class DarkStyle extends UIStyle {
	
	public DarkStyle(){
		backgroundColor.set(0.18f, 0.18f, 0.18f, 1);
		borderColor.set(0.18f, 0.18f, 0.18f, 0);
		borderSize = 0;
		textColor.set(0.9f, 0.9f, 0.9f, 1);
		textShadowColor.set(0, 0, 0, 0.0f);
		textShadowOffset.set(0, 0);
		font = FontLoader.getFont("fonts/corbel_18.fnt");
	}
	
}