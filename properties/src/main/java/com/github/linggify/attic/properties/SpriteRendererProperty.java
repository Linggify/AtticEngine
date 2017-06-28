package com.github.linggify.attic.properties;

import com.github.linggify.attic.render.IContext.VertexAttribute;
import com.github.linggify.attic.render.RenderData;
import com.github.linggify.attic.util.Color;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Vector2D;

/**
 * The SpriteRendererProperty is used to render Textured Quads at a specific
 * transorm
 * 
 * @author Fredie
 *
 */
public class SpriteRendererProperty extends BaseProperty<RenderData> {

	private RenderData mData;

	private boolean mHasTransform;
	private PropertyListener mTransformChangeListener;
	private Matrix33 mTransform;

	/**
	 * Creates a new {@link SpriteRendererProperty} with static or non static
	 * {@link RenderData}
	 * 
	 * @param isStatic
	 */
	public SpriteRendererProperty(boolean isStatic) {
		super();
		
		// setup sprite
		mData = new RenderData(4, isStatic, VertexAttribute.POSITION, VertexAttribute.COLOR,
				VertexAttribute.TEX_COORD_0);
		// corners
		mData.setVertexData(0, VertexAttribute.POSITION, new Vector2D(0.5f, -0.5f));
		mData.setVertexData(1, VertexAttribute.POSITION, new Vector2D(-0.5f, -0.5f));
		mData.setVertexData(2, VertexAttribute.POSITION, new Vector2D(-0.5f, 0.5f));
		mData.setVertexData(3, VertexAttribute.POSITION, new Vector2D(0.5f, 0.5f));
		// color white
		Color white = new Color(1, 1, 1, 1);
		mData.setVertexData(0, VertexAttribute.COLOR, white);
		mData.setVertexData(1, VertexAttribute.COLOR, white);
		mData.setVertexData(2, VertexAttribute.COLOR, white);
		mData.setVertexData(3, VertexAttribute.COLOR, white);
		// texture coordinates
		mData.setVertexData(0, VertexAttribute.TEX_COORD_0, new Vector2D(1, 1));
		mData.setVertexData(1, VertexAttribute.TEX_COORD_0, new Vector2D(0, 1));
		mData.setVertexData(2, VertexAttribute.TEX_COORD_0, new Vector2D(0, 0));
		mData.setVertexData(3, VertexAttribute.TEX_COORD_0, new Vector2D(1, 0));

		// use transform changelistener
		mHasTransform = false;
		mTransformChangeListener = (target, event) -> {
			if (mHasTransform) {
				if (target instanceof TransformProperty) {
					if (event == PropertyEvent.PROPERTY_CHANGED)
						mTransform = ((TransformProperty) target).get();
					else if (event == PropertyEvent.PROPERTY_REMOVED || event == PropertyEvent.PROPERTY_DISABLED) {
						target.removeListener(mTransformChangeListener);
						mHasTransform = false;
						mTransform = new Matrix33();
					}
				}
			}
		};
	}

	/**
	 * Sets the Color of this {@link SpriteRendererProperty}
	 * 
	 * @param color
	 */
	public void setTint(Color color) {
		// color white
		mData.setVertexData(0, VertexAttribute.COLOR, color);
		mData.setVertexData(1, VertexAttribute.COLOR, color);
		mData.setVertexData(2, VertexAttribute.COLOR, color);
		mData.setVertexData(3, VertexAttribute.COLOR, color);
	}

	@Override
	public Class<RenderData> getContentType() {
		return RenderData.class;
	}

	@Override
	public void update(double delta) {
		// do nothing here
	}

	@Override
	public RenderData get() {
		//if there is no transform, search for one
		if(!mHasTransform && hasParent()) {
			TransformProperty property = getParent().propertyByType(TransformProperty.class);
			if(property != null) {
				property.addListener(mTransformChangeListener);
				mHasTransform = true;
			}
		}
		
		//set transform and return
		mData.setTransform(mTransform);
		return mData;
	}

}
