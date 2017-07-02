package com.github.linggify.attic.render;

import com.github.linggify.attic.logic.IProperty;

/**
 * Batches are groups of geometry batched together to reduce driver overhead when rendering.
 * their implementation may be platform-specific.
 * 
 * A Batch recieves its Vertex-attributes at creation.
 * 
 * @author Fredie
 *
 */
public interface IBatch {
	
	/**
	 * Tries to add the given {@link IProperty} to this {@link IBatch} for rendering
	 * @param property
	 * @return true if the given Property was accepted
	 */
	boolean accept(IProperty<RenderData> property);
}
