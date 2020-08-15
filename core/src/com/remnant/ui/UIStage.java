package com.remnant.ui;

import java.util.ArrayList;
import java.util.Collections;

import org.joml.Vector2f;

import com.remnant.geometry.AABB;
import com.remnant.input.Input;
import com.remnant.input.KeyboardListener;
import com.remnant.input.Mouse;

import static org.lwjgl.glfw.GLFW.*;

public class UIStage {
	
	final int WINDOW_Z_INDEX = 100;
	final int FRONT_WINDOW_Z_INDEX = 1000;
	
	//Application inputs
	Input input;
	
	KeyboardListener keyboardListener;
	
	//Root of tree
	UINode root;
	
	//Nodes found when "walking" root node
	ArrayList<UINode> nodes;
	
	ArrayList<UINode> intersectedNodes;
	
	//Bounding box for mouse events
	AABB boundingBox;
	
	Vector2f previousMousePosition;
	Vector2f mousePosition;
	
	boolean mousePrevious = false;
	boolean mouseDown = false;
	
	UINode lastClickedNode = null;
	
	//Window(s) found when "walking" root node
	ArrayList<Window> Windows;
	Window frontWindow = null;
	
	MenuBar menuBar = null;

	ArrayList<Window> activeDialogs;
	
	boolean isInteracting = false;
	
	public UIStage(UINode root){
		this.root = root;
		nodes = new ArrayList<UINode>();
		Windows = new ArrayList<Window>();
		intersectedNodes = new ArrayList<UINode>();
		traverse(root);
		boundingBox = new AABB(0, 0);
		previousMousePosition = new Vector2f();
		mousePosition = new Vector2f();
		keyboardListener = new KeyboardListener() {
			
			@Override
			public void onKeyRelease(int key) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKeyPress(int key) {
				if(key == GLFW_KEY_BACKSPACE || key == GLFW_KEY_DELETE){
					if(lastClickedNode instanceof TextBox){
						TextBox textBox = (TextBox) lastClickedNode;
						textBox.backspace();
					}
				}
				
				if(key == GLFW_KEY_ENTER){
					if(lastClickedNode instanceof TextBox){
						TextBox textbox = (TextBox) lastClickedNode;
						textbox.onValueAccept();
					}
				}
			}

			@Override
			public void onChar(int codepoint) {
				if(lastClickedNode instanceof TextBox){
					TextBox textBox = (TextBox) lastClickedNode;
					textBox.onChar(codepoint);
				}
			}
		};
		activeDialogs = new ArrayList<Window>();
	}
	
	void traverse(UINode root){
		root.stage = this;
		nodes.add(root);
		
		if(root instanceof MenuBar)
			menuBar = (MenuBar) root;
		
		if(root instanceof Window){
			Window window = (Window) root;
			Windows.add(window);
		}
		
		if(root.isParent()){
			for(int i = 0; i < root.getChildren().size(); i++){
				UINode child = root.getChildren().get(i);
				traverse(child);
			}
		}
	}
	
	void bringWindowToFront(Window pane){
		if(pane == frontWindow)
			return;
		
		int zIndex = WINDOW_Z_INDEX;
		for(int i = 0; i < Windows.size(); i++){
			Window node = Windows.get(i);
			zIndex += 100;
			node.setZIndex(zIndex);
		}
		zIndex+=100;
		
		frontWindow = pane;
		pane.setZIndex(zIndex);
	}
	
	public void updateDialogs(){
		
		for(UINode node : activeDialogs){
			if(!node.isVisible)
				continue;
			traverse(node);
		}
		
		intersectedNodes.clear();
		for(int i = 0; i < nodes.size(); i++){
			UINode node = nodes.get(i);
			
			if(!node.isVisible)
				continue;
			
			if(node instanceof Label)
				continue;
			
			boundingBox.width = node.getWidth();
			boundingBox.height = node.getHeight();
			boundingBox.x = node.getX();
			boundingBox.y = node.getY();
			
			boolean containsMouse = boundingBox.contains(mousePosition.x, mousePosition.y);
			node.previousIsMouseOver = node.isMouseOver;
			node.isMouseOver = containsMouse;
			
			if(node.isMouseOver && !node.previousIsMouseOver)
				node.onHoverStart();
			if(!node.isMouseOver && node.previousIsMouseOver)
				node.onHoverEnd();
			
			if(containsMouse)
				intersectedNodes.add(node);
		}
		
		if(intersectedNodes.size() > 0){
			Collections.sort(intersectedNodes);
			UINode node = intersectedNodes.get(intersectedNodes.size() - 1);
			if(!mousePrevious && mouseDown){
				if(lastClickedNode != null)
					lastClickedNode.offFocus();
				lastClickedNode = node;
				//node.style.backgroundColor.set(1, 1, 1, 0.2f);
				lastClickedNode.onFocus();
				node.onMousePress();
			}
			if(mousePrevious && !mouseDown)
				node.onMouseRelease();
		}
	}
	
