package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.ActOnAdd;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by macbook on 26/03/2017.
 */
public class FxFlame extends RectangleActor implements RenderLast, ActOnAdd {

    public static final Animation ANIMATION = new Animation("flame_fx.png", 1, 5, 0, 4, 0.25f);

    protected Player player;

    static {
        // Scales the animation to the full screen width
        // TODO: remove the +100 after hardcoded flames in GameScreen get deleted
        ANIMATION.setScale((TukeQuestGame.SCREEN_WIDTH + 100) / ANIMATION.getWidth());
    }

    public FxFlame(Player player, float x, float y) {
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
        setFlameVelocity(FxFlameMaster.MIN_SPEED_START);
    }

    public boolean setFlameVelocity(float velocity) {
        if (getBody().getLinearVelocity().y == velocity) {
            return false;
        }
        getBody().setLinearVelocity(new Vector2(0, velocity));
        return true;
    }

    @Override
    public void act() {
        if (collides(this.player)) {
            this.player.killedByFlame();
        }
    }

    @Override
    public int getRenderLastOrder() {
        // Draws over standard game actors and ground
        return 3;
    }

}
