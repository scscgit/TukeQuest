package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.Strategist;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.Strategy;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Steve on 27.03.2017.
 */
public class FullScreenImage extends AbstractAnimatedActor implements Strategist, RenderLast, ActLast {

    private static float nextX;
    private static float nextY;
    private List<Strategy> strategies = new LinkedList<>();
    private int actLastOrder;

    public FullScreenImage(Animation animation) {
        super(initConstructor(animation), nextX, nextY);
    }

    /**
     * Camera position copying constructor, implicitly centering.
     */
    public FullScreenImage(Animation animation, Camera camera) {
        super(
            initConstructor(animation),
            nextX + camera.position.x - TukeQuestGame.SCREEN_WIDTH / 2,
            nextY + camera.position.y - TukeQuestGame.SCREEN_HEIGHT / 2);
    }

    /**
     * Camera position copying constructor with custom offset.
     */
    public FullScreenImage(Animation animation, Camera camera, float x, float y) {
        super(
            animation,
            x + camera.position.x - TukeQuestGame.SCREEN_WIDTH / 2,
            y + camera.position.y - TukeQuestGame.SCREEN_HEIGHT / 2);
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
        this.strategies.forEach(Strategy::act);
    }

    @Override
    public void addStrategy(Strategy strategy) {
        this.strategies.add(strategy);
    }

    public void setLastActOrder(int actLastOrder) {
        this.actLastOrder = actLastOrder;
    }

    @Override
    public int getActLastOrder() {
        return this.actLastOrder;
    }

    @Override
    public int getRenderLastOrder() {
        // Is an override
        return 99;
    }
}
