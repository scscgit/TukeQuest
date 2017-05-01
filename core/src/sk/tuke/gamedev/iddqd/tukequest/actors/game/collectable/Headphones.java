package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 01.05.2017.
 */
public class Headphones extends AbstractCollectable {

    private static final Animation ANIMATION = new Animation("headphones.png");

    public Headphones(float x, float y) {
        super(ANIMATION, x, y);
    }

    @Override
    public float causesJump() {
        return 0;
    }

    @Override
    protected Music getSoundBonus() {
        return null;
    }

    @Override
    protected void onCollected(Player player) {
        // Resets the gravity, causes the player to go into the "flow"
        // Headphones cannot cause harm
        if (getGameScreen().getGravity().y < -GameScreen.GRAVITY_START) {
            getGameScreen().setGravity(new Vector2(0, -GameScreen.GRAVITY_START));
        }
    }

}
