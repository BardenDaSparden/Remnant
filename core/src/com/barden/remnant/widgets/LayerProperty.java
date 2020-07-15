package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class LayerProperty extends Property {
	
	BooleanAttribute ba;
	
	public LayerProperty(Skin skin){
		super("Layer", skin);
		
		ba = new BooleanAttribute(skin);
		ba.setName("IsVisible");
		
		content.addActor(ba);
		content.align(Align.top);
	}
	
}