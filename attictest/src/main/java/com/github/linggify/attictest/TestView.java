package com.github.linggify.attictest;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.View;

public class TestView implements View{

	@Override
	public void setup(Application app, ViewState state) {
		//do nothing here
	}

	@Override
	public ViewState stop(ViewState next) {
		//return an empty viewstate
		return new ViewState();
	}

}
