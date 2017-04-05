package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

public class TukeQuestGame extends Game {

    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 500;

    // Only Player uses this when he dies
    public static TukeQuestGame THIS;

    public static AssetManager manager;
    public static boolean debug;

    private GameScreen gameScreen;

    @Override
    public void create() {
        THIS = this;
        TukeQuestGame.manager = loadAssets();

        this.gameScreen = new GameScreen(this);
        setScreen(this.gameScreen);

        // Delaying turning the debug on
        TaskManager.INSTANCE.scheduleTimer(null, 1, () -> debug = true);

        System.out.println("Game " + this + " created");
    }

    private AssetManager loadAssets() {
        manager = new AssetManager();
        loadAudio(manager);
        manager.finishLoading();
        System.out.println("All assets loaded");
        return manager;
    }

    private void loadAudio(AssetManager manager) {
        manager.load("audio/music/backgroundmusic.mp3", Music.class);
        manager.load("audio/music/gameover.mp3", Music.class);
        manager.load("audio/sounds/bonus.mp3", Music.class);
        manager.load("audio/sounds/jump.mp3", Music.class);
        manager.load("audio/sounds/poruban1.wma", Music.class);
        manager.load("audio/sounds/genci1.wma", Music.class);
        System.out.println("Audio loaded");
    }

    @Override
    public void dispose() {
        super.dispose();
        gameScreen.dispose();
        System.out.println("Game " + this + " disposed");
    }

}
