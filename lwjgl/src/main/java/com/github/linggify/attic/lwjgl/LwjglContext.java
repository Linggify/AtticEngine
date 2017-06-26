package com.github.linggify.attic.lwjgl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Batch;
import com.github.linggify.attic.render.Context;
import com.github.linggify.attic.render.Context.TextureFormat;
import com.github.linggify.attic.util.Color;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Pair;
import com.github.linggify.attic.util.Vector2D;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * The LwjglContext is used in {@link LwjglWindow} be able to recreate data if the
 * OpenGL-Context is lost
 * 
 * @author Fredie
 *
 */
public class LwjglContext implements Context {
	
	/**
	 * All types of Resources
	 * 
	 * @author Fredie
	 *
	 */
	private enum Resource {
		TEXTURE, SHADER
	}

	// the window handle
	private long mHandle;

	private String mLog;

	// the source data of the resources
	private List<Object> mData;

	// the resources themselves
	private List<Pair<Integer, Resource>> mResources;
	private List<Integer> mFreeIds;

	// currently used handles
	private int mCurrentProgram;

	// global framebuffer
	private int mFramebufferHandle;
	private int[] mActiveRenderTargets;
	private Color[] mTargetClearColors;
	private Color mClearColor;

	private final int[] mAllRenderTargets = new int[] { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1,
			GL_COLOR_ATTACHMENT2, GL_COLOR_ATTACHMENT3, GL_COLOR_ATTACHMENT4, GL_COLOR_ATTACHMENT5,
			GL_COLOR_ATTACHMENT6, GL_COLOR_ATTACHMENT7, GL_COLOR_ATTACHMENT8, GL_COLOR_ATTACHMENT9,
			GL_COLOR_ATTACHMENT10, GL_COLOR_ATTACHMENT11, GL_COLOR_ATTACHMENT12, GL_COLOR_ATTACHMENT13,
			GL_COLOR_ATTACHMENT14, GL_COLOR_ATTACHMENT15, GL_COLOR_ATTACHMENT16, GL_COLOR_ATTACHMENT17,
			GL_COLOR_ATTACHMENT18, GL_COLOR_ATTACHMENT19, GL_COLOR_ATTACHMENT20, GL_COLOR_ATTACHMENT21,
			GL_COLOR_ATTACHMENT22, GL_COLOR_ATTACHMENT23, GL_COLOR_ATTACHMENT24, GL_COLOR_ATTACHMENT25,
			GL_COLOR_ATTACHMENT26, GL_COLOR_ATTACHMENT27, GL_COLOR_ATTACHMENT28, GL_COLOR_ATTACHMENT29,
			GL_COLOR_ATTACHMENT30, GL_COLOR_ATTACHMENT31 };

	/**
	 * Create a new {@link LwjglContext} this Context can only be used after the
	 * window handle is set
	 */
	public LwjglContext() {
		GL.createCapabilities();
		
		mData = new ArrayList<>();
		mResources = new ArrayList<>();
		mFreeIds = new LinkedList<>();
		mHandle = NULL;

		mLog = "";
		mCurrentProgram = 0;

		// occupy id 0
		mData.add(null);
		mResources.add(null);

		// create framebuffer
		mActiveRenderTargets = new int[glGetIntegeri(GL_MAX_COLOR_ATTACHMENTS, 0)];
		mFramebufferHandle = glGenFramebuffers();
		if (mFramebufferHandle == 0)
			throw new AtticRuntimeException("Could not create framebuffer");

		mClearColor = new Color();
		mTargetClearColors = new Color[mActiveRenderTargets.length];
		for (int i = 0; i < mTargetClearColors.length; i++)
			mTargetClearColors[i] = new Color();
	}

	/**
	 * Checks whether the given handle exists and if it is of the given resource
	 * 
	 * @param handle
	 * @param resource
	 * @return whether the handle is valid or not
	 */
	private boolean checkHandle(int handle, Resource resource) {
		// if the handle is 0 its always valid
		if (handle == 0)
			return true;

		// if the handle does not exist, do nothing
		if (handle >= mResources.size() || mResources.get(handle) == null)
			return false;

		// if this is not a texture, do nothing
		if (mResources.get(handle).getValue() != resource)
			return false;

		return true;
	}

