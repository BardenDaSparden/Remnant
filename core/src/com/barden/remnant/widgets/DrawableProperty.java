package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DrawableProperty extends Property {

	BooleanAttribute va;
	IntegerAttribute za;
	TransformProperty ta;
	MaterialProperty ma;
	
	public DrawableProperty(Skin skin){
		super("Drawable", skin);
		va = new BooleanAttribute(skin);
		va.setName("Visible");
		
		za = new IntegerAttribute(skin);
		za.setName("Z-Index");
		
		ta = new TransformProperty(skin);
		
		ma = new MaterialProperty(skin);
		
		content.addActor(va);
		content.addActor(za);
		content.addActor(ta);
		content.addActor(ma);
	}
	
}
