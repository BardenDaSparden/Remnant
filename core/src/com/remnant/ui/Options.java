package com.remnant.ui;

import java.util.ArrayList;

public class Options extends VBox {

	public static final int WIDTH = 150;
	public static final int HEIGHT = 30;
	
	Button toggleButton;
	Label selectedLabel;
	AnchorPane optionsHeader;
	VBox optionsContent;
	
	boolean contentVisible = false;
	
	ArrayList<Option> options;
	Option selected = null;
	
	ArrayList<OnValueAccepted> valueAcceptedListeners;
	
	public Options() {
		super();
		setAlignment(FlowLayout.LEADING);
		setPrefSize(WIDTH, HEIGHT);
		setSizeMode(PREFERRED, PREFERRED);
		style.backgroundColor.set(0, 0, 0, 0.15f);
		
		toggleButton = new Button("");
		toggleButton.style.backgroundColor.set(1, 1, 1, 0.1f);
		toggleButton.setPrefSize(HEIGHT, HEIGHT);
		toggleButton.setSizeMode(PREFERRED, PREFERRED);
		toggleButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				toggleContent();
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void offFocus() {
				// TODO Auto-generated method stub
				
			}
		});
		
		selectedLabel = new Label("NULL");
		selectedLabel.setAlignment(Label.ALIGNMENT_LEFT);
		
		optionsHeader = new AnchorPane();
		optionsHeader.setPrefSize(WIDTH, HEIGHT);
		optionsHeader.setSizeMode(PREFERRED, PREFERRED);
		
		optionsHeader.add(toggleButton, AnchorLayout.RIGHT);
		optionsHeader.add(selectedLabel, AnchorLayout.LEFT);
		
		optionsContent = new VBox();
		optionsContent.setAlignment(FlowLayout.CENTER);
		optionsContent.style.backgroundColor.set(0, 0, 0, 0.15f);
		//optionsContent.style.backgroundColor.set(0, 0, 1, 1);
		optionsContent.setPrefSize(WIDTH, HEIGHT * 5);
		optionsContent.setSizeMode(PREFERRED, PREFERRED);
		optionsContent.hide();
		
		options = new ArrayList<Option>();
		valueAcceptedListeners = new ArrayList<OnValueAccepted>();
		
		add(optionsHeader);
		add(optionsContent);
	}
	
	public void select(Option option){
		selected = option;
		selectedLabel.setText(option.obj.toString());
		onValueAccept();
		closeContent();
	}
	
	public void show(){
		super.show();
		optionsContent.hide();
		if(contentVisible)
			optionsContent.show();
	}
	
	public void closeContent(){
		contentVisible = false;
		optionsContent.hide();
	}
	
	public void toggleContent(){
		contentVisible = !contentVisible;
		optionsContent.setVisibility(contentVisible);
	}
	
	public void addOption(Option option){
		option.options = this;
		options.add(option);
		if(options.size() == 1){
			select(option);
		}
		optionsContent.add(option);
	}
	
	public Option getSelected(){
		return selected;
	}
	
	public void selectByString(String value){
		for(int i = 0; i < options.size(); i++){
			Option option = options.get(i);
			if(option.obj.toString().equals(value)){
				selected = option;
				selectedLabel.setText(option.obj.toString());
				break;
			}
		}
	}
	
	protected void onValueAccept(){
		for(int i = 0; i < valueAcceptedListeners.size(); i++){
			OnValueAccepted listener = valueAcceptedListeners.get(i);
			listener.acceptValue(selected.obj.toString());
		}
	}
	
	public void addOnValueAcceptedListener(OnValueAccepted listener){
		valueAcceptedListeners.add(listener);
	}
}

class Option extends UINode {
	
	Options options;
	Label label;
	Object obj;
	
	public Option(Object obj){
		this.obj = obj;
		style.backgroundColor.set(0, 0, 0, 0);
		setPrefSize(Options.WIDTH, Options.HEIGHT);
		setSizeMode(PREFERRED, PREFERRED);
		label = new Label(obj.toString());
		add(label);
	}
	
	protected void onHoverStart(){
		super.onHoverStart();
		style.backgroundColor.set(1, 1, 1, 0.15f);
	}
	
	protected void onHoverEnd(){
		super.onHoverEnd();
		style.backgroundColor.set(0.0f, 0.0f, 0.0f, 0);
	}
	
	protected void onMouseRelease(){
		super.onMouseRelease();
		options.select(this);
	}
	
}