package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.ActOnAdd;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by macbook on 26/03/2017.
 */
public class FxFlameActor extends RectangleActor implements RenderLast, ActOnAdd {

    public static final Animation ANIMATION = new Animation("flame_fx.png", 1, 5, 0, 4, 0.65f);
    public static final float INITIAL_SPEED = 2;

    static {
        // Scales the animation to the full screen width
        // TODO: remove the +100 after hardcoded flames in GameScreen get deleted
        ANIMATION.setScale((TukeQuestGame.SCREEN_WIDTH + 100) / ANIMATION.getWidth());
    }

    private Player player;

    public FxFlameActor(Player player, float x, float y) {
        super(
            ANIMATION,
            BodyDef.BodyType.KinematicBody,
            x,
            y
        );
        this.player = player;
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        super.configureFixtureDef(fixtureDef);
        // Flame does not collide, instead, it kills
        fixtureDef.isSensor = true;
    }

    @Override
    public void onAddedToScreen(AbstractScreen screen) {
        setFlameVelocity(INITIAL_SPEED);
    }

    public void setFlameVelocity(float velocity) {
        getBody().setLinearVelocity(new Vector2(0, velocity));
    }

    public void increaseFlameVelocity(float velocity) {
        setFlameVelocity(getBody().getLinearVelocity().y + velocity);
    }

    @Override
    public void act() {
        if (collides(this.player)) {
            this.player.killedByFlame();
        }
    }

}
