package com.github.linggify.attic;

import java.util.Stack;

import com.github.linggify.attic.View.ViewState;
import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Genius;
import com.github.linggify.attic.render.Context;
import com.github.linggify.attic.render.Renderer;

/**
 * The Application is the main component of the engine
 * 
 * @author Fredie
 *
 */
public class Application {

	private Backend mBackend;

	private Window mWindow;

	private Genius mGenius;
	private Stack<View> mViewStack;
	private Stack<ViewState> mViewStates;
	private Renderer mRenderer;
	private Context mContext;

	/**
	 * Creates a new {@link Application} using the given {@link Backend}
	 * 
	 * @param backend
	 */
	public Application(Backend backend) {
		mBackend = backend;
	}

	/**
	 * 
	 * @return the {@link Renderer} used by this {@link Application}
	 */
	public Renderer renderer() {
		return mRenderer;
	}

	/**
	 * 
	 * @return the {@link Genius} used by this {@link Application}
	 */
	public Genius genius() {
		return mGenius;
	}

	/**
	 * Launches this {@link Application}
	 */
	public void launch(View root) {
		mBackend.init();

		mWindow = mBackend.createWindow();
		mWindow.center();

		// init renderer
		mContext = mWindow.getContext();
		mRenderer = new Renderer(mContext);

		// init and start genius
		mGenius = new Genius(mRenderer);
		mGenius.start();

		// create viewstack and push root view
		mViewStack = new Stack<>();
		pushView(root);

		mWindow.show();

		while (!mWindow.shouldClose()) {
			mContext.clearRenderTargets();
			mWindow.postRender();
		}

		// stop genius
		if (mGenius.isRunning())
			mGenius.stop();

		// destroy window
		mWindow.hide();
		mWindow.destroy();

		mBackend.destroy();
	}

	/**
	 * Pushes the given {@link View} on top of the View-stack
	 * 
	 * @param view
	 */
	public void pushView(View view) {
		mGenius.submitTask(() -> {
			if (view == null)
				throw new AtticRuntimeException("Cannot push NULL as a View");

			ViewState next = new ViewState();
			
			// stop the current view
			if (!mViewStack.isEmpty())
				mViewStates.push(mViewStack.peek().stop(next));

			// setup view
			view.setup(this, next);
			mViewStack.push(view);
		});
	}

	/**
	 * Pops the previous {@link View} (if any) and pushes the given View
	 * 
	 * @param view
	 *            the View to start next
	 */
	public void switchView(View view) {
		mGenius.submitTask(() -> {
			if (view == null)
				throw new AtticRuntimeException("Cannot switch to a View that is NULL");

			ViewState next = new ViewState();
			// pop last view
			if (!mViewStack.isEmpty()) {
				View last = mViewStack.pop();
				last.stop(next);
			}

			// push new view
			view.setup(this, next);
			mViewStack.push(view);
		});
	}

	/**
	 * Stops the currently running {@link View} from the stack and starts the
	 * next lower one with the last {@link ViewState} it had. If there is no
	 * View left in the Stack, the {@link Application} will stop running
	 */
	public void popView() {
		mGenius.submitTask(() -> {
			// pop last view
			View last = mViewStack.pop();
			
			//get previous viewstate if any
			ViewState next = null;
			if(!mViewStack.isEmpty())
				next = mViewStates.pop();
			
			last.stop(next);

			if (!mViewStack.isEmpty())
				mViewStack.peek().setup(this, next);
			else
				mWindow.setShouldClose(true);
		});
	}
}
