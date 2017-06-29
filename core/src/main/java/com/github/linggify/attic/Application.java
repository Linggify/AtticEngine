package com.github.linggify.attic;

import java.io.File;
import java.util.Stack;

import com.github.linggify.attic.IView.ViewState;
import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Genius;
import com.github.linggify.attic.render.IContext;
import com.github.linggify.attic.render.Renderer;

/**
 * The Application is the main component of the engine
 * 
 * @author Fredie
 *
 */
public class Application {

	private IBackend mBackend;

	private IWindow mWindow;

	private Genius mGenius;
	private Stack<IView> mViewStack;
	private Stack<ViewState> mViewStates;
	private Renderer mRenderer;
	private IContext mContext;

	/**
	 * Creates a new {@link Application} using the given {@link IBackend}
	 * 
	 * @param backend
	 */
	public Application(IBackend backend) {
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
	public void launch(ApplicationConfiguration config, IView root) {
		mBackend.init();

		//setup window
		mWindow = mBackend.createWindow();
		mWindow.setIcon(config.getIcon());
		mWindow.setTitle(config.getTitle());
		mWindow.setDimensions(config.getWidth(), config.getHeight());
		mWindow.setFullScreen(config.isFullscreen());
		mWindow.enableVSync(config.isVSync());
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

		//if an exception arises during rendering, stop the application and print the log
		try {
			while (!mWindow.shouldClose()) {
				mRenderer.render();
				mWindow.postRender();
			}
		} catch (AtticRuntimeException e) {
			e.printStackTrace();
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
	 * Pushes the given {@link IView} on top of the View-stack
	 * 
	 * @param view
	 */
	public void pushView(IView view) {
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
	 * Pops the previous {@link IView} (if any) and pushes the given View
	 * 
	 * @param view
	 *            the View to start next
	 */
	public void switchView(IView view) {
		mGenius.submitTask(() -> {
			if (view == null)
				throw new AtticRuntimeException("Cannot switch to a View that is NULL");

			ViewState next = new ViewState();
			// pop last view
			if (!mViewStack.isEmpty()) {
				IView last = mViewStack.pop();
				last.stop(next);
			}

			// push new view
			view.setup(this, next);
			mViewStack.push(view);
		});
	}

	/**
	 * Stops the currently running {@link IView} from the stack and starts the
	 * next lower one with the last {@link ViewState} it had. If there is no
	 * View left in the Stack, the {@link Application} will stop running
	 */
	public void popView() {
		mGenius.submitTask(() -> {
			// pop last view
			IView last = mViewStack.pop();

			// get previous viewstate if any
			ViewState next = null;
			if (!mViewStack.isEmpty())
				next = mViewStates.pop();

			last.stop(next);

			if (!mViewStack.isEmpty())
				mViewStack.peek().setup(this, next);
			else
				mWindow.setShouldClose(true);
		});
	}

	/**
	 * Used to pass basic information to the {@link Application} on startup
	 * 
	 * @author Freddy
	 *
	 */
	public static class ApplicationConfiguration {

		private int mSizeX;
		private int mSizeY;

		private boolean mVSync;
		private boolean mFullscreen;

		private String mTitle;
		private File mIcon;

		public ApplicationConfiguration() {
			mSizeX = 800;
			mSizeY = 600;
			mVSync = false;
			mFullscreen = false;
			mTitle = "Attic";
			mIcon = null;
		}
		
		/**
		 * @return the width of the {@link IWindow}
		 */
		public int getWidth() {
			return mSizeX;
		}

		/**
		 * @param width
		 *            the width of the {@link IWindow}
		 */
		public void setWidth(int width) {
			this.mSizeX = width;
		}

		/**
		 * @return the height of the {@link IWindow}
		 */
		public int getHeight() {
			return mSizeY;
		}

		/**
		 * @param height
		 *            the height of the {@link IWindow}
		 */
		public void setHeight(int height) {
			this.mSizeY = height;
		}

		/**
		 * @return whether v-sync shall be enabled
		 */
		public boolean isVSync() {
			return mVSync;
		}

		/**
		 * @param mVSync
		 *            whether v-sync shall be enabled
		 */
		public void setIsVSync(boolean vSync) {
			this.mVSync = vSync;
		}

		/**
		 * @return whether the {@link Application} is launched in fullscreen
		 */
		public boolean isFullscreen() {
			return mFullscreen;
		}

		/**
		 * @param flag
		 *            whether to launch the {@link Application} in fullscreen
		 */
		public void setFullscreen(boolean flag) {
			this.mFullscreen = flag;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return mTitle;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public void setTitle(String title) {
			this.mTitle = title;
		}

		/**
		 * @return the Icon
		 */
		public File getIcon() {
			return mIcon;
		}

		/**
		 * @param icon
		 *            the Icon to set
		 */
		public void setIcon(File icon) {
			this.mIcon = icon;
		}

	}
}
