package com.remnant.ui;

public class SaveMapDialog extends Window {

	Label mapName;
	Button saveButton;
	Button cancelButton;
	
	public SaveMapDialog(){
		super("Save Map");
		isDialog = true;
		dragable = false;
		
		UIStyle btnStyle = new UIStyle();
		btnStyle.backgroundColor.set(0, 0, 0, 0.15f);
		btnStyle.textColor.set(1, 1, 1, 1);
		
		UIStyle hoverStyle = new UIStyle();
		hoverStyle.backgroundColor.set(1, 1, 1, 0.05f);
		hoverStyle.textColor.set(1, 1, 1, 1);
		
		mapName = new Label("");
		
		saveButton = new Button("Save");
		saveButton.setPrefSize(115, 50);
		saveButton.setSizeMode(PREFERRED, PREFERRED);
		saveButton.setStyle(btnStyle);
		saveButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				SaveMapData data = new SaveMapData();
				data.mapName = SaveMapDialog.this.mapName.text;
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
				saveButton.setStyle(hoverStyle);
			}
			
			@Override
			public void onHoverEnd() {
				saveButton.setStyle(btnStyle);
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
		addToContent(saveButton);
	}
	
	public void setMapName(String mapName){
		this.mapName.setText(mapName);
	}
	
}
