package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 02.05.2017.
 */
public class PublicClass extends AbstractCollectable {

    public static final String FAST_JUMP_TIMER_NAME = "publicClassFastJump";
    private static final Animation ANIMATION = new Animation("public_class.png");
    private static final int FAST_JUMPING_FOR_SECONDS = 4;

    public PublicClass(float x, float y) {
        super(ANIMATION, x, y);
    }

    @Override
    public float causesJump() {
        return 0.5f;
    }

    @Override
    protected Music getSoundBonus() {
        return Surprise.SOUND_BONUS;
    }

    @Override
    protected void onCollected(Player player) {
        // Fast jumping for a few seconds
        float previousGravity = getGameScreen().getGravity().y;
        getGameScreen().setGravity(new Vector2(0, previousGravity / 2));
        if (TaskManager.INSTANCE.hasTimers(FAST_JUMP_TIMER_NAME)) {
            // Won't re-schedule the existing event
            return;
        }
        TaskManager.INSTANCE.scheduleTimer(FAST_JUMP_TIMER_NAME, FAST_JUMPING_FOR_SECONDS, () -> {
            getGameScreen().setGravity(new Vector2(0, previousGravity));
        });
    }

}
