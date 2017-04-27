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

    public HUD (){
        this.stage = new Stage(new ScreenViewport());
        this.scoreLabel = new Label(formatScore(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(scoreLabel).expandX();

        stage.addActor(table);
    }

    public String formatScore() {
        return String.format("Score: %d", score);
    }

    public void setScore(int newScore){
        score = newScore;
        scoreLabel.setText(formatScore());
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public void dispose() { stage.dispose(); }
}
