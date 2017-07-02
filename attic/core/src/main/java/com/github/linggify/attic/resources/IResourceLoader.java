package com.github.linggify.attic.resources;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.IFileManager;

/**
 * {@link IResourceLoader}s are used to load specific files as resources
 * @author Freddy
 */
public interface IResourceLoader {

    /**
     *
     * @return the type of resource this {@link IResourceLoader} loads
     */
    String loadingType();

    /**
     * Sets the {@link Application} to load the resources into
     * @param app
     */
    void setApplication(Application app);

    /**
     * Loads the given resource
     * @param file
     * @return the handle of the resource
     */
    int load(IFileManager.IFileHandle file);

    /**
     * Destroys the given resource, thereby making it unusable
     * @param handle
     */
    void destroy(int handle);
}
