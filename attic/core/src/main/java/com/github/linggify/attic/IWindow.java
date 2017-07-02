package com.github.linggify.attic;

import java.io.File;

import com.github.linggify.attic.render.IContext;

/**
 * A Window represents and manages a window on the screen
 * @author Fredie
 *
 */
public interface IWindow {

	/**
	 * Sets the title of this {@link IWindow}
	 * @param title
	 */
	void setTitle(String title);
	
	/**
	 * @return the title of this {@link IWindow}
	 */
	String getTitle();
	
	/**
	 * Sets the icon of this {@link IWindow}
	 * @param file
	 */
	void setIcon(File file);
	
	/**
	 * Sets the dimensions of the {@link IWindow}
	 * @param width
	 * @param height
	 */
	void setDimensions(int width, int height);
	
	/**
	 * @return the dimensions of this Window
	 */
	int[] getDimensions();
	
	/**
	 * Sets the position of this {@link IWindow} on the screen
	 * @param x
	 * @param y
	 */
	void setPosition(int x, int y);
	
	/**
	 * @return the position of this {@link IWindow}
	 */
	int[] getPosition();
	
	/**
	 * Sets this {@link IWindow} to fullscreen
	 * @param flag
	 */
	void setFullScreen(boolean flag);
	
	/**
	 * @return whether this {@link IWindow} is in fullscreen or not
	 */
	boolean isFullScreen();
	
	/**
	 * Enables V-Sync on this {@link IWindow}
	 * @param flag
	 */
	void enableVSync(boolean flag);
	
	/**
	 * @return whether V-Sync is enabled in this {@link IWindow}
	 */
	boolean vSyncEnabled();
	
	/**
	 * Sets whether this {@link IWindow} is resizable or not
	 * @param flag
	 */
	void setResizable(boolean flag);
	
	/**
	 * @return whether this {@link IWindow} is resizable or not
	 */
	boolean isResizable();
	
	/**
	 * Sets whether this {@link IWindow} should be closed
	 * @param flag
	 */
	void setShouldClose(boolean flag);
	
	/**
	 * 
	 * @return whether this {@link IWindow} should be closed
	 */
	boolean shouldClose();
	
	/**
	 * 
	 * @return a {@link IContext} for this {@link IWindow}, if not yet existing, creates one
	 */
	IContext getContext();
	
	/**
	 * Shows this {@link IWindow}
	 */
	void show();
	
	/**
	 * Hides this {@link IWindow}
	 */
	void hide();
	
	/**
	 * Destroys the {@link IWindow}
	 */
	void destroy();
	
	/**
	 * Centers this {@link IWindow} on screen
	 */
	void center();
	
	/**
	 * Called after rendering to update the {@link IWindow}
	 * and poll events
	 */
	void postRender();
	
	//TODO Methods for Keyboard-Events
	//TODO Methods for Mouse-Events
	
	//TODO Methods for Controller-Events
}