	/**
	 * Sets the native handle of this {@link LwjglContext}
	 * 
	 * @param handle
	 */
	public void setHandle(long handle) {
		if (mHandle != NULL) {
			// TODO reload resources
		}

		mHandle = handle;
	}

	/**
	 * 
	 * @return the native handle of this {@link LwjglContext}
	 */
	public long getHandle() {
		return mHandle;
	}

	/**
	 * Makes this {@link LwjglContext} the current one
	 */
	public void makeCurrent() {
		glfwMakeContextCurrent(mHandle);
	}

	@Override
	public String getLog() {
		String tmp = mLog;
		mLog = "";

		return tmp;
	}

	@Override
	public int getMaxRenderTargets() {
		return mActiveRenderTargets.length;
	}

	@Override
	public void setClearColor(int target, Color color) {
		if (target < -1 || target >= mTargetClearColors.length)
			throw new AtticRuntimeException("Illegal rendertarget " + target);

		if(target == -1)
			mClearColor = color;
		else
			mTargetClearColors[target] = color;
	}

	@Override
	public void clearRenderTargets() {
		boolean cleared = false;
		glBindFramebuffer(GL_FRAMEBUFFER, mFramebufferHandle);
		for (int i = 0; i < mActiveRenderTargets.length; i++) {
			if (mActiveRenderTargets[i] != 0) {
				Color c = mTargetClearColors[i];
				glClearColor((float) c.red() / 256f, (float) c.green() / 256f, (float) c.blue() / 256f,
						(float) c.alpha() / 256f);
				glDrawBuffer(mAllRenderTargets[i]);
				glClear(GL_COLOR_BUFFER_BIT);
				cleared = true;
			}
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		if (!cleared) {
			glClearColor(mClearColor.red(), mClearColor.green(), mClearColor.blue(), mClearColor.alpha());
			glClear(GL_COLOR_BUFFER_BIT);
		}
	}

	@Override
	public int newTexture() {
		int handle = glGenTextures();
		if (handle == 0)
			return 0;

		Pair<Integer, Resource> resource = new Pair<>(handle, Resource.TEXTURE);
		Object source = new TextureData(0, 0, TextureFormat.RGBA_8888);

		int id;
		if (!mFreeIds.isEmpty()) {
			id = mFreeIds.remove(0);
			mResources.set(id, resource);
			mData.set(id, source);
		} else {
			id = mResources.size();
			mResources.add(resource);
			mData.add(source);
		}

		return id;
	}

	@Override
	public void destroyTexture(int handle) {
		if (!checkHandle(handle, Resource.TEXTURE))
			throw new AtticRuntimeException("Invalid texture-handle " + handle);

		if (handle != 0) {
			int realHandle = mResources.get(handle).getKey();
			mResources.set(handle, null);
			mData.set(handle, null);
			glDeleteTextures(realHandle);
		}
	}

	@Override
	public void setPixelData(int handle, int width, int height, TextureFormat format, byte[] data) {
		if (!checkHandle(handle, Resource.TEXTURE))
			throw new AtticRuntimeException("Invalid texture-handle " + handle);

		if (handle != 0) {
			int rformat = 0;
			int tformat = 0;
			switch (format) {
			case RGBA_8888:
				rformat = GL_RGBA;
				tformat = GL_UNSIGNED_BYTE;
				break;
			case RGBA_FFFF:
				rformat = GL_RGBA;
				tformat = GL_UNSIGNED_SHORT;
				break;
			case RGB_888:
				rformat = GL_RGB;
				tformat = GL_UNSIGNED_BYTE;
				break;
			case RGB_FFF:
				rformat = GL_RGB;
				tformat = GL_UNSIGNED_SHORT;
				break;
			default:
			}

			bindTexture(0, handle);
			glActiveTexture(GL_TEXTURE0);

			TextureData tdata = (TextureData) mData.get(handle);

			ByteBuffer buffer = MemoryUtil.memAlloc(data.length);
			buffer.put(data);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, rformat, tformat, buffer);
			tdata.setPixels(data);
			tdata.setFormat(format);
			tdata.setWidth(width);
			tdata.setHeight(height);

			unbindTexture(0);
		}
	}

