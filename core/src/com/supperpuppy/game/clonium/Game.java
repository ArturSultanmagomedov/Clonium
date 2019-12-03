package com.supperpuppy.game.clonium;

import com.supperpuppy.game.clonium.screens.HomeScreen;

public class Game extends com.badlogic.gdx.Game {

	@Override
	public void create() {

		setScreen(new HomeScreen(this));

	}

}

