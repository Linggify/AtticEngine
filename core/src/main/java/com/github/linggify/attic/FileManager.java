package com.github.linggify.attic;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.linggify.attic.exceptions.AtticRuntimeException;

/**
 * A FileManager creates an interface to the file-system of a specific platform
 * 
 * @author Freddy
 *
 */
public interface FileManager {

	/**
	 * 
	 * @return a String representing the path to the local directory
	 */
	public String getLocalPath();
	
	/**
	 * Opens a Stream to the file at the given path, relative to the location of the application
	 * @param path
	 * @return an InputStream reading from the given file
	 * @throws AtticRuntimeException if there is no file or it cannot be read
	 */
	public FileHandle getLocal(String path) throws AtticRuntimeException;
	
	/**
	 * Opens a Stream to the file at the given path
	 * @param path
	 * @return an InputStream reading from the given file
	 * @throws AtticRuntimeException if there is no file or it cannot be read
	 */
	public FileHandle getAbsolute(String path) throws AtticRuntimeException;
	
	/**
	 * FileHandle is a wrapper for basic file-activity
	 * 
	 * @author Freddy
	 *
	 */
	public interface FileHandle {
		
		/**
		 * 
		 * @return whether the {@link FileHandle} exists or not
		 */
		public boolean exists();
		
		/**
		 * 
		 * @return whether this {@link FileHandle} is a directory
		 */
		public boolean isDirectory();
		
		/**
		 * 
		 * @return reads a String from the file
		 * @throws AtticRuntimeException if the file does not exist
		 */
		public String readString() throws AtticRuntimeException;
		
		/**
		 * Writes the given String to the file
		 * @param value
		 * @throws AtticRuntimeException if the file does not exist
		 */
		public void writeString(String value) throws AtticRuntimeException;
		
		/**
		 * 
		 * @return an InputStream reading from the file
		 * @throws AtticRuntimeException if the file does not exist
		 */
		public InputStream openInput() throws AtticRuntimeException;
		
		/**
		 * 
		 * @return an OutputStream writing to the file
		 * @throws AtticRuntimeException if the file does not exist
		 */
		public OutputStream openOutput() throws AtticRuntimeException;
	}
}
