package sk.tuke.gamedev.iddqd.tukequest.actors.game.player;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.CameraFollow;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.ClickedStrategy;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.Strategist;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.Strategy;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Steve on 01.05.2017.
 */
public class LifeImage extends AbstractAnimatedActor implements Strategist {

    public static final Animation LIFE_ANIMATION = new Animation("paper_pencil.png", 0.7f);

    private List<Strategy> strategies = new LinkedList<>();

    public LifeImage(float x, float y, Camera camera, Player player) {
        super(LIFE_ANIMATION, x, y);
        addStrategy(new CameraFollow(camera, this));
        addStrategy(new ClickedStrategy(this, player, camera, param -> clickedOn(player)));
    }

    @Override
    public void act() {
        this.strategies.forEach(Strategy::act);
    }

    @Override
    public void addStrategy(Strategy strategy) {
        this.strategies.add(strategy);
    }

    private void clickedOn(Player player) {
        Log.i(this, "Clicked on");
        if (player.getLives() > 1) {
            player.killedByFlame();
        }
    }

}
