package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.Game;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

public class TukeQuestGame extends Game {

    private GameScreen gameScreen;

    @Override
    public void create() {
        this.gameScreen = new GameScreen(this);
        setScreen(this.gameScreen);

        System.out.println("Game " + this + " created");
    }

    @Override
    public void dispose() {
        super.dispose();
        System.out.println("Game " + this + " disposed");
    }
}
