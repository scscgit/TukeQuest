package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.levels.Level;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;
import sk.tuke.gamedev.iddqd.tukequest.visual.particles.FlameFootParticle;

/**
 * Created by Ruza on 5.4.2017.
 */
public class Genci extends AbstractTeacher {

    private static Animation ANIMATION = new Animation("genci.png", 0.3f);

    public Genci(GameScreen screen, Level level, float x, float y) {
        super(
            screen,
            level,
            ANIMATION,
            x,
            y,
            TukeQuestGame.manager.get("audio/sounds/genci1.mp3", Music.class));
    }

    @Override
    protected void onMeetPlayer() {
        // Math is not appropriate here, but whatever... We have lack of teachers
        getPlayer().getParticle().setImage(FlameFootParticle.ParticleImage.MATH);
    }

    @Override
    protected int soundDuration(boolean wasVisited) {
        return 4;
    }

}
