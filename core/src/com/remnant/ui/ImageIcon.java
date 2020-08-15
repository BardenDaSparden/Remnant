package com.remnant.ui;

import com.remnant.assets.Texture;

public class ImageIcon extends UINode {

	Texture texture;
	
	public ImageIcon(String iconPath){
		texture = new Texture(iconPath);
		width = texture.getWidth();
		height = texture.getHeight();
		setPrefSize(width, height);
		setSizeMode(USE_PREF_WIDTH, USE_PREF_HEIGHT);
		style.setBackground(texture);
	}
	
}
