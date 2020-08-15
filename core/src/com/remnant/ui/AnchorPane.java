package com.remnant.ui;

public class AnchorPane extends Container {

	AnchorLayout layout;
	
	public AnchorPane(){
		this(null);
	}
	
	public AnchorPane(UINode parent){
		super.parent = parent;
		style.backgroundColor.set(0, 0, 0, 0);
		layout = new AnchorLayout();
		setLayout(layout);
	}
	
	public void add(UINode node, Object attribute){
		super.add(node, attribute);
		resizeSelf();
	}
	
	public void remove(UINode node, Object attribute){
		super.remove(node);
		layout.remove(node, attribute);
	}
	
	public void setSpacing(int x, int y){
		layout.setSpacing(x, y);
		layout.positionNodes();
	}
}
