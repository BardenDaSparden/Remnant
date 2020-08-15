package com.remnant.physics;

public interface ContactListener {

	public void onContactBegin(ContactEvent e);
	public void onContactEnd(ContactEvent e);
	public void onRayContact(RayContactEvent e);
	
	//public void process(ContactEvent event);
	//public void process(RayContactEvent event);
	
}
