package com.github.linggify.attic.lwjgl;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;

import com.github.linggify.attic.IBackend;
import com.github.linggify.attic.IFileManager;
import com.github.linggify.attic.IWindow;

/**
 * LwjglBackend is the provider for all Lwjgl specific interfaces
 * 
 * @author Freddy
 *
 */
public class LwjglBackend implements IBackend {

	private LwjglFileManager mFileManager;
	
	/**
	 * Creates a new {@link LwjglBackend}
	 */
	public LwjglBackend() {
		mFileManager = new LwjglFileManager();
	}
	
	@Override
	public IWindow createWindow() {
		return new LwjglWindow();
	}

	@Override
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("GLFW could not be initialized");
	}

	@Override
	public void destroy() {
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	@Override
	public IFileManager getFileManager() {
		return mFileManager;
	}
}
