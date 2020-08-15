package org.barden.util;

public class Timer {

	int maxTick;
	int ticks;
	TimerCallback callback;
	boolean active = false;
	
	public Timer(int tickLength){
		this.maxTick = tickLength;
	}
	
	public void tick(){
		if(!active)
			return;
		if(ticks == maxTick){
			if(callback != null)
				callback.onTimerComplete();
		} else if(ticks < maxTick){
			ticks++;
		}
	}
	
	public void start(){
		active = true;
	}
	
	public void stop(){
		active = false;
	}
	
	public boolean hasStarted(){
		return active;
	}
	
	public void restart(){
		ticks = 0;
		active = false;
	}
	
	public float percent(){
		return (float)ticks / (float)maxTick;
	}
	
	public void onComplete(TimerCallback callback){
		this.callback = callback;
	}
	
}
