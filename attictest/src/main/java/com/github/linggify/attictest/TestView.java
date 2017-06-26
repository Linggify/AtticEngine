package com.github.linggify.attictest;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.View;
import com.github.linggify.attic.logic.Entity;

public class TestView implements View{

	@Override
	public void setup(Application app, ViewState state) {
		Entity e = app.genius().createEntity(new TestProperty());
	}

	@Override
	public ViewState stop(ViewState next) {
		return new ViewState();
	}
}
