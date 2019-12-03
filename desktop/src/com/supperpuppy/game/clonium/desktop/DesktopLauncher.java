package com.supperpuppy.game.clonium.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.supperpuppy.game.clonium.Game;

public class DesktopLauncher {
    public static void main(String[] arg) {
        System.setProperty("user.name", "Public");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "blue";
        config.useGL30 = true;
//		config.addIcon("images/icon_128.png", Files.FileType.Internal);
//		config.addIcon("images/icon_32.png", Files.FileType.Internal);
//		config.addIcon("images/icon_16.png", Files.FileType.Internal);
        config.resizable = true;
        config.foregroundFPS = 60;
        config.backgroundFPS = 60;
        config.height = 16 * 35;
        config.width = 9 * 35;
        config.y = 150;
        new LwjglApplication(new Game(), config);
    }
}
