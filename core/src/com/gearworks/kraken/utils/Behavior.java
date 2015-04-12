package com.gearworks.kraken.utils;

public interface Behavior {
	/*
	 * Called once when the behavior is attached to its parent, should set this.parent = parent
	 */
	public void onAttach(Object parent)
	/*
	 * Called once when the behavior is detached from its parent, should set parent to null
	 */
	public void onDetach();
	/*
	 * Called at STEP rate
	 */
	public void update();
	/*
	 * Get object this Behavior is attached to
	 */
	public Object getParent();
}
