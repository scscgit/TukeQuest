package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SimpleButton {

    private Texture texture;
    private TextureRegion region;
    private TextureRegionDrawable drawable;
    private ImageButton button;

    public SimpleButton(String filename) {
        this.texture = new Texture(Gdx.files.internal(filename));
        this.region = new TextureRegion(texture);
        this.drawable = new TextureRegionDrawable(region);
        this.button = new ImageButton(drawable);
    }

    public ImageButton getButton() {
        return button;
    }
}
