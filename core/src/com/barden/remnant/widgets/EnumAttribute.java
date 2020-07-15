package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EnumAttribute extends AttributeViewer {

	SelectBox<Object> sb;
	
	public EnumAttribute(Skin skin){
		super("Enum", skin);
		sb = new SelectBox<Object>(skin);
		sb.setItems(new Object[]{"Ground", "Wall", "Door"});
		setInput(sb);
	}
	
}
