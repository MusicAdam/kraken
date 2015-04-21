package com.gearworks.kraken.tests;

import com.gearworks.kraken.utils.Behavior;
import com.gearworks.kraken.utils.State;

public class TestState implements State{
	private TestState previousState;
	protected boolean done = false; 
	
	@Override
	public void onEnter() {
	}

	@Override
	public void onExit() {
	}

	@Override
	public boolean canEnter() {	
		return (previousState == null || previousState.isDone());
	}

	@Override
	public boolean canExit() {
		return done;
	}

	@Override
	public Behavior attachBehavior(Behavior b) {
		return null;
	}

	@Override
	public Behavior detachBehavior(Behavior b) {
		return null;
	}

	@Override
	public void update() {		
	}
	
	public void setPreviousState(TestState state){
		previousState = state;
	}
	
	public boolean isDone(){
		return done;
	}

}
