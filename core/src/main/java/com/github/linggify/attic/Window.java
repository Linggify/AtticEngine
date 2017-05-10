package com.github.linggify.attic;

import java.io.File;

import com.github.linggify.attic.render.Context;

/**
 * A Window represents and manages a window on the screen
 * @author Fredie
 *
 */
public interface Window {

	/**
	 * Sets the title of this {@link Window}
	 * @param title
	 */
	public void setTitle(String title);
	
	/**
	 * @return the title of this {@link Window}
	 */
	public String getTitle();
	
	/**
	 * Sets the icon of this {@link Window}
	 * @param file
	 */
	public void setIcon(File file);
	
	/**
	 * Sets the dimensions of the {@link Window}
	 * @param width
	 * @param height
	 */
	public void setDimensions(int width, int height);
	
	/**
	 * @return the dimensions of this Window
	 */
	public int[] getDimensions();
	
	/**
	 * Sets the position of this {@link Window} on the screen
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y);
	
	/**
	 * @return the position of this {@link Window}
	 */
	public int[] getPosition();
	
	/**
	 * Sets this {@link Window} to fullscreen
	 * @param flag
	 */
	public void setFullScreen(boolean flag);
	
	/**
	 * @return whether this {@link Window} is in fullscreen or not
	 */
	public boolean isFullScreen();
	
	/**
	 * Enables V-Sync on this {@link Window}
	 * @param flag
	 */
	public void enableVSync(boolean flag);
	
	/**
	 * @return whether V-Sync is enabled in this {@link Window}
	 */
	public boolean vSyncEnabled();
	
	/**
	 * Sets whether this {@link Window} is resizable or not
	 * @param flag
	 */
	public void setResizable(boolean flag);
	
	/**
	 * @return whether this {@link Window} is resizable or not
	 */
	public boolean isResizable();
	
	/**
	 * Sets whether this {@link Window} should be closed
	 * @param flag
	 */
	public void setShouldClose(boolean flag);
	
	/**
	 * 
	 * @return whether this {@link Window} should be closed
	 */
	public boolean shouldClose();
	
	/**
	 * 
	 * @return a {@link Context} for this {@link Window}, if not yet existing, creates one
	 */
	public Context getContext();
	
	/**
	 * Shows this {@link Window}
	 */
	public void show();
	
	/**
	 * Hides this {@link Window}
	 */
	public void hide();
	
	/**
	 * Destroys the {@link Window}
	 */
	public void destroy();
	
	/**
	 * Centers this {@link Window} on screen
	 */
	public void center();
	
	/**
	 * Called after rendering to update the {@link Window}
	 * and poll events
	 */
	public void postRender();
	
	//TODO Methods for Keyboard-Events
	//TODO Methods for Mouse-Events
	
	//TODO Methods for Controller-Events
}
