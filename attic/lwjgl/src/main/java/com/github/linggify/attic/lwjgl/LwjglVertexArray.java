package com.github.linggify.attic.lwjgl;

import com.github.linggify.attic.lwjgl.LwjglVertexBuffer.BufferSegment;
import com.github.linggify.attic.render.IContext;
import com.github.linggify.attic.render.IContext.VertexAttribute;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;

public class LwjglVertexArray {

	private int mArrayHandle;
	private int mIndicesHandle;
	private LwjglVertexBuffer mBuffer;
	private VertexAttribute[] mAttributes;
	
	/**
	 * Creates a new {@link LwjglVertexArray} using the given {@link LwjglVertexBuffer}
	 * @param buffer
	 */
	public LwjglVertexArray(LwjglVertexBuffer buffer, VertexAttribute...attributes) {
		mAttributes = attributes;
		mBuffer = buffer;
		
		mArrayHandle = glGenVertexArrays();
		glBindVertexArray(mArrayHandle);
		buffer.bindTo(GL_ARRAY_BUFFER);
		
		int globalOffset = 0;
		for(int i = 0; i < mAttributes.length; i++) {
			VertexAttribute attribute = mAttributes[i];
			
			int glType = 0;
			switch(attribute.type()) {
			case FLOAT:
				glType = GL_FLOAT;
				break;
			case INTEGER:
				glType = GL_INT;
				break;
			case SHORT:
				glType = GL_SHORT;
				break;
			case BYTE:
				glType = GL_BYTE;
				break;
			}
			glVertexAttribIPointer(i, attribute.elementCount(), glType, 0, globalOffset);
			globalOffset += attribute.elementCount();
		}
		buffer.unbindFrom(GL_ARRAY_BUFFER);
		
		mIndicesHandle = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndicesHandle);
		
		glBindVertexArray(0);
	}
	
	/**
	 * Allocates a {@link BufferSegment} as specified in {@link LwjglVertexBuffer#allocate(int)}
	 * @param size
	 * @return the allocated BufferSegment
	 */
	public BufferSegment allocate(int size) {
		return mBuffer.allocate(size);
	}
	
	/**
	 * Sets the indices of this {@link LwjglVertexArray}
	 * @param indices
	 */
	public void setIndices(int[] indices) {
		glBindVertexArray(mArrayHandle);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndicesHandle);
		glBufferData(mIndicesHandle, indices, GL_STATIC_DRAW);
		glBindVertexArray(0);
	}
	
	/**
	 * Binds this {@link LwjglVertexArray} for rendering and sets the VertexAttributes to the currently active shader
	 * @param helper
	 */
	public void bind(IContext helper) {
		glBindVertexArray(mArrayHandle);
		for(int i = 0; i < mAttributes.length; i++) {
			helper.setAttribute(mAttributes[i].attribute(), i);
			glEnableVertexAttribArray(i);
		}
	}
	
	/**
	 * Unbinds this {@link LwjglVertexArray} and disables all associated {@link VertexAttribute}s
	 */
	public void unbind() {
		for(int i = 0; i < mAttributes.length; i++)
			glDisableVertexAttribArray(i);
		
		glBindVertexArray(0);
	}
	
	/**
	 * Destroys this {@link LwjglVertexArray} thereby making it unusable
	 */
	public void destroy() {
		mBuffer.destroy();
		
		//delete indices and vertexarray
		glDeleteBuffers(mIndicesHandle);
		glDeleteVertexArrays(mArrayHandle);
	}
}
