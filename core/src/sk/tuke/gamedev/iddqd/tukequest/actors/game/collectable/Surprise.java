package sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 27.03.2017.
 */
public class Surprise extends RectangleActor implements Collectable {

    public static final Animation ANIMATION = new Animation("gift.png");

    private static final Music SOUND_BONUS = TukeQuestGame.manager.get("audio/sounds/bonus.mp3", Music.class);

    private GameScreen screen;

    public Surprise(AbstractScreen screen, float x, float y) {
        super(ANIMATION, BodyDef.BodyType.KinematicBody, x, y);
        this.screen = (GameScreen) screen;
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        super.configureFixtureDef(fixtureDef);
        fixtureDef.isSensor = true;
    }

    @Override
    public void act() {
        super.act();
        if (collides(screen.getPlayer())) {
            screen.getPlayer().collected(this);
        }
    }

    @Override
    public void collected(Player player) {
        SOUND_BONUS.play();
        // TODO delete Surprise
        setAnimation(Animation.INVISIBLE);
        getBody().setActive(false);
    }
}
