package com.gearworks.kraken;

//Provide a basic implementation of StateManager
public class StateManager {
	private static StateManager sm; 
	
	public StateManager Get(){
		if(sm == null)
			sm = new StateManager();
		return sm;
	}
	
	public StateManager(){
	}
}
