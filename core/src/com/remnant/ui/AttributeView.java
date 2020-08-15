package com.remnant.ui;

public class AttributeView extends AnchorPane {

	Label label;
	AnchorPane labelContainer;
	AnchorPane content;
	
	public AttributeView(String attributeName){
		setPercentSize(1, 0.2f);
		setSizeMode(PERCENT, PERCENT);
		setSpacing(20, 0);
		
		label = new Label(attributeName);
		labelContainer = new AnchorPane();
		labelContainer.setPercentSize(0.28f, 1);
		labelContainer.setSizeMode(PERCENT, PERCENT);
		labelContainer.add(label, AnchorLayout.LEFT);
		add(labelContainer, AnchorLayout.LEFT);
		content = new AnchorPane();
		content.setPercentSize(0.72f, 1);
		content.setSizeMode(PERCENT, PERCENT);
		add(content, AnchorLayout.RIGHT);
		add(new HorizontalSeperator(1), AnchorLayout.BOTTOM);
	}
	
}
