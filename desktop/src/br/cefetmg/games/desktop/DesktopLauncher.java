package br.cefetmg.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import br.cefetmg.games.TowerDefense;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new TowerDefense(), config);
        config.width = 1024;
        config.height = 700;
        config.resizable = false;
    }
}
