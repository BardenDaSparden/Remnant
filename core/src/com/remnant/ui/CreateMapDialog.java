package com.remnant.ui;

public class CreateMapDialog extends Window {

	AttributeView mapNameAttribute;
	AttributeView mapSizeAttribute;
	
	TextBox nameTextbox;
	TextBox sizeTextbox;
	
	AnchorPane buttonContainer;
	Button createButton;
	
	public CreateMapDialog() {
		super("Create New Map");
		isDialog = true;
		dragable = false;
		nameTextbox = new TextBox();
		sizeTextbox = new TextBox();
		
		mapNameAttribute = new AttributeView("Map Name: ");
		mapNameAttribute.content.add(nameTextbox, AnchorLayout.RIGHT);
		addToContent(mapNameAttribute);
		//mapSizeAttribute = new AttributeView("Map Size: ");
		//.content.add(sizeTextbox, Anchor.RIGHT);
		//addToContent(mapSizeAttribute);
		
		UIStyle btnStyle = new UIStyle();
		btnStyle.backgroundColor.set(0, 0, 0, 0.15f);
		btnStyle.textColor.set(1, 1, 1, 1);
		
		UIStyle hoverStyle = new UIStyle();
		hoverStyle.backgroundColor.set(1, 1, 1, 0.05f);
		hoverStyle.textColor.set(1, 1, 1, 1);
		
		createButton = new Button("Create Map");
		createButton.setPrefSize(115, 50);
		createButton.setSizeMode(PREFERRED, PREFERRED);
		createButton.setStyle(btnStyle);
		
		createButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				CreateMapData data = new CreateMapData();
				data.mapName = nameTextbox.text;
				//data.mapSize = Integer.parseInt(sizeTextbox.text);
				onDataSubmit(data);
				onClose();
				hide();
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				createButton.setStyle(hoverStyle);
			}
			
			@Override
			public void onHoverEnd() {
				createButton.setStyle(btnStyle);
			}
			
			@Override
			public void onFocus() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void offFocus() {
				// TODO Auto-generated method stub
				
			}
		});
		
		buttonContainer = new AnchorPane();
		buttonContainer.setPercentSize(1, 0.15f);
		buttonContainer.setSizeMode(PERCENT, PERCENT);
		buttonContainer.add(createButton, AnchorLayout.RIGHT);
		addToContent(buttonContainer);
		
	}
	
}
