package sk.tuke.gamedev.iddqd.tukequest.actors.game.player;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.Command;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.HorizontalMovement;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.Jump;

/**
 * Created by Steve on 26.03.2017.
 */
public class MovementController {

    private Command chain;
    private Player player;

    public MovementController(float forceMultiplier, float jumpForce, Player player, Camera camera) {
        this.chain = new HorizontalMovement(forceMultiplier, camera, new Jump(jumpForce, null));
        this.player = player;
    }

    public void act() {
        chain.execute(this.player);
    }

}
