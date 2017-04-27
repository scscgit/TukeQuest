package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Color;
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

    public Stage stage;

    private Integer score = 0;
    private Label scoreLabel;
    private Label comboLabel;

    public HUD() {
        this.stage = new Stage(new ScreenViewport());
        this.scoreLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        this.comboLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(scoreLabel).expandX();
        table.row();
        table.add(comboLabel).expandX();

        stage.addActor(table);
    }

    public void setScoreText(String text) {
        this.scoreLabel.setText(text);
    }

    public void setComboText(String text) {
        this.comboLabel.setText(text);
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
