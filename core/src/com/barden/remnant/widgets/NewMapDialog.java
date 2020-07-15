package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class NewMapDialog extends Dialog {

	public NewMapDialog(Skin skin){
		super("Create New Map", skin);
		button("Create", true);
		button("Cancel", false);
	}
	
	@Override
	protected void result(Object object) {
		if(object.equals(true)){
			System.out.println("Create Map...");
		} else {
			System.out.println("Cancel Map Creation...");
		}
		super.result(object);
	}
	
}
