package com.remnant.prototype;

import java.util.ArrayList;
import java.util.List;

public class CharactorPersistanceState {

	//store unlocked abilities
	//store equiped/unequiped enabled/disabled
	
	static final String DATA_PATH = "data/player.dat";
	
	private boolean morphBallUnlocked = false;
	private boolean morphBallEquipped = false;
	private List<StateListener> stateListeners;
	
	public CharactorPersistanceState(){
		stateListeners = new ArrayList<StateListener>();
	}
	
	public void initialize(){
		//Call resource loader, and parse runtime state from file
	}
	
	protected void notifyStateChange(){
		for(int i = 0; i < stateListeners.size(); i++){
			StateListener listener = stateListeners.get(i);
			listener.onPersistanceStateChange(this);
		}
	}
	
	public void addListener(StateListener listener){
		stateListeners.add(listener);
	}
	
	public void removeListener(StateListener listener){
		stateListeners.remove(listener);
	}
	
	public void debug_unlockMorphBall(){
		morphBallUnlocked = true;
		notifyStateChange();
	}
	
	public void debug_equipMorphBall(){
		morphBallEquipped = true;
		notifyStateChange();
	}
	
	public boolean isMorphBallEquipped(){
		return morphBallEquipped;
	}
	
	public boolean isMorphBallUnlocked(){
		return morphBallUnlocked;
	}
	
}
