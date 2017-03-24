package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Timer;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

public class TukeQuestGame extends Game {

    private GameScreen gameScreen;

    @Override
    public void create() {
        this.gameScreen = new GameScreen(this);
        setScreen(this.gameScreen);

        // Testing screen transition

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                setScreen(new GameScreen(TukeQuestGame.this));
            }
        }, 1);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                setScreen(new GameScreen(TukeQuestGame.this));
            }
        }, 2);

        System.out.println("Game " + this + " created");
    }

    @Override
    public void dispose() {
        super.dispose();
        System.out.println("Game " + this + " disposed");
    }
}
