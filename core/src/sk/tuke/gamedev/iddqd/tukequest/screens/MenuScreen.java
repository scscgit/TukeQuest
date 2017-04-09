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
import sk.tuke.gamedev.iddqd.tukequest.visual.SimpleButton;

public class MenuScreen extends AbstractScreen {

    public static final Music MENU_MUSIC = TukeQuestGame.manager.get("audio/music/backgroundmusic.mp3", Music.class);
    private TukeQuestGame game;
    private Stage stage;

    private ImageButton startBtn;
    private ImageButton exitBtn;

    public MenuScreen(TukeQuestGame game) {
        super(game, MENU_MUSIC);
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

        this.startBtn = new SimpleButton("StartBtn.png").getButton();
        this.exitBtn = new SimpleButton("ExitBtn.png").getButton();

        stage = new Stage(new ScreenViewport());

        Table menuTable = new Table();
        tableButtons(menuTable);
        menuTable.setFillParent(true);
        stage.addActor(menuTable);

        Gdx.input.setInputProcessor(stage);

        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                stage.dispose();
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    protected void tableButtons(Table menuTable) {
        menuTable.add(startBtn).expandX().center().padTop(startButtonPadTop()).row(); //padding because of main logo
        menuTable.add(exitBtn).expandX().center().padTop(exitButtonPadTop()).row();
    }

    protected int startButtonPadTop() {
        return 80;
    }

    protected int exitButtonPadTop() {
        return 10;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }
}
