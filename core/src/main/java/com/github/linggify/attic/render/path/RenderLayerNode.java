package com.github.linggify.attic.render.path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Batch;
import com.github.linggify.attic.render.Context;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Pair;
import com.github.linggify.attic.util.Vector2D;

/**
 * A RenderLayerNode is a {@link Node} that renders RenderData from a given Layer
 * to its output (a framebuffer if the Node is not the root of the {@link RenderPath})
 * @author Fredie
 *
 */
public class RenderLayerNode implements Node{

	private Context mHelper;
	
	private Input mShader;
	private Input mLayer;
	private Input mTarget;
	private HashMap<String, Pair<Input, Class<?>>> mUniforms;
	
	private boolean mMappingsDirty;
	private Map<String, Integer> mUniformMappings;
	
	private boolean mDirty;
	
	/**
	 * Creates a new {@link RenderLayerNode}
	 */
	public RenderLayerNode() {
		mUniforms = new HashMap<>();
		mDirty = true;
		mMappingsDirty = true;
	}
	
	/**
	 * Sets a Uniform to use with the shader
	 * @param name the name of the uniform
	 * @param input the input providing the info
	 * @param type the type of info. Should be {@link Matrix33}, {@link Vector2D}, a {@link Number} otherwise it will be ignored
	 */
	public void setUniform(String name, Input input, Class<?> type) {
		mUniforms.put(name, new Pair<>(input, type));
	}
	
	@Override
	public void setRenderHepler(Context helper) {
		mHelper = helper;
	}

	@Override
	public void setInput(String name, Input input) {
		switch(name) {
		case "Shader": mShader = input;
			mMappingsDirty = true;
			break;
		case "Layer": mLayer = input;
			break;
		case "Target": mTarget = input;
			break;
		default: throw new AtticRuntimeException("No Such Input " + name);
		}
	}

	@Override
	public void prepare() {
		mDirty = true;
	}

	@Override
	public int getOutputId(String name) {
		if(name.equals("result"))
			return 0;
		else return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOutput(int index, Class<T> type) throws AtticRuntimeException {
		if(!type.equals(Integer.class))
			throw new AtticRuntimeException("Unexpected Type. Expected Integer but was " + type.getTypeName());
		
		if(mDirty) {
			int shader = mShader.getValue(Integer.class);
			
			//setup shader
			mHelper.bindShader(shader);
			
			//check mappings
			if(mMappingsDirty) {
				mUniformMappings = mHelper.getUniforms(shader);
				mMappingsDirty = false;
			}
			
			//set uniforms
			for(Entry<String, Pair<Input, Class<?>>> entry : mUniforms.entrySet()) {
				String uname = entry.getKey();
				Class<?> utype = entry.getValue().getValue();
				if(utype == Double.class)
					mHelper.setUniformDouble(mUniformMappings.get(uname), entry.getValue().getKey().getValue(Double.class));
				else if(utype == Integer.class)
					mHelper.setUniformInt(mUniformMappings.get(uname), entry.getValue().getKey().getValue(Integer.class));
				else if(utype == Matrix33.class)
					mHelper.setUniformMatrix(mUniformMappings.get(uname), entry.getValue().getKey().getValue(Matrix33.class));
				else if(utype == Vector2D.class)
					mHelper.setUniformVector(mUniformMappings.get(uname), entry.getValue().getKey().getValue(Vector2D.class));
			}
			
			//bind render target
			mHelper.bindRenderTarget(0, mTarget.getValue(Integer.class));
			
			//render frame
			List<Batch> layer = mLayer.getValue(List.class);
			
			//render batch
			for(Batch batch : layer)
				mHelper.renderBatch(batch);
			
			//unbind stuff
			mHelper.unbindRenderTarget(0);
			mHelper.unbindShader();
			
			//remember not to render this node again in this frame
			mDirty = false;
		}
		
		return (T) mTarget.getValue(Integer.class);
	}

}
