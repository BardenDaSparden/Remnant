package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BooleanAttribute extends AttributeViewer {

	CheckBox checkBox;
	
	public BooleanAttribute(Skin skin){
		super("Bool", skin);
		checkBox = new CheckBox("", skin);
		setInput(checkBox);
	}
	
}
