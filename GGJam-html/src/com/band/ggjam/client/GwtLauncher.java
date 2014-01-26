package com.band.ggjam.client;

import com.band.ggjam.GGJam;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(768, 512);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new GGJam();
	}
}