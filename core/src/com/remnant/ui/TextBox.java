package com.remnant.ui;

import java.util.ArrayList;

import org.joml.Vector4f;

public class TextBox extends AnchorPane {

	Vector4f bc = new Vector4f(0, 0, 0, 0);
	Vector4f bc2 = new Vector4f(1, 1, 1, 0.25f);
	
	int startWidth = 200;
	int startHeight = 30;
	
	Label label;
	String text;
	
	ArrayList<OnValueAccepted> valueAcceptedListeners;
	
	public TextBox(){
		style.backgroundColor.set(0, 0, 0, 0.16f);
		style.borderColor.set(bc);
		style.borderSize = 2;
		setPrefSize(startWidth, startHeight);
		setSizeMode(PREFERRED, PREFERRED);
		label = new Label("");
		label.setAlignment(Label.ALIGNMENT_CENTER);
		text = "";
		add(label, AnchorLayout.CENTER);
		addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverEnd() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFocus() {
				style.borderColor.set(bc2);
			}

			@Override
			public void offFocus() {
				style.borderColor.set(bc);
			}
		});
		
		valueAcceptedListeners = new ArrayList<OnValueAccepted>();
	}
	
	public void backspace(){
		if(text.length() >  0){
			text = new String(text.substring(0, text.length() - 1));
		}
		setText(text);
	}
	
	public void onChar(int codepoint){
		text += new StringBuilder().appendCodePoint(codepoint).toString().charAt(0);
		setText(text);
	}
	
	public void setText(String t){
		text = t;
		label.setText(t);
	}
	
	protected void onValueAccept(){
		for(int i = 0; i < valueAcceptedListeners.size(); i++){
			OnValueAccepted listener = valueAcceptedListeners.get(i);
			listener.acceptValue(text);
		}
	}
	
	public void addOnValueAcceptedListener(OnValueAccepted listener){
		valueAcceptedListeners.add(listener);
	}
	
}
