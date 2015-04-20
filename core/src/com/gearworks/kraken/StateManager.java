package com.gearworks.kraken;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.gearworks.kraken.utils.State;

//Provide a basic implementation of StateManager
public class StateManager {
	private static StateManager sm; 
	
	public static void init(){
		sm = new StateManager();
	}
	
	private class StateWrapper{
		public StateWrapper(State s){
			state = s;
			entered = false;
		}
		public State state;
		public boolean entered;
		
		@Override
		public boolean equals(Object obj){
			if(obj == null || !(obj instanceof StateWrapper)) return false;
			StateWrapper sw = (StateWrapper)obj;
			return sw.state == state;
		}
	}
	
	private Array<StateWrapper> states;
	private State current;
	
	public StateManager(){
		states = new Array<StateWrapper>();
	}
	
	public static void update(){
		Iterator<StateWrapper> it = sm.states.iterator();
		while(it.hasNext()){
			StateWrapper stateWrapper = it.next();
			State state = stateWrapper.state;
			if(!stateWrapper.entered && state.canEnter() && sm.current == null){
				state.onEnter();
				sm.current = state;
			}
			
			sm.current.update();
			
			if(sm.current.canExit()){
				sm.current.onExit();
				it.remove();
				sm.current = null;
			}
		}
	}
	
	public static State attach(State state){
		sm.states.add(sm.new StateWrapper(state));
		return state;
	}
	
	public static State detach(State state){
		if(sm.states.removeValue(sm.new StateWrapper(state), false))
			return state;
		return null;
	}
}
