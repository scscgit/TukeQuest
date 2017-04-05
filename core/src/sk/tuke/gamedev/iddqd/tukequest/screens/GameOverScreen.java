package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.GameOverBackground;
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
        float emptyHeight = TukeQuestGame.SCREEN_HEIGHT - GAME_OVER_BACKGROUND.getHeight();
        float emptyWidth = TukeQuestGame.SCREEN_WIDTH - GAME_OVER_BACKGROUND.getWidth();
        addActor(new GameOverBackground(
            GAME_OVER_BACKGROUND,
            emptyWidth > 0 ? emptyWidth / 2 : 0,
            emptyHeight > 0 ? emptyHeight / 2 : 0) {
        });

        // Schedule game restart
        TaskManager.INSTANCE.scheduleTimer(
            "gameRestart", 7, () -> getGame().setScreen(new GameScreen(getGame())));
    }

}
