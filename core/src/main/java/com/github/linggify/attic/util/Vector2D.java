package com.github.linggify.attic.util;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.RenderData.VertexData;

/**
 * Vector2D is a math-utility class representing a 2-dimensional vector
 * @author Fredie
 *
 */
public class Vector2D implements VertexData {

	/**
	 * 
	 * @param x
	 * @param y
	 * @return a new {@link Vector2D} with the given coords
	 */
	public static Vector2D at(float x, float y) {
		return new Vector2D(x, y);
	}
	
	private float mX;
	private float mY;
	private boolean mBytesDirty;
	private byte[] mBytes;
	
	/**
	 * Creates a new {@link Vector2D} at the origin (0, 0)
	 */
	public Vector2D() {
		this(0, 0);
	}

	public Vector2D(Vector2D vector) {
		set(vector);
		mBytes = new byte[8];
		mBytesDirty = true;
	}
	
	/**
	 * Creates a new {@link Vector2D} at the given location
	 * @param x
	 * @param y
	 * @return this Vector2D for chaining
	 */
	public Vector2D(float x, float y) {
		set(x, y);
		mBytes = new byte[8];
		mBytesDirty = true;
	}
	
	/**
	 * Sets the values of this {@link Vector2D} to the values of the given one
	 * @param vector
	 * @return this Vector2D for chaining
	 */
	public Vector2D set(Vector2D vector) {
		if(vector == null) throw new AtticRuntimeException("The source Vector2D may not be null");
		set(vector.getX(), vector.getY());
		return this;
	}
	
	/**
	 * Sets the values of this {@link Vector2D} to the given ones
	 * @param x
	 * @param y
	 * @return this Vector2D for chaining
	 */
	public Vector2D set(float x, float y) {
		setX(x);
		setY(y);
		return this;
	}
	
	/**
	 * Sets the x value of this {@link Vector2D}
	 * @param x
	 * @return this Vector2D for chaining
	 */
	public Vector2D setX(float x) {
		mX = x;
		mBytesDirty = true;
		return this;
	}
	
	/**
	 * Sets the Y value for this {@link Vector2D}
	 * @param y
	 * @return this Vector2D for chaining
	 */
	public Vector2D setY(float y) {
		mY = y;
		mBytesDirty = true;
		return this;
	}
	
	/**
	 * 
	 * @return the X value of this {@link Vector2D}
	 */
	public float getX() {
		return mX;
	}
	
	/**
	 * 
	 * @return the Y value of this {@link Vector2D}
	 */
	public float getY() {
		return mY;
	}
	
	/**
	 * Adds the given {@link Vector2D} to this Vector2D
	 * @param vector
	 * @return this Vector2D for chaining
	 */
	public Vector2D add(Vector2D vector) {
		if(vector == null) throw new AtticRuntimeException("The source Vector2D may not be null");
		add(vector.getX(), vector.getY());
		return this;
	}
	
	/**
	 * Adds the given values to this {@link Vector2D}s values
	 * @param x
	 * @param y
	 * @return this Vector2D for chaining
	 */
	public Vector2D add(float x, float y) {
		setX(getX() + x);
		setY(getY() + y);
		return this;
	}
	
	/**
	 * Subtracts the given {@link Vector2D} from this Vector2D
	 * @param vector
	 * @return this Vector2D for chaining
	 */
	public Vector2D sub(Vector2D vector) {
		if(vector == null) throw new AtticRuntimeException("The source Vector2D may not be null");
		sub(vector.getX(), vector.getY());
		return this;
	}
	
	/**
	 * Subtracts the given values from this {@link Vector2D}
	 * @param x
	 * @param y
	 * @return this Vector2D for chaining
	 */
	public Vector2D sub(float x, float y) {
		setX(getX() - x);
		setY(getY() - y);
		return this;
	}
	
	/**
	 * Scales this {@link Vector2D} by the given scalar
	 * @param scalar
	 * @return this Vector2D for chaining
	 */
	public Vector2D scale(float scalar) {
		scale(scalar, scalar);
		return this;
	}
	
	/**
	 * Scales this {@link Vector2D}s values by the given Vector2D
	 * @param vector
	 * @return this Vector2D for chaining
	 */
	public Vector2D scale(Vector2D vector) {
		if(vector == null) throw new AtticRuntimeException("The source Vector2D may not be null");
		scale(vector.getX(), vector.getY());
		return this;
	}
	
	/**
	 * Scales this {@link Vector2D}s values with the given ones
	 * @param x the scalar for the x-axis
	 * @param y the scalar for the y-axis
	 * @return this Vector2D for chaining
	 */
	public Vector2D scale(float x, float y) {
		scaleX(x);
		scaleY(y);
		return this;
	}
	
	/**
	 * Scales this {@link Vector2D} on the x-axis by the given amount
	 * @param scalar
	 * @return this Vector2D for chaining
	 */
	public Vector2D scaleX(float scalar) {
		setX(getX() * scalar);
		return this;
	}
	
	/**
	 * Scales this {@link Vector2D} on the y-axis by the given amount
	 * @param scalar
	 * @return this Vector2D for chaining
	 */
	public Vector2D scaleY(float scalar) {
		setY(getY() * scalar);
		return this;
	}
	
	/**
	 * 
	 * @return the squared length of this {@link Vector2D}
	 */
	public float length2() {
		return dot(this);
	}
	
	/**
	 * 
	 * @return the length of this {@link Vector2D}
	 */
	public float length() {
		return (float) Math.sqrt(length2());
	}
	
	/**
	 * Normalizes this {@link Vector2D}
	 * @return this Vector2D for chaining
	 */
	public Vector2D normalize() {
		scale(1.0f / length());
		return this;
	}
	
	/**
	 * 
	 * @param other
	 * @return the dot-product of this {@link Vector2D} with the given other Vector2D
	 */
	public float dot(Vector2D other) {
		if(other == null) throw new AtticRuntimeException("The target Vector2D may not be null");
		return getX() * other.getX() + getY() * other.getY();
	}
	
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(mX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Vector2D other = (Vector2D) obj;
		if (Math.abs(mX - other.mX) > 0.0001)
			return false;
		if (Math.abs(mY - other.mY) > 0.0001)
			return false;
		return true;
	}

	@Override
	public byte[] asBytes() {
		if(mBytesDirty) {
			//convert to byte array
			int x = Float.floatToRawIntBits(mX);
			mBytes[0] = (byte) (x & 0xFF);
			mBytes[1] = (byte) ((x >>> 8) & 0xFF);
			mBytes[2] = (byte) ((x >>> 16) & 0xFF);
			mBytes[3] = (byte) ((x >>> 24) & 0xFF);
			
			int y = Float.floatToRawIntBits(mY);
			mBytes[4] = (byte) (y & 0xFF);
			mBytes[5] = (byte) ((y >>> 8) & 0xFF);
			mBytes[6] = (byte) ((y >>> 16) & 0xFF);
			mBytes[7] = (byte) ((y >>> 24) & 0xFF);
			
			mBytesDirty = false;
		}
		
		return mBytes;
	}
}
