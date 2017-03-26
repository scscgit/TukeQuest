package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by macbook on 26/03/2017.
 */
public class FxFlameActor extends RectangleActor implements RenderLast {

    public static final Animation ANIMATION = new Animation("flame_fx.png", 1, 5, 0, 4, 0.25f);

    static {
        // Scales the animation to the full screen width
        ANIMATION.setScale(TukeQuestGame.SCREEN_WIDTH / ANIMATION.getWidth());
    }

    public FxFlameActor(float x, float y) {
        super(
            ANIMATION,
            BodyDef.BodyType.DynamicBody,
            x,
            y
        );

    }

    @Override
    public void act() {
        getBody().setActive(false);
    }
}
