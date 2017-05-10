package com.github.linggify.attic.util;

import java.util.Arrays;

import com.github.linggify.attic.exceptions.AtticRuntimeException;

/**
 * Matrix33 is a math-utility class representing a 3x3 Matrix used for geometric calculations
 * @author Fredie
 *
 */
public class Matrix33 {

	private final float[] mValues;
	
	/**
	 * Creates a new Matrix33 as the identy-matrix
	 */
	public Matrix33() {
		this(new float[] {1, 0, 0, 0, 1, 0, 0, 0, 1});
	}
	
	/**
	 * Creates a new Matrix33 with the given values
	 * @param values
	 */
	public Matrix33(float[] values) {
		mValues = new float[9];
		set(values);
	}
	
	/**
	 * 
	 * @return a copy of the values of this {@link Matrix33}
	 */
	public float[] getValues() {
		float[] values = new float[9];
		System.arraycopy(mValues, 0, values, 0, 9);
		return values;
	}
	
	/**
	 * Copies the values from the given array into this {@link Matrix33}. the given array must have a length of 9
	 * @param values
	 * @return this Matix33 for chaining
	 */
	public Matrix33 set(float[] values) {
		if(values == null) throw new AtticRuntimeException("Values of a Matrix33 may not be null");
		if(values.length != 9) throw new AtticRuntimeException("Need 9 values for Matrix33, but only got " + values.length);
		System.arraycopy(values, 0, mValues, 0, 9);
		
		return this;
	}
	
	/**
	 * Sets this {@link Matrix33} to the given matrix
	 * @param matrix
	 * @return this Matix33 for chaining
	 */
	public Matrix33 set(Matrix33 matrix) {
		if(matrix == null) throw new AtticRuntimeException("The source Matrix33 may not be null");
		set(matrix.getValues());
		
		return this;
	}
	
	/**
	 * Sets this matrix to the identy-matrix
	 * @return this Matix33 for chaining
	 */
	public Matrix33 identy() {
		set(new float[] {1, 0, 0, 0, 1, 0, 0, 0, 1});
		return this;
	}
	
	/**
	 * Adds the given {@link Matrix33} to this one (component wise)
	 * @param other
	 * @return this Matix33 for chaining
	 */
	public Matrix33 add(Matrix33 other) {
		if(other == null) throw new AtticRuntimeException("The source Matrix33 may not be null");
		//use the field directly to avoid unnecessary coping
		float[] others = other.mValues;
		for(int i = 0; i < mValues.length; i++) mValues[i] += others[i];
		
		return this;
	}
	
	/**
	 * Subtracts the given {@link Matrix33} to this one (component wise)
	 * @param other
	 * @return this Matix33 for chaining
	 */
	public Matrix33 sub(Matrix33 other) {
		if(other == null) throw new AtticRuntimeException("The source Matrix33 may not be null");
		//use the field directly to avoid unnecessary coping
		float[] others = other.mValues;
		for(int i = 0; i < mValues.length; i++) mValues[i] -= others[i];
		
		return this;
	}
	
	/**
	 * Multiplies this {@link Matrix33} with the other matrix (the given matrix is used as the right matrix)
	 * @param other
	 * @return this Matix33 for chaining
	 */
	public Matrix33 multiply(Matrix33 other) {
		if(other == null) throw new AtticRuntimeException("The source Matrix33 may not be null");
		//use the field directly to avoid unnecessary coping
		float[] others = other.mValues;
		float[] newVals = new float[9];
		for(int i = 0; i < mValues.length; i++) {
			for(int c = 0; c < 3; c++) {
				float a = mValues[((int) (i / 3)) * 3 + c];
				float b = others[(i % 3) + (3 * c)];
				newVals[i] += a * b;
			}
		}
		
		set(newVals);
		
		return this;
	}
	
	/**
	 * Multiplies the given {@link Vector2D} with this {@link Matrix33}
	 * @param vector
	 * @return the given Vector2D for chaining
	 */
	public Vector2D multiply(Vector2D vector) {
		if(vector == null) throw new AtticRuntimeException("The target Vector2D may not be null");
		
		float x = vector.getX();
		float y = vector.getY();
		vector.setX(mValues[0] * x + mValues[1] * y + mValues[2]);
		vector.setY(mValues[3] * x + mValues[4] * y + mValues[5]);
		
		return vector;
	}
	
