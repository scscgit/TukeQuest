package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

/**
 * Created by Steve on 26.03.2017.
 */
public class HorizontalMovement extends AbstractCommand {

    private float forceMultiplier;
    private Camera camera;

    public HorizontalMovement(float forceMultiplier, Camera camera, Command nextCommand) {
        super(nextCommand);
        this.forceMultiplier = forceMultiplier;
        this.camera = camera;
    }

    @Override
    public void execute(Player player) {
        float direction = keyboardInput() + mouseInput(this.camera);
        if (direction != 0) {
            player.getBody().applyForceToCenter(
                new Vector2(direction * 50 * Gdx.graphics.getDeltaTime() * this.forceMultiplier, 0f), true);

            if (direction < 0) {
                player.setAnimation(player.isJumping() ? Player.ANIMATION_LEFT_JUMP : Player.ANIMATION_LEFT_WALK);
            } else {
                player.setAnimation(player.isJumping() ? Player.ANIMATION_RIGHT_JUMP : Player.ANIMATION_RIGHT_WALK);
            }
        }
        runNextCommand(player);
    }

    private static float keyboardInput() {
        float direction = 0;
        // Apply force using keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction--;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction++;
        }
        return direction;
    }

    private static float mouseInput(Camera camera) {
        // Apply force using mouse
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // Transform touch/mouse position coordinates to our camera's coordinate system
            camera.unproject(touchPos);
            return (touchPos.x - camera.viewportWidth / 2) / camera.viewportWidth;
        }
        return 0;
    }

}
