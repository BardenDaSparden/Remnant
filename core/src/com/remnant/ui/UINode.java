package com.remnant.ui;

import java.util.ArrayList;
import java.util.List;

import com.remnant.math.Math2D;

public abstract class UINode implements Comparable<UINode>{

	public static final int USE_PREF_WIDTH = 0;
	public static final int USE_PREF_HEIGHT = 0;
	
	public static final int PREFERRED = 0;
	public static final int PERCENT = 1;
	public static final int PARENT = 2;
	public static final int FILL = 3;
	public static final int AUTO = 4;
	
	public static final int USE_PARENT_WIDTH = 2;
	public static final int USE_PARENT_HEIGHT = 2;
	
	public static final int USE_FILL_WIDTH = 3;
	public static final int USE_FILL_HEIGHT = 3;
	
	public static final int USE_AUTO_WIDTH = 4;
	public static final int USE_AUTO_HEIGHT = 4;
	
	public static final int RENDER_STATE_IDLE = 0;
	public static final int RENDER_STATE_HOVER = 1;
	public static final int RENDER_STATE_CLICKED = 2;
	public static final int RENDER_STATE_DISABLED = 3;
	
	//Position in pixels
	float posX;
	float posY;
	
	int minWidth = 0;
	int maxWidth = Integer.MAX_VALUE;
	
	int minHeight = 0;
	int maxHeight = Integer.MAX_VALUE;
	
	//The current width/height used by node
	int width;
	int height;
	
	//The pseudo z value for layering
	int zIndex = 1;
	
	//zIndex offset
	int zIndexOffset = 0;
	
	//The width/height calculated without children, but with padding, margins
	int prefWidth;
	int prefHeight;
	
	float percentWidth;
	float percentHeight;
	
	//Mode used to represent how the width/height are calculated. Either using pref size, or calculated based on child(s) dimensions
	float widthMode = AUTO;
	float heightMode = AUTO;
	
	//Padding / Margin horizontal and vertical insets
	Insets padding;
	Insets margin;
	
	UINode parent = null;
	
	//Child nodes
	List<UINode> children;
	
	List<Listener> listeners;
	
	boolean isDisabled = false;
	
	boolean isVisible = true;
	
	boolean inDrag = false;
	
	boolean dragable = false;
	
	boolean isFocused = false;
	
	boolean previousIsMouseOver = false;
	boolean isMouseOver = false;
	
	//Animations and Event Handling may call for later changes
	UIStyle style;
	
	UIStyle hoverStyle;
	
	protected boolean debugDraw = false;
	
	protected UIStage stage = null;
	
	public UINode(){
		this(null);
	}
	
	public UINode(UINode parent){
		posX = 0;
		posY = 0;
		width = 100;
		height = 50;
		prefWidth = 100;
		prefHeight = 50;
		percentWidth = 1;
		percentHeight = 1;
		this.parent = parent;
		padding = new Insets();
		margin = new Insets();
		children = new ArrayList<UINode>();
		listeners = new ArrayList<Listener>();
		style = new UIStyle();
		hoverStyle = new UIStyle();
	}
	
	//Recursively calculate the width occupied by all child UINodes
	int minX;
	int maxX;
	int calcExtentX(UINode node){
		if(!node.isVisible)
			return 0;
		
		if(node.isParent()){
			for(int i = 0; i < node.children.size(); i++){
				UINode child = node.children.get(i);
				calcExtentX(child);
			}
		} else {
			int nodeWidth = node.width;// + node.padding.horizontal * 2 + node.margin.horizontal * 2;
			float min = node.posX - (nodeWidth / 2.0f);
			float max = node.posX + (nodeWidth / 2.0f);
			if(max > maxX)
				maxX = Math.round(max);
			if(min > maxX)
				maxX = Math.round(min);
			if(max < minX)
				minX = Math.round(max);
			if(min < minX)
				minX = Math.round(min);
		}
		return (maxX - minX);
	}
	
