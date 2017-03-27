package sk.tuke.gamedev.iddqd.tukequest.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;

import java.security.InvalidParameterException;

/**
 * Actor's graphical representation, that can take any visual form, either a static image or animated.
 * <p>
 * Created by Steve on 09.03.2017.
 */
public class Animation {

    public enum ScaleType {
        SCALE_WIDTH, SCALE_HEIGHT
    }

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

    public Animation(Sprite sprite, float scale) {
        this.scale = scale;
        setSprite(sprite);
    }

    public Animation(Texture texture, float scale) {
        // Special case of invisible animation that does not offer width or height
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

    /**
     * Repeat-scaled texture.
     *
     * @param texture Texture.
     * @param scale   Symmetrical scale multiplier for both horizontal and vertical dimensions.
     * @param width   New width of the texture. If not equal to original width, it will get repeated.
     * @param height  New height of the texture. If not equal to original width, it will get repeated.
     */
    public Animation(Texture texture, float scale, int width, int height) {
        // Special case of invisible animation that does not offer width or height
        if (texture == null) {
            return;
        }
        this.scale = scale;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        setSprite(new Sprite(texture, width, height));
    }

    public Animation(String textureName, float scale, int width, int height) {
        this(getTexture(textureName), scale, width, height);
    }

    /**
     * Scales the texture symmetrically to a certain size by modifying the width or height.
     *
     * @param texture       Texture to be scaled.
     * @param scaleType     Defines whether the width or height will be changed.
     * @param widthOrHeight New width or height.
     */
    public Animation(Texture texture, ScaleType scaleType, int widthOrHeight) {
        this(texture, scaleTexture(texture, scaleType, widthOrHeight), texture.getWidth(), texture.getHeight());
    }

    private static float scaleTexture(Texture texture, ScaleType scaleType, int widthOrHeight) {
        switch (scaleType) {
            case SCALE_WIDTH:
                return (float) widthOrHeight / texture.getWidth();
            case SCALE_HEIGHT:
                return (float) widthOrHeight / texture.getHeight();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Animation(String textureName, ScaleType scaleType, int widthOrHeight) {
        this(getTexture(textureName), scaleType, widthOrHeight);
    }

    // Animated image constructors

    public Animation(com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animationGdx) {
        setAnimationGdx(animationGdx);
    }

    /**
     * An actual animation support.
     *
     * @param spriteSheet   Texture containing a grid of multiple evenly-spaced images to be used in animation.
     * @param columns       Number of grid columns of images that the spriteSheet texture contains.
     * @param rows          Number of grid rows of images that the spriteSheet texture contains.
     * @param firstImage    Index of the first image to be used, starting from 0, maximum value of columns * rows - 1.
     * @param lastImage     Index of the last image to be used, starting from 0, maximum value of columns * rows - 1.
     * @param frameDuration Duration of the single animation frame.
     */
    public Animation(Texture spriteSheet, int columns, int rows, int firstImage, int lastImage, float frameDuration) {
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are all aligned.
        TextureRegion[][] splitSpriteSheet = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / columns,
            spriteSheet.getHeight() / rows);

        // Initialize the Animation with the frame interval and array of frames
        setAnimationGdx(new com.badlogic.gdx.graphics.g2d.Animation<>(
            frameDuration, walkFrames(splitSpriteSheet, columns, rows, firstImage, lastImage)));
    }

    public Animation(Texture spriteSheet, int columns, int rows, int images, float frameDuration) {
        this(spriteSheet, columns, rows, 0, images, frameDuration);
    }

    /**
     * Place the regions into a 1D array in the correct order, starting from the top
     * left, going across first. The Animation constructor requires a 1D array.
     */
    private static TextureRegion[] walkFrames(TextureRegion[][] splitSpriteSheet,
                                              int columns,
                                              int rows,
                                              int firstImage,
                                              int lastImage) {
        if (firstImage < 0 || lastImage > columns * rows - 1 || firstImage > lastImage) {
            throw new InvalidParameterException("Invalid input values");
        }
        TextureRegion[] walkFrames = new TextureRegion[lastImage - firstImage + 1];
        int sheetIndex = 0;
        int frameIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (sheetIndex++ < firstImage) {
                    continue;
                }
                walkFrames[frameIndex++] = splitSpriteSheet[i][j];
                if (sheetIndex > lastImage) {
                    return walkFrames;
                }
            }
        }
        return walkFrames;
    }

    public Animation(String spriteSheet, int columns, int rows, int firstImage, int lastImage, float speed) {
        this(getTexture(spriteSheet), columns, rows, firstImage, lastImage, speed);
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

    public void draw(Batch batch, AnimatedActor actor) {
        if (this.animationGdx != null) {
            drawAnimationGdx(batch, actor);
        } else if (this.sprite != null) {
            drawSprite(batch, actor);
        }
    }

    private void drawAnimationGdx(Batch batch, AnimatedActor actor) {
        // Accumulate elapsed animation time
        this.animationTime += Gdx.graphics.getDeltaTime();
        TextureRegion frame = this.animationGdx.getKeyFrame(this.animationTime, true);
        setSprite(new Sprite(frame));
        drawSprite(batch, actor);
    }

    private void drawSprite(Batch batch, AnimatedActor actor) {
        this.sprite.setPosition(actor.getX(), actor.getY());
        this.sprite.setRotation(actor.getRotation());
        this.sprite.draw(batch);
    }

}
