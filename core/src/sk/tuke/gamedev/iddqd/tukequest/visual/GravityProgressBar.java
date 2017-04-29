package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Steve on 29.04.2017.
 */
public class GravityProgressBar extends AbstractProgressBar {

    public GravityProgressBar(int maxValue) {
        this(
            maxValue,
            Color.RED,
            // TODO: fix the black color, these values don't seem to be respected
            Color.GREEN,
            Color.WHITE,
            Color.GOLD
        );
    }

    public GravityProgressBar(int maxValue, Color fullColor, Color fullBack, Color emptyColor, Color emptyBack) {
        super(
            maxValue,
            Type.Vertical,
            fullColor,
            fullBack,
            emptyColor,
            emptyBack
        );
        // By default there is no gravity
        setValue(0);
    }

    @Override
    public GravityProgressBar addToStage(Stage stage) {
        return (GravityProgressBar) super.addToStage(stage);
    }

}
