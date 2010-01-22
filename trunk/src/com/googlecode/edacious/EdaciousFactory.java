package com.googlecode.edacious;

/**
 * Factory for getting an implementation instance of Edacious.
 *
 * @author jon
 */
public class EdaciousFactory
{
	private static boolean DEBUG = false;
	private static Edacious instance = null;

	/** */
	public static Edacious getInstance()
	{
		if (instance == null)
			instance = new EdaciousImpl(DEBUG);
		return instance;
	}

	/** */
	public static void setDebug(boolean debug)
	{
		DEBUG = debug;
	}
}
