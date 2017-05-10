package com.github.linggify.attic.properties;

import java.util.HashSet;
import java.util.Set;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.logic.Property;
import com.github.linggify.attic.render.Context.VertexAttribute;
import com.github.linggify.attic.render.RenderData;
import com.github.linggify.attic.util.Color;
import com.github.linggify.attic.util.Vector2D;

/**
 * The SpriteRendererProperty is used to render Textured Quads at a specific transorm
 * @author Fredie
 *
 */
public class SpriteRendererProperty implements Property<RenderData>{
	
	private Entity mParent;
	private boolean mIsActive;
	private Set<PropertyListener> mListeners;
	
	private RenderData mData;
	
	/**
	 * Creates a new {@link SpriteRendererProperty} with static or non static {@link RenderData}
	 * @param isStatic
	 */
	public SpriteRendererProperty(boolean isStatic) {
		mListeners = new HashSet<>();
		mParent = null;
		
		//is active by default
		mIsActive = true;

		//setup sprite
		mData = new RenderData(4, isStatic, VertexAttribute.POSITION, VertexAttribute.COLOR, VertexAttribute.TEX_COORD_0);
		//corners
		mData.setVertexData(0, VertexAttribute.POSITION, new Vector2D(0.5f, -0.5f));
		mData.setVertexData(1, VertexAttribute.POSITION, new Vector2D(-0.5f, -0.5f));
		mData.setVertexData(2, VertexAttribute.POSITION, new Vector2D(-0.5f, 0.5f));
		mData.setVertexData(3, VertexAttribute.POSITION, new Vector2D(0.5f, 0.5f));
		//color white
		Color white = new Color(1, 1, 1, 1);
		mData.setVertexData(0, VertexAttribute.COLOR, white);
		mData.setVertexData(1, VertexAttribute.COLOR, white);
		mData.setVertexData(2, VertexAttribute.COLOR, white);
		mData.setVertexData(3, VertexAttribute.COLOR, white);
		//texture coordinates
		mData.setVertexData(0, VertexAttribute.TEX_COORD_0, new Vector2D(1, 1));
		mData.setVertexData(1, VertexAttribute.TEX_COORD_0, new Vector2D(0, 1));
		mData.setVertexData(2, VertexAttribute.TEX_COORD_0, new Vector2D(0, 0));
		mData.setVertexData(3, VertexAttribute.TEX_COORD_0, new Vector2D(1, 0));
	}
	
	/**
	 * Sets the Color of this {@link SpriteRendererProperty}
	 * @param color
	 */
	public void setTint(Color color) {
		//color white
		mData.setVertexData(0, VertexAttribute.COLOR, color);
		mData.setVertexData(1, VertexAttribute.COLOR, color);
		mData.setVertexData(2, VertexAttribute.COLOR, color);
		mData.setVertexData(3, VertexAttribute.COLOR, color);
	}
	
	@Override
	public void addListener(PropertyListener listener) {
		mListeners.add(listener);
	}

	@Override
	public boolean removeListener(PropertyListener listener) {
		return mListeners.remove(listener);
	}

	@Override
	public void onAttach(Entity parent) throws AtticRuntimeException {
		if(mParent != null)
			throw new AtticRuntimeException("Property is already attached to an Entity");
		
		mParent = parent;
		for(PropertyListener listener : mListeners)
			listener.onEvent(this, PropertyEvent.PROPERTY_ADDED);
	}

	@Override
	public void onDetach() throws AtticRuntimeException {
		if(mParent == null)
			throw new AtticRuntimeException("Property is not attached to an Entity");
		
		mParent = null;
		for(PropertyListener listener : mListeners)
			listener.onEvent(this, PropertyEvent.PROPERTY_REMOVED);
	}

	@Override
	public void setActive(boolean flag) {
		if(flag ^ mIsActive) {
			mIsActive = flag;
			for(PropertyListener listener : mListeners)
				listener.onEvent(this, mIsActive ? PropertyEvent.PROPERTY_ENABLED : PropertyEvent.PROPERTY_DISABLED);
		}
	}

	@Override
	public boolean isActive() {
		return mIsActive;
	}

	@Override
	public Class<RenderData> getContentType() {
		return RenderData.class;
	}

	@Override
	public void update(double delta) {
		//do nothing here
	}

	@Override
	public RenderData get() {
		return mData;
	}

}
