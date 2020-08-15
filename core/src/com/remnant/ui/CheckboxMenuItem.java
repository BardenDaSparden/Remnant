package com.remnant.ui;

import java.util.ArrayList;

public class CheckboxMenuItem extends MenuItem {

	CheckBox checkBox;
	
	ArrayList<OnChangeListener> onChangeListeners = new ArrayList<OnChangeListener>();
	
	public CheckboxMenuItem(String itemName) {
		super(itemName);
		checkBox = new CheckBox();
		checkBox.clickable = false;
		add(checkBox, AnchorLayout.RIGHT);
	}

	void toggle(){
		checkBox.toggle();
		onChange();
	}
	
	protected void onMouseRelease(){
		super.onMouseRelease();
		if(isDisabled)
			return;
		toggle();
	}
	
	public void setValue(boolean value){
		boolean currentValue = checkBox.isActive;
		if(value != currentValue){
			checkBox.setValue(value);
			onChange();
		}
	}
	
	public boolean getValue(){
		return checkBox.isActive();
	}
	
	void onChange(){
		for(int i = 0; i < onChangeListeners.size(); i++){
			OnChangeListener listener = onChangeListeners.get(i);
			listener.onChange(checkBox.isActive());
		}
	}
	
	public void addOnChangeListener(OnChangeListener listener){
		onChangeListeners.add(listener);
	}
	
}