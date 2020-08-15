package com.remnant.ui;

import java.text.DecimalFormat;

import com.remnant.map.LightDefinition;

public class LightOptionsDialog extends Window {

	TextBox colorRTextbox;
	TextBox colorGTextbox;
	TextBox colorBTextbox;
	AttributeView colorAttribute;
	
	TextBox radiusTextbox;
	AttributeView radiusAttribute;
	
	TextBox zTextbox;
	AttributeView zAttribute;
	
	CheckBox castShadowsCheckbox;
	AttributeView castShadowsAttribute;
	
	DecimalFormat formatter = new DecimalFormat("#.#");
	
	LightDefinition properties;
	
	public LightOptionsDialog(LightDefinition properties){
		super("Light Properties");
		this.properties = properties;
		
		final int SMALL_TEXTBOX_WIDTH = 50;
		colorRTextbox = new TextBox();
		colorRTextbox.setPrefSize(SMALL_TEXTBOX_WIDTH, colorRTextbox.prefHeight);
		colorRTextbox.setText(formatter.format(properties.color.x));
		colorRTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.color.x = Float.parseFloat(newValue);
		});
		
		colorGTextbox = new TextBox();
		colorGTextbox.setPrefSize(SMALL_TEXTBOX_WIDTH, colorGTextbox.prefHeight);
		colorGTextbox.setText(formatter.format(properties.color.y));
		colorGTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.color.y = Float.parseFloat(newValue);
		});
		
		colorBTextbox = new TextBox();
		colorBTextbox.setPrefSize(SMALL_TEXTBOX_WIDTH, colorBTextbox.prefHeight);
		colorBTextbox.setText(formatter.format(properties.color.z));
		colorBTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.color.z = Float.parseFloat(newValue);
		});
		
		colorAttribute = new AttributeView("Color");
		colorAttribute.content.add(colorBTextbox, AnchorLayout.RIGHT);
		colorAttribute.content.add(colorGTextbox, AnchorLayout.RIGHT);
		colorAttribute.content.add(colorRTextbox, AnchorLayout.RIGHT);
		addToContent(colorAttribute);
		
		radiusTextbox = new TextBox();
		radiusTextbox.setText(formatter.format(properties.radius) + "");
		radiusTextbox.addOnValueAcceptedListener((newValue) -> {
			properties.radius = Float.parseFloat(newValue);
		});
		radiusAttribute = new AttributeView("Radius");
		radiusAttribute.content.add(radiusTextbox, AnchorLayout.RIGHT);
		addToContent(radiusAttribute);
		
		zTextbox = new TextBox();
		zTextbox.setText(properties.z + "");
		zTextbox.addOnValueAcceptedListener((newValue)->{
			properties.z = Float.parseFloat(newValue);
		});
		
		zAttribute = new AttributeView("Z-Value");
		zAttribute.content.add(zTextbox, AnchorLayout.RIGHT);
		addToContent(zAttribute);
		
		castShadowsCheckbox = new CheckBox();
		castShadowsCheckbox.setValue(properties.castShadows);
		castShadowsCheckbox.addOnChangeListener((newValue) -> {
			properties.castShadows = newValue;
		});
		castShadowsAttribute = new AttributeView("Cast Shadows");
		castShadowsAttribute.content.add(castShadowsCheckbox, AnchorLayout.RIGHT);
		addToContent(castShadowsAttribute);
		
		isDialog = true;
	}
	
	public void updateProperties(){
		colorRTextbox.setText(properties.color.x + "");
		colorGTextbox.setText(properties.color.y + "");
		colorBTextbox.setText(properties.color.z + "");
		radiusTextbox.setText(properties.radius + "");
		zTextbox.setText(properties.z + "");
		castShadowsCheckbox.setValue(properties.castShadows);
	}
	
}
