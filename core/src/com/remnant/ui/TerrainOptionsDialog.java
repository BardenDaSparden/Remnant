package com.remnant.ui;

import com.remnant.map.TerrainDefinition;

public class TerrainOptionsDialog extends Window {

	TextBox diffuseTextbox;
	AttributeView diffuseAttribute;
	
	TextBox normalTextbox;
	AttributeView normalAttribute;
	
	TextBox emissiveTextbox;
	AttributeView emissiveAttribute;
	
	TextBox sizeTextbox;
	AttributeView sizeAttribute;
	
	//TextBox typeTextbox;
	Options typeOptions;
	AttributeView typeAttribute;
	
	TerrainDefinition properties;
	
	public TerrainOptionsDialog(TerrainDefinition properties){
		super("Terrain Properties");
		this.properties = properties;
		//isDialog = true;
		
		typeOptions = new Options();
		typeOptions.addOption(new Option("Floor"));
		typeOptions.addOption(new Option("Wall"));
		typeOptions.selectByString(properties.type);
		
		typeOptions.addOnValueAcceptedListener((newValue) -> {
			properties.type = newValue;
		});
		
		typeAttribute = new AttributeView("Type");
		typeAttribute.content.add(typeOptions, AnchorLayout.RIGHT);
		//typeAttribute.content.add(typeTextbox, AnchorLayout.RIGHT);
		addToContent(typeAttribute);
		
		sizeTextbox = new TextBox();
		sizeTextbox.setText(properties.size + "");
		sizeTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.size = Integer.parseInt(newValue);
		});
		sizeAttribute = new AttributeView("Tile Size");
		sizeAttribute.content.add(sizeTextbox, AnchorLayout.RIGHT);
		addToContent(sizeAttribute);
		
		diffuseTextbox = new TextBox();
		diffuseTextbox.setText(properties.diffusePath);
		diffuseTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.diffusePath = newValue;
		});
		diffuseAttribute = new AttributeView("Diffuse Texture");
		diffuseAttribute.content.add(diffuseTextbox, AnchorLayout.RIGHT);
		addToContent(diffuseAttribute);
		
		normalTextbox = new TextBox();
		normalTextbox.setText(properties.normalPath);
		normalTextbox.addOnValueAcceptedListener((newValue) ->{
			properties.normalPath = newValue;
		});
		
		normalAttribute = new AttributeView("Normal Texture");
		normalAttribute.content.add(normalTextbox, AnchorLayout.RIGHT);
		addToContent(normalAttribute);
		
		emissiveTextbox = new TextBox();
		emissiveTextbox.setText(properties.emissivePath);
		emissiveTextbox.addOnValueAcceptedListener((newValue)->{
			properties.emissivePath = newValue;
		});
		
		emissiveAttribute = new AttributeView("Emissive Texture");
		emissiveAttribute.content.add(emissiveTextbox, AnchorLayout.RIGHT);
		addToContent(emissiveAttribute);
		
//		typeTextbox = new TextBox();
//		typeTextbox.setText(properties.type);
//		typeTextbox.addOnValueAcceptedListener((newValue)->{
//			properties.type = newValue;
//		});
		
	}
	
	public void updateProperties(){
		diffuseTextbox.setText(properties.diffusePath);
		normalTextbox.setText(properties.normalPath);
		emissiveTextbox.setText(properties.emissivePath);
		sizeTextbox.setText(properties.size + "");
		//typeTextbox.setText(properties.type);
		typeOptions.selectByString(properties.type);
	}
}
