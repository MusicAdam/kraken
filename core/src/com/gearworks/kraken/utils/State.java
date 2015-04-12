package com.gearworks.kraken.utils;

import com.gearworks.kraken.Kraken;

public interface State {
	/*
	 * Called immediately after the state becomes active
	 */
	public void onEnter();
	/*
	 * Called immediately before the state becomes active
	 */
	public void onExit();
	/*
	 * When true, the state can become active
	 */
	public boolean canEnter();
	/*
	 * When true, the state can become deactivated
	 */
	public boolean canExit();
	/*
	 * Adds a behavior which should be updated in the update loop
	 */
	public Behavior attachBehavior(Behavior b);
	/*
	 * Detach behavior
	 */
	public Behavior detachBehavior(Behavior b);
	/*
	 * Update
	 */
	public void update(Kraken kraken);
}
