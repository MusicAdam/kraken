package com.gearworks.kraken.input;

public class KeyboardEvent {

	public static final int KEY_DOWN 	= 1;
	public static final int KEY_UP		= 2;
	
	public int event;
	public int key;
	
	public KeyboardEvent(int event, int key){
		this.event = event;
		this.key = key;
	}
	
	public KeyboardEvent(){
		event = 0;
		key = -1;
	}
	
	public boolean callback(){ return false; }; //Should be overriden to perform an action
	
	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		
		KeyboardEvent evt = (KeyboardEvent)other;
		if(evt == null) return false; //Eclipse is complaining this is dead code, thats not true if other is not an instance of KeyboardEvent
		
		return (event == evt.event &&
				key == evt.key);
	}
}
