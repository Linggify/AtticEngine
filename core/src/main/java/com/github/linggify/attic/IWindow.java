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
	public void setTitle(String title);
	
	/**
	 * @return the title of this {@link IWindow}
	 */
	public String getTitle();
	
	/**
	 * Sets the icon of this {@link IWindow}
	 * @param file
	 */
	public void setIcon(File file);
	
	/**
	 * Sets the dimensions of the {@link IWindow}
	 * @param width
	 * @param height
	 */
	public void setDimensions(int width, int height);
	
	/**
	 * @return the dimensions of this Window
	 */
	public int[] getDimensions();
	
	/**
	 * Sets the position of this {@link IWindow} on the screen
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y);
	
	/**
	 * @return the position of this {@link IWindow}
	 */
	public int[] getPosition();
	
	/**
	 * Sets this {@link IWindow} to fullscreen
	 * @param flag
	 */
	public void setFullScreen(boolean flag);
	
	/**
	 * @return whether this {@link IWindow} is in fullscreen or not
	 */
	public boolean isFullScreen();
	
	/**
	 * Enables V-Sync on this {@link IWindow}
	 * @param flag
	 */
	public void enableVSync(boolean flag);
	
	/**
	 * @return whether V-Sync is enabled in this {@link IWindow}
	 */
	public boolean vSyncEnabled();
	
	/**
	 * Sets whether this {@link IWindow} is resizable or not
	 * @param flag
	 */
	public void setResizable(boolean flag);
	
	/**
	 * @return whether this {@link IWindow} is resizable or not
	 */
	public boolean isResizable();
	
	/**
	 * Sets whether this {@link IWindow} should be closed
	 * @param flag
	 */
	public void setShouldClose(boolean flag);
	
	/**
	 * 
	 * @return whether this {@link IWindow} should be closed
	 */
	public boolean shouldClose();
	
	/**
	 * 
	 * @return a {@link IContext} for this {@link IWindow}, if not yet existing, creates one
	 */
	public IContext getContext();
	
	/**
	 * Shows this {@link IWindow}
	 */
	public void show();
	
	/**
	 * Hides this {@link IWindow}
	 */
	public void hide();
	
	/**
	 * Destroys the {@link IWindow}
	 */
	public void destroy();
	
	/**
	 * Centers this {@link IWindow} on screen
	 */
	public void center();
	
	/**
	 * Called after rendering to update the {@link IWindow}
	 * and poll events
	 */
	public void postRender();
	
	//TODO Methods for Keyboard-Events
	//TODO Methods for Mouse-Events
	
	//TODO Methods for Controller-Events
}
