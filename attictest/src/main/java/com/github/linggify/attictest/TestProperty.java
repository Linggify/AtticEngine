package com.github.linggify.attictest;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.logic.Property;

public class TestProperty implements Property<Integer>{

	private Entity mParent;
	private boolean mActive;
	
	@Override
	public void addListener(PropertyListener listener) {
		//do nothing
	}

	@Override
	public boolean removeListener(PropertyListener listener) {
		return false;
	}

	@Override
	public void onAttach(Entity parent) throws AtticRuntimeException {
		mParent = parent;
	}

	@Override
	public void onDetach() throws AtticRuntimeException {
		mParent = null;
	}

	@Override
	public void setActive(boolean flag) {
		mActive = flag;
	}

	@Override
	public boolean isActive() {
		return mActive;
	}

	@Override
	public Class<Integer> getContentType() {
		return Integer.class;
	}

	@Override
	public void update(double delta) {
		String message = "Property of ";
		if(mParent != null) message += mParent.toString();
		else message += "null";
		message += " reporting after " + delta + " secs of delay";
		System.out.println(message);
	}

	@Override
	public Integer get() {
		return 0;
	}

}
