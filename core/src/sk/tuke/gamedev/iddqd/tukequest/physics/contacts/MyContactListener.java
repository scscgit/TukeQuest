package sk.tuke.gamedev.iddqd.tukequest.physics.contacts;

import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

/**
 * Extracted from sk.tuke.gamelib2.physics.contacts.
 */
public class MyContactListener implements ContactListener {

    private Map<Fixture, List<BeginEndContactHandler>> fixtureHandlersMap = new HashMap<>();

    public MyContactListener() {
    }

    public <CH extends BeginEndContactHandler> CH addHandler(CH contactHandler) {
        Fixture fixture = contactHandler.getFixture();
        if (!this.fixtureHandlersMap.containsKey(fixture)) {
            this.fixtureHandlersMap.put(fixture, new ArrayList<>());
        }

        ((List) this.fixtureHandlersMap.get(fixture)).add(contactHandler);
        return contactHandler;
    }

    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        List<BeginEndContactHandler> handlers = (List) this.fixtureHandlersMap.get(fixtureA);
        Iterator i$;
        BeginEndContactHandler handler;
        if (handlers != null) {
            i$ = handlers.iterator();

            while (i$.hasNext()) {
                handler = (BeginEndContactHandler) i$.next();
                if (handler.isAppropriate(contact, fixtureA, fixtureB)) {
                    handler.onBegin(contact, fixtureA, fixtureB);
                }
            }
        }

        handlers = (List) this.fixtureHandlersMap.get(fixtureB);
        if (handlers != null) {
            i$ = handlers.iterator();

            while (i$.hasNext()) {
                handler = (BeginEndContactHandler) i$.next();
                if (handler.isAppropriate(contact, fixtureA, fixtureB)) {
                    handler.onBegin(contact, fixtureA, fixtureB);
                }
            }
        }
    }

    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        List<BeginEndContactHandler> handlers = (List) this.fixtureHandlersMap.get(fixtureA);
        Iterator i$;
        BeginEndContactHandler handler;
        if (handlers != null) {
            i$ = handlers.iterator();

            while (i$.hasNext()) {
                handler = (BeginEndContactHandler) i$.next();
                if (handler.isAppropriate(contact, fixtureA, fixtureB)) {
                    handler.onEnd(contact, fixtureA, fixtureB);
                }
            }
        }

        handlers = (List) this.fixtureHandlersMap.get(fixtureB);
        if (handlers != null) {
            i$ = handlers.iterator();

            while (i$.hasNext()) {
                handler = (BeginEndContactHandler) i$.next();
                if (handler.isAppropriate(contact, fixtureA, fixtureB)) {
                    handler.onEnd(contact, fixtureA, fixtureB);
                }
            }
        }
    }

    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
