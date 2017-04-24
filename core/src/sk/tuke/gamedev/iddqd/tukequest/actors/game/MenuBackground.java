package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderFirst;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 26.03.2017.
 */
public class MenuBackground extends FullScreenImage implements RenderFirst {

    public static final Animation ANIMATION = new Animation(
        "menu.jpg", Animation.ScaleType.SCALE_WIDTH, TukeQuestGame.SCREEN_WIDTH);

    public MenuBackground() {
        super(ANIMATION);
    }

    @Override
    public void act() {
    }


}
