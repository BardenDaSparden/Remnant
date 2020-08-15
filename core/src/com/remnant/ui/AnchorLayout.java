package com.remnant.ui;

import java.util.ArrayList;

public class AnchorLayout implements Layout {

	public static final int CENTER = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int TOP = 4;
	public static final int BOTTOM = 5;
	
	int spacingX = 0;
	int spacingY = 0;
	
	ArrayList<UINode> leftNodes, rightNodes, topNodes, bottomNodes, centerNodes;
	
	Container container;
	
	public AnchorLayout(){
		leftNodes = new ArrayList<UINode>();
		rightNodes = new ArrayList<UINode>();
		topNodes = new ArrayList<UINode>();
		bottomNodes = new ArrayList<UINode>();
		centerNodes = new ArrayList<UINode>();
	}
	
	@Override
	public void add(UINode node) {
		add(node, CENTER);
	}

	@Override
	public void add(UINode node, Object attribute) {
		int anchor = (int) attribute;
		if(anchor == CENTER){
			centerNodes.add(node);
		} else if(anchor == LEFT){
			leftNodes.add(node);
		} else if(anchor == RIGHT){
			rightNodes.add(node);
		} else if(anchor == TOP){
			topNodes.add(node);
		} else if(anchor == BOTTOM){
			bottomNodes.add(node);
		}
	}

	@Override
	public void remove(UINode node) {
		
	}
	
	public void remove(UINode node, Object attribute){
		int anchor = (int) attribute;
		if(anchor == CENTER){
			centerNodes.remove(node);
		} else if(anchor == LEFT){
			leftNodes.remove(node);
		} else if(anchor == RIGHT){
			rightNodes.remove(node);
		} else if(anchor == TOP){
			topNodes.remove(node);
		} else if(anchor == BOTTOM){
			bottomNodes.remove(node);
		}
	}

	@Override
	public void setContainer(Container c) {
		container = c;
	}
	
	@Override
	public void positionNodes(){
		
		float startX = container.posX;
		float startY = container.posY;
		float nodeOffsetX = 0;
		float nodeOffsetY = 0;
		
		//Re-position center container nodes
		for(int i = 0; i < centerNodes.size(); i++){
			UINode node = centerNodes.get(i);
			if(!node.isVisible)
				continue;
			float nextYPos = nodeOffsetY + startY - node.height / 2.0f;
			node.setPosition(node.posX, nextYPos);
			nodeOffsetY -= (node.height + spacingY);
		}
		nodeOffsetY += spacingY;
		for(int i = 0; i < centerNodes.size(); i++){
			UINode node = centerNodes.get(i);
			if(!node.isVisible)
				continue;
			node.translate(0, -nodeOffsetY / 2.0f);
		}
		startX = container.posX;
		startY = container.posY;
		nodeOffsetX = 0;
		nodeOffsetY = 0;
		
		startY = container.posY + container.getHeight() / 2.0f - spacingY;
		for(int i = 0; i < topNodes.size(); i++){
			UINode node = topNodes.get(i);
			if(!node.isVisible)
				continue;
			float nextXPos = nodeOffsetX + startX;
			float nextYPos = nodeOffsetY + startY - node.height / 2.0f;
			node.setPosition(nextXPos, nextYPos);
			nodeOffsetY -= (node.height + spacingY);
		}
		startX = container.posX;
		startY = container.posY;
		nodeOffsetX = 0;
		nodeOffsetY = 0;
		
		startY = container.posY - container.getHeight() / 2.0f + spacingY;
		for(int i = bottomNodes.size() - 1; i > -1; i--){
			UINode node = bottomNodes.get(i);
			if(!node.isVisible)
				continue;
			float nextYPos = nodeOffsetY + startY + node.height / 2.0f;
			node.setPosition(node.posX, nextYPos);
			nodeOffsetY += (node.height + spacingY);
		}
		startX = container.posX;
		startY = container.posY;
		nodeOffsetX = 0;
		nodeOffsetY = 0;
		
		
		startX = container.posX - container.getWidth() / 2.0f;
		nodeOffsetX = spacingX;
		for(int i = 0; i < leftNodes.size(); i++){
			UINode node = leftNodes.get(i);
			if(!node.isVisible)
				continue;
			float nextXPos = nodeOffsetX + startX + node.width / 2.0f;
			float nextYPos = nodeOffsetY + startY;
			node.setPosition(nextXPos, nextYPos);
			nodeOffsetX += (node.width + spacingX);
		}
		startX = container.posX;
		startY = container.posY;
		nodeOffsetX = 0;
		nodeOffsetY = 0;
		
		startX = container.posX + container.getWidth() / 2.0f - spacingX;
		for(int i = 0; i < rightNodes.size(); i++){
			UINode node = rightNodes.get(i);
			if(!node.isVisible)
				continue;
			float nextXPos = nodeOffsetX + startX - node.width / 2.0f;
			float nextYPos = nodeOffsetY + startY;
			node.setPosition(nextXPos, nextYPos);
			nodeOffsetX -= (node.width + spacingX);
		}
		
	}

	public void setSpacing(int sx, int sy){
		spacingX = sx;
		spacingY = sy;
	}

	@Override
	public void setScrollOffset(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
}
