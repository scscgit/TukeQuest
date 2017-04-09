package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.MenuBackground;
import sk.tuke.gamedev.iddqd.tukequest.visual.SimpleButton;

public class MenuScreen extends AbstractScreen {

    private TukeQuestGame game;
    private Stage stage;

    public MenuScreen(TukeQuestGame game) {
        super(game, TukeQuestGame.manager.get("audio/music/backgroundmusic.mp3", Music.class));
        this.game = game;
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

        addActor(new MenuBackground());

        ImageButton startBtn = new SimpleButton("StartBtn.png").getButton();
        startBtn.setPosition((float) (TukeQuestGame.SCREEN_WIDTH/3.4), (float) (TukeQuestGame.SCREEN_HEIGHT/2.5));

        ImageButton exitBtn = new SimpleButton("ExitBtn.png").getButton();
        exitBtn.setPosition((float) (TukeQuestGame.SCREEN_WIDTH/3.4), TukeQuestGame.SCREEN_HEIGHT/6);

        stage = new Stage(new ScreenViewport());

        stage.addActor(startBtn);
        stage.addActor(exitBtn);
        Gdx.input.setInputProcessor(stage);

        startBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new GameScreen(game));
            }
        });

        exitBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }
}
