package com.github.linggify.attic.nodes;

import java.util.ArrayList;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Context;
import com.github.linggify.attic.render.path.Node;

/**
 * A Node used to clear all Textures connected to its inputs
 * @author Freddy
 *
 */
public class ClearTexturesNode implements Node{

	public static final String TEXTURE_LINK = "texture_";
	
	private Context mContext;
	
	private boolean mCleared;
	
	private ArrayList<Input> mTargets;
	
	/**
	 * Creates a new {@link ClearTexturesNode}
	 */
	public ClearTexturesNode() {
		mTargets = new ArrayList<>();
	}
	
	@Override
	public void setRenderHepler(Context helper) {
		mContext = helper;
	}

	@Override
	public void setInput(String name, Input input) {
		if(name.startsWith(TEXTURE_LINK)) {
			int id = Integer.parseInt(name.split("_")[1]);
			while(id >= mTargets.size()) mTargets.add(null);
			mTargets.set(id, input);
		}
	}

	@Override
	public void prepare() {
		mCleared = false;
	}

	@Override
	public int getOutputId(String name) {
		if(name.startsWith(TEXTURE_LINK))
			return Integer.parseInt(name.split("_")[1]);
		
		throw new AtticRuntimeException(name + " is not a valid output in a ClearTexturesNode");
	}

	@Override
	public <T> T getOutput(int index, Class<T> type) throws AtticRuntimeException {
		if(!mCleared) {
			int maxTargets = mContext.getMaxRenderTargets();
			int count = 0;
			while(count < mTargets.size()) {
				int tmpCount = count;
				for(int i = 0; i < maxTargets && count < mTargets.size(); i++, count++) {
					if(mTargets.get(count) != null)
						mContext.bindRenderTarget(i, mTargets.get(count).getValue(Integer.class));
				}
				mContext.clearRenderTargets();
				for(int i = tmpCount; i < count; i++) {
					mContext.unbindRenderTarget(i);
				}
			}
		}
		
		return mTargets.get(index).getValue(type);
	}

}
