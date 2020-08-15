package com.remnant.ui;

public interface Layout {

	public void add(UINode node);
	public void add(UINode node, Object attribute);
	public void setScrollOffset(float x, float y);
	public void remove(UINode node);
	public void setContainer(Container c);
	public void positionNodes();
	
}
