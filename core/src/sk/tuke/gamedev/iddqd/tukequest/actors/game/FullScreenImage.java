package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 27.03.2017.
 */
public class FullScreenImage extends AbstractAnimatedActor {

    private static float nextX;
    private static float nextY;

    public FullScreenImage(Animation animation) {
        super(initConstructor(animation), nextX, nextY);
    }

    /**
     * Camera following constructor.
     */
    public FullScreenImage(Animation animation, Camera camera) {
        super(initConstructor(animation), nextX, nextY + camera.position.y - TukeQuestGame.SCREEN_HEIGHT / 2);
    }

    /**
     * Can someone explain to me what could be an alternative without this static initialization hack?
     *
     * @param animation Animation to initialize positions.
     * @return Unchanged parameter animation.
     */
    private static Animation initConstructor(Animation animation) {
        float emptyHeight = TukeQuestGame.SCREEN_HEIGHT - animation.getHeight();
        float emptyWidth = TukeQuestGame.SCREEN_WIDTH - animation.getWidth();
        FullScreenImage.nextX = emptyWidth > 0 ? emptyWidth / 2 : 0;
        FullScreenImage.nextY = emptyHeight > 0 ? emptyHeight / 2 : 0;
        return animation;
    }

    @Override
    public void act() {
    }

}