	//Recursively calculate the height occupied by all child UINodes
	int minY;
	int maxY;
	int calcExtentY(UINode node){
		
		if(!node.isVisible)
			return 0;
		
		if(node.isParent()){
			for(int i = 0; i < node.children.size(); i++){
				UINode child = node.children.get(i);
				calcExtentY(child);
			}
		} else {
			int nodeHeight = node.height;// + node.padding.vertical * 2 + node.margin.vertical * 2;
			float min = node.posY - (nodeHeight / 2.0f);// - node.padding.vertical - node.margin.vertical;
			float max = node.posY + (nodeHeight / 2.0f);// + node.padding.vertical + node.margin.vertical;
			if(max > maxY)
				maxY = Math.round(max);
			if(min > maxY)
				maxY = Math.round(min);
			if(max < minY)
				minY = Math.round(max);
			if(min < minY)
				minY = Math.round(min);
		}
		return (maxY - minY);
	}
	
	void resizeSelf(){
		if(widthMode == PREFERRED){
			width = prefWidth;
		} else if(widthMode == PERCENT){
			if(hasParent())
				width = (int)Math.round((float)parent.getWidth() * percentWidth);
		} else if(widthMode == PARENT){
			if(hasParent())
				width = parent.getWidth();
		} else if(widthMode == FILL){
			int remaining = parent.getWidth();
			for(UINode child : parent.children){
				if(child == this)
					continue;
				remaining -= child.getWidth();
			}
			width = remaining;
		} else if(widthMode == AUTO){
			minX = Integer.MAX_VALUE;
			maxX = -Integer.MAX_VALUE;
			width = calcExtentX(this);
		}
		
		
		if(heightMode == USE_PREF_HEIGHT){
			height = prefHeight;
		} else if(heightMode == PERCENT){
			if(hasParent())
				height = (int)Math.round((float)parent.getHeight() * percentHeight);
		} else if(heightMode == USE_PARENT_HEIGHT){
			if(hasParent())
				height = parent.getHeight();
		} else if(heightMode == USE_FILL_HEIGHT){
			if(hasParent()){
				int remaining = parent.getHeight();
				for(UINode child : parent.children){
					if(child == this)
						continue;
					remaining -= child.getHeight();
				}
				height = remaining;
			}
		} else if(heightMode == USE_AUTO_HEIGHT){
			minY = Integer.MAX_VALUE;
			maxY = -Integer.MAX_VALUE;
			height = calcExtentY(this);
		}
		
		width = (int)Math2D.clamp(minWidth, maxWidth, width);
		height = (int)Math2D.clamp(minHeight, maxHeight, height);
	}
	
	void resizeParent(){
		if(hasParent()){
			parent.resizeSelf();
			parent.resizeParent();
		}
	}
	
	protected void resizeChildren(){
		for(int i = 0; i < children.size(); i++){
			UINode child = children.get(i);
			child.resizeDescending();
		}
	}
	
	protected void resizeAscending(){
		resizeSelf();
		resizeParent();
	}
	
	//Helper method to recalculate width and height of node
	protected void resizeDescending(){
		resizeSelf();
		resizeChildren();
	}
	
	//Helper method to move child UINodes when parent's position changes
	protected void translate(float offsetX, float offsetY){
		posX += offsetX;
		posY += offsetY;
		if(isParent()){
			for(int i = 0; i < children.size(); i++){
				UINode child = children.get(i);
				child.translate(offsetX, offsetY);
			}
		}
	}
	
	protected void offFocus(){
		for(int i = 0; i < listeners.size(); i++){
			Listener listener = listeners.get(i);
			listener.offFocus();
		}
	}
	
	protected void onFocus(){
		for(int i = 0; i < listeners.size(); i++){
			Listener listener = listeners.get(i);
			listener.onFocus();
		}
	}
	
	protected void onMousePress(){
		if(hasParent())
			parent.onMousePress();
		for(int i = 0; i < listeners.size(); i++){
			Listener listener = listeners.get(i);
			listener.onMousePress();
		}
	}
	
	protected void onMouseRelease(){
		if(hasParent())
			parent.onMouseRelease();
		for(int i = 0; i < listeners.size(); i++){
			Listener listener = listeners.get(i);
			listener.onMouseRelease();
		}
	}
	
	protected void onHoverStart(){
		for(int i = 0; i < listeners.size(); i++){
			Listener listener = listeners.get(i);
			listener.onHoverStart();
		}
	}
	
