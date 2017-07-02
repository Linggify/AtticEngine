package com.github.linggify.attic.resources;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.IFileManager;
import com.github.linggify.attic.render.IContext;
import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

/**
 * An {@link IResourceLoader} that loads shaders into the context
 * @author Freddy
 */
@MetaInfServices
public class ShaderLoader implements IResourceLoader {

    private IContext mContext;

    @Override
    public String loadingType() {
        return "shader";
    }

    @Override
    public void setApplication(Application app) {
        mContext = app.renderer().context();
    }

    @Override
    public int load(IFileManager.IFileHandle file) {
        //read properties from json file
        JSONObject obj = new JSONObject(file.readString()).getJSONObject("shader");
        String vertexLoc = obj.getString("vertexShader");
        String fragmentLoc = obj.getString("fragmentShader");

        //load the shader with the current context
        IFileManager.IFileHandle sDir = file.parent();
        return mContext.genShader(sDir.child(vertexLoc).readString(), sDir.child(fragmentLoc).readString());
    }

    @Override
    public void destroy(int handle) {
        //try destroying the given handle
        mContext.destroyShader(handle);
    }
}
