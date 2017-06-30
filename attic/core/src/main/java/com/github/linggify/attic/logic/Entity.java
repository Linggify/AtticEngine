package com.github.linggify.attic.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Entities are used to represent all objects in a scene.
 * They contain {@link IProperty}s that define their behavior
 * @author Fredie
 *
 */
public class Entity {

	private List<IProperty<?>> mProperties;
	private Set<Class<?>> mPropertyTypes;
	private final Genius mGenius;
	private boolean mDead = false;
	
	/**
	 * Creates a new {@link Entity} with the given {@link IProperty}s
	 * @param properties
	 */
	public Entity(Genius genius, IProperty<?>...properties) {
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
	 * Kills this {@link Entity} thereby stripping it of all its {@link IProperty}s
	 * and thus making it unusable.
	 */
	public void kill() {
		for(IProperty<?> property : mProperties) property.onDetach();
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
	 * Adds all the given {@link IProperty}s to this {@link Entity}
	 * @param properties
	 */
	public void addProperties(IProperty<?>...properties) {
		mProperties.addAll(Arrays.asList(properties));
		for(IProperty<?> property : properties) property.onAttach(this);
	}
	
	/**
	 * 
	 * @param type
	 * @return the {@link IProperty} of the given type, or null if none exists
	 */
	@SuppressWarnings("unchecked")
	public <T extends IProperty<?>> T propertyByType(Class<T> type) {
		//if no property of the given type exists, return nothing
		if(!mPropertyTypes.contains(type))
			return null;
		
		//search for the property
		for(IProperty<?> property : mProperties) {
			if(property.getClass() == type)
				return (T) property;
		}
		
		//this line is probably never reached
		return null;
	}
	
	/**
	 * Finds all {@link IProperty}s containing the given type contained by this {@link Entity}
	 * @param type
	 * @return a List of all Properties of the given type
	 */
	@SuppressWarnings("unchecked")
	public <T> List<IProperty<T>> propertiesByValue(Class<T> type) {
		List<IProperty<T>> result = new ArrayList<>();
		for(IProperty<?> property : mProperties) {
			if(property.getContentType() == type) result.add((IProperty<T>) property);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param property
	 * @return true if the {@link IProperty} was removed, false otherwise
	 */
	public boolean removeProperty(IProperty<?> property) {
		boolean result = mProperties.remove(property);
		if(result) property.onDetach();
		
		return result;
	}
	
	/**
	 * Updates this {@link Entity}s properties
	 * @param delta the time since the last update
	 */
	public void update(double delta) {
		for(IProperty<?> property : mProperties) {
			property.update(delta);
		}
	}
}
