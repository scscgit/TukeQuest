package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;

/**
 * Created by Steve on 02.04.2017.
 */
public class CameraOnlyMovement extends AbstractCommand {

    private static final int DEBUG_TOGGLE_KEY = Input.Keys.X;
    private static final float DEBUG_CAMERA_SPEED = 50;

    private final Camera camera;
    private boolean cameraDebugMovement;

    public CameraOnlyMovement(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void onExecute(Player player) {
        // Toggle between camera debug mode and normal navigation
        if (Gdx.input.isKeyJustPressed(DEBUG_TOGGLE_KEY)) {
            this.cameraDebugMovement ^= true;
        }

        if (this.cameraDebugMovement) {
            debugCameraMovement();
        } else {
            levelCameraOnPlayerPosition(player);
        }
    }

    private void debugCameraMovement() {
        // Apply force using keyboard
        if (InputHelper.isUp()) {
            this.camera.position.y += DEBUG_CAMERA_SPEED;
        }
        if (InputHelper.isDown()) {
            this.camera.position.y -= DEBUG_CAMERA_SPEED;
        }
        if (InputHelper.isRight()) {
            this.camera.position.x += DEBUG_CAMERA_SPEED;
        }
        if (InputHelper.isLeft()) {
            this.camera.position.x -= DEBUG_CAMERA_SPEED;
        }
        this.camera.update();
    }

    /**
     * Make sure camera does not go below ground.
     */
    private void levelCameraOnPlayerPosition(Player player) {
        int calculatedY = (int) player.getY();
        if (calculatedY - (TukeQuestGame.SCREEN_HEIGHT / 2) <= 0) {
            calculatedY = TukeQuestGame.SCREEN_HEIGHT / 2;
        }
        this.camera.position.y = calculatedY;
        this.camera.position.x = TukeQuestGame.SCREEN_WIDTH / 2;
        this.camera.update();
    }

}
