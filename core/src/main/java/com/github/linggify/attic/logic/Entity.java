package com.github.linggify.attic.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entities are used to represent all objects in a scene.
 * They contain {@link Property}s that define their behavior
 * @author Fredie
 *
 */
public class Entity {

	private List<Property<?>> mProperties;
	private final Genius mGenius;
	private boolean mDead = false;
	
	/**
	 * Creates a new {@link Entity} with the given {@link Property}s
	 * @param properties
	 */
	public Entity(Genius genius, Property<?>...properties) {
		mProperties = new ArrayList<>();
		addProperties(properties);
		mGenius = genius;
	}
	
	/**
	 * 
	 * @return the {@link Genius} this {@link Entity} is attached to
	 */
	public Genius getGenius() {
		return mGenius;
	}
	
	/**
	 * <blockquote><strong>this method should only be called by the {@link Genius}</strong></blockquote>
	 * Kills this {@link Entity} thereby stripping it of all its {@link Property}s
	 * and thus making it unusable.
	 */
	public void kill() {
		for(Property<?> property : mProperties) property.onDetach();
		mProperties.clear();
	}
	
	/**
	 * Sets this {@link Entity} dead, thereby causing it to be killed and removed by the {@link Genius} in the next update
	 */
	public void setDead() {
		mDead = true;
	}
	
	/**
	 * 
	 * @return whether this entity is dead or not
	 */
	public boolean isDead() {
		return mDead;
	}
	
	/**
	 * Adds all the given {@link Property}s to this {@link Entity}
	 * @param properties
	 */
	public void addProperties(Property<?>...properties) {
		mProperties.addAll(Arrays.asList(properties));
		for(Property<?> property : properties) property.onAttach(this);
	}
	
	/**
	 * Finds all {@link Property}s containing the given type contained by this {@link Entity}
	 * @param type
	 * @return a List of all Properties of the given type
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Property<T>> getProperties(Class<T> type) {
		List<Property<T>> result = new ArrayList<>();
		for(Property<?> property : mProperties) {
			if(property.getContentType() == type) result.add((Property<T>) property);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param property
	 * @return true if the {@link Property} was removed, false otherwise
	 */
	public boolean removeProperty(Property<?> property) {
		boolean result = mProperties.remove(property);
		if(result) property.onDetach();
		
		return result;
	}
	
	/**
	 * Updates this {@link Entity}s properties
	 * @param delta the time since the last update
	 */
	public void update(double delta) {
		for(Property<?> property : mProperties) {
			property.update(delta);
		}
	}
}
