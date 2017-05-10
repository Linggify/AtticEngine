package com.github.linggify.attic.render;

import java.util.Map;

import com.github.linggify.attic.util.Color;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Vector2D;

/**
 * The Context is used to create a Platform-specific interface to the
 * graphics hardware. It is also capable of reloading all of the previously loaded
 * resources in case the context is lost
 * 
 * @author Fredie
 *
 */
public interface Context {
	// GL Util

	/**
	 * Gets the current log and resets its contents
	 * 
	 * @return the current log of the underlying graphicspipeline
	 */
	public String getLog();

	/**
	 * 
	 * @return how many rendertargets are allowed on the graphics hardware
	 */
	public int getMaxRenderTargets();

	/**
	 * Clears all currently bound rendertargets, if none is bound, the default
	 * framebuffer is cleared
	 */
	public void clearRenderTargets();

	/**
	 * Sets the clear color for a given rendertarget. the maximum accepted for
	 * value target is <code>{@link #getMaxRenderTargets()} - 1</code>
	 * 
	 * use target -1 to set the clear-color for the default framebuffer
	 * 
	 * @param target
	 * @param color
	 */
	public void setClearColor(int target, Color color);

	// Textures

	/**
	 * Valid framebuffer formats
	 * 
	 * @author Fredie
	 *
	 */
	public enum TextureFormat {
		/** 3 color channels, 8 bit per channel */
		RGB_888,
		/** 3 color channels, 1 transparency channel, 8 bit per channel */
		RGBA_8888,
		/** 3 color channels, 16 bit per channel */
		RGB_FFF,
		/** 3 color channels, 1 transparancy channel, 16 bit per channel */
		RGBA_FFFF;
	}

	/**
	 * Enumeration of all standard texture-filters
	 * 
	 * @author Freddy
	 *
	 */
	public enum TextureFilter {
		/** the nearest pixel to a given texture-coordinate is chosen */
		NEAREST,
		/** interpolates between pixels, based on a given texture-coordinate */
		LINEAR
	}

	/**
	 * Generates a new Texture on the graphics-hardware
	 * 
	 * @return the handle of this texture or 0 if the generation of a texture
	 *         failed
	 */
	public int newTexture();

	/**
	 * Destroys the given texture or does nothing if no texture with the given
	 * handle exists
	 * 
	 * @param handle
	 */
	public void destroyTexture(int handle);

	/**
	 * Sets the Pixeldata for the given texture
	 * 
	 * @param handle
	 * @param data
	 */
	public void setPixelData(int handle, int width, int height, TextureFormat format, byte[] data);

	/**
	 * Sets the texture-filters for a given texture
	 * 
	 * @param minFilter
	 * @param magFilter
	 */
	public void setTextureFilter(int handle, TextureFilter minFilter, TextureFilter magFilter);

	/**
	 * Binds the given texture to the given texture unit
	 * 
	 * @param unit
	 * @param handle
	 * @return whether the bind was successful or not
	 */
	public boolean bindTexture(int unit, int handle);

	/**
	 * Unbinds any texture bound to the given texture unit. this is similar to
	 * calling {@link #bindTexture(int, int)} and using 0 as the handle
	 * 
	 * @param unit
	 */
	public void unbindTexture(int unit);

	/**
	 * binds the given texture to the given rendertarget
	 * 
	 * @param target
	 * @param handle
	 * @return whether binding the target was successful
	 */
	public boolean bindRenderTarget(int target, int handle);

	/**
	 * Frees the given rendertarget from the current binding
	 * 
	 * @param target
	 */
	public void unbindRenderTarget(int target);

	// Shaders

	/**
	 * Generates a new Shaderprogram on the graphics hardware with the given
	 * vertex- and fragmentshader code
	 * 
	 * @param vertexshader
	 * @param fragmentshader
	 * @return the handle of the Shaderprogram or 0 if no program was generated
	 */
	public int genShader(String vertexshader, String fragmentshader);

	/**
	 * Destroys the given Shader and all its attached resources or does nothing
	 * if no shader with the given handle exists
	 * 
	 * @param handle
	 */
	public void destroyShader(int handle);

	/**
	 * Binds the given shader for use
	 * 
	 * @param handle
	 * @return whether the bind was successful
	 */
	public boolean bindShader(int handle);

	/**
	 * Unbinds the currently active shader (simmilar to calling
	 * {@link #bindShader(int)} using 0 as the handle)
	 */
	public void unbindShader();

	/**
	 * Generates a Map that maps uniform names of the given shader to their
	 * respective id
	 * 
	 * @param handle
	 * @return the generated Map or null if no shader with the given id exists
	 */
	public Map<String, Integer> getUniforms(int handle);

	/**
	 * Finds the location of a given uniform in the active shader
	 * 
	 * @param name
	 * @return the location of the given uniform, -1 if it does not exist
	 */
	public int getUniformLocation(String name);

	/**
	 * Binds the given integer to the given uniform of the currently active
	 * shader
	 * 
	 * @param location
	 * @param value
	 * @return whether binding the integer was successful
	 */
	public boolean setUniformInt(int location, int value);

