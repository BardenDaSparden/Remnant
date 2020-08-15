package com.remnant.ui;

import java.util.ArrayList;

import org.joml.Vector2f;

public class FlowLayout implements Layout {

	public static final int CENTER = 1;
	public static final int LEADING = 2;
	public static final int TRAILING = 3;
	
	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 2;
	
	Container container;
	
	private int alignment = CENTER;
	private int direction = HORIZONTAL;
	private int spacingX = 0;
	private int spacingY = 0;
	
	private float scrollOffsetX = 0;
	private float scrollOffsetY = 0;
	
	ArrayList<UINode> nodes = new ArrayList<UINode>();
	
	boolean useChildrenSize = false;
	
	@Override
	public void add(UINode node) {
		nodes.add(node);
	}

	@Override
	public void add(UINode node, Object attribute) {
		add(node);
	}

	@Override
	public void remove(UINode node) {
		nodes.remove(node);
	}

	@Override
	public void setContainer(Container c){
		container = c;
	}
	
	public void useChildrenSize(boolean value){
		useChildrenSize = value;
	}
	
	public Vector2f getChildrenExtents(){
		int totalWidth = 0;
		int totalHeight = 0;
		int maxWidth = 0;
		int maxHeight = 0;
		
		for(int i = 0; i < nodes.size(); i++){
			UINode node = nodes.get(i);
			node.resizeDescending();
			int w = node.width;
			int h = node.height;
			totalWidth = w;
			totalHeight = h;
			if(w > maxWidth)
				maxWidth = w;
			if(h > maxHeight)
				maxHeight = h;
		}
		
		totalWidth += (nodes.size() + 1) * spacingX;
		totalHeight += (nodes.size() + 1) * spacingY;
		maxWidth += 2 * spacingX;
		maxHeight += 2 * spacingY;
		
		Vector2f extents = new Vector2f();
		extents.x = maxWidth;
		extents.y = maxHeight;
		return extents;
	}
	
	@Override
	public void positionNodes(){
		
		float startX = container.posX;
		float startY = container.posY;
		float nodeOffsetX = scrollOffsetX;
		float nodeOffsetY = scrollOffsetY;
		
		if(useChildrenSize){
			
			int totalWidth = 0;
			int totalHeight = 0;
			int maxWidth = 0;
			int maxHeight = 0;
			
			for(int i = 0; i < nodes.size(); i++){
				UINode node = nodes.get(i);
				node.resizeDescending();
				int w = node.width;
				int h = node.height;
				totalWidth = w;
				totalHeight = h;
				if(w > maxWidth)
					maxWidth = w;
				if(h > maxHeight)
					maxHeight = h;
			}
			
			totalWidth += (nodes.size() + 1) * spacingX;
			totalHeight += (nodes.size() + 1) * spacingY;
			maxWidth += 2 * spacingX;
			maxHeight += 2 * spacingY;
			
			if(direction == VERTICAL){
				container.width = maxWidth;
				container.height = totalHeight;
				container.prefWidth = maxWidth;
				container.prefHeight = totalHeight;
			} else if(direction == HORIZONTAL){
				container.width = totalWidth;
				container.height = maxHeight;
				container.prefWidth = totalWidth;
				container.prefHeight = totalHeight;
			}
			
			container.resizeChildren();
			
		}
		
		if(direction == VERTICAL){
			
			if(alignment == CENTER){
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					float nextYPos = nodeOffsetY + startY - node.height / 2.0f;
					node.setPosition(node.posX, nextYPos);
					nodeOffsetY -= (node.height + spacingY);
				}
				nodeOffsetY += spacingY;
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					node.translate(0, -nodeOffsetY / 2.0f);
				}
			} else if(alignment == LEADING){
				startY = container.posY + container.getHeight() / 2.0f - spacingY;
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					float nextXPos = nodeOffsetX + startX;
					float nextYPos = nodeOffsetY + startY - node.height / 2.0f;
					node.setPosition(nextXPos, nextYPos);
					nodeOffsetY -= (node.height + spacingY);
				}
			} else if(alignment == TRAILING){
				startY = container.posY - container.getHeight() / 2.0f + spacingY;
				for(int i = nodes.size() - 1; i > -1; i--){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					float nextYPos = nodeOffsetY + startY + node.height / 2.0f;
					node.setPosition(node.posX, nextYPos);
					nodeOffsetY += (node.height + spacingY);
				}
			}
			
		} else if(direction == HORIZONTAL){
			
			if(alignment == CENTER){
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					float nextXPos = nodeOffsetX + startX + node.width / 2.0f;
					float nextYPos = nodeOffsetY + startY;
					node.setPosition(nextXPos, nextYPos);
					nodeOffsetX += (node.width + spacingX);
				}
				nodeOffsetX -= spacingX;
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					node.translate(-nodeOffsetX / 2.0f, 0);
				}
			} else if(alignment == LEADING){
				startX = container.posX - container.getWidth() / 2.0f;
				nodeOffsetX = spacingX;
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					float nextXPos = nodeOffsetX + startX + node.width / 2.0f;
					float nextYPos = nodeOffsetY + startY;
					node.setPosition(nextXPos, nextYPos);
					nodeOffsetX += (node.width + spacingX);
				}
			} else if(alignment == TRAILING){
				startX = container.posX + container.getWidth() / 2.0f - spacingX;
				for(int i = 0; i < nodes.size(); i++){
					UINode node = nodes.get(i);
					if(!node.isVisible)
						continue;
					float nextXPos = nodeOffsetX + startX - node.width / 2.0f;
					node.setPosition(nextXPos, node.posY);
					nodeOffsetX -= (node.width + spacingX);
				}
			}
			
		}
	}
	
