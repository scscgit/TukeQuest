package sk.tuke.gamedev.iddqd.tukequest.physics.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import sk.tuke.gamedev.iddqd.tukequest.actors.Ground;

/**
 * Created by Steve on 26.03.2017.
 */
public class GroundContactHandler extends OneTypeContactHandler {

    public GroundContactHandler(Fixture fixture) {
        super(fixture, Ground.class);
    }

    @Override
    void onBegin(Contact contact, Fixture fixture1, Fixture fixture2) {
        super.onBegin(contact, fixture1, fixture2);
    }

}
