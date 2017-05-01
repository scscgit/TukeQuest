package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FullScreenImage;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 02.05.2017.
 */
public class TutorialScreen extends AbstractScreen {

    public static final Animation TUTORIAL = new Animation("tutorial.png");

    protected TutorialScreen(TukeQuestGame game) {
        super(game, TukeQuestGame.manager.get("audio/music/backgroundmusic.mp3", Music.class));
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
        addActor(new FullScreenImage(TUTORIAL));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (InputHelper.isJustExit()) {
            getGame().setScreen(new MenuScreen(getGame()));
        }
    }

}
