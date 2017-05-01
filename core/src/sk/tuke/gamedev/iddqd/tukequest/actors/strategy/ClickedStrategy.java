package sk.tuke.gamedev.iddqd.tukequest.actors.strategy;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;

import java.util.function.Consumer;

/**
 * Created by Steve on 01.05.2017.
 */
public class ClickedStrategy implements Strategy {

    private final AnimatedActor observedActor;
    private final Player player;
    private final Camera camera;
    private final Consumer<Player> action;

    public ClickedStrategy(AnimatedActor observedActor, Player player, Camera camera, Consumer<Player> action) {
        this.observedActor = observedActor;
        this.player = player;
        this.camera = camera;
        this.action = action;
    }

    @Override
    public void act() {
        if (InputHelper.clickWithinBounds(
            this.camera,
            this.observedActor.getX(),
            this.observedActor.getX() + this.observedActor.getAnimation().getWidth(),
            this.observedActor.getY(),
            this.observedActor.getY() + this.observedActor.getAnimation().getHeight())
            ) {
            this.action.accept(this.player);
        }
    }

}