	protected void onHoverEnd(){
		for(int i = 0; i < listeners.size(); i++){
			Listener listener = listeners.get(i);
			listener.onHoverEnd();
		}
	}
	
	protected boolean hasParent(){
		return parent != null;
	}
	
	//Add child node to parent
	public void add(UINode child){
		child.parent = this;
		children.add(child);
		resizeDescending();
	}
	
	public void remove(UINode child){
		child.parent = null;
		children.remove(child);
		resizeDescending();
	}
	
	public void addListener(Listener listener){
		listeners.add(listener);
	}
	
	public boolean isParent(){
		return children.size() > 0;
	}
	
	@Override
	public int compareTo(UINode node){
		return (this.zIndex < node.zIndex) ? -1 :
				(this.zIndex == node.zIndex) ? 0 : 1;
	}
	
	//Set position for current node, affects children
	public void setPosition(float x, float y){
		translate((x - posX), (y - posY));
	}
	
	public void setPrefWidth(int prefWidth){
		this.prefWidth = prefWidth;
		resizeDescending();
	}
	
	public void setPrefHeight(int prefHeight){
		this.prefHeight = prefHeight;
		resizeDescending();
	}
	
	public void setPrefSize(int prefWidth, int prefHeight){
		this.prefWidth = prefWidth;
		this.prefHeight = prefHeight;
		resizeDescending();
	}
	
	public void setPercentSize(float x, float y){
		percentWidth = Math2D.clamp(0, 1, x);
		percentHeight = Math2D.clamp(0, 1, y);
		resizeDescending();
	}
	
	public void setMinSize(int minWidth, int minHeight){
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		resizeDescending();
	}
	
	public void setMaxSize(int maxWidth, int maxHeight){
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		resizeDescending();
	}
	
	public void setSizeMode(int widthMode, int heightMode){
		this.widthMode = widthMode;
		this.heightMode = heightMode;
		resizeDescending();
	}
	
	public void setPadding(int padding){
		this.padding.vertical = padding;
		this.padding.horizontal = padding;
		resizeDescending();
	}
	
	public void setPadding(int vertical, int horizontal){
		padding.vertical = vertical;
		padding.horizontal = horizontal;
		resizeDescending();
	}
	
	public void setMargin(int margin){
		this.margin.vertical = margin;
		this.margin.horizontal = margin;
		resizeDescending();
	}
	
	public void setMargin(int vertical, int horizontal){
		margin.vertical = vertical;
		margin.horizontal = horizontal;
		resizeDescending();
	}
	
	public float getX(){
		return posX;
	}
	
	public float getY(){
		return posY;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getZIndex(){
		return zIndex;
	}
	
	public boolean isVisible(){
		return isVisible;
	}
	
	public void setZIndex(int zIndex){
		this.zIndex = zIndex;
		for(int i = 0; i < children.size(); i++){
			UINode child = children.get(i);
			child.setZIndex(zIndex + 1);
		}
	}
	
	public void setZIndexOffset(int offset){
		zIndexOffset = offset;
	}
	
	public void setDragable(boolean canDrag){
		dragable = canDrag;
	}
	
	public void show(){
		isVisible = true;
		for(int i = 0; i < children.size(); i++){
			UINode node = children.get(i);
			node.show();
		}
		resizeDescending();
		resizeAscending();
	}
	
	public void hide(){
		isVisible = false;
		for(int i = 0; i < children.size(); i++){
			UINode node = children.get(i);
			node.hide();
		}
		resizeDescending();
		resizeAscending();
	}
	
	public void setVisibility(boolean visible){
		if(visible)
			show();
		else
			hide();
	}
	
	public void disable(){
		isDisabled = true;
		for(int i = 0; i < children.size(); i++){
			UINode node = children.get(i);
			node.disable();
		}
	}
	
	public void enable(){
		isDisabled = false;
		for(int i = 0; i < children.size(); i++){
			UINode node = children.get(i);
			node.enable();
		}
	}
	
	public boolean isDisabled(){
		return isDisabled;
	}
	
	public List<UINode> getChildren(){
		return children;
	}
	
	public UIStyle getStyle(){
		return style;
	}
	
	public void setStyle(UIStyle style){
		this.style.set(style);
	}
	
}

class Insets {
	int vertical;
	int horizontal;
}