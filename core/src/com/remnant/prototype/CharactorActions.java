package com.remnant.prototype;

public class CharactorActions {

	static final int LEFT = 0;
	static final int LEFT_RELEASE = 1;
	
	static final int RIGHT = 2;
	static final int RIGHT_RELEASE = 3;
	
	static final int UP = 4;
	static final int UP_RELEASE = 5;
	
	static final int DOWN = 6;
	static final int DOWN_RELEASE = 7;
	
	static final int JUMP = 8;
	static final int JUMP_RELEASE = 9;
	
	static final int FIRE = 10;
	static final int FIRE_RELEASE = 11;
	
	static final int ALTFIRE = 12;
	static final int ALTFIRE_RELEASE = 13;
	
	static final int SPRINT = 14;
	static final int SPRINT_RELEASE = 15;
	
	static final int ALTLOOK = 16;
	static final int ALTLOOK_RELEASE = 17;
	
	static final int PRINT_DEBUG_LOG = 18;
	static final int UNLOCK_MORPHBALL = 19;
	
	public static String getString(int actionID){
		
		String str = "";
		
		if(actionID == 0)
			str =  "ACTION = LEFT";
		
		if(actionID == 2)
			str =  "ACTION = RIGHT";
		
		if(actionID == 4)
			str =  "ACTION = UP";
		
		if(actionID == 6)
			str =  "ACTION = DOWN";
		
		if(actionID == 8)
			str =  "ACTION = JUMP";
		
		if(actionID == 10)
			str =  "ACTION = FIRE";
		
		if(actionID == 12)
			str =  "ACTION = ALTFIRE";
		
		if(actionID == 14)
			str =  "ACTION = SPRINT";
		
		if(actionID == 16)
			str =  "ACTION = ALTLOOK";
		
		if(actionID == 18)
			str =  "ACTION = DEBUG_PRINT";
		
		return str;
	}
	
}
