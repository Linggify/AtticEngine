package com.github.linggify.attic.render.path;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.IContext;

/**
 * A Node is one step in a {@link RenderPath} it may have inputs and outputs
 * @author Fredie
 *
 */
public interface INode {

	/**
	 * Sets the {@link IContext} for this {@link INode}.
	 * @param helper
	 */
	void setRenderHepler(IContext helper);

	/**
	 * Sets the input with the given name
	 * @param name
	 * @param input
	 */
	void setInput(String name, IInput input);
	
	/**
	 * Prepares the {@link INode} for rendering a new frame,
	 * this includes resetting all cached outputs
	 */
	void prepare();
	
	/**
	 * @param name
	 * @return the id of the given output or -1 if no output with the given name exists
	 */
	int getOutputId(String name);
	
	/**
	 * 
	 * @param index the index of the output
	 * @param type the expected type
	 * @return the value of the given output
	 * @throws AtticRuntimeException when the expected and actual type do not match
	 */
	<T> T getOutput(int index, Class<T> type) throws AtticRuntimeException;
	
	/**
	 * Inputs are providing data for {@link INode}s
	 * @author Fredie
	 *
	 */
	interface IInput {
		
		/**
		 * 
		 * @param type the expected type
		 * @return the value of the input
		 * @throws AtticRuntimeException when the Requested type does not match the actual Type of the value
		 */
		<T> T getValue(Class<T> type) throws AtticRuntimeException;
	}
}
