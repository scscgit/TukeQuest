package sk.tuke.gamedev.iddqd.tukequest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = TukeQuestGame.SCREEN_WIDTH;
        config.height = TukeQuestGame.SCREEN_HEIGHT;
        new LwjglApplication(new TukeQuestGame(), config);
    }
}
