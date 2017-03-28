package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

/**
 * Created by Steve on 26.03.2017.
 */
public class Jump extends AbstractCommand {

    private static final float JUMP_SPRINT_FACTOR = 1.5f;

    private float jumpForce;

    public Jump(float jumpForce, Command command) {
        super(command);
        this.jumpForce = jumpForce;
    }

    @Override
    public void execute(Player player) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.isOnGround()) {
            // TODO: jump height should increase if player is moving sideways (sprinting}
            float xVelocityJumpCoeficient = Math.abs(player.getBody().getLinearVelocity().x) / 40;

            // this is to make sure we won't reduce jump height if the player is just standing there
            float jumpForceAppliedInThisJump = this.jumpForce;
            if (xVelocityJumpCoeficient * this.jumpForce > this.jumpForce) {
                jumpForceAppliedInThisJump = xVelocityJumpCoeficient * this.jumpForce;
            }
            //System.out.println("Linear velocity before jump: " + Math.abs(player.getBody().getLinearVelocity().x)/40);

            if (player.isSprinting()) {
                jumpForceAppliedInThisJump *= JUMP_SPRINT_FACTOR;
            }
            player.getBody().applyForceToCenter(new Vector2(0, jumpForceAppliedInThisJump), true);
            player.setJumping(true);
        }
        runNextCommand(player);
    }

}
