package com.remnant.application;

public interface ApplicationPropertiesListener {

	public void onResize(int width, int height);
	public void onVSyncChange(boolean newVsync);
	
}
