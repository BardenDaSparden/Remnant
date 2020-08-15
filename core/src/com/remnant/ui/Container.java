package com.remnant.ui;

public class Container extends UINode {

	private Layout layout;
	
	public Container(){
		//debugDraw = false;
		setLayout(new FlowLayout());
		setPrefSize(400, 400);
		setSizeMode(PREFERRED, PREFERRED);
	}
	
	public void setLayout(Layout layout){
		this.layout = layout;
		layout.setContainer(this);
	}
	
	public void updateLayout(){
		layout.positionNodes();
	}
	
	protected void resizeSelf(){
		super.resizeSelf();
		layout.positionNodes();
	}
	
	public void add(UINode node){
		super.add(node);
		layout.add(node);
		layout.positionNodes();
	}
	
	public void show(){
		super.show();
		layout.positionNodes();
	}
	
	public void hide(){
		super.hide();
		layout.positionNodes();
	}
	
	public void add(UINode node, Object attributes){
		super.add(node);
		layout.add(node, attributes);
		layout.positionNodes();
	}
	
	public void remove(UINode node){
		super.remove(node);
		layout.remove(node);
		layout.positionNodes();
	}
	
}
