package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class Property extends VerticalGroup {

	TextButton button;
	public VerticalGroup content;
	boolean contentVisible = true;
	
	public Property(Skin skin){
		this("Propertee", skin);
	}
	
	public Property(String name, Skin skin){
		button = new TextButton(name, skin);
		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				toggle();
				super.clicked(event, x, y);
			}
		});
		button.getLabel().setAlignment(Align.left, Align.left);
		button.pad(6);
		
		content = new VerticalGroup();
		content.fill().expand();
		content.padLeft(20);
		content.padRight(20);
		
		addActor(button);
		addActor(content);
		fill();
		expand();
	}
	
	protected void toggle(){
		contentVisible = !contentVisible;
		
		if(contentVisible)
			addActor(content);
		else
			removeActor(content);
	}
	
}
