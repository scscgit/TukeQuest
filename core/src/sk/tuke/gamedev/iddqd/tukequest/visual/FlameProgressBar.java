package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;

/**
 * Created by Steve on 27.04.2017.
 */
public class FlameProgressBar {

    private int maxValue;
    private ProgressBar bar;

    public FlameProgressBar(int max) {
        this(
            max,
            Color.DARK_GRAY,
            Color.RED,
            Color.GOLDENROD,
            Color.GREEN
        );
    }

    public FlameProgressBar(int maxValue, Color fullColor, Color fullBack, Color emptyColor, Color emptyBack) {
        this.maxValue = maxValue;
        Pixmap fullMap = new Pixmap(45, 15, Pixmap.Format.RGBA8888);
        fullMap.setColor(fullColor);
        fullMap.fill();

        Pixmap emptyMap = new Pixmap(45, 15, Pixmap.Format.RGBA8888);
        emptyMap.setColor(emptyColor);
        emptyMap.fill();

        Skin skin = new Skin();
        skin.add("full", new Texture(fullMap));
        skin.add("empty", new Texture(emptyMap));

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("empty", emptyBack),
            skin.newDrawable("full", fullBack));
        this.bar = new ProgressBar(0, maxValue, 0.1f, false, barStyle);

        // By default it goes over the entire screen width
        bar.setSize(TukeQuestGame.SCREEN_WIDTH, bar.getPrefHeight());
        // By default it is full
        setValue(getMaxValue());
        //bar.setAnimateDuration(1);
    }

    public FlameProgressBar addToStage(Stage stage) {
        stage.addActor(this.bar);
        return this;
    }

    public void setPosition(float x, float y) {
        this.bar.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.bar.setSize(width, height);
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setValue(float value) {
        this.bar.setValue(value);
    }

}
