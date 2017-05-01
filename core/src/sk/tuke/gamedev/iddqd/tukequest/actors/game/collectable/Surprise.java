package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.util.RandomHelper;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 27.03.2017.
 */
public class Surprise extends AbstractCollectable {

    public static final Animation ANIMATION = new Animation("gift.png");
    public static final Music SOUND_BONUS = TukeQuestGame.manager.get("audio/sounds/bonus.mp3", Music.class);
    private boolean jump;

    public Surprise(float x, float y) {
        super(ANIMATION, x, y);
    }

    @Override
    protected Music getSoundBonus() {
        return SOUND_BONUS;
    }

    @Override
    protected void onCollected(Player player) {
        this.jump = RandomHelper.random.nextBoolean();
        if (this.jump) {
            return;
        }
        // Surprise either causes jump, or adds score
        ScoreManager.INSTANCE.addScore(5000);
    }

    @Override
    public float causesJump() {
        return this.jump ? 1 : 0;
    }

}
