package com.github.linggify.attic.lwjgl;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;

import com.github.linggify.attic.Backend;
import com.github.linggify.attic.FileManager;
import com.github.linggify.attic.Window;

/**
 * LwjglBackend is the provider for all Lwjgl specific interfaces
 * 
 * @author Freddy
 *
 */
public class LwjglBackend implements Backend {

	private LwjglFileManager mFileManager;
	
	/**
	 * Creates a new {@link LwjglBackend}
	 */
	public LwjglBackend() {
		mFileManager = new LwjglFileManager();
	}
	
	@Override
	public Window createWindow() {
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
	public FileManager getFileManager() {
		return mFileManager;
	}
}
