package sk.tuke.gamedev.iddqd.tukequest.physics.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Extracted from sk.tuke.gamelib2.physics.contacts.
 */
abstract class BeginEndContactHandler {

    protected final Fixture fixture;
    private int hitCount = 0;

    public BeginEndContactHandler(Fixture fixture) {
        this.fixture = fixture;
    }

    public Fixture getFixture() {
        return this.fixture;
    }

    public int getHitCount() {
        return this.hitCount;
    }

    abstract boolean isAppropriate(Contact var1, Fixture var2, Fixture var3);

    public boolean isHitting() {
        return this.hitCount > 0;
    }

    void onBegin(Contact contact, Fixture fixture1, Fixture fixture2) {
        ++this.hitCount;
    }

    void onEnd(Contact contact, Fixture fixture1, Fixture fixture2) {
        --this.hitCount;
    }

    public void resetHitCount() {
        this.hitCount = 0;
    }

}
