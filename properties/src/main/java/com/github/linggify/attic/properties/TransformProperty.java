package com.github.linggify.attic.properties;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Vector2D;

/**
 * The TransformProperty represents an {@link Entity}s position and rotation
 * 
 * @author Freddy
 *
 */
public class TransformProperty extends BaseProperty<Matrix33> {

	private float mRotation;
	private float mScale;
	private final Vector2D mPosition;
	private boolean mTransformDirty;
	private final Matrix33 mTransform;

	private final Matrix33 mTmpMatrix;
	private TransformProperty mParentTransform;
	private PropertyListener mParentListener;

	/**
	 * Creates a new {@link TransformProperty} with the identy-matrix
	 */
	public TransformProperty() {
		mPosition = new Vector2D();
		mScale = 1.0f;
		mRotation = 0.0f;
		mTransform = new Matrix33();
		mTmpMatrix = new Matrix33();

		mParentListener = (t, e) -> {
			if (e == PropertyEvent.PROPERTY_CHANGED)
				mTransformDirty = true;
		};
	}

	/**
	 * Sets the parent-transform for this {@link TransformProperty}
	 * 
	 * @param parent
	 */
	public void setParentTransform(TransformProperty parent) {
		// check to avoid setting this to its own parent
		if (parent == this)
			throw new AtticRuntimeException("Cannot set the parent of a TransformProperty to itself");

		// detach from previous parent
		if (mParentTransform != null)
			mParentTransform.removeListener(mParentListener);

		mParentTransform = parent;
		mTransformDirty = true;

		// attach to current parent
		if (mParentTransform != null)
			mParentTransform.addListener(mParentListener);
	}

	/**
	 * Sets the position of this {@link TransformProperty}
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		mPosition.set(x, y);
		mTransformDirty = true;
	}

	/**
	 * Sets the position of this {@link TransformProperty}
	 * 
	 * @param vector
	 */
	public void setPosition(Vector2D vector) {
		mPosition.set(vector);
		mTransformDirty = true;
	}

	/**
	 * Moves this {@link TransformProperty} by the given amount
	 * 
	 * @param x
	 * @param y
	 */
	public void move(float x, float y) {
		mPosition.add(x, y);
		mTransformDirty = true;
	}

	/**
	 * Moves this {@link TransformProperty} by the given amount
	 * 
	 * @param vector
	 */
	public void move(Vector2D vector) {
		mPosition.add(vector);
		mTransformDirty = true;
	}

	/**
	 * Sets the rotation of this {@link TransformProperty} in degrees
	 * 
	 * @param angle
	 */
	public void setRotation(float angle) {
		mRotation = angle;
		mTransformDirty = true;
	}

	/**
	 * Adds the given angle to the rotation of this {@link TransformProperty}
	 * 
	 * @param angle
	 */
	public void rotate(float angle) {
		mRotation += angle;
		mTransformDirty = true;
	}

	/**
	 * Sets the scale of this {@link TransformProperty}
	 * 
	 * @param scalar
	 */
	public void setScale(float scalar) {
		mScale = scalar;
		mTransformDirty = true;
	}

	/**
	 * Multiplies the current scale of this {@link TransformProperty} with the
	 * given value
	 * 
	 * @param scalar
	 */
	public void scale(float scalar) {
		mScale *= scalar;
		mTransformDirty = true;
	}

	/**
	 * This method must be called after you are done changing the
	 * {@link TransformProperty} to notify its listeners.
	 */
	public void notifyChanged() {
		notifyListeners(PropertyEvent.PROPERTY_CHANGED);
	}

	@Override
	public Class<Matrix33> getContentType() {
		return Matrix33.class;
	}

	@Override
	public void update(double delta) {
		// do nothing
	}

	@Override
	public Matrix33 get() {
		if (mTransformDirty) {
			mTransform.identy();
			mTransform.scale(mScale);
			mTransform.rotate(mRotation);
			mTransform.transform(mPosition);

			if (mParentTransform != null) {
				mTmpMatrix.set(mParentTransform.get());
				mTransform.set(mTmpMatrix.multiply(mTransform));
			}

			mTransformDirty = false;
		}

		return mTransform;
	}

}
