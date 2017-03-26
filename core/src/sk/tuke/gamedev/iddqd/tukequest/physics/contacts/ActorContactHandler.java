package sk.tuke.gamedev.iddqd.tukequest.physics.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;

import java.util.HashSet;
import java.util.Set;

/**
 * Extracted from sk.tuke.gamelib2.physics.contacts.
 */
public class ActorContactHandler extends OneTypeContactHandler {

    private Set<BodyActor> contacts = new HashSet<>();

    public ActorContactHandler(Fixture fixture) {
        super(fixture, Actor.class);
    }

    public boolean collides(BodyActor actor) {
        return this.contacts.contains(actor);
    }

    public Set<BodyActor> getAllCollisions() {
        return this.contacts;
    }

    void onBegin(Contact contact, Fixture fixture1, Fixture fixture2) {
        super.onBegin(contact, fixture1, fixture2);
        if (fixture1 != null && fixture2 != null) {
            BodyActor actor1 = (BodyActor) fixture1.getUserData();
            BodyActor actor2 = (BodyActor) fixture2.getUserData();
            if (fixture1 != this.getFixture()) {
                this.contacts.add(actor1);
            } else {
                this.contacts.add(actor2);
            }
        }
    }

    void onEnd(Contact contact, Fixture fixture1, Fixture fixture2) {
        super.onEnd(contact, fixture1, fixture2);
        if (fixture1 != null && fixture2 != null) {
            BodyActor actor1 = (BodyActor) fixture1.getUserData();
            BodyActor actor2 = (BodyActor) fixture2.getUserData();
            if (fixture1 != this.getFixture()) {
                this.contacts.remove(actor1);
            } else {
                this.contacts.remove(actor2);
            }
        }
    }

}
