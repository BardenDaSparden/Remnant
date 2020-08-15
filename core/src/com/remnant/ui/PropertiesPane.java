package com.remnant.ui;

public class PropertiesPane extends TitledPane {

	public PropertiesPane(UINode parent){
		super("Properties");
		setPrefSize(400, parent.height);
		setSizeMode(PREFERRED, PREFERRED);
	}
	
}
