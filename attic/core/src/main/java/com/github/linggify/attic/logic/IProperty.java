package com.github.linggify.attic.logic;

import com.github.linggify.attic.exceptions.AtticRuntimeException;

/**
 * Properties are the building blocks of {@link Entity}s
 * 
 * @author Fredie
 *
 */
public interface IProperty<T> {

	/**
	 * Adds a  {@link PropertyListener} to this {@link IProperty}
	 * @param listener
	 */
	void addListener(PropertyListener listener);
	
	/**
	 * Removes the given {@link PropertyListener} from this {@link IProperty}
	 * @param listener
	 * @return whether the listener was successfully removed or not
	 */
	boolean removeListener(PropertyListener listener);
	
	/**
	 * Called when this {@link IProperty} is attached to an Entity
	 * @param parent
	 * @throws AtticRuntimeException when this {@link IProperty} is already attached to a different parent
	 */
	void onAttach(Entity parent) throws AtticRuntimeException;
	
	/**
	 * Called when this {@link IProperty} is detached from its current parent
	 * @throws AtticRuntimeException when the {@link IProperty} has not yet been attached to a parent
	 */
	void onDetach() throws AtticRuntimeException;
	
	/**
	 * Sets whether this {@link IProperty} is active
	 * @param flag
	 */
	void setActive(boolean flag);
	
	/**
	 * 
	 * @return whether this {@link IProperty} is active
	 */
	boolean isActive();
	
	/**
	 * 
	 * @return the type the value of this {@link IProperty} is
	 */
	Class<T> getContentType();
	
	/**
	 * Updates the value of this {@link IProperty}
	 * 
	 * @param delta
	 *            the time since the last update
	 */
	void update(double delta);

	/**
	 * 
	 * @return the current value of this {@link IProperty}
	 */
	T get();

	/**
	 * An enumeration of possible property-events that need to be reported to
	 * the listeners of this property
	 * 
	 * @author Fredie
	 *
	 */
	enum PropertyEvent {
		/** Event called when a {@link IProperty} is added */
		PROPERTY_ADDED,
		/** Event called when a {@link IProperty} is changed */
		PROPERTY_CHANGED,
		/** Event called when a {@link IProperty} is removed */
		PROPERTY_REMOVED,
		/** Event called when a {@link IProperty} is disabled */
		PROPERTY_DISABLED,
		/** Event called when a {@link IProperty} is enabled */
		PROPERTY_ENABLED;
	}
	
	/**
	 * PropertyListeners are used to listen to {@link PropertyEvent}s fired by {@link IProperty}s
	 * @author Fredie
	 *
	 */
	interface PropertyListener {
		
		/**
		 * Called when a {@link IProperty} this {@link PropertyListener} listens to changes in some way
		 * @param target
		 * @param event
		 */
		void onEvent(IProperty<?> target, PropertyEvent event);
	}
}
