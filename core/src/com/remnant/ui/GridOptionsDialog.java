package com.remnant.ui;

public class GridOptionsDialog extends Window {

	static final String TITLE = "Grid Properties";
	
	TextBox cellSizeTextbox;
	AttributeView cellSizeAttribute;
	
	TextBox divisionsTextbox;
	AttributeView divisionsAttribute;
	
	CheckBox snapCheckbox;
	AttributeView snapToAttribute;
	
	CheckBox drawCheckbox;
	AttributeView drawAttribute;
	
	EditorProperties properties;
	
	public GridOptionsDialog(EditorProperties properties) {
		super(TITLE);
		this.properties = properties;
		isDialog = true;
		dragable = false;
		cellSizeTextbox = new TextBox();
		cellSizeTextbox.addOnValueAcceptedListener((value)->{
			properties.cellSize = Integer.parseInt(value);
		});
		cellSizeTextbox.setText(properties.cellSize + "");
		cellSizeAttribute = new AttributeView("Cell Size");
		cellSizeAttribute.content.add(cellSizeTextbox, AnchorLayout.RIGHT);
		addToContent(cellSizeAttribute);
		
		divisionsTextbox = new TextBox();
		divisionsTextbox.setText(properties.numDivisions + "");
		divisionsTextbox.addOnValueAcceptedListener((value) -> {
			properties.numDivisions = Integer.parseInt(value);
		});
		divisionsAttribute = new AttributeView("Cell Divisions");
		divisionsAttribute.content.add(divisionsTextbox, AnchorLayout.RIGHT);
		addToContent(divisionsAttribute);
		
		drawCheckbox = new CheckBox();
		drawCheckbox.setValue(properties.drawGrid);
		drawCheckbox.addOnChangeListener((newValue) ->{
			properties.drawGrid = newValue;
		});
		drawAttribute = new AttributeView("Draw Grid");
		drawAttribute.content.add(drawCheckbox, AnchorLayout.RIGHT);
		addToContent(drawAttribute);
		
		snapToAttribute = new AttributeView("Snap Position");
		snapCheckbox = new CheckBox();
		snapCheckbox.setValue(properties.snapToGrid);
		snapCheckbox.addOnChangeListener((newValue)->{
			properties.snapToGrid = newValue;
		});
		snapToAttribute.content.add(snapCheckbox, AnchorLayout.RIGHT);
		addToContent(snapToAttribute);
	}
	
	public void updateProperties(){
		cellSizeTextbox.setText(properties.cellSize + "");
		divisionsTextbox.setText(properties.numDivisions + "");
		drawCheckbox.setValue(properties.drawGrid);
		snapCheckbox.setValue(properties.snapToGrid);
	}

}