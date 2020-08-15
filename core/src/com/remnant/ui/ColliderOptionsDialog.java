package com.remnant.ui;

import com.remnant.map.ColliderDefinition;

public class ColliderOptionsDialog extends Window {

	static final String TITLE = "Collider Options";
	
	TextBox typeTextbox;
	AttributeView typeAttribute;
	
	TextBox sizeTextbox;
	AttributeView sizeAttribute;
	
	TextBox rotTextbox;
	AttributeView rotAttribute;
	
	ColliderDefinition properties;
	
	public ColliderOptionsDialog(ColliderDefinition properties) {
		super(TITLE);
		this.properties = properties;
		
		typeTextbox = new TextBox();
		typeTextbox.setText(properties.type);
		typeTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.type = newValue;
		});
		typeAttribute = new AttributeView("Type");
		typeAttribute.content.add(typeTextbox, AnchorLayout.RIGHT);
		addToContent(typeAttribute);
		
		
		sizeTextbox = new TextBox();
		sizeTextbox.setText(properties.size + "");
		sizeTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.size = Float.parseFloat(newValue);
		});
		sizeAttribute = new AttributeView("Size");
		sizeAttribute.content.add(sizeTextbox, AnchorLayout.RIGHT);
		addToContent(sizeAttribute);
		
		rotTextbox = new TextBox();
		rotTextbox.setText(properties.orientation + "");
		rotTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.orientation = Float.parseFloat(newValue);
		});
		rotAttribute = new AttributeView("Orientation");
		rotAttribute.content.add(rotTextbox, AnchorLayout.RIGHT);
		addToContent(rotAttribute);
		
		isDialog = true;
	}

	
	
}
