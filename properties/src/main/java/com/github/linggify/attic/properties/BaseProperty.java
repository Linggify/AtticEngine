package com.github.linggify.attic.properties;

import java.util.HashSet;
import java.util.Set;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.logic.IProperty;

/**
 * A Base for all {@link IProperty}s containing basic functionality for lísteners
 * @author Fredie
 *
 * @param <T>
 */
public abstract class BaseProperty<T> implements IProperty<T>{

	private Entity mParent;
	
	private Set<PropertyListener> mListeners;
	private boolean mIsActive;
	
	/**
	 * Creates a new {@link BaseProperty}
	 */
	public BaseProperty() {
		mListeners = new HashSet<>();
		
		//no parent by default
		mParent = null;
		
		//property is active by default
		mIsActive = true;
	}
	
	/**
	 * Calls all {@link PropertyListener} added to this {@link IProperty} with the given {@link PropertyEvent}
	 * if the Property is active
	 * 
	 * @param event the PropertyEvent that happened
	 */
	public void notifyListeners(PropertyEvent event) {
		notifyListeners(event, false);
	}
	
	/**
	 * Calls all {@link PropertyListener} added to this {@link IProperty} with the given {@link PropertyEvent}
	 * if the Property is active or force is true
	 * 
	 * @param event the PropertyEvent that happened
	 * @param force whether to force the notification or not
	 */
	protected void notifyListeners(PropertyEvent event, boolean force) {
		if(isActive() || force) {
			for(PropertyListener listener : mListeners)
				listener.onEvent(this, event);
		}
	}
	
	/**
	 * 
	 * @return whether this {@link IProperty} has a parent or not
	 */
	protected boolean hasParent() {
		return mParent != null;
	}
	
	/**
	 * 
	 * @return the current parent of the {@link IProperty} or null if it has no parent
	 */
	protected Entity getParent() {
		return mParent;
	}
	
	@Override
	public void addListener(PropertyListener listener) {
		if(listener == null)
			throw new AtticRuntimeException("Listener cannot be null");

		mListeners.add(listener);
		
		//notify the listener if is active
		if(isActive())
			listener.onEvent(this, PropertyEvent.PROPERTY_CHANGED);
	}

	@Override
	public boolean removeListener(PropertyListener listener) {
		return mListeners.remove(listener);
	}

	@Override
	public void onAttach(Entity parent) throws AtticRuntimeException {
		if (mParent != null)
			throw new AtticRuntimeException("Property is already attached to an Entity");

		mParent = parent;
		notifyListeners(PropertyEvent.PROPERTY_ADDED, true);
	}

	@Override
	public void onDetach() throws AtticRuntimeException {
		if (mParent == null)
			throw new AtticRuntimeException("Property is not attached to an Entity");

		mParent = null;
		notifyListeners(PropertyEvent.PROPERTY_REMOVED, true);
	}

	@Override
	public void setActive(boolean flag) {
		//if the status is changed, notify listeners
		if (flag ^ mIsActive) {
			mIsActive = flag;
			notifyListeners(mIsActive ? PropertyEvent.PROPERTY_ENABLED : PropertyEvent.PROPERTY_DISABLED, true);
		}
	}

	@Override
	public boolean isActive() {
		return mIsActive;
	}
}