	/**
	 * Transforms this {@link Matrix33} by the given {@link Vector2D}
	 * @param vector
	 * @return this Matix33 for chaining
	 */
	public Matrix33 transform(Vector2D vector) {
		if(vector == null) throw new AtticRuntimeException("The source Vector2D may not be null");
		transform(vector.getX(), vector.getY());
		
		return this;
	}
	
	/**
	 * Transforms this {@link Matrix33} by the given values
	 * @param x
	 * @param y
	 * @return this Matix33 for chaining
	 */
	public Matrix33 transform(float x, float y) {
		transformX(x);
		transformY(y);
		
		return this;
	}
	
	/**
	 * Transforms this {@link Matrix33} along the x-axis by the given amount
	 * @param value
	 * @return this Matix33 for chaining
	 */
	public Matrix33 transformX(float value) {
		mValues[2] += value;
		return this;
	}
	
	/**
	 * Transforms this {@link Matrix33} along the y-axis by the given amount
	 * @param value
	 * @return this Matix33 for chaining
	 */
	public Matrix33 transformY(float value) {
		mValues[5] += value;
		return this;
	}
	
	/**
	 * Scales this matrix on the diagonal by the given amount
	 * @param scalar
	 * @return this Matix33 for chaining
	 */
	public Matrix33 scale(float scalar) {
		for(int i = 0; i < mValues.length; i++) {
			mValues[i] *= scalar;
		}
		return this;
	}
	
	/**
	 * Rotates this {@link Matrix33} counter-clockwise by the given angle
	 * @param x angle in degrees
	 * @return this Matix33 for chaining
	 */
	public Matrix33 rotate(float angle) {
		float sin = (float) Math.sin(Math.toRadians(angle));
		float cos = (float) Math.cos(Math.toRadians(angle));
		
		float o0 = mValues[0];
		float o1 = mValues[1];
		float o3 = mValues[3];
		float o4 = mValues[4];
		
		float o2 = mValues[2];
		float o5 = mValues[5];
		
		mValues[0] = o0 * cos - o3 * sin;
		mValues[3] = o0 * sin + o3 * cos;
		mValues[1] = o1 * cos - o4 * sin;
		mValues[4] = o1 * sin + o4 * cos;
		
		mValues[2] = o2 * cos - o5 * sin;
		mValues[5] = o2 * sin + o5 * cos;
		return this;
	}
	
	/**
	 * @return the determinant of this {@link Matrix33}
	 */
	public float det() {
		return mValues[0] * mValues[4] * mValues[8] + 
				mValues[1] * mValues[5] * mValues[6] +
				mValues[3] * mValues[7] * mValues[2] -
				mValues[6] * mValues[4] * mValues[2] -
				mValues[3] * mValues[1] * mValues[8] -
				mValues[0] * mValues[7] * mValues[5];
	}
	
	/**
	 * Calculate the partial determinant of this matrix from the given coords
	 * @param a
	 * @param j
	 * @return the partial determinant of this matrix
	 */
	private float uDet(int a, int j) {
		a = realMod(a, 3);
		j = realMod(j, 3);
		int x0 = realMod(a - 1, 3);
		int x1 = realMod(a + 1, 3);
		int y0 = realMod(j - 1, 3);
		int y1 = realMod(j + 1, 3);
		
		return mValues[x0 + 3 * y0] * mValues[x1 + 3 * y1] - mValues[x1 + 3 * y0] * mValues[x0 + 3 * y1];
	}
	
	/**
	 * 
	 * @param value
	 * @param mod
	 * @return the real modulus for the given value and base
	 */
	private int realMod(int value, int mod) {
		return value < 0 ? mod + (value % mod) : value % mod;
	}
	
	/**
	 * Inverts this {@link Matrix33}
	 * @return this Matix33 for chaining
	 */
	public Matrix33 invert() {
		float[] newVals = new float[9];
		float factor = 1.0f / det();
		
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				//use the transposed cofactor-matrix
				newVals[x + 3 * y] = factor * uDet(y, x);
			}
		}
		
		set(newVals);
		return this;
	}

	@Override
	public String toString() {
		String result = "[(" + mValues[0] + ", " + mValues[1] + ", " + mValues[2] + "), (";
		result += mValues[3] + ", " + mValues[4] + ", " + mValues[5] + "), (";
		result +=  mValues[6] + ", " + mValues[7] + ", " + mValues[8] + ")]";
		
		return result;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(mValues);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix33 other = (Matrix33) obj;
		if (!Arrays.equals(mValues, other.mValues))
			return false;
		return true;
	}
}
