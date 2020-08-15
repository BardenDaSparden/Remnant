package com.remnant.ui;

import java.util.ArrayList;

public class CheckBox extends AnchorPane {

	int width = 32;
	int height = 32;
	
	CheckBoxInset inset;
	boolean isActive = false;
	boolean clickable = true;
	
	ArrayList<OnChangeListener> onChangeListeners;
	
	public CheckBox(){
		style.backgroundColor.set(0, 0, 0, 0.25f);
		setPrefSize(width, height);
		setSizeMode(PREFERRED, PREFERRED);
		inset = new CheckBoxInset(width - 18, height - 18);
		add(inset, AnchorLayout.CENTER);
		onChangeListeners = new ArrayList<OnChangeListener>();
	}
	
	void toggle(){
		isActive = !isActive;
		onChange();
	}
	
	
	public boolean isActive(){
		return isActive;
	}
	
	public void setValue(boolean active){
		isActive = active;
	}
	
	protected void onMouseRelease(){
		super.onMouseRelease();
		
		if(!clickable)
			return;
		toggle();
		updateInset();
	}
	
	void updateInset(){
		if(isActive)
			inset.show();
		else
			inset.hide();
	}
	
	public void show(){
		isVisible = true;
		updateInset();
	}
	
	public void hide(){
		isVisible = false;
		inset.hide();
	}
	
	protected void onChange(){
		for(int i = 0; i < onChangeListeners.size(); i++){
			OnChangeListener listener = onChangeListeners.get(i);
			listener.onChange(isActive);
		}
	}
	
	public void addOnChangeListener(OnChangeListener listener){
		onChangeListeners.add(listener);
	}
}

class CheckBoxInset extends UINode {
	
	public CheckBoxInset(int w, int h){
		style.backgroundColor.set(0, 1, 1, 0.25f);
		setPrefSize(w, h);
		setSizeMode(PREFERRED, PREFERRED);
		hide();
	}	
	
}