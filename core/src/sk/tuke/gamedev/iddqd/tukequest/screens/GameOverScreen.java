package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FullScreenImage;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 27.03.2017.
 */
public class GameOverScreen extends AbstractScreen {

    public static final Animation GAME_OVER_BACKGROUND = new Animation(
        "gameover.jpg", Animation.ScaleType.SCALE_WIDTH, TukeQuestGame.SCREEN_HEIGHT);

    public GameOverScreen(TukeQuestGame game) {
        super(game, TukeQuestGame.manager.get("audio/music/gameover.mp3", Music.class));
    }

    @Override
    protected Camera initCamera() {
        return new OrthographicCamera();
    }

    @Override
    protected Viewport initViewport(Camera camera) {
        return new FitViewport(TukeQuestGame.SCREEN_WIDTH, TukeQuestGame.SCREEN_HEIGHT, camera);
    }

    @Override
    protected World initWorld() {
        return null;
    }

    @Override
    public void show() {
        super.show();

        // Displaying Game Over image for a moment
        addActor(new FullScreenImage(GAME_OVER_BACKGROUND));

        // Schedule game restart
        TaskManager.INSTANCE.scheduleTimer(
            "gameRestart", 7, () -> getGame().setScreen(new GameScreen(getGame())));
    }

}
