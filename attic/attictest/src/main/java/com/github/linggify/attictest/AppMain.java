package com.github.linggify.attictest;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.Application.ApplicationConfiguration;
import com.github.linggify.attic.lwjgl.LwjglBackend;

public class AppMain {

	public static void main(String[] args) {		
		Application app = new Application(new LwjglBackend());
		ApplicationConfiguration config = new ApplicationConfiguration();
		config.setWidth(800);
		config.setHeight(600);
		config.setTitle("Attic-Test");
		
		app.launch(config, new TestView());
	}
}
