package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by 123 on 27.4.2017.
 */
public class HUD implements Disposable {

    public static final int FLAME_PROGRESS_BAR_MAX_DISTANCE = 2000;

    private Stage stage;
    private Label scoreLabel;
    private Label comboLabel;
    private FlameProgressBar progressBar;

    public HUD() {
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

        this.progressBar = new FlameProgressBar(FLAME_PROGRESS_BAR_MAX_DISTANCE).addToStage(this.stage);
    }

    public void setScore(int score) {
        this.scoreLabel.setText(String.format("Score: %d", score));
    }

    public void setCombo(int platformStreak, int jumpingStreak, int multipliedScore) {
        if (jumpingStreak == 0) {
            this.comboLabel.setText("");
            return;
        }
        this.comboLabel.setText(
            "Jump streak (" + jumpingStreak
                + " over " + platformStreak
                + " platforms) combo score: " + multipliedScore);
    }

    public void setFlameDistance(float distance) {
        // The distance is practically zero when a Player can see the flame
        distance -= 500;
        this.progressBar.setValue(distance);
    }

    public void draw(Batch batch) {
        // Projection matrix fixes a problem with flames randomly turning invisible
        batch.setProjectionMatrix(this.stage.getCamera().combined);
        this.stage.draw();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }

}
