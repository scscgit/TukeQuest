package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 01.05.2017.
 */
public class Paper extends AbstractCollectable {

    public static final Animation ANIMATION = new Animation("paper_pencil.png", 0.6f);

    public Paper(float x, float y) {
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
        player.addLife();
    }

}
