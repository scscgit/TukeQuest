package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;

/**
 * Actor's graphical representation, that can take any visual form, either a static image or animated.
 * <p>
 * Created by Steve on 09.03.2017.
 */
public class Animation {

    // Invisible animation does not provide width or height, attempt at accessing it is illegal
    public static final Animation INVISIBLE = new Animation((Texture) null);

    private Sprite sprite;

    public Animation(Texture texture, float scale) {
        if (texture == null) {
            return;
        }
        this.sprite = new Sprite(texture);
        setScale(scale);
        // The origin needs to be at the starting corner in order for scaling to preserve position
        this.sprite.setOrigin(0, 0);
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

    public Sprite getSprite() {
        return this.sprite;
    }

    public float getWidth() {
        return this.sprite.getWidth() * this.sprite.getScaleX();
    }

    public float getHeight() {
        return this.sprite.getHeight() * this.sprite.getScaleY();
    }

    public void setScale(float scale) {
        this.sprite.setScale(scale, scale);
    }

    public void draw(Batch batch, BodyActor actor) {
        if (this.sprite == null) {
            return;
        }
        this.sprite.setPosition(actor.getX(), actor.getY());
        this.sprite.setRotation(actor.getRotation());
        this.sprite.draw(batch);
    }

}
