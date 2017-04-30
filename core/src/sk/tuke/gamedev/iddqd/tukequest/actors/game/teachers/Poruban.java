package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;
import sk.tuke.gamedev.iddqd.tukequest.visual.particles.FlameFootParticle;

/**
 * Created by Ruza on 5.4.2017.
 */
public class Poruban extends AbstractTeacher {

    private static Animation ANIMATION_STANDING = new Animation("poruban1.png", 0.25f);

    public Poruban(GameScreen screen, float x, float y) {

        super(
            screen,
            ANIMATION_STANDING,
            x,
            y,
            TukeQuestGame.manager.get("audio/sounds/poruban1.mp3", Music.class));
    }

    @Override
    protected void onMeetPlayer() {
        getPlayer().getParticle().setImage(FlameFootParticle.ParticleImage.PROGRAMMING);
    }

    @Override
    protected int soundDuration(boolean wasVisited) {
        return 9;
    }

}
