package com.github.linggify.attic;

import java.util.HashMap;

import com.github.linggify.attic.logic.Genius;

/**
 * A View is an Interface used by the {@link Application} to setup the scene
 * 
 * @author Fredie
 *
 */
public interface IView {

	/**
	 * Sets up the scene using the given {@link Genius}
	 * 
	 * @param app
	 * @param state
	 *            the state of this {@link IView}
	 */
	void setup(Application app, ViewState state);

	/**
	 * Pauses this {@link IView}
	 * 
	 * @param next
	 *            the {@link ViewState} of the View that is started next, to
	 *            submit information to the upcoming View
	 * @return all relevant information needed to resume the View as a ViewState
	 */
	ViewState stop(ViewState next);

	/**
	 * ViewStates are used by the {@link Application} to remember the state of a
	 * {@link IView} when it not currently active
	 * 
	 * @author Fredie
	 *
	 */
	class ViewState {

		private HashMap<String, Object> mProperties;

		/**
		 * Creates a new {@link ViewState}
		 */
		public ViewState() {
			mProperties = new HashMap<>();
		}

		/**
		 * Puts the given property to the {@link ViewState}
		 * 
		 * @param name
		 * @param property
		 */
		public void putProperty(String name, Object property) {
			mProperties.put(name, property);
		}

		/**
		 * 
		 * @param name
		 * @param type
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <T> T getProperty(String name, Class<T> type) {
			return (T) mProperties.get(name);
		}
	}
}
