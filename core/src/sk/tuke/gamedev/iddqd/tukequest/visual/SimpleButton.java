package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SimpleButton {

    private Texture texture;
    private TextureRegion region;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public TextureRegionDrawable getDrawable() {
        return drawable;
    }

    public void setDrawable(TextureRegionDrawable drawable) {
        this.drawable = drawable;
    }

    public ImageButton getButton() {
        return button;
    }

    public void setButton(ImageButton button) {
        this.button = button;
    }

    private TextureRegionDrawable drawable;
    private ImageButton button;

    public SimpleButton(String filename) {
        texture = new Texture(Gdx.files.internal(filename));
        region = new TextureRegion(texture);
        drawable = new TextureRegionDrawable(region);
        button = new ImageButton(drawable);
    }
}
