package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.MenuBackground;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
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
        ImageButton exitBtn = new SimpleButton("ExitBtn.png").getButton();

        stage = new Stage(new ScreenViewport());

        Table menuTable = new Table();
        menuTable.add(startBtn).expandX().center().padTop(80).row(); //padding because of main logo
        menuTable.add(exitBtn).expandX().center().padTop(10).row();
        menuTable.setFillParent(true);
        stage.addActor(menuTable);

        Gdx.input.setInputProcessor(stage);

        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runGame();
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitGame();
            }
        });
    }

    private void runGame() {
        game.setScreen(new GameScreen(game));
        stage.dispose();
    }

    private void exitGame() {
        Gdx.app.exit();
    }

    private void showHelp() {
        getGame().setScreen(new TutorialScreen(getGame()));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
        if (InputHelper.isEnter()) {
            runGame();
        } else if (InputHelper.isJustExit()) {
            exitGame();
        } else if (InputHelper.isHelp()) {
            showHelp();
        }
    }

    @Override
    public void pause() {
        Log.i(this, "Not pausing menu screen");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }
}
