package com.github.linggify.attic.render.path;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.path.Node.Input;

/**
 * An OutputLinkInput is an {@link Input} that links a value of an output of another {@link Node}
 * @author Fredie
 *
 */
public class OutputLinkInput implements Input{

	private Node mTarget;
	private int mOutput;
	
	/**
	 * Creates a new {@link OutputLinkInput} to the given {@link Node}s output
	 * @param node
	 * @param output
	 */
	public OutputLinkInput(Node node, String output) {
		set(node, output);
	}
	
	/**
	 * Sets the {@link Node} and its output to link to
	 * @param node
	 * @param output
	 */
	public void set(Node node, String output) {
		if(node != null) {
			mTarget = node;
			mOutput = node.getOutputId(output);
		} else mTarget = null;
	}
	
	@Override
	public <T> T getValue(Class<T> type) throws AtticRuntimeException {
		if(mTarget != null) return mTarget.getOutput(mOutput, type);
		else return null;
	}
}
