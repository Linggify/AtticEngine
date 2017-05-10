package com.github.linggify.attic.render.path;

import java.util.Arrays;
import java.util.List;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Context;
import com.github.linggify.attic.render.Renderer;

/**
 * RenderPaths are used to define the method of rendering for a {@link Renderer}
 * 
 * @author Fredie
 *
 */
public class RenderPath {

	private List<Node> mNodes;
	private Node mRoot;
	private int mOutput;
	private Context mRenderHelper;

	/**
	 * Creates a new {@link RenderPath} with the given list of {@link Node}s
	 * 
	 * @param root
	 *            the node providing the output for the screen
	 * @param outputName
	 *            the name of the output
	 * @param nodes
	 */
	public RenderPath(Node root, String outputName, Node... nodes) {
		mNodes = Arrays.asList(nodes);
		mRoot = root;
		mOutput = mRoot.getOutputId(outputName);
	}

	/**
	 * Sets the {@link Context} used by this {@link RenderPath}
	 * @param helper
	 */
	public void setRenderHelper(Context helper) {
		mRenderHelper = helper;
	}
	
	/**
	 * Resets all cached outputs of any {@link Node}s in this {@link RenderPath}
	 */
	public void prepare() {
		for (Node node : mNodes)
			node.prepare();
	}

	/**
	 * Requests output from the root of this {@link RenderPath} thereby forcing
	 * a render of the current data
	 */
	public void pollFrame() {
		if (mRoot.getOutput(mOutput, Integer.class) < 0)
			throw new AtticRuntimeException("Rendering failed with log: " + mRenderHelper.getLog());
	}
}