	@Override
	public void setTextureFilter(int handle, TextureFilter minfilter, TextureFilter magfilter) {
		bindTexture(0, handle);
		int minf = minfilter == TextureFilter.LINEAR ? GL_LINEAR : GL_NEAREST;
		int magf = magfilter == TextureFilter.LINEAR ? GL_LINEAR : GL_NEAREST;

		int rHandle = mResources.get(handle).getKey();
		glTexParameteri(rHandle, GL_TEXTURE_MIN_FILTER, minf);
		glTexParameteri(rHandle, GL_TEXTURE_MAG_FILTER, magf);
		unbindTexture(0);
	}

	@Override
	public boolean bindTexture(int unit, int handle) {
		if (!checkHandle(handle, Resource.TEXTURE))
			throw new AtticRuntimeException("Invalid texture-handle " + handle);

		if (handle != 0) {
			int realHandle = mResources.get(handle).getKey();
			glActiveTexture(unit);
			glBindTexture(GL_TEXTURE_2D, realHandle);
		} else
			unbindTexture(unit);

		return true;
	}

	@Override
	public void unbindTexture(int unit) {
		glActiveTexture(unit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	@Override
	public boolean bindRenderTarget(int target, int handle) {
		if (target < 0 || target >= mActiveRenderTargets.length)
			throw new AtticRuntimeException("Invalid render target " + target);

		if (!checkHandle(handle, Resource.TEXTURE))
			throw new AtticRuntimeException("Invalid texture-handle " + handle);

		glBindFramebuffer(GL_FRAMEBUFFER, mFramebufferHandle);
		if (handle != 0) {
			int rHandle = mResources.get(handle).getKey();
			glFramebufferTexture2D(GL_FRAMEBUFFER, mAllRenderTargets[target], GL_TEXTURE_2D, rHandle, 0);
		} else {
			glFramebufferTexture2D(GL_FRAMEBUFFER, mAllRenderTargets[target], GL_TEXTURE_2D, 0, 0);
		}
		mActiveRenderTargets[target] = handle;
		glBindFramebuffer(GL_FRAMEBUFFER, mFramebufferHandle);

		return true;
	}

	@Override
	public void unbindRenderTarget(int target) {
		bindRenderTarget(target, 0);
	}

	@Override
	public int genShader(String vertexshader, String fragmentshader) {
		// create handles
		int nHandle = glCreateProgram();
		if (nHandle == 0) {
			mLog += "Failed to create shader\n";
			return 0;
		}

		int vertexhandle = glCreateShader(GL_VERTEX_SHADER);
		if (vertexhandle == 0) {
			mLog += "Failed to create vertex-shader\n";
			glDeleteProgram(nHandle);
			return 0;
		}

		int fragmenthandle = glCreateShader(GL_FRAGMENT_SHADER);
		if (fragmenthandle == 0) {
			mLog += "Failed to create fragment-shader\n";
			glDeleteProgram(nHandle);
			glDeleteShader(vertexhandle);
			return 0;
		}

		// attach source and compile
		glShaderSource(vertexhandle, vertexshader);
		glCompileShader(vertexhandle);
		String vLog = glGetShaderInfoLog(vertexhandle);
		if (vLog != "") {
			mLog += vLog;
			glDeleteShader(vertexhandle);
			glDeleteShader(fragmenthandle);
			glDeleteProgram(nHandle);
			return 0;
		}

		glShaderSource(fragmenthandle, fragmentshader);
		glCompileShader(fragmenthandle);
		String fLog = glGetShaderInfoLog(vertexhandle);
		if (fLog != "") {
			mLog += fLog;
			glDeleteShader(vertexhandle);
			glDeleteShader(fragmenthandle);
			glDeleteProgram(nHandle);
			return 0;
		}

		// link program and validate
		glAttachShader(nHandle, vertexhandle);
		glAttachShader(nHandle, fragmenthandle);
		glLinkProgram(nHandle);
		glValidateProgram(nHandle);
		String pLog = glGetProgramInfoLog(nHandle);
		if (pLog != "") {
			mLog += pLog;
			glDeleteShader(vertexhandle);
			glDeleteShader(fragmenthandle);
			glDeleteProgram(nHandle);
			return 0;
		}

		// construct source container for later recreation
		ShaderData data = new ShaderData(vertexhandle, fragmenthandle);
		data.setShaderSource(vertexshader, fragmentshader);

		int id = 0;
		if (!mFreeIds.isEmpty()) {
			id = mFreeIds.remove(0);
			mData.set(id, data);
			mResources.set(id, new Pair<>(nHandle, Resource.SHADER));
		} else {
			id = mResources.size();
			mData.add(data);
			mResources.add(new Pair<>(nHandle, Resource.SHADER));
		}

		return id;
	}

	@Override
	public void destroyShader(int handle) {
		if (!checkHandle(handle, Resource.SHADER))
			throw new AtticRuntimeException("Invalid shader-handle " + handle);

		int rHandle = mResources.get(handle).getKey();
		ShaderData data = (ShaderData) mData.get(handle);

		glDeleteProgram(rHandle);
		glDeleteShader(data.fragmentHandle());
		glDeleteShader(data.vertexHandle());

		mResources.set(handle, null);
		mData.set(handle, null);
		mFreeIds.add(handle);
	}

	@Override
	public boolean bindShader(int handle) {
		if (!checkHandle(handle, Resource.SHADER))
			return false;

		if (handle != 0)
			glUseProgram(mResources.get(handle).getKey());
		else
			glUseProgram(0);

		mCurrentProgram = handle;
		return true;
	}

	@Override
	public void unbindShader() {
		bindShader(0);
	}

	@Override
	public Map<String, Integer> getUniforms(int handle) {
		Map<String, Integer> result = new HashMap<>();

		int rHandle = mResources.get(handle).getKey();
		int[] uCount = new int[1];
		glGetProgramiv(rHandle, GL_ACTIVE_UNIFORMS, uCount);

		IntBuffer tmpSize = MemoryUtil.memAllocInt(1);
		IntBuffer tmpType = MemoryUtil.memAllocInt(1);

		for (int i = 0; i < uCount[0]; i++) {
			String name = glGetActiveUniform(rHandle, i, tmpSize, tmpType);
			int id = glGetUniformLocation(rHandle, name);

			result.put(name, id);
		}

		return result;
	}

	@Override
	public int getUniformLocation(String name) {
		if (mCurrentProgram > 0)
			return glGetUniformLocation(mResources.get(mCurrentProgram).getKey(), name);
		return -1;
	}

	@Override
	public boolean setUniformInt(int location, int value) {
		if (mCurrentProgram == 0)
			return false;

		glUniform1i(location, value);
		return true;
	}

	@Override
	public boolean setUniformDouble(int location, double value) {
		if (mCurrentProgram == 0)
			return false;

		glUniform1f(location, (float) value);
		return true;
	}

	@Override
	public boolean setUniformTexture(int location, int texture) {
		if (!checkHandle(texture, Resource.TEXTURE))
			throw new AtticRuntimeException("Invalid texture-handle " + texture);

		if (mCurrentProgram == 0)
			return false;

		glUniform1i(location, mResources.get(texture).getKey());
		return true;
	}

	@Override
	public boolean setUniformMatrix(int location, Matrix33 matrix) {
		if (mCurrentProgram == 0)
			return false;

		glUniformMatrix3fv(location, false, matrix.getValues());
		return true;
	}

	@Override
	public boolean setUniformVector(int location, Vector2D vector) {
		if (mCurrentProgram == 0)
			return false;

		glUniform2f(location, vector.getX(), vector.getY());
		return true;
	}

	@Override
	public boolean setAttribute(String name, int value) {
		if (mCurrentProgram == 0)
			return false;
		glBindAttribLocation(mResources.get(mCurrentProgram).getKey(), value, name);
		return true;
	}

	@Override
	public Batch genBatch(boolean isStatic, VertexAttribute... attributes) {
		// create a batch holding up to 2^16 vertices
		return new LwjglBatch(65536, isStatic, attributes);
	}

	@Override
	public void destroyBatch(Batch batch) {
		LwjglBatch lbatch = (LwjglBatch) batch;
		lbatch.destroy();
	}

	@Override
	public boolean renderBatch(Batch batch) {
		LwjglBatch lbatch = (LwjglBatch) batch;
		lbatch.render(this);
		return true;
	}

	/**
	 * Stores data about a texture
	 * 
	 * @author Fredie
	 *
	 */
	private class TextureData {

		private int[] mDims;
		private Context.TextureFormat mFormat;
		private byte[] mPixels;

		/**
		 * Creates a new {@link TextureData} with the given data
		 * 
		 * @param width
		 * @param height
		 * @param format
		 */
		TextureData(int width, int height, Context.TextureFormat format) {
			mDims = new int[] { width, height };
			mFormat = format;
			mPixels = null;
		}

		/**
		 * Sets the width of the texture
		 * 
		 * @param width
		 */
		void setWidth(int width) {
			mDims[0] = width;
		}

		/**
		 * Sets the height of the texture
		 * 
		 * @param height
		 */
		void setHeight(int height) {
			mDims[1] = height;
		}

		/**
		 * Sets the format of the texture
		 * 
		 * @param format
		 */
		void setFormat(TextureFormat format) {
			mFormat = format;
		}

		/**
		 * Sets the pixeldata of the texture
		 * 
		 * @param data
		 */
		void setPixels(byte[] data) {
			mPixels = data;
		}

		/**
		 * 
		 * @return the width of the texture
		 */
		int width() {
			return mDims[0];
		}

		/**
		 * 
		 * @return the height if the texture
		 */
		int height() {
			return mDims[1];
		}

		/**
		 * 
		 * @return the {@link TextureFormat} of the texture
		 */
		Context.TextureFormat format() {
			return mFormat;
		}

		/**
		 * 
		 * @return the pixeldata of the texture
		 */
		byte[] pixels() {
			return mPixels;
		}
	}

	/**
	 * ShaderData contains data about a shaderprogram
	 * 
	 * @author Fredie
	 *
	 */
	private class ShaderData {

		private String mFragSource;
		private String mVertSource;

		private int[] mShaderHandles;

		/**
		 * Creates a new {@link ShaderData}
		 * 
		 * @param verthandle
		 * @param fraghandle
		 */
		ShaderData(int verthandle, int fraghandle) {
			mFragSource = null;
			mVertSource = null;

			mShaderHandles = new int[] { verthandle, fraghandle };
		}

		/**
		 * Sets the source files of the shader
		 * 
		 * @param vert
		 * @param frag
		 */
		void setShaderSource(String vert, String frag) {
			mFragSource = frag;
			mVertSource = vert;
		}

		/**
		 * Sets the shader-handles of the shader
		 * 
		 * @param verthandle
		 * @param fraghandle
		 */
		void setHandles(int verthandle, int fraghandle) {
			mShaderHandles = new int[] { verthandle, fraghandle };
		}

		/**
		 * 
		 * @return the source of the fragment shader
		 */
		String fragSource() {
			return mFragSource;
		}

		/**
		 * 
		 * @return the source of the vertex-shader
		 */
		String vertSource() {
			return mVertSource;
		}

		/**
		 * 
		 * @return the handle of the vertex-shader
		 */
		int vertexHandle() {
			return mShaderHandles[0];
		}

		/**
		 * 
		 * @return the handle of the fragment-shader
		 */
		int fragmentHandle() {
			return mShaderHandles[1];
		}
	}
}
