package sk.tuke.gamedev.iddqd.tukequest.actors.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractBodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;

/**
 * Created by Steve on 24.04.2017.
 */
public class CameraFollow implements Strategy {

    private Camera camera;
    private AnimatedActor follower;
    private Vector2 lastPosition;

    public CameraFollow(Camera camera, AnimatedActor follower) {
        if (camera == null || follower == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        if (follower instanceof AbstractBodyActor) {
            throw new IllegalArgumentException("Body Actor can only change its position using physics, never directly");
        }
        this.camera = camera;
        this.follower = follower;
        storeLastPosition();
    }

    @Override
    public void act() {
        this.follower.setPosition(
            this.follower.getX() + camera.position.x - lastPosition.x,
            this.follower.getY() + camera.position.y - lastPosition.y);
        storeLastPosition();
    }

    private void storeLastPosition() {
        this.lastPosition = new Vector2(camera.position.x, camera.position.y);
    }

}
