package com.github.linggify.attic.lwjgl;

import static org.lwjgl.opengl.GL15.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import com.github.linggify.attic.exceptions.AtticRuntimeException;

public class LwjglVertexBuffer {

	private int mBufferHandle;
	private int mSize;
	private int mVertexSize;
	
	private int mFreeBytes;
	private List<BufferSegment> mSegments;

	/**
	 * Creates a new {@link LwjglVertexBuffer} of the given size (in bytes)
	 * 
	 * @param size
	 */
	public LwjglVertexBuffer(int size, int vertexSize, boolean isStatic) {
		int mBufferHandle = glGenBuffers();
		if (mBufferHandle == 0)
			throw new AtticRuntimeException("Failed to create GPU-side buffer");

		// init GPU-side buffer with arbitrary values
		ByteBuffer buffer = MemoryUtil.memAlloc(size);
		glBufferData(mBufferHandle, buffer, isStatic ? GL15.GL_STATIC_DRAW : GL15.GL_DYNAMIC_DRAW);
		MemoryUtil.memFree(buffer);

		mSize = size;
		mVertexSize = vertexSize;
		mFreeBytes = mSize;
		mSegments = new ArrayList<>();
		mSegments.add(new BufferSegment(mBufferHandle, 0, mSize, mVertexSize));
	}

	/**
	 * Allocates a {@link BufferSegment} of the given length in this
	 * {@link LwjglVertexBuffer}
	 * 
	 * @param length
	 * @return the allocated {@link BufferSegment} or null of there is not
	 *         enough space
	 */
	public BufferSegment allocate(int length) {
		if (mFreeBytes < length)
			return null;

		// find the best fitting BufferSegment
		int clen = Integer.MAX_VALUE;
		BufferSegment fit = null;
		for (BufferSegment segment : mSegments) {
			if (!segment.isUsed() && segment.length() >= length && segment.length() < clen) {
				fit = segment;
				clen = segment.length();
			}
		}

		// if no fitting segment was found, group all free bytes together
		if (fit == null) {
			// sort segments and try again
			// this algorithm also ensures that every used segment is moved only
			// once
			mSegments.sort(null);
			for (int i = 0; i < mSegments.size(); i++) {
				BufferSegment a = mSegments.get(i);
				BufferSegment b = mSegments.get(i + 1);

				if (!a.isUsed() && b.isUsed()) {
					int aoff = a.index(0);
					int boff = b.index(0);

					a.move(boff);
					b.move(aoff);
				} else if (!a.isUsed() && !b.isUsed()) {
					a.setLength(a.length() + b.length());
					mSegments.remove(i + 1);
					i--;
				}
			}

			// at this point all free bytes should be in a single BufferSegment
			// at the end of the list
			fit = mSegments.get(mSegments.size() - 1);
		}

		if (fit.length() == length)
			return fit;

		int toffset = fit.index(0);
		fit.setLength(fit.length() - length);
		fit.move(toffset + length);

		BufferSegment segment = new BufferSegment(mBufferHandle, toffset, length, mVertexSize);
		mSegments.add(segment);
		segment.use();
		
		return segment;
	}

	/**
	 * Binds this {@link LwjglVertexBuffer} to the given target
	 * 
	 * @param target
	 */
	public void bindTo(int target) {
		glBindBuffer(target, mBufferHandle);
	}

	/**
	 * unbinds the currently bound buffer from the given target
	 * 
	 * @param target
	 */
	public void unbindFrom(int target) {
		glBindBuffer(target, 0);
	}

	/**
	 * Destroys this {@link LwjglVertexBuffer} making it unusable in the process
	 */
	public void destroy() {
		//clear all segments
		for(BufferSegment segment : mSegments)
			segment.clear();
		
		//delete the GPU-side buffer
		glDeleteBuffers(mBufferHandle);
	}
	
	/**
	 * BufferSegments represent a segment in the underlying vertex-buffer
	 * 
	 * @author Freddy
	 *
	 */
	public class BufferSegment implements Comparable<BufferSegment> {

		private int mOffset;
		private int mVertexOffset;
		private int mVertexSize;
		private int mLength;
		private int mBuffer;
		private boolean mUsed;
		private boolean mIsStatic;

		private byte[] mData;
		private ByteBuffer mRamBuffer;

		/**
		 * Creates a new {@link BufferSegment}
		 * 
		 * @param offset
		 * @param length
		 */
		private BufferSegment(int buffer, int offset, int length, int vertexsize) {
			mOffset = offset;
			mLength = length;
			mBuffer = buffer;
			mVertexSize = vertexsize;
			mVertexOffset = mOffset / mVertexSize;
		}

		/**
		 * 
		 * @return the length of this {@link BufferSegment}
		 */
		private int length() {
			return mLength;
		}

		/**
		 * Sets the length of this {@link BufferSegment}
		 * 
		 * @param length
		 */
		private void setLength(int length) {
			if (mUsed)
				throw new AtticRuntimeException("Cannot change the length of a BufferSegment that is already in use");
			mLength = length;
		}
		
		/**
		 * Moves this BufferSegment to the new offset
		 * 
		 * @param dest
		 */
		private void move(int dest) {
			mOffset = dest;

			// if this BufferSegment is not in use, do nothing else
			if (!mUsed)
				return;

			if (mIsStatic) {
				// if this BufferSegment is static, the buffer already exists
				glBufferSubData(mBuffer, mOffset, mRamBuffer);
			} else {
				mRamBuffer = MemoryUtil.memAlloc(mLength);
				mRamBuffer.put(mData);
				glBufferSubData(mBuffer, mOffset, mRamBuffer);
				MemoryUtil.memFree(mRamBuffer);
			}
		}

		/**
		 * Marks this BufferSegment as used
		 */
		private void use() {
			mUsed = true;
			
			if(!mIsStatic)
				mRamBuffer = MemoryUtil.memAlloc(mLength);
		}
		
		/**
		 * Sets the data in this {@link BufferSegment}. Using <code>null</code>
		 * not do anything
		 * 
		 * @param data
		 */
		public void set(byte[] data) {
			if(!mUsed)
				throw new AtticRuntimeException("BufferSegment is not in use and cant accept data");
			
			if (data.length > mLength)
				throw new AtticRuntimeException(
						"Data is too large. Expected " + mLength + " bytes but got " + data.length);

			if (!mUsed && !mIsStatic)
				mRamBuffer = MemoryUtil.memAlloc(mLength);


			if (mIsStatic)
				mRamBuffer.put(data);
			else
				mData = data;
			move(mOffset);
		}

		/**
		 * Sets whether this {@link BufferSegment} is used for static or dynamic
		 * interaction. If set to static, the BufferSegment will not keep a
		 * ByteBuffer in offheap memory for faster moving and changing values,
		 * but instead create a new one everytime its data is changed
		 * 
		 * @param flag
		 */
		public void setStatic(boolean flag) {
			mIsStatic = flag;

			if (mUsed && mIsStatic && mRamBuffer != null)
				MemoryUtil.memFree(mRamBuffer);
			else if (mUsed && !mIsStatic && mRamBuffer == null)
				mRamBuffer = MemoryUtil.memAlloc(mLength);
		}

		/**
		 * Converts an index which is relative to this {@link BufferSegment} to
		 * be relative to the underlying-buffer
		 * 
		 * @param index
		 * @return the given index relative to the underlying buffer
		 */
		public int index(int index) {
			return mVertexOffset + index;
		}

		/**
		 * Clears all memory used by this {@link BufferSegment}
		 * and marks it as unused
		 */
		public void clear() {
			mUsed = false;
			
			if(!mIsStatic)
				MemoryUtil.memFree(mRamBuffer);
			else mData = null;
		}
		
		/**
		 * 
		 * @return whether this {@link BufferSegment} is used or not
		 */
		public boolean isUsed() {
			return mUsed;
		}

		@Override
		public int compareTo(BufferSegment o) {
			return Integer.compare(mOffset, o.mOffset);
		}
	}
}
