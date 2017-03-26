package sk.tuke.gamedev.iddqd.tukequest.physics.contacts;

import com.badlogic.gdx.physics.box2d.Fixture;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Ground;

/**
 * Created by Steve on 26.03.2017.
 */
public class GroundContactListener extends OneTypeContactHandler {

    public GroundContactListener(Fixture fixture) {
        super(fixture, Ground.class);
    }

}
