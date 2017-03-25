package sk.tuke.gamedev.iddqd.tukequest.actors;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

public class Platform extends RectangleActor {

    private static final Animation ANIMATION;
    static {
        ANIMATION = new Animation("jerusrockwallsml.jpg");
        ANIMATION.getSprite().getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    // length specifies how many blocks the platform consists of
    public Platform(float x, float y, int length) {
        super(
            ANIMATION,
            BodyDef.BodyType.StaticBody,
            x,
            y,
            ANIMATION.getWidth() * length,
            ANIMATION.getHeight()
        );
        // fixme: nasty hack to aling libgdx object texture to box2d object
        // does not work, since ANIMATION is static!!!
//        ANIMATION.getSprite().setScale(length, 1);
    }
}
