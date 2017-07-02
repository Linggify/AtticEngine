package com.github.linggify.attic.render.path;

import com.github.linggify.attic.exceptions.AtticRuntimeException;

/**
 * An OutputLinkInput is an {@link INode.IInput} that links a value of an output of another {@link INode}
 * @author Fredie
 *
 */
public class OutputLinkInput implements INode.IInput {

	private INode mTarget;
	private int mOutput;
	
	/**
	 * Creates a new {@link OutputLinkInput} to the given {@link INode}s output
	 * @param node
	 * @param output
	 */
	public OutputLinkInput(INode node, String output) {
		set(node, output);
	}
	
	/**
	 * Sets the {@link INode} and its output to link to
	 * @param node
	 * @param output
	 */
	public void set(INode node, String output) {
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
