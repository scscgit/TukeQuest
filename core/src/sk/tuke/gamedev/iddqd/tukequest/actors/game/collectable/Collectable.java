package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

/**
 * Created by Steve on 02.04.2017.
 */
public interface Collectable extends BodyActor {

    void collected(Player player);

    float causesJump();

}
