package com.github.linggify.attic.render.path;

import java.util.Arrays;
import java.util.List;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.IContext;
import com.github.linggify.attic.render.Renderer;

/**
 * RenderPaths are used to define the method of rendering for a {@link Renderer}
 * 
 * @author Fredie
 *
 */
public class RenderPath {

	private List<INode> mNodes;
	private INode mRoot;
	private int mOutput;
	private IContext mRenderHelper;

	/**
	 * Creates a new {@link RenderPath} with the given list of {@link INode}s
	 * 
	 * @param root
	 *            the node providing the output for the screen
	 * @param outputName
	 *            the name of the output
	 * @param nodes
	 */
	public RenderPath(INode root, String outputName, INode... nodes) {
		mNodes = Arrays.asList(nodes);
		mRoot = root;
		mOutput = mRoot.getOutputId(outputName);
	}

	/**
	 * Sets the {@link IContext} used by this {@link RenderPath}
	 * @param helper
	 */
	public void setRenderHelper(IContext helper) {
		mRenderHelper = helper;
		for (INode node : mNodes)
			node.setRenderHepler(helper);
	}
	
	/**
	 * Resets all cached outputs of any {@link INode}s in this {@link RenderPath}
	 */
	public void prepare() {
		for (INode node : mNodes)
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
