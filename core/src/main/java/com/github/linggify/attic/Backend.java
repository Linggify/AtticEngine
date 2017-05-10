package com.github.linggify.attic;

/**
 * A Backend provides the {@link Application} with all functionality that is hardware specific
 * @author Fredie
 *
 */
public interface Backend {

	/**
	 * Initializes the {@link Backend}
	 */
	public void init();
	
	/**
	 * Destroys all resources used by this {@link Backend} making it unusable
	 */
	public void destroy();
	
	/**
	 * 
	 * @return a {@link FileManager} to interface with the local file-system
	 */
	public FileManager getFileManager();
	
	/**
	 * Creates a new {@link Window} on the current Thread
	 * @return the Window or <code>null</code> if creation has failed
	 */
	public Window createWindow();
}
