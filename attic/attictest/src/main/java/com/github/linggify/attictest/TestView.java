package com.github.linggify.attictest;

import com.github.linggify.attic.Application;
import com.github.linggify.attic.IView;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.nodes.ClearScreenNode;
import com.github.linggify.attic.render.path.RenderPath;
import com.github.linggify.attic.render.path.ValueInput;
import com.github.linggify.attic.util.Color;

public class TestView implements IView{

	@Override
	public void setup(Application app, ViewState state) {
		Entity e = app.genius().createEntity(new TestProperty());
		
		ClearScreenNode node = new ClearScreenNode();
		node.setInput(ClearScreenNode.CLEAR_COLOR, new ValueInput<Color>(new Color(1, 0, 0, 1)));
		
		RenderPath path = new RenderPath(node, "out", node);
		app.renderer().setRenderPath(path);
	}

	@Override
	public ViewState stop(ViewState next) {
		return new ViewState();
	}
}
