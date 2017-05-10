package com.github.linggify.attic.render;

import com.github.linggify.attic.logic.Property;

/**
 * Batches are groups of geometry batched together to reduce driver overhead when rendering.
 * their implementation may be platform-specific.
 * 
 * A Batch recieves its Vertex-attributes at creation.
 * 
 * @author Fredie
 *
 */
public interface Batch {
	
	/**
	 * Tries to add the given {@link Property} to this {@link Batch} for rendering
	 * @param property
	 * @return true if the given Property was accepted
	 */
	public boolean accept(Property<RenderData> property);
}
