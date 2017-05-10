package com.github.linggify.attic.lwjgl;

import java.io.File;

import org.lwjgl.glfw.GLFWVidMode;

import com.github.linggify.attic.Window;
import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Context;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The {@link Window} of the {@link LwjglBackend}
 * 
 * Destroys and re-creates itself if it is set to fullscreen or if changes to resizability are done
 * 
 * @author Fredie
 *
 */
public class LwjglWindow implements Window {

	private long mWindowHandle;
	private long mMonitor;

	private LwjglContext mContext;
	
	private boolean mVisible;
	private String mTitle;
	private File mFile;
	private int[] mDimensions;
	private int[] mPosition;
	private boolean mIsFullScreen;
	private boolean mEnableVSync;
	private boolean mIsResizable;
	
	public LwjglWindow() {
		mMonitor = glfwGetPrimaryMonitor();
		mWindowHandle = NULL;
		
		mVisible = false;
		mTitle = "Attic-Lwjgl";
		mFile = null;
		mDimensions = new int[]{600, 400};
		mPosition = new int[]{0, 0};
		mIsFullScreen = false;
		mEnableVSync = false;
		mIsResizable = false;
	}
	
	@Override
	public void setTitle(String title) {
		mTitle = title;
		
		if(mWindowHandle != NULL) {
			glfwSetWindowTitle(mWindowHandle, mTitle);
		}
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	@Override
	public void setIcon(File file) {
		mFile = file;
		
		if(mWindowHandle != NULL && mFile != null) {
			//TODO set icon
		}
	}

	@Override
	public void setDimensions(int width, int height) {
		mDimensions = new int[]{width, height};
		
		if(mWindowHandle != NULL) {
			glfwSetWindowSize(mWindowHandle, mDimensions[0], mDimensions[1]);
		}
	}

	@Override
	public int[] getDimensions() {
		return mDimensions;
	}

	@Override
	public void setPosition(int x, int y) {
		mPosition = new int[]{x, y};
		
		if(mWindowHandle != NULL) {
			glfwSetWindowPos(mWindowHandle, mPosition[0], mPosition[1]);
		}
	}

	@Override
	public int[] getPosition() {
		return mPosition;
	}

	@Override
	public void setFullScreen(boolean flag) {
		mIsFullScreen = flag;
		
		if(mWindowHandle != NULL) {
			//TODO set window fullscreen
		}
	}

	@Override
	public boolean isFullScreen() {
		return mIsFullScreen;
	}

	@Override
	public void enableVSync(boolean flag) {
		mEnableVSync = flag;
		
		if(mWindowHandle != NULL) {
			glfwSwapInterval(mEnableVSync ? 1 : 0);
		}
	}

	@Override
	public boolean vSyncEnabled() {
		return mEnableVSync;
	}

	@Override
	public void setResizable(boolean flag) {
		mIsResizable = flag;
		
		if(mWindowHandle != NULL) {
			//TODO toggle window resizable
		}
	}

	@Override
	public boolean isResizable() {
		return mIsResizable;
	}

	@Override
	public void setShouldClose(boolean flag) {
		glfwSetWindowShouldClose(mWindowHandle, flag);
	}

	@Override
	public boolean shouldClose() {
		return glfwWindowShouldClose(mWindowHandle);
	}
	
	@Override
	public Context getContext() {
		if(mContext == null) {
			if(mWindowHandle == NULL) {
				glfwDefaultWindowHints();
				glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
				glfwWindowHint(GLFW_RESIZABLE, mIsResizable ? GLFW_TRUE : GLFW_FALSE);
				
				//Create the window
				mWindowHandle = glfwCreateWindow(mDimensions[0], mDimensions[1], mTitle, mIsFullScreen ? mMonitor : NULL, NULL);
				if(mWindowHandle == NULL) throw new AtticRuntimeException("Failed to create Window");
				
				glfwSetWindowPos(mWindowHandle,	mPosition[0], mPosition[1]);
				glfwMakeContextCurrent(mWindowHandle);
				glfwSwapInterval(mEnableVSync ? 1 : 0);
			}
			
			mContext = new LwjglContext();
			mContext.setHandle(mWindowHandle);
			mContext.makeCurrent();
		}
		
		return mContext;
	}
	
	@Override
	public void show() {
		//if the window is already visible, do nothing
		if(mVisible) return;
		
		if(mWindowHandle == NULL) {
			glfwDefaultWindowHints();
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
			glfwWindowHint(GLFW_RESIZABLE, mIsResizable ? GLFW_TRUE : GLFW_FALSE);
			
			//Create the window
			mWindowHandle = glfwCreateWindow(mDimensions[0], mDimensions[1], mTitle, mIsFullScreen ? mMonitor : NULL, NULL);
			if(mWindowHandle == NULL) throw new AtticRuntimeException("Failed to create Window");
			
			glfwSetWindowPos(mWindowHandle,	mPosition[0], mPosition[1]);
			glfwMakeContextCurrent(mWindowHandle);
			glfwSwapInterval(mEnableVSync ? 1 : 0);
		}
		
		mVisible = true;
		glfwShowWindow(mWindowHandle);
	}

	@Override
	public void hide() {
		//if window is not visible, do not do anything
		if(!mVisible) return;
		
		//window is always existent at this point
		glfwHideWindow(mWindowHandle);
	}

	@Override
	public void center() {
		//if window is fullscreen, do not center.
		if(!mIsFullScreen) {
			GLFWVidMode mode = glfwGetVideoMode(mMonitor);
			mPosition[0] = (mode.width() - mDimensions[0]) / 2;
			mPosition[1] = (mode.height() - mDimensions[1]) / 2;
		}
		
		if(mWindowHandle != NULL) {
			glfwSetWindowPos(mWindowHandle,	mPosition[0], mPosition[1]);
		}
	}

	@Override
	public void destroy() {
		if(mWindowHandle != NULL) {
			glfwDestroyWindow(mWindowHandle);
		}
	}
	
	@Override
	public void postRender() {
		glfwSwapBuffers(mWindowHandle);
		glfwPollEvents();
	}
}
