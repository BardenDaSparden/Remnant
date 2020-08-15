package com.remnant.weapon;

import com.remnant.assets.Texture;
import com.remnant.graphics.TypedUIRenderer;

public class WeaponCursor extends TypedUIRenderer {

	static final int LAYER = 10002;
	
	Texture outer;
	Texture inner;
	Texture crossHair;
	
	public WeaponCursor() {
		super(LAYER);
		outer = new Texture("images/cursorOuter.png");
		inner = new Texture("images/cursorInner.png");
		crossHair = new Texture("images/cursorCrosshair.png");
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
	
}
