package com.github.linggify.attic.logic;

import com.github.linggify.attic.exceptions.AtticRuntimeException;

/**
 * Properties are the building blocks of {@link Entity}s
 * 
 * @author Fredie
 *
 */
public interface Property<T> {

	/**
	 * Adds a {@link PropertyListener} to this {@link Property}
	 * @param listener
	 */
	public void addListener(PropertyListener listener);
	
	/**
	 * Removes the given {@link PropertyListener} from this {@link Property}
	 * @param listener
	 * @return whether the listener was successfully removed or not
	 */
	public boolean removeListener(PropertyListener listener);
	
	/**
	 * Called when this {@link Property} is attached to an Entity
	 * @param parent
	 * @throws AtticRuntimeException when this {@link Property} is already attached to a different parent
	 */
	public void onAttach(Entity parent) throws AtticRuntimeException;
	
	/**
	 * Called when this {@link Property} is detached from its current parent
	 * @throws AtticRuntimeException when the {@link Property} has not yet been attached to a parent
	 */
	public void onDetach() throws AtticRuntimeException;
	
	/**
	 * Sets whether this {@link Property} is active
	 * @param flag
	 */
	public void setActive(boolean flag);
	
	/**
	 * 
	 * @return whether this {@link Property} is active
	 */
	public boolean isActive();
	
	/**
	 * 
	 * @return the type the value of this {@link Property} is
	 */
	public Class<T> getContentType();
	
	/**
	 * Updates the value of this {@link Property}
	 * 
	 * @param delta
	 *            the time since the last update
	 */
	public void update(double delta);

	/**
	 * 
	 * @return the current value of this {@link Property}
	 */
	public T get();

	/**
	 * An enumeration of possible property-events that need to be reported to
	 * the listeners of this property
	 * 
	 * @author Fredie
	 *
	 */
	public enum PropertyEvent {
		/** Event called when a {@link Property} is added */
		PROPERTY_ADDED,
		/** Event called when a {@link Property} is changed */
		PROPERTY_CHANGED,
		/** Event called when a {@link Property} is removed */
		PROPERTY_REMOVED,
		/** Event called when a {@link Property} is disabled */
		PROPERTY_DISABLED,
		/** Event called when a {@link Property} is enabled */
		PROPERTY_ENABLED;
	}
	
	/**
	 * PropertyListeners are used to listen to {@link PropertyEvent}s fired by {@link Property}s
	 * @author Fredie
	 *
	 */
	public interface PropertyListener {
		
		/**
		 * Called when a {@link Property} this {@link PropertyListener} listens to changes in some way
		 * @param target
		 * @param event
		 */
		public void onEvent(Property<?> target, PropertyEvent event);
	}
}
