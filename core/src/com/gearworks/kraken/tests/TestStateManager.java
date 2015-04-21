package com.gearworks.kraken.tests;

import com.gearworks.kraken.StateManager;

public class TestStateManager {
	
	public static void runTests(){
		SceneTest sceneTest = new SceneTest();
		
		StateManager.attach(sceneTest);
	}
	
}
