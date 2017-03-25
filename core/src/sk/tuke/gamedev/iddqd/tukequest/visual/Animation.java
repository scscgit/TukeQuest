package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;

/**
 * Actor's graphical representation, that can take any visual form, either a static image or animated.
 * <p>
 * Created by Steve on 09.03.2017.
 */
public class Animation {

    // Invisible animation does not provide width or height, attempt at accessing it is illegal
    public static final Animation INVISIBLE = new Animation((Texture) null);

    private float scale;

    /**
     * Static image representation, must be written to using @{link setSprite} in order to update the scale.
     */
    private Sprite sprite;

    // Animated image representation
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animationGdx;
    private float animationTime;

    // Static image constructors

    public Animation(Texture texture, float scale) {
        if (texture == null) {
            return;
        }
        this.scale = scale;
        setSprite(new Sprite(texture));
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

    // Animated image constructors

    public Animation(com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animationGdx) {
        setAnimationGdx(animationGdx);
    }

    public Animation(Texture spriteSheet, int columns, int rows, int images, float frameDuration) {
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are all aligned.
        TextureRegion[][] splitSpriteSheet = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / columns,
            spriteSheet.getHeight() / rows);

        // Initialize the Animation with the frame interval and array of frames
        setAnimationGdx(new com.badlogic.gdx.graphics.g2d.Animation<>(
            frameDuration, walkFrames(splitSpriteSheet, columns, rows, images)));
    }

    private static TextureRegion[] walkFrames(TextureRegion[][] splitSpriteSheet, int columns, int rows, int images) {
        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                walkFrames[index++] = splitSpriteSheet[i][j];
                // TODO: check boundary input cases
                if (index > images) {
                    return walkFrames;
                }
            }
        }
        return walkFrames;
    }

    public Animation(String spriteSheet, int columns, int rows, int images, float speed) {
        this(getTexture(spriteSheet), columns, rows, images, speed);
    }

    private void setAnimationGdx(com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animationGdx) {
        this.animationGdx = animationGdx;
        this.scale = 1;
        // Setting up initial sprite in order for the width and height getters to work
        setSprite(new Sprite(animationGdx.getKeyFrame(0)));
    }

    private static Texture getTexture(String textureName) {
        return textureName == null ? null : new Texture(textureName);
    }

    /**
     * After setting up a new sprite (either static or from animation), we must always update its scale.
     */
    private void setSprite(Sprite sprite) {
        this.sprite = sprite;
        // The origin needs to be at the starting corner in order for scaling to preserve position
        this.sprite.setOrigin(0, 0);
        if (this.scale <= 0) {
            throw new RuntimeException("Invalid scale!");
        }
        this.sprite.setScale(this.scale, this.scale);
    }

    public void setScale(float scale) {
        this.scale = scale;
        setSprite(this.sprite);
    }

    public float getWidth() {
        return this.sprite.getWidth() * this.sprite.getScaleX();
    }

    public float getHeight() {
        return this.sprite.getHeight() * this.sprite.getScaleY();
    }

    public void draw(Batch batch, BodyActor actor) {
        if (this.animationGdx != null) {
            drawAnimationGdx(batch, actor);
        } else if (this.sprite != null) {
            drawSprite(batch, actor);
        }
    }

    private void drawAnimationGdx(Batch batch, BodyActor actor) {
        // Accumulate elapsed animation time
        this.animationTime += Gdx.graphics.getDeltaTime();
        TextureRegion frame = this.animationGdx.getKeyFrame(this.animationTime, true);
        setSprite(new Sprite(frame));
        drawSprite(batch, actor);
    }

    private void drawSprite(Batch batch, BodyActor actor) {
        this.sprite.setPosition(actor.getX(), actor.getY());
        this.sprite.setRotation(actor.getRotation());
        this.sprite.draw(batch);
    }

}
