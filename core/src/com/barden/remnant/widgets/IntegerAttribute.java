package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

public class IntegerAttribute extends AttributeViewer {

	TextField field;
	
	public IntegerAttribute(Skin skin){
		super("Integer", skin);
		field = new TextField("", skin);
		field.setTextFieldFilter(new IntFilter());
		setInput(field);
	}
	
}

class IntFilter implements TextFieldFilter {

	private char[] accepted;
	
	public IntFilter() {
	    accepted = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	}

	@Override
	public boolean acceptChar(TextField textField, char c) {
	    for (char a : accepted)
	        if (a == c) return true;
	    return false;
	}

}