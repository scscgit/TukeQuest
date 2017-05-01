package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.LifeImage;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 123 on 27.4.2017.
 */
public class HUD implements Disposable {

    public static final int FLAME_PROGRESS_BAR_MAX_DISTANCE = 2000;
    private static final float LIVES_X_POSITION = TukeQuestGame.SCREEN_WIDTH - 120;
    private static final float LIVES_Y_POSITION = 55;

    private Camera camera;
    private Stage stage;
    private Label scoreLabel;
    private Label comboLabel;
    private FlameProgressBar flameProgressBar;
    private GravityProgressBar gravityProgressBar;
    private final List<LifeImage> lifeActors = new LinkedList<>();

    public HUD(Camera camera) {
        this.camera = camera;
        this.stage = new Stage(new ScreenViewport());
        // TODO: increase the font size; BitmapFont does not scale well, this could be better with a custom font:
        // https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        this.scoreLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        this.comboLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));

        Table scoreTable = new Table();
        scoreTable.top();
        scoreTable.padTop(10);
        scoreTable.setFillParent(true);
        scoreTable.add(scoreLabel).expandX();
        scoreTable.row();
        scoreTable.add(comboLabel).expandX();
        this.stage.addActor(scoreTable);

        this.flameProgressBar = new FlameProgressBar(FLAME_PROGRESS_BAR_MAX_DISTANCE)
            .addToStage(this.stage);
        this.gravityProgressBar = new GravityProgressBar((int) (GameScreen.GRAVITY_LIMIT - GameScreen.GRAVITY_START))
            .addToStage(this.stage);
        // The starting gravity will be set
        setGravity(GameScreen.GRAVITY_START);
    }

    public void setScore(int score) {
        this.scoreLabel.setText(String.format("Score: %d", score));
    }

    public void setCombo(int platformStreak, int jumpingStreak, int score, int multiplier) {
        if (jumpingStreak == 0) {
            this.comboLabel.setText("");
            return;
        }
        this.comboLabel.setText(
            "Jump streak " + jumpingStreak
                + " (over " + platformStreak
                + " platforms) Combo Score: " + score
                + "x" + multiplier);
    }

    public void setFlameDistance(float distance) {
        // The distance is practically zero when a Player can see the flame
        distance -= 500;
        this.flameProgressBar.setValue(distance);
    }

    public void act() {
        try {
            this.lifeActors.forEach(LifeImage::act);
        } catch (ConcurrentModificationException e) {
            // This is expected and for now it is temporarily okay
            Log.e(this, e.getMessage());
        }
    }

    public void draw(Batch batch) {
        this.lifeActors.forEach(lifeImage -> lifeImage.draw(batch));
        // Projection matrix fixes a problem with flames randomly turning invisible
        batch.setProjectionMatrix(this.stage.getCamera().combined);
        this.stage.draw();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }

    public void setGravity(float gravity) {
        if (gravity < 0) {
            gravity = -gravity;
        }
        this.gravityProgressBar.setValue(gravity - GameScreen.GRAVITY_START);
    }

    public void setLives(int lives, Player player) {
        this.lifeActors.clear();
        for (int i = 0; i < lives; i++) {
            this.lifeActors.add(new LifeImage(
                LIVES_X_POSITION - LifeImage.LIFE_ANIMATION.getWidth() * i
                    + this.camera.position.x - TukeQuestGame.SCREEN_WIDTH / 2,
                LIVES_Y_POSITION + this.camera.position.y - TukeQuestGame.SCREEN_HEIGHT / 2,
                this.camera,
                player));
        }
    }

}
