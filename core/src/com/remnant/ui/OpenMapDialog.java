package com.remnant.ui;

import java.util.ArrayList;

public class OpenMapDialog extends Window {

	final int NUM_BUTTONS = 10;
	Button[] mapButtons = new Button[NUM_BUTTONS];
	int activeButtons = 0;
	
	public OpenMapDialog() {
		super("Open Map");
		//dragable = false;
		
		UIStyle btnStyle = new UIStyle();
		btnStyle.backgroundColor.set(0, 0, 0, 0.15f);
		btnStyle.textColor.set(1, 1, 1, 1);
		
		UIStyle hoverStyle = new UIStyle();
		hoverStyle.backgroundColor.set(1, 1, 1, 0.05f);
		hoverStyle.textColor.set(1, 1, 1, 1);
		
		for(int i = 0; i < NUM_BUTTONS; i++){
			Button btn = mapButtons[i] = new Button("");
			btn.setPrefSize(200, 50);
			btn.setSizeMode(PREFERRED, PREFERRED);
			btn.setStyle(btnStyle);
			btn.addListener(new Listener() {
				
				@Override
				public void onMouseRelease() {
					OpenMapData mapData = new OpenMapData();
					mapData.mapName = btn.getText();
					OpenMapDialog.this.onDataSubmit(mapData);
					OpenMapDialog.this.hide();
					OpenMapDialog.this.onClose();
				}
				
				@Override
				public void onMousePress() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onHoverStart() {
					btn.setStyle(hoverStyle);
				}
				
				@Override
				public void onHoverEnd() {
					btn.setStyle(btnStyle);
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
			mapButtons[i].hide();
			super.addToContent(mapButtons[i]);
		}
	}
	
	protected void onClose(){
		super.onClose();
//		for(int i = 0; i < NUM_MAPS; i++){
//			mapButtons[i].hide();
//		}
	}

	public void show(){
		super.show();
		for(int i = 0; i < NUM_BUTTONS; i++){
			Button btn = mapButtons[i];
			btn.hide();
		}
		
		for(int i = 0; i < activeButtons; i++){
			Button btn = mapButtons[i];
			btn.show();
		}
	}
	
	public void loadDiscoveredMaps(ArrayList<String> mapNames){
		int min = Math.max(mapNames.size() - 1, NUM_BUTTONS);
		int count = 0;
		for(String mapName : mapNames){
			if(count < NUM_BUTTONS){
				mapButtons[count].setText(mapName);
				mapButtons[count].show();
				count++;
			}
		}
		activeButtons = count;
	}
	
}