	/**
	 * Binds the given double to the given uniform of the currently active
	 * shader
	 * 
	 * @param location
	 * @param value
	 * @return whether binding the double was successful
	 */
	public boolean setUniformDouble(int location, double value);

	/**
	 * Binds the given texture to the given uniform (includes binding the
	 * texture to a texture unit)
	 * 
	 * @param location
	 * @param value
	 * @return whether binding the texture was successful
	 */
	public boolean setUniformTexture(int location, int texture);

	/**
	 * Binds the given {@link Matrix33} to the given uniform of the currently
	 * active shader
	 * 
	 * @param location
	 * @param texture
	 * @return whether binding the Matrix33 was successful
	 */
	public boolean setUniformMatrix(int location, Matrix33 texture);

	/**
	 * Binds the given {@link Vector2D} to the given uniform of the currently
	 * active shader
	 * 
	 * @param location
	 * @param vector
	 * @return whether binding the Vector2D was successful
	 */
	public boolean setUniformVector(int location, Vector2D vector);

	// TODO method for uniform color

	/**
	 * Sets the given attribute of the currently active shader-program to the
	 * given value
	 * 
	 * @param name
	 * @param value
	 * @return whether the operation was completed successfully
	 */
	public boolean setAttribute(String name, int value);

	// Batches

	/**
	 * An {@link ElementType} is used to specify the elements of a
	 * {@link VertexAttribute}
	 * 
	 * @author Freddy
	 *
	 */
	public enum ElementType {
		/** An {@link ElementType} representing a float */
		FLOAT(4),
		/** An {@link ElementType} representing an integer */
		INTEGER(4),
		/** An {@link ElementType} representing a short */
		SHORT(2),
		/** An {@link ElementType} representing a byte */
		BYTE(1);

		private int mSize;

		/**
		 * Creates a new {@link ElementType} with the given size in bytes
		 * 
		 * @param size
		 */
		ElementType(int size) {
			mSize = size;
		}

		/**
		 * 
		 * @return the size of this {@link ElementType} in bytes
		 */
		public int size() {
			return mSize;
		}
	}

	/**
	 * A collection of permitted vertex-attributes. The later id of an attribute
	 * is determined by its position int the list of all vertexattributes used
	 * by a given batch
	 * 
	 * @author Fredie
	 *
	 */
	public enum VertexAttribute {
		/** Position vertex attribute */
		POSITION("a_pos", ElementType.FLOAT, 2, false),

		/** Color vertex attribute (RGBA as floats) */
		COLOR("a_color", ElementType.BYTE, 4, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_0("a_texcoord_0", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_1("a_texcoord_1", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_2("a_texcoord_2", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_3("a_texcoord_3", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_4("a_texcoord_4", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_5("a_texcoord_5", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_6("a_texcoord_6", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_7("a_texcoord_7", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_8("a_texcoord_8", ElementType.FLOAT, 2, false),

		/** VertexAttribute for texture-coordinates */
		TEX_COORD_9("a_texcoord_9", ElementType.FLOAT, 2, false);

		private final int mECount;
		private final ElementType mType;
		private final boolean mNormalized;
		private final int mOffset;
		private final String mName;

		/**
		 * Creates a new {@link VertexAttribute}
		 * 
		 * @param name
		 *            the name it is represented by in the shader
		 * @param fp
		 *            whether the elements are floatingpoint numbers or not
		 * @param esize
		 *            the size in bytes of one element permitted are 1, 2, 4 and
		 *            8
		 * @param ecount
		 *            the count of elements in this
		 * @param normalized
		 *            whether elements in the attribute are normalized or not
		 */
		private VertexAttribute(String name, ElementType type, int ecount, boolean normalized) {
			mName = name;
			mType = type;
			mECount = ecount;
			mOffset = type.size() * mECount;
			mNormalized = normalized;
		}

		/**
		 * 
		 * @return the {@link ElementType} of this {@link VertexAttribute}
		 */
		public ElementType type() {
			return mType;
		}

		/**
		 * 
		 * @return how many elements there are in the attribute
		 */
		public int elementCount() {
			return mECount;
		}

		/**
		 * 
		 * @return whether values in this attribute are normalized or not
		 */
		public boolean normalized() {
			return mNormalized;
		}

		/**
		 * 
		 * @return how many bytes are consumed by this vertex-attribute
		 */
		public int offset() {
			return mOffset;
		}

		/**
		 * 
		 * @return the name of this {@link VertexAttribute} used in shaders
		 */
		public String attribute() {
			return mName;
		}
	}

	/**
	 * Creates a new {@link Batch} on the graphics hardware
	 * 
	 * @param isStatic
	 * @param attributes
	 * @return the created batch or null if the creation failed
	 */
	public Batch genBatch(boolean isStatic, VertexAttribute... attributes);

	/**
	 * Destroys the given {@link Batch} or does nothing if batch is null
	 * 
	 * @param batch
	 */
	public void destroyBatch(Batch batch);

	/**
	 * Renders the given {@link Batch} using the currently active shader
	 * 
	 * @param batch
	 * @return whether rendering the batch was successful or not
	 */
	public boolean renderBatch(Batch batch);
}
