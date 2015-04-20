package com.gearworks.kraken.tests;

import com.gearworks.kraken.Kraken;
import com.gearworks.kraken.utils.Behavior;
import com.gearworks.kraken.utils.State;

public class SceneTest implements State{

	@Override
	public void onEnter() {
		System.out.println("Enter scene test");
	}

	@Override
	public void onExit() {
		System.out.println("Exit scene test");		
	}

	@Override
	public boolean canEnter() {
		return true;
	}

	@Override
	public boolean canExit() {
		return true;
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
		System.out.println("Update scene test");			
	}
}
