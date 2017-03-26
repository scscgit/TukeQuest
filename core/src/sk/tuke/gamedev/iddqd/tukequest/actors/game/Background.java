package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 26.03.2017.
 */
public class Background extends AbstractAnimatedActor implements RenderFirst {

    public static final Animation ANIMATION = new Animation(
        "background.jpg", Animation.ScaleType.SCALE_WIDTH, TukeQuestGame.SCREEN_WIDTH);

    public Background(float y) {
        super(ANIMATION, 0, y);
    }

    @Override
    public void act() {
    }

}
