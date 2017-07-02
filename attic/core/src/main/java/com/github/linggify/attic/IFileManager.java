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
public interface IFileManager {

	/**
	 * 
	 * @return a String representing the path to the local directory
	 */
	String getLocalPath();
	
	/**
	 * Opens a Stream to the file at the given path, relative to the location of the application
	 * @param path
	 * @return an InputStream reading from the given file
	 * @throws AtticRuntimeException if there is no file or it cannot be read
	 */
	IFileHandle getLocal(String path) throws AtticRuntimeException;
	
	/**
	 * Opens a Stream to the file at the given path
	 * @param path
	 * @return an InputStream reading from the given file
	 * @throws AtticRuntimeException if there is no file or it cannot be read
	 */
	IFileHandle getAbsolute(String path) throws AtticRuntimeException;
	
	/**
	 * IFileHandle is a wrapper for basic file-activity
	 * 
	 * @author Freddy
	 *
	 */
	public interface IFileHandle {

		/**
		 *
		 * @return the name of the file
		 */
		String getName();

		/**
		 *
		 * @return the ending of the file
		 */
		String getEnding();

		/**
		 * 
		 * @return whether the {@link IFileHandle} exists or not
		 */
		boolean exists();
		
		/**
		 * 
		 * @return whether this {@link IFileHandle} is a directory
		 */
		boolean isDirectory();

		/**
		 *
		 * @return the parent filehandle of this one or null if there is none
		 */
		IFileHandle parent();

		/**
		 *
		 * @param path
		 * @return the file with the given path inside this directory, or null if this is not a directory
		 */
		IFileHandle child(String path);
		
		/**
		 * 
		 * @return reads a String from the file
		 * @throws AtticRuntimeException if the file does not exist
		 */
		String readString() throws AtticRuntimeException;
		
		/**
		 * Writes the given String to the file
		 * @param value
		 * @throws AtticRuntimeException if the file does not exist
		 */
		void writeString(String value) throws AtticRuntimeException;
		
		/**
		 * 
		 * @return an InputStream reading from the file
		 * @throws AtticRuntimeException if the file does not exist
		 */
		InputStream openInput() throws AtticRuntimeException;
		
		/**
		 * 
		 * @return an OutputStream writing to the file
		 * @throws AtticRuntimeException if the file does not exist
		 */
		OutputStream openOutput() throws AtticRuntimeException;
	}
}
