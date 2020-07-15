package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class AttributeViewer extends Table {

	Label label;
	Actor input;
	
	public AttributeViewer(String name, Skin skin){
		label = new Label(name, skin);
		add(label).left().expand().fill();
		//pad(4);
		//debug();
	}
	
	protected void setInput(Actor actor){
		add(actor).right();
	}
	
	public void setName(String name) {
		label.setText(name);
	}
	
}
