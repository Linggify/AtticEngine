package com.github.linggify.attic.util;

import java.util.Arrays;

import com.github.linggify.attic.render.RenderData.VertexData;

/**
 * Colors are used to represent a color as 4 color channels,
 * red, green blue and alpha (for transparency)
 * @author Freddy
 *
 */
public class Color implements VertexData{
	
	private float[] mData;
	private byte[] mBytes;
	private boolean mBytesDirty;
	
	/**
	 * Constructs a new {@link Color} which is black
	 */
	public Color() {
		this(0, 0, 0, 1);
	}
	
	/**
	 * Constructs a new {@link Color} using the same values as the given Color
	 * @param color
	 */
	public Color(Color color) {
		this(color.red(), color.green(), color.blue(), color.alpha());
	}
	
	/**
	 * Creates a new Color with the given parameters
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Color(float r, float g, float b, float a) {
		mBytes = new byte[4];
		mData = new float[4];
		set(r, g, b, a);
	}

	/**
	 * 
	 * @return the red value of this {@link Color}
	 */
	public float red() {
		return mData[0];
	}
	
	/**
	 * 
	 * @return the red value of this {@link Color}
	 */
	public float green() {
		return mData[1];
	}
	
	/**
	 * 
	 * @return the red value of this {@link Color}
	 */
	public float blue() {
		return mData[2];
	}
	
	/**
	 * 
	 * @return the red value of this {@link Color}
	 */
	public float alpha() {
		return mData[3];
	}
	
	/**
	 * Sets this {@link Color} to the values of the given Color
	 * @param color
	 * @return this color for chaining
	 */
	public Color set(Color color) {
		return set(color.red(), color.green(), color.blue(), color.alpha());
	}
	
	/**
	 * Sets this {@link Color} to the specific values
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return this Color for chaining
	 */
	public Color set(float r, float g, float b, float a) {
		mData[0] = r;
		mData[1] = g;
		mData[2] = b;
		mData[3] = a;
		mBytesDirty = true;
		return this;
	}
	
	/**
	 * Multiplies the 2 {@link Color}s
	 * @param color
	 * @return this Color for chaining
	 */
	public Color tint(Color color) {
		return tint(color.red(), color.green(), color.blue(), color.alpha());
	}
	
	/**
	 * Multiplies this {@link Color} with the given values
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 * @return this color for chaining
	 */
	public Color tint(float red, float green, float blue, float alpha) {
		mData[0] *= red;
		mData[1] *= green;
		mData[2] *= blue;
		mData[3] *= alpha;
		mBytesDirty = true;
		return this;
	}
	
	@Override
	public byte[] asBytes() {
		if(mBytesDirty) {
			mBytes[0] = (byte) ((int) (mData[0] * 255.0f) & 0xFF);
			mBytes[1] = (byte) ((int) (mData[1] * 255.0f) & 0xFF);
			mBytes[2] = (byte) ((int) (mData[2] * 255.0f) & 0xFF);
			mBytes[3] = (byte) ((int) (mData[3] * 255.0f) & 0xFF);
			mBytesDirty = false;
		}
		return mBytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(mData);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color other = (Color) obj;
		if (!Arrays.equals(mData, other.mData))
			return false;
		return true;
	}
}
