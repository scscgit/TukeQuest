package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 27.03.2017.
 */
public class Surprise extends RectangleActor {

    public static final Animation ANIMATION = new Animation("gift.png");

    private Music music;
    private GameScreen screen;

    public Surprise(AbstractScreen screen, float x, float y) {
        super(ANIMATION, BodyDef.BodyType.KinematicBody, x, y);
        this.screen = (GameScreen) screen;
        music = TukeQuestGame.manager.get("audio/sounds/bonus.wav",Music.class);
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
            music.play();
            // TODO delete Surprise
            setAnimation(Animation.INVISIBLE);
            getBody().setActive(false);
        }
    }

}
