package cn.appkf.flappybird.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import cn.appkf.flappybird.MainGame;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(380, 580);
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new MainGame();
	}
	
}
