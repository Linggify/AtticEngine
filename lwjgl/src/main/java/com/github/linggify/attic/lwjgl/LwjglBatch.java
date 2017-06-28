package com.github.linggify.attic.lwjgl;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.linggify.attic.logic.IProperty;
import com.github.linggify.attic.logic.IProperty.PropertyEvent;
import com.github.linggify.attic.logic.IProperty.PropertyListener;
import com.github.linggify.attic.lwjgl.LwjglVertexBuffer.BufferSegment;
import com.github.linggify.attic.render.IBatch;
import com.github.linggify.attic.render.RenderData;
import com.github.linggify.attic.render.RenderData.VertexData;
import com.github.linggify.attic.util.Pair;
import com.github.linggify.attic.render.IContext;
import com.github.linggify.attic.render.IContext.VertexAttribute;

import static org.lwjgl.opengl.GL11.*;

/**
 * A LwjglBatch is used to render using the {@link LwjglBackend}
 * 
 * @author Freddy
 *
 */
public class LwjglBatch implements IBatch {
	
	private List<Pair<BufferSegment, RenderData>> mProperties;
	private List<Boolean> mActiveProperties;
	private List<Integer> mFreeIds;

	private Set<VertexAttribute> mAttributeSet;
	private VertexAttribute[] mAttributes;
	private int mVertexSize;

	private int[] mIndices;
	private int mHighIndex;
	private boolean mIndicesDirty;
	
	private LwjglVertexArray mBuffer;
	
	/**
	 * Creates a new {@link LwjglBatch}
	 */
	public LwjglBatch(int size, boolean isStatic, VertexAttribute... attributes) {
		mProperties = new ArrayList<>();
		mActiveProperties = new ArrayList<>();
		mFreeIds = new LinkedList<>();

		mAttributes = attributes;
		mAttributeSet = new HashSet<>(Arrays.asList(attributes));

		mVertexSize = 0;
		for (VertexAttribute attribute : mAttributes)
			mVertexSize += attribute.offset();
		
		mBuffer = new LwjglVertexArray(new LwjglVertexBuffer(mVertexSize * size, mVertexSize, isStatic), mAttributes);
	}

	/**
	 * Packs the indices of this {@link LwjglBatch}
	 */
	private void packIndices() {
		if (mIndicesDirty) {
			int indexCount = 0;
			for (int i = 0; i < mProperties.size(); i++) {
				if (mActiveProperties.get(i))
					indexCount += mProperties.get(i).getValue().getIndices().length;
			}

			if (mIndices.length < indexCount)
				mIndices = new int[indexCount];
			mHighIndex = indexCount;

			int ioff = 0;
			for (int i = 0; i < mProperties.size(); i++) {
				if (mActiveProperties.get(i)) {
					Pair<BufferSegment, RenderData> data = mProperties.get(i);
					int[] indices = data.getValue().getIndices();
					for (int c = 0; c < indices.length; c++) {
						mIndices[c + ioff] = data.getKey().index(indices[c]);
					}

					ioff += indices.length;
				}
			}

			mBuffer.setIndices(mIndices);
			mIndicesDirty = false;
		}
	}

	/**
	 * Puts the given {@link RenderData} into the underlying buffer at the given
	 * offset
	 * 
	 * @param data
	 * @param offset
	 */
	private void put(RenderData data, BufferSegment segment) {
		byte[] bdata = new byte[mVertexSize * data.getVertexCount()];
		int off = 0;
		for (int i = 0; i < data.getVertexCount(); i++) {
			for (int c = 0; c < mAttributes.length; c++) {
				VertexData vdata = data.getVertexData(i, mAttributes[c]);

				int aSize = mAttributes[c].elementCount() * mAttributes[c].type().size();
				if (vdata != null) {
					byte[] vbdata = vdata.asBytes();
					for (int j = 0; j < aSize; j++)
						bdata[off + j] = vbdata[j];
				}
				off += aSize;
			}
		}
		segment.set(bdata);
		mIndicesDirty = true;
	}

	/**
	 * Renders this {@link LwjglBatch} with the currently active
	 * shader
	 * @param helper
	 */
	public void render(IContext helper) {
		packIndices();
		mBuffer.bind(helper);
		glDrawElements(GL_TRIANGLES, mHighIndex, GL_INT, 0);
		mBuffer.unbind();
	}

	/**
	 * Destroys this {@link LwjglBatch} making it unusable
	 */
	public void destroy() {
		mProperties.clear();
		mActiveProperties.clear();
		mFreeIds.clear();
		mAttributes = null;
		mIndices = null;
		
		mBuffer.destroy();
	}
	
	@Override
	public boolean accept(IProperty<RenderData> property) {
		RenderData data = property.get();

		// check if the renderdatas attributes match the batches attributes
		if (!mAttributeSet.equals(data.getAttributes()))
			return false;

		// check if there is enough space in this LwjglBatch for the renderdata
		int size = mVertexSize * data.getVertexCount();
		BufferSegment segment = mBuffer.allocate(size);
		if (segment == null)
			return false;

		// if allocation succeded, add the property to the batch
		put(data, segment);

		int preId;
		if (!mFreeIds.isEmpty()) {
			preId = mFreeIds.remove(0);
			mProperties.set(preId, new Pair<>(segment, data));
			mActiveProperties.set(preId, property.isActive());
		} else {
			preId = mProperties.size();
			mProperties.add(new Pair<>(segment, data));
			mActiveProperties.add(property.isActive());
		}
		final int id = preId;

		property.addListener(new PropertyListener() {
			public void onEvent(IProperty<?> p, PropertyEvent e) {
				switch (e) {
				case PROPERTY_CHANGED:
					if (mActiveProperties.get(id)) {
						Pair<BufferSegment, RenderData> prop = mProperties.get(id);
						put(prop.getValue(), prop.getKey());
					}
					break;
				case PROPERTY_DISABLED:
					mActiveProperties.set(id, false);
					mIndicesDirty = true;
					break;
				case PROPERTY_ENABLED:
					mActiveProperties.set(id, true);
					Pair<BufferSegment, RenderData> prop = mProperties.get(id);
					put(prop.getValue(), prop.getKey());
					mIndicesDirty = true;
					break;
				case PROPERTY_REMOVED:
					mProperties.get(id).getKey().clear();
					mProperties.set(id, null);
					mActiveProperties.set(id, false);
					mFreeIds.add(id);
					p.removeListener(this);
					break;
				default:
					// do nothing
					break;
				}
			}
		});

		return true;
	}
}
