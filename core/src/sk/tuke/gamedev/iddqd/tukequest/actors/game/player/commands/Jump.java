package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;

/**
 * Created by Steve on 26.03.2017.
 */
public class Jump extends AbstractCommand {

    private static final Music JUMP_SOUND = TukeQuestGame.manager.get("audio/sounds/jump.mp3", Music.class);

    private final float jumpForce;
    private final float jumpSprintFactor;

    public Jump(float jumpForce, float jumpSprintFactor) {
        this.jumpForce = jumpForce;
        this.jumpSprintFactor = jumpSprintFactor;
    }

    @Override
    public void onExecute(Player player) {
        if (!player.isOnGround() || player.getBody().getLinearVelocity().y > 0) {
            return;
        }
        if (InputHelper.isJump()) {
            System.out.println("Jump started");
            JUMP_SOUND.play();

            // TODO: jump height should increase if player is moving sideways (sprinting)
            float xVelocityJumpCoefficient = Math.abs(player.getBody().getLinearVelocity().x) / 40;

            // This is to make sure we won't reduce jump height if the player is just standing there
            float jumpForceAppliedInThisJump = this.jumpForce;
            if (xVelocityJumpCoefficient * this.jumpForce > this.jumpForce) {
                jumpForceAppliedInThisJump = xVelocityJumpCoefficient * this.jumpForce;
            }

            if (player.isSprinting()) {
                jumpForceAppliedInThisJump *= this.jumpSprintFactor;
            }
            player.getBody().applyForceToCenter(new Vector2(0, jumpForceAppliedInThisJump), true);
            player.setJumping(true);
        } else {
            player.setJumping(false);


            // claculate score on landing
            if (ScoreManager.platformsInRow.size() > 0) {
                // TODO: remove the platforms that player went above, but then fell down without landing
                int countOfPlatformsReallyJumped = 0;
                for (Platform platform : ScoreManager.platformsInRow) {
                    if (platform.getBody().isActive()) {
                        countOfPlatformsReallyJumped = countOfPlatformsReallyJumped + 1;
                        platform.scoreCollected = true;
                    } else {
                        platform.scoreCollected = false;
                    }
                }

                System.out.println("Jump ended, platformsJumped = " + ScoreManager.platformsInRow.size() + " really jumped counter = " + countOfPlatformsReallyJumped);
                ScoreManager.INSTANCE.addScoreForPlatforms(countOfPlatformsReallyJumped);
                ScoreManager.platformsInRow.clear();

            }
        }
    }

}
