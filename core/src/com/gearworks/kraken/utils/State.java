package com.gearworks.kraken.utils;

import com.gearworks.kraken.Kraken;

public interface State {
	/*
	 * Called immediately after the state becomes active
	 */
	public void onEnter(Kraken kraken);
	/*
	 * Called immediately before the state becomes active
	 */
	public void onExit(Kraken kraken);
	/*
	 * When true, the state can become active
	 */
	public boolean canEnter(Kraken kraken);
	/*
	 * When true, the state can become deactivated
	 */
	public boolean canExit(Kraken kraken);
	public Behavior attachkBehavior(Behavior b);
	public Behavior detachBehavior();
	public void update();
}
