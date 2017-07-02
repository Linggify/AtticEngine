package com.github.linggify.attic.nodes;

import java.util.ArrayList;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.IContext;
import com.github.linggify.attic.render.path.INode;
import com.github.linggify.attic.util.Color;
import com.github.linggify.attic.util.Pair;

/**
 * A Node used to clear all Textures connected to its inputs
 * @author Freddy
 *
 */
public class ClearTexturesNode implements INode{

	public static final String TEXTURE_LINK = "texture_";
	public static final String COLOR_LINK = "color_";
	
	private IContext mContext;
	
	private boolean mCleared;
	
	private ArrayList<Pair<IInput, IInput>> mTargets;
	
	/**
	 * Creates a new {@link ClearTexturesNode}
	 */
	public ClearTexturesNode() {
		mTargets = new ArrayList<>();
	}
	
	@Override
	public void setRenderHepler(IContext helper) {
		mContext = helper;
	}

	@Override
	public void setInput(String name, IInput input) {
		if(name.startsWith(TEXTURE_LINK)) {
			int id = Integer.parseInt(name.split("_")[1]);
			while(id >= mTargets.size()) mTargets.add(null);
			
			if(mTargets.get(id) != null) {
				mTargets.set(id, new Pair<>(input, mTargets.get(id).getValue()));
			} else {
				mTargets.set(id, new Pair<>(input, null));
			}
		} else if(name.startsWith(COLOR_LINK)) {
			int id = Integer.parseInt(name.split("_")[1]);
			while(id >= mTargets.size()) mTargets.add(null);
			
			if(mTargets.get(id) != null) {
				mTargets.set(id, new Pair<>(mTargets.get(id).getValue(), input));
			} else {
				mTargets.set(id, new Pair<>(null, input));
			}
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
					if(mTargets.get(count) != null) {
						Pair<IInput, IInput> element = mTargets.get(count);
						if(element.getKey() != null && element.getValue() != null) {
							mContext.bindRenderTarget(i, element.getKey().getValue(Integer.class));
							mContext.setClearColor(i, element.getValue().getValue(Color.class));
						}
					}
				}
				mContext.clearRenderTargets();
				for(int i = tmpCount; i < count; i++) {
					mContext.unbindRenderTarget(i);
				}
			}
		}
		
		if(mTargets.get(index) == null || mTargets.get(index).getKey() == null)
			throw new AtticRuntimeException("The index " + index + " is not a valid output");
			
		return mTargets.get(index).getKey().getValue(type);
	}

}
