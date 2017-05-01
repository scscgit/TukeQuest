package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 01.05.2017.
 */
public abstract class AbstractCollectable extends RectangleActor implements Collectable, RenderLast {

    protected AbstractCollectable(Animation animation, float x, float y) {
        super(animation, BodyDef.BodyType.KinematicBody, x, y);
    }

    protected abstract Music getSoundBonus();

    @Override
    public final void collected(Player player) {
        Music soundBonus = getSoundBonus();
        if (soundBonus != null) {
            soundBonus.play();
        }
        onCollected(player);
        // TODO delete, for now it's invisible
        setAnimation(Animation.INVISIBLE);
        getBody().setActive(false);
    }

    protected abstract void onCollected(Player player);

    protected GameScreen getGameScreen() {
        return (GameScreen) getScreen();
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        super.configureFixtureDef(fixtureDef);
        fixtureDef.isSensor = true;
    }

    @Override
    public void act() {
        super.act();
        if (collides(getGameScreen().getPlayer())) {
            getGameScreen().getPlayer().collected(this);
        }
    }

}
