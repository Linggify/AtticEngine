package com.github.linggify.attic.render.path;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Context;

/**
 * A Node is one step in a {@link RenderPath} it may have inputs and outputs
 * @author Fredie
 *
 */
public interface Node {

	/**
	 * Sets the {@link Context} for this {@link Node}.
	 * @param helper
	 */
	public void setRenderHepler(Context helper);

	/**
	 * Sets the input with the given name
	 * @param name
	 * @param input
	 */
	public void setInput(String name, Input input);
	
	/**
	 * Prepares the {@link Node} for rendering a new frame,
	 * this includes resetting all cached outputs
	 */
	public void prepare();
	
	/**
	 * @param name
	 * @return the id of the given output or -1 if no output with the given name exists
	 */
	public int getOutputId(String name);
	
	/**
	 * 
	 * @param index the index of the output
	 * @param type the expected type
	 * @return the value of the given output
	 * @throws AtticRuntimeException when the expected and actual type do not match
	 */
	public <T> T getOutput(int index, Class<T> type) throws AtticRuntimeException;
	
	/**
	 * Inputs are providing data for {@link Node}s
	 * @author Fredie
	 *
	 */
	public interface Input {
		
		/**
		 * 
		 * @param type the expected type
		 * @return the value of the input
		 * @throws AtticRuntimeException when the Requested type does not match the actual Type of the value
		 */
		public <T> T getValue(Class<T> type) throws AtticRuntimeException;
	}
}