	public void update(){
		
		previousMousePosition.set(mousePosition);
		mousePosition.x = (float) input.getMouse().getX();
		mousePosition.y = (float) input.getMouse().getY();
		mousePrevious = mouseDown;
		mouseDown = input.getMouse().isButtonDown(Mouse.LEFT_BUTTON);
		
		nodes.clear();
		Windows.clear();
		isInteracting = false;
		
		//Check for Dialog interaction
		if(isDialogOpen()){
			isInteracting = true;
			updateDialogs();
			return;
		}
		
		//Check for Top MenuBar interaction
		if(menuBar != null){
			boundingBox.width = menuBar.width;
			boundingBox.height = menuBar.height;
			boundingBox.x = menuBar.posX;
			boundingBox.y = menuBar.posY;
			if(boundingBox.contains(mousePosition.x, mousePosition.y))
				isInteracting = true;
		}
		
		traverse(root);
		
		intersectedNodes.clear();
		for(int i = 0; i < nodes.size(); i++){
			UINode node = nodes.get(i);
			
			if(!node.isVisible)
				continue;
			
			if(node instanceof Label)
				continue;
			
			boundingBox.width = node.getWidth();
			boundingBox.height = node.getHeight();
			boundingBox.x = node.getX();
			boundingBox.y = node.getY();
			
			boolean containsMouse = boundingBox.contains(mousePosition.x, mousePosition.y);
			node.previousIsMouseOver = node.isMouseOver;
			node.isMouseOver = containsMouse;
			
			if(node.isMouseOver && !node.previousIsMouseOver)
				node.onHoverStart();
			if(!node.isMouseOver && node.previousIsMouseOver)
				node.onHoverEnd();
			
			if(containsMouse)
				intersectedNodes.add(node);
		}
		
		if(intersectedNodes.size() > 0){
			Collections.sort(intersectedNodes);
			UINode node = intersectedNodes.get(intersectedNodes.size() - 1);
			if(!mousePrevious && mouseDown){
				if(lastClickedNode != null)
					lastClickedNode.offFocus();
				lastClickedNode = node;
				//node.style.backgroundColor.set(1, 1, 1, 0.2f);
				lastClickedNode.onFocus();
				node.onMousePress();
			}
			if(mousePrevious && !mouseDown)
				node.onMouseRelease();
		}
		
		intersectedNodes.clear();
		Collections.sort(Windows);
		for(int i = Windows.size() - 1; i > -1; i--){
			Window window = Windows.get(i);
			
			if(!window.isVisible)
				continue;
			
			boundingBox.width = window.width;
			boundingBox.height = window.height;
			boundingBox.x = window.posX;
			boundingBox.y = window.posY;
			
			boolean containsMouse = boundingBox.contains(mousePosition.x, mousePosition.y);
			if(containsMouse){
				isInteracting = true;		//Check for Window interaction
				if(!mousePrevious && mouseDown)
					intersectedNodes.add(window);
			}
		}
		
		if(intersectedNodes.size() > 0){
			Window topMostWindow = (Window)intersectedNodes.get(0);
			bringWindowToFront(topMostWindow);
		}
		
	}
	
	public void showDialog(Window dialogWindow){
		dialogWindow.show();
		dialogWindow.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose() {
				activeDialogs.remove(dialogWindow);
			}
		});
		activeDialogs.add(dialogWindow);
	}
	
	public boolean isDialogOpen(){
		return activeDialogs.size() > 0;
	}
	
	public boolean interacting(){
		return isInteracting;
	}
	
	public UINode getRoot(){
		return root;
	}

	protected void setInput(Input input){
		this.input = input;
		input.getKeyboard().addListener(keyboardListener);
	}
	
}
