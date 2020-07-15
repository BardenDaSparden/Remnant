package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class FloatAttribute extends AttributeViewer {

	Slider slider;
	
	public FloatAttribute(Skin skin){
		super("Float", skin);
		slider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
		setInput(slider);
	}
	
}