//	@Override
//	public void positionNodes() {
//		float containerX = container.posX;
//		float containerY = container.posY;
//		float offsetX = 0;
//		float offsetY = 0;
//		
//		float minX = containerX - container.width / 2.0f;
//		float minY = containerY - container.height / 2.0f;
//		float maxX = containerX + container.width / 2.0f;
//		float maxY = containerY + container.height / 2.0f;
//		
//		int containerWidth = 0;
//		int containerHeight = 0;
//		
//		if(direction == HORIZONTAL){
//			//Use min and max extents of child nodes to find the dimensions of parent container
//			//THe intent is for the containing element to wrap all child nodes
//			offsetX += spacingX;
//			for(int i = 0; i < nodes.size(); i++){
//				UINode node = nodes.get(i);
//				float newX = containerX + offsetX + node.getWidth() / 2.0f;
//				float newY = containerY + offsetY;
//				node.setPosition(newX, newY);
//				offsetX += node.getWidth() + spacingX;
//				if(node.getHeight() > containerHeight)
//					containerHeight = node.getHeight();
//			}
//			
//			//Width but spacingX amount to remove spacing from last node
//			//containerWidth = (int)offsetX;
//			//containerHeight += (spacingY * 2);
//			
//			//Center nodes
//			for(int i = 0; i < nodes.size(); i++){
//				UINode node = nodes.get(i);
//				node.translate(-offsetX / 2.0f, 0);
//			}
//		} else if(direction == VERTICAL){
//			//Use min and max extents of child nodes to find the dimensions of parent container
//			//THe intent is for the containing element to wrap all child nodes
//			offsetY += spacingY;
//			for(int i = 0; i < nodes.size(); i++){
//				UINode node = nodes.get(i);
//				float newX = containerX + offsetX;// + node.getWidth() / 2.0f;
//				float newY = containerY + offsetY - node.getHeight() / 2.0f;
//				node.setPosition(newX, newY);
//				offsetY -= node.getHeight() + spacingY;
//				if(node.getWidth() > containerWidth)
//					containerWidth = node.getWidth();
//			}
//			//containerWidth += (spacingX * 2);
//			//containerHeight = (int)offsetY;
//			
//			if(alignment == CENTER){
//				//Center nodes
//				for(int i = 0; i < nodes.size(); i++){
//					UINode node = nodes.get(i);
//					node.translate(0, -offsetY / 2.0f);
//				}
//			} else if(alignment == LEADING){
//				//Center nodes
//				for(int i = 0; i < nodes.size(); i++){
//					UINode node = nodes.get(i);
//					float toTop = (container.height / 2.0f) - offsetY / 2.0f; 
//					node.translate(0, toTop);
//				}
//			} else if(alignment == TRAILING){
//				
//			}
//		}
//		
//		container.width = container.prefWidth;
//		container.height = container.prefHeight;
//		
//		//container.width = containerWidth;
//		//container.height = containerHeight;
//	}

	public void setAlignment(int alignment){
		this.alignment = alignment;
	}
	
	public void setSpacing(int spacingX, int spacingY){
		this.spacingX = spacingX;
		this.spacingY = spacingY;
	}
	
	public void setSpacing(int spacing){
		setSpacing(spacing, spacing);
	}
	
	public void setDirection(int direction){
		this.direction = direction;
	}

	@Override
	public void setScrollOffset(float x, float y) {
		scrollOffsetX = x;
		scrollOffsetY = y;
	}
	
}
