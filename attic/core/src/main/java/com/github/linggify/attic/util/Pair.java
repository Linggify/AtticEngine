package com.github.linggify.attic.util;

/**
 * An immutable Pair of arbitrary values
 * @author Fredie
 *
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> {

	private final K mKey;
	private final V mValue;
	
	/**
	 * Creates a new {@link Pair} with the given values
	 * @param key
	 * @param value
	 */
	public Pair(K key, V value) {
		mKey = key;
		mValue = value;
	}
	
	/**
	 * 
	 * @return the key of this {@link Pair}
	 */
	public K getKey() {
		return mKey;
	}
	
	/**
	 * 
	 * @return the value of this {@link Pair}
	 */
	public V getValue() {
		return mValue;
	}
}
