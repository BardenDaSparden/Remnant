package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ColorProperty extends Property {

	FloatAttribute ra;
	FloatAttribute ga;
	FloatAttribute ba;
	FloatAttribute aa;
	
	public ColorProperty(Skin skin){
		super("Color", skin);
		
		ra = new FloatAttribute(skin);
		ra.setName("R");
		
		ga = new FloatAttribute(skin);
		ga.setName("G");
		
		ba = new FloatAttribute(skin);
		ba.setName("B");
		
		aa = new FloatAttribute(skin);
		aa.setName("A");
		
		content.addActor(ra);
		content.addActor(ga);
		content.addActor(ba);
		content.addActor(aa);
	}
	
}
