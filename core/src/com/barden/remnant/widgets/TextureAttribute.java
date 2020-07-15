package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TextureAttribute extends AttributeViewer {

	SelectBox<Object> sb;
	
	public TextureAttribute(Skin skin){
		super("Texture", skin);
		sb = new SelectBox<Object>(skin);
		sb.setItems(new Object[]{"Texture1", "Texture2", "Texture3"});
		setInput(sb);
	}
	
}
