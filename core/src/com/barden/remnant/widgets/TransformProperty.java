package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TransformProperty extends Property {

	IntegerAttribute xa;
	IntegerAttribute ya;
	IntegerAttribute ra;
	IntegerAttribute sa;
	
	public TransformProperty(Skin skin){
		super("Transform", skin);
		
		xa = new IntegerAttribute(skin);
		xa.setName("X");
		
		ya = new IntegerAttribute(skin);
		ya.setName("Y");
		
		ra = new IntegerAttribute(skin);
		ra.setName("Rotation");
		
		sa = new IntegerAttribute(skin);
		sa.setName("Scale");
		
		content.addActor(xa);
		content.addActor(ya);
		content.addActor(ra);
		content.addActor(sa);
	}
	
}
