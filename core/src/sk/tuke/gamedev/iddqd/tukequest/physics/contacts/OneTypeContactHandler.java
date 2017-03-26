package sk.tuke.gamedev.iddqd.tukequest.physics.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * Extracted from sk.tuke.gamelib2.physics.contacts.
 */
class OneTypeContactHandler extends BeginEndContactHandler {

    private Class clazz;

    public OneTypeContactHandler(Fixture fixture, Class clazz) {
        super(fixture);
        this.clazz = clazz;
    }

    public boolean isAppropriate(Contact contact, Fixture fixture1, Fixture fixture2) {
        if (fixture1 != null && fixture2 != null) {
            if (fixture1 == this.getFixture() && fixture2.getBody().getUserData() != null
                && ClassReflection.isAssignableFrom(this.clazz, fixture2.getBody().getUserData().getClass())) {
                return true;
            }
            if (fixture2 == this.getFixture() && fixture1.getBody().getUserData() != null
                && ClassReflection.isAssignableFrom(this.clazz, fixture1.getBody().getUserData().getClass())) {
                return true;
            }
        }
        return false;
    }

}
