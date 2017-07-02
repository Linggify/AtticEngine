package com.github.linggify.attic.resources;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.IFileManager;
import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.ServiceLoader;

/**
 * A ResourceManager manages the resources loaded for an {@link com.github.linggify.attic.Application}
 * @author Freddy
 */
public class ResourceManager {

    private HashMap<String, IResourceLoader> mLoaders;
    private HashMap<String, Pair<Integer, String>> mResources;

    private Application mApp;

    /**
     * Creates a new ResourceManager
     */
    public ResourceManager(Application app) {
        mLoaders = new HashMap<>();
        mResources = new HashMap<>();
        mApp = app;

        //load IResourceLoaders as services
        ServiceLoader<IResourceLoader> loaders = ServiceLoader.load(IResourceLoader.class);
        for(IResourceLoader loader : loaders) {
            registerLoader(loader);
        }
    }

    /**
     * Adds a new {@link IResourceLoader} to this {@link ResourceManager}
     * @param loader the {@link IResourceLoader} itself
     */
    public void registerLoader(IResourceLoader loader) {
        loader.setApplication(mApp);
        mLoaders.put(loader.loadingType(), loader);
    }

    /**
     * Loads the given file as a resource
     * @param name the name which is used to retrieve the resource handle later
     * @param type the tag of the {@link IResourceLoader} to use
     * @param file the {@link IFileManager.IFileHandle} to load from
     */
    public void load(String name, String type, IFileManager.IFileHandle file) {
        if(mLoaders.get(type) == null)
            throw new AtticRuntimeException(type + " is not a valid resource-type");

        //load resource
        int id = mLoaders.get(type).load(file);
        mResources.put(name, new Pair<>(id, type));
    }

    /**
     * Destroys the resource with the given name
     * @param name
     */
    public void destroyResource(String name) {
        //only destroy if resource exists
        if(mResources.get(name) != null) {
            Pair<Integer, String> resource = mResources.get(name);
            mLoaders.get(resource.getValue()).destroy(resource.getKey());
        }
    }

    /**
     *
     * @param name
     * @return the handle of the resource with the given name or -1 if no such resource exists
     */
    public int getResourceHandle(String name) {
        if(mResources.get(name) == null)
            return -1;

        return mResources.get(name).getKey();
    }

    /**
     * Loads all the resources described in the given index
     * @param index
     */
    public void loadSynchronous(IFileManager.IFileHandle index) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(index.openInput());
        } catch (ParserConfigurationException e) {
            throw new AtticRuntimeException("Could not create DocumentBuilder");
        } catch (SAXException e) {
            throw new AtticRuntimeException("Could not parse Document");
        } catch (IOException e) {
            throw new AtticRuntimeException("Could not read Document");
        }

        NodeList resourceIds = document.getDocumentElement().getElementsByTagName("Resource");

        IFileManager.IFileHandle resDir = index.parent();

        for(int i = 0; i < resourceIds.getLength(); i++) {
            Node resourceId = resourceIds.item(i);
            NamedNodeMap attribs = resourceId.getAttributes();
            Node name = attribs.getNamedItem("name");
            Node type = attribs.getNamedItem("type");
            Node file = attribs.getNamedItem("file");

            load(name.getTextContent(), type.getTextContent(), resDir.child(file.getTextContent()));
        }
    }
}
