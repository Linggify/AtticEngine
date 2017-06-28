package com.github.linggify.attic.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.linggify.attic.render.IContext.VertexAttribute;
import com.github.linggify.attic.util.Matrix33;

/**
 * {@link RenderData} contains the data needed to render one specific object on
 * the screen. (e.g.: layer, vertices, indices, textures etc.)
 * 
 * @author Fredie
 *
 */
public class RenderData {

	private String mLayer;
	private boolean mIsStatic;

	private Matrix33 mTransform;

	private Set<VertexAttribute> mAttributes;
	private int[] mIndices;

	private Map<VertexAttribute, List<VertexData>> mVertexData;
	private int mVertexCount;

	/**
	 * Creates new {@link RenderData} on the layer "default"
	 */
	public RenderData(int size, boolean isStatic, VertexAttribute... attributes) {
		this(size, "default", isStatic, attributes);
	}

	/**
	 * Creates new RenderData on the given layer
	 * 
	 * @param layer
	 */
	public RenderData(int size, String layer, boolean isStatic, VertexAttribute... attributes) {
		mLayer = layer;
		mIsStatic = isStatic;
		mAttributes = new HashSet<>();
		mTransform = new Matrix33();

		mVertexCount = size;
		mVertexData = new HashMap<>();
		for (VertexAttribute attribute : attributes) {
			mAttributes.add(attribute);
			mVertexData.put(attribute, new ArrayList<>(mVertexCount));
		}
	}

	/**
	 * Sets the {@link Matrix33} used by this {@link RenderData}<
	 * @param transform
	 */
	public void setTransform(Matrix33 transform) {
		mTransform = transform;
	}
	
	/**
	 * 
	 * @return the {@link Matrix33} used as a transform by this
	 *         {@link RenderData} for manipulation
	 */
	public Matrix33 getTransform() {
		return mTransform;
	}

	/**
	 * 
	 * @return the layer this {@link RenderData} is rendered on
	 */
	public String getLayer() {
		return mLayer;
	}

	/**
	 * 
	 * @return whether this {@link RenderData} is static or not
	 */
	public boolean isStatic() {
		return mIsStatic;
	}

	/**
	 * 
	 * @return an array containing the vertex-attributes used by this
	 *         {@link RenderData}
	 */
	public Set<VertexAttribute> getAttributes() {
		return mAttributes;
	}

	// TODO implement actual functionality

	/**
	 * Sets the data for a given vertex for the given attribute
	 * 
	 * @param vertex
	 * @param attribute
	 * @param data
	 */
	public void setVertexData(int vertex, VertexAttribute attribute, VertexData data) {
		mVertexData.get(attribute).set(vertex, data);
	}

	/**
	 * 
	 * @param vertex
	 * @param attribute
	 * @return the vertex-data for the given vertex and {@link VertexAttribute}
	 *         combination
	 */
	public VertexData getVertexData(int vertex, VertexAttribute attribute) {
		return mVertexData.get(attribute).get(vertex);
	}

	/**
	 * 
	 * @return how many vertices are used
	 */
	public int getVertexCount() {
		return mVertexCount;
	}

	/**
	 * Sets the indices of this {@link RenderData}
	 * 
	 * @param indices
	 */
	public void setIndices(int[] indices) {
		mIndices = indices;
	}

	/**
	 * 
	 * @return the indices of this {@link RenderData}
	 */
	public int[] getIndices() {
		return mIndices;
	}

	/**
	 * An interface that must be implemented by data-types used for rendering
	 * 
	 * @author Freddy
	 *
	 */
	public interface VertexData {

		/**
		 * 
		 * @return a byte array containing all data of the datatype relevant for
		 *         rendering
		 */
		public byte[] asBytes();
	}
}