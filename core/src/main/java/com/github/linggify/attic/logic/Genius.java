package com.github.linggify.attic.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.render.Renderer;

/**
 * The Genius is responsible for all gamelogic updates and listening to UI
 * events
 * 
 * @author Fredie
 *
 */
public class Genius {

	private List<Entity> mEntities;
	private List<Integer> mDead;

	private Renderer mRenderer;

	private ReadWriteLock mRunLock;
	private boolean mRunning;
	private Thread mThread;

	private Lock mTaskLock;
	private List<Runnable> mTasks;
	private List<Runnable> mLastTasks;

	/**
	 * Creates a new Genius
	 */
	public Genius(Renderer renderer) {
		mEntities = new ArrayList<>();
		mDead = new ArrayList<>();
		mRenderer = renderer;

		mRunning = false;
		mRunLock = new ReentrantReadWriteLock(true);

		mTaskLock = new ReentrantLock();
		mTasks = new LinkedList<>();
		mLastTasks = new LinkedList<>();
	}

	/**
	 * Creates a new {@link Entity} with the given {@link Property}s
	 * 
	 * @param properties
	 * @return the newly created Entity
	 */
	public Entity createEntity(Property<?>... properties) {
		Entity result = new Entity(this, properties);
		submitTask(() -> {
			mEntities.add(result);
			mRenderer.registerEntityForRendering(result);
		});

		return result;
	}

	/**
	 * Updates all {@link Entity}s managed by this {@link Genius}
	 * 
	 * @param delta
	 */
	public void update(double delta) {
		mDead.clear();
		for (int i = 0; i < mEntities.size(); i++) {
			Entity entity = mEntities.get(i);
			if (entity.isDead())
				mDead.add(i);
			else
				entity.update(delta);
		}

		// kill all dead entities
		for (int id : mDead) {
			mEntities.remove(id).kill();
		}
	}

	/**
	 * Submits a Runnable to execute on the Thread of this {@link Genius}
	 * 
	 * @param task
	 */
	public void submitTask(Runnable task) {
		if (!isRunning())
			throw new AtticRuntimeException("Cannot submit Task: Genius is not yet running");

		mTaskLock.lock();
		mTasks.add(task);
		mTaskLock.unlock();
	}

	/**
	 * 
	 * @return whether this genius is running or not
	 */
	public boolean isRunning() {
		mRunLock.readLock().lock();
		boolean tmp = mRunning;
		mRunLock.readLock().unlock();

		return tmp;
	}

	/**
	 * Starts this {@link Genius} in its own Thread
	 */
	public void start() {
		if (isRunning())
			throw new AtticRuntimeException("Genius is already running");

		mThread = new Thread(() -> {
			try {
				mRunLock.writeLock().lock();
				mRunning = true;
				mRunLock.writeLock().unlock();

				long lastMillis = System.currentTimeMillis();
				long millis = lastMillis;
				while (isRunning()) {
					lastMillis = millis;
					millis = System.currentTimeMillis();
					update((double) (millis - lastMillis) / 1000.0);

					// execute submitted tasks
					List<Runnable> tmp = mTasks;
					mTaskLock.lock();
					mTasks = mLastTasks;
					mTaskLock.unlock();
					mLastTasks = tmp;

					// after this the list of tasks is empty, so no need to
					// clear it
					while (!mLastTasks.isEmpty())
						mLastTasks.remove(0).run();
				}
			} catch (AtticRuntimeException e) {
				mRunning = false;
				e.printStackTrace();
			}
		});

		mThread.start();
	}

	/**
	 * Stops this {@link Genius} after the currently running tick
	 */
	public void stop() {
		if (!isRunning())
			throw new AtticRuntimeException("");
		mRunLock.writeLock().lock();
		mRunning = false;
		mRunLock.writeLock().unlock();
	}
}
