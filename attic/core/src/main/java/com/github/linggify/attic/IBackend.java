package com.github.linggify.attic;

/**
 * A Backend provides the {@link Application} with all functionality that is hardware specific
 * @author Fredie
 *
 */
public interface IBackend {

	/**
	 * Initializes the {@link IBackend}
	 */
	void init();
	
	/**
	 * Destroys all resources used by this {@link IBackend} making it unusable
	 */
	void destroy();
	
	/**
	 * 
	 * @return a {@link IFileManager} to interface with the local file-system
	 */
	IFileManager getFileManager();
	
	/**
	 * Creates a new {@link IWindow} on the current Thread
	 * @return the Window or <code>null</code> if creation has failed
	 */
	IWindow createWindow();
}
