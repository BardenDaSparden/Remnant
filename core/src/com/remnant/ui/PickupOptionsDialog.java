package com.remnant.ui;

import com.remnant.map.PickupDefinition;

public class PickupOptionsDialog extends Window {

	static final String TITLE = "Pickup Options";
	
	TextBox typeTextbox;
	AttributeView typeAttribute;
	
	TextBox respawnTextbox;
	AttributeView respawnAttribute;
	
	TextBox valueTextbox;
	AttributeView valueAttribute;
	
	PickupDefinition properties;
	
	public PickupOptionsDialog(PickupDefinition properties) {
		super(TITLE);
		this.properties = properties;
		isDialog = true;
		
		typeTextbox = new TextBox();
		typeTextbox.setText(properties.type);
		typeTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.type = newValue;
		});
		typeAttribute = new AttributeView("Type");
		typeAttribute.content.add(typeTextbox, AnchorLayout.RIGHT);
		addToContent(typeAttribute);
		
		respawnTextbox = new TextBox();
		respawnTextbox.setText(properties.respawnTime + "");
		respawnTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.respawnTime = Integer.parseInt(newValue);
		});
		respawnAttribute = new AttributeView("Respawn Time");
		respawnAttribute.content.add(respawnTextbox, AnchorLayout.RIGHT);
		addToContent(respawnAttribute);
		
		valueTextbox = new TextBox();
		valueTextbox.setText(properties.value + "");
		valueTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.value = Integer.parseInt(newValue);
		});
		valueAttribute = new AttributeView("Value");
		valueAttribute.content.add(valueTextbox, AnchorLayout.RIGHT);
		addToContent(valueAttribute);
		
	}

	
	
}
