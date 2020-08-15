package com.remnant.prototype;

import com.remnant.input.Input;
import com.remnant.input.Keyboard;

public class CharactorController implements ActionEventListener , StateListener {

	CharactorPersistanceState state;
	Keyboard keyboard;
	CharactorKeyboardListener keyboardListener;
	
	static final int STANCE_STANDING = 0;
	static final int STANCE_CROUCHING = 1;
	static final int STANCE_MORPHBALL = 2;
	int stance;
	boolean isMorphUsable = false;
	
	public CharactorController(Input input){
		state = new CharactorPersistanceState();
		keyboard = input.getKeyboard();
		keyboardListener = new CharactorKeyboardListener();
		
		state.initialize();
		state.addListener(this);
		keyboardListener.addListener(this);
	}
	
	public void load(){
		keyboard.addListener(keyboardListener);
	}
	
	public void unload(){
		keyboard.addListener(keyboardListener);
	}
	
	//Allow for deactivation of input listening for things such as menu, screens, pauses, etc
	public void setInputListeningState(boolean isListening){
		keyboardListener.setListening(isListening);
	}
	
	//Process incoming Actions from keyboard listener
	@Override
	public void processAction(int action) {
		
		System.out.println(CharactorActions.getString(action));
		
		if(action == CharactorActions.PRINT_DEBUG_LOG){
			System.out.println(getStanceAsString());
		}
		
		if(action == CharactorActions.UNLOCK_MORPHBALL){
			state.debug_unlockMorphBall();
			state.debug_equipMorphBall();
		}
		
		if(action == CharactorActions.DOWN){
			if(stance == STANCE_MORPHBALL){
				
			} else if(stance == STANCE_CROUCHING){
				setStance(STANCE_MORPHBALL);
			} else if(stance == STANCE_STANDING){
				setStance(STANCE_CROUCHING);
			}
		}
		
		if(action == CharactorActions.UP){
			if(stance == STANCE_STANDING){
				//set charactor aim upwards
			} else if(stance == STANCE_CROUCHING){
				setStance(STANCE_STANDING);
			} else if(stance == STANCE_MORPHBALL){
				setStance(STANCE_CROUCHING);
			}
		}
		
	}
	
	//Process incoming changes to our charactor's persistant state within the game world
	@Override
	public void onPersistanceStateChange(CharactorPersistanceState persistanceState) {
		isMorphUsable = persistanceState.isMorphBallEquipped() && persistanceState.isMorphBallUnlocked();
		System.out.println("On Persistance Change: " + isMorphUsable);
	}
	
	//Temp method to set stance of charactor, will be changed with animation impl.
	public void setStance(int newStance){
		if(newStance < 0 || newStance > 2){
			System.err.println("Attempt to set invalid CharactorState");
			return;
		}
		
		if(newStance != 2){
			stance = newStance;
		} else {
			if(isMorphUsable)
				stance = newStance;
		}
	}
	
	//Temp helper method for base char state
	private String getStanceAsString(){
		String str = "";
		if(stance == 0)
			str = "STANCE = STANDING";
		else if(stance == 1)
			str = "STANCE = CROUCHING";
		else 
			str = "STANCE = MORPHBALL";
		return str;
	}
}
