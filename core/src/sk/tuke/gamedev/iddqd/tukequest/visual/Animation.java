package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;

/**
 * Actor's graphical representation, that can take any visual form, either a static image or animated.
 * <p>
 * Created by Steve on 09.03.2017.
 */
public class Animation {

    public static final Animation INVISIBLE = new Animation((Texture) null);

    private Texture texture;
    private float scale;

    public Animation(Texture texture, float scale) {
        this.texture = texture;
        this.scale = scale;
    }

    public Animation(String textureName, float scale) {
        this(getTexture(textureName), scale);
    }

    public Animation(String textureName) {
        this(textureName, 1);
    }

    public Animation(Texture texture) {
        this(texture, 1);
    }

    private static Texture getTexture(String textureName) {
        return textureName == null ? null : new Texture(textureName);
    }

    public float getWidth() {
        return texture.getWidth() * scale;
    }

    public float getHeight() {
        return texture.getHeight() * scale;
    }

    public void draw(Batch batch, BodyActor actor) {
        if (texture == null) {
            return;
        }
        batch.draw(texture, actor.getX(), actor.getY(), getWidth(), getHeight());
    }

}
