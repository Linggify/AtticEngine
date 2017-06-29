package com.github.linggify.attic.nodes;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.IContext;
import com.github.linggify.attic.render.path.INode;
import com.github.linggify.attic.util.Color;

/**
 * An {@link INode} used to clear the default framebuffer
 * @author ueuui
 *
 */
public class ClearScreenNode implements INode{

	public static final String CLEAR_COLOR = "clear_color";
	
	private IContext mContext;
	private boolean mCleared;
	
	private Input mColor;
	
	@Override
	public void setRenderHepler(IContext helper) {
		mContext = helper;
	}

	@Override
	public void setInput(String name, Input input) {
		if(name.equals(CLEAR_COLOR)) mColor = input;
		else throw new AtticRuntimeException(name + " is not a valid input");
	}

	@Override
	public void prepare() {
		mCleared = false;
	}

	@Override
	public int getOutputId(String name) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOutput(int index, Class<T> type) throws AtticRuntimeException {
		if(!mCleared) {
			mContext.setClearColor(-1, mColor.getValue(Color.class));
			mContext.clearRenderTargets();
		}
		
		Integer result = 0;
		return (T) result;
	}

}
