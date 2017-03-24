package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.Game;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

public class TukeQuestGame extends Game {

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 500;

    public static boolean debug;

    private GameScreen gameScreen;

    @Override
    public void create() {
        this.gameScreen = new GameScreen(this);
        setScreen(this.gameScreen);

        // Delaying turning the debug on
        debug = false;
        TaskManager.INSTANCE.scheduleTimer(null, 2, () -> debug = true);

        System.out.println("Game " + this + " created");
    }

    @Override
    public void dispose() {
        super.dispose();
        gameScreen.dispose();
        System.out.println("Game " + this + " disposed");
    }

}
