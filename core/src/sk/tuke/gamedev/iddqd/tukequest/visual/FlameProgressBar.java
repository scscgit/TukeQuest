package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Steve on 27.04.2017.
 */
public class FlameProgressBar extends AbstractProgressBar {

    public FlameProgressBar(int maxValue) {
        this(
            maxValue,
            Color.WHITE,
            Color.RED,
            Color.WHITE,
            Color.GREEN
        );
    }

    public FlameProgressBar(int maxValue, Color fullColor, Color fullBack, Color emptyColor, Color emptyBack) {
        super(
            maxValue,
            Type.Horizontal,
            fullColor,
            fullBack,
            emptyColor,
            emptyBack
        );
        // By default it is full
        setValue(getMaxValue());
    }

    @Override
    public FlameProgressBar addToStage(Stage stage) {
        return (FlameProgressBar) super.addToStage(stage);
    }

}
