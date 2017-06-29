package com.github.linggify.attic.render.path;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.path.INode.Input;

/**
 * A ValueInput is an {@link Input} holding a single value
 * @author Fredie
 *
 * @param <T>
 */
public class ValueInput<T> implements Input {

	private T mValue;
	
	/**
	 * Creates a new {@link ValueInput} with the given value
	 * @param value
	 */
	public ValueInput(T value) {
		set(value);
	}
	
	/**
	 * Sets the current value of this {@link ValueInput}
	 * @param value
	 */
	public void set(T value) {
		mValue = value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R getValue(Class<R> type) {
		try {
			return (R) mValue;
		} catch (ClassCastException e) {
			throw new AtticRuntimeException("Unexpected input type request");
		}
	}

}
