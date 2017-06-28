package com.github.linggify.attic.render;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.logic.IProperty;
import com.github.linggify.attic.render.path.INode;
import com.github.linggify.attic.render.path.RenderPath;
import com.github.linggify.attic.render.path.ValueInput;

/**
 * The Renderer is used to manage the rendering process
 * 
 * @author Fredie
 *
 */
public class Renderer {

	private IContext mHelper;
	private RenderPath mRenderPath;

	private HashMap<String, List<IBatch>> mScene;
	private HashMap<String, INode.Input> mGlobals;

	/**
	 * Creates a new {@link Renderer} using the given {@link IContext}
	 * 
	 * @param helper
	 */
	public Renderer(IContext helper) {
		mScene = new HashMap<>();
		mHelper = helper;
	}

	/**
	 * Registers all {@link IProperty}s of this {@link Entity} containing
	 * {@link RenderData} for rendering in a suitable {@link IBatch}
	 * 
	 * @param entity
	 */
	public void registerEntityForRendering(Entity entity) {
		List<IProperty<RenderData>> properties = entity.propertiesByValue(RenderData.class);
		for (IProperty<RenderData> property : properties) {
			RenderData subject = property.get();

			// if the scene does not yet contain the given layer, create it
			if (!mScene.containsKey(subject.getLayer()))
				mScene.put(subject.getLayer(), new LinkedList<>());

			// get a list of static or dynamic batches to fit the property to
			List<IBatch> batches = mScene.get(subject.getLayer());

			// find an existing batch that accepts the given property
			boolean accepted = false;
			for (IBatch batch : batches) {
				accepted |= batch.accept(property);
				if (accepted)
					break;
			}

			// if no batch accepted it, try generating a new one that should be
			// able to accept it
			if (!accepted) {
				IBatch batch = mHelper.genBatch(subject.isStatic(),
						subject.getAttributes().toArray(new IContext.VertexAttribute[0]));
				// if the batch does not accept it, the object is most likely
				// not renderable on this hardware
				if (!batch.accept(property))
					throw new AtticRuntimeException("Failed to add Property for rendering.");

				batches.add(batch);
			}
		}
	}
	
	/**
	 * Creates a new {@link ValueInput} containing the Global-variable with the given name
	 * @param name
	 * @param type
	 * @return the ValueInput that was created
	 */
	@SuppressWarnings("unchecked")
	public <T> ValueInput<T> getGlobal(String name, Class<T> type) {
		try {
			return new ValueInput<T>((T) mGlobals.get(name));
		} catch(ClassCastException e) {
			throw new AtticRuntimeException("Unexpected Variable-Type");
		}
	}
	
	/**
	 * Creates a new {@link ValueInput} containing the data of the given layer.
	 * If no layer with the given name exists, it will be created empty
	 * @param name
	 * @return the newly created ValueInput
	 */
	public ValueInput<List<IBatch>> getLayerData(String name) {
		if (!mScene.containsKey(name))
			mScene.put(name, new LinkedList<>());
		
		return new ValueInput<>(mScene.get(name));
	}
	
	/**
	 * 
	 * @return the {@link IContext} currently used by this {@link Renderer}
	 */
	public IContext getHelper() {
		return mHelper;
	}
	
	/**
	 * Renders the current scene
	 */
	public void render() {
		mRenderPath.pollFrame();
	}

	/**
	 * Sets the {@link RenderPath} to use when rendering
	 * @param path
	 */
	public void setRenderPath(RenderPath path) {
		mRenderPath = path;
	}
}
