package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;

/**
 * Created by Steve on 29.04.2017.
 */
public abstract class AbstractProgressBar {

    public enum Type {
        Vertical, Horizontal
    }

    public static final int BAR_WIDTH = 15;
    public static final int VALUE_WIDTH = 45;

    protected int maxValue;
    protected com.badlogic.gdx.scenes.scene2d.ui.ProgressBar bar;

    public AbstractProgressBar(int maxValue, Type type, Color fullColor, Color fullBack, Color emptyColor, Color emptyBack) {
        this.maxValue = maxValue;
        int valueWidth = type == Type.Horizontal ? VALUE_WIDTH : BAR_WIDTH;
        int valueHeight = type == Type.Horizontal ? BAR_WIDTH : VALUE_WIDTH;
        Pixmap fullMap = new Pixmap(valueWidth, valueHeight, Pixmap.Format.RGBA8888);
        fullMap.setColor(fullColor);
        fullMap.fill();
        Pixmap emptyMap = new Pixmap(valueWidth, valueHeight, Pixmap.Format.RGBA8888);
        emptyMap.setColor(emptyColor);
        emptyMap.fill();

        Skin skin = new Skin();
        skin.add("full", new Texture(fullMap));
        skin.add("empty", new Texture(emptyMap));

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("empty", emptyBack),
            skin.newDrawable("full", fullBack));
        this.bar = new ProgressBar(0, maxValue, 0.1f, type == Type.Vertical, barStyle);

        // By default it goes over the entire screen width or height
        switch (type) {
            case Vertical:
                // Will start right above Horizontal Progress Bar
                setSize(bar.getPrefWidth(), TukeQuestGame.SCREEN_HEIGHT - bar.getPrefWidth());
                setPosition(TukeQuestGame.SCREEN_WIDTH - bar.getPrefWidth(), bar.getPrefWidth());
                break;
            case Horizontal:
                setSize(TukeQuestGame.SCREEN_WIDTH, bar.getPrefHeight());
                break;
            default:
                // Assertion to prevent unhandled state in the future
                throw new IllegalArgumentException("Unexpected Progress Bar type");
        }
        //bar.setAnimateDuration(1);
    }

    public AbstractProgressBar addToStage(Stage stage) {
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
