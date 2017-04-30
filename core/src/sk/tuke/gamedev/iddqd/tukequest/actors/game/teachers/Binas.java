package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;
import sk.tuke.gamedev.iddqd.tukequest.visual.particles.FlameFootParticle;

/**
 * Created by Ruza on 5.4.2017.
 */
public class Binas extends AbstractTeacher {

    private static Animation ANIMATION_LEFT = new Animation("binas.png", 8, 4, 16, 25, 0.2f);
    private static Animation ANIMATION_RIGHT = new Animation("binas.png", 8, 4, 0, 9, 0.2f);

    public Binas(GameScreen screen, float x, float y) {
        super(
            screen,
            ANIMATION_LEFT,
            ANIMATION_RIGHT,
            x,
            y,
            TukeQuestGame.manager.get("audio/sounds/binas1.mp3", Music.class),
            TukeQuestGame.manager.get("audio/sounds/binas2.mp3", Music.class));
    }

    @Override
    protected void onMeetPlayer() {
        getPlayer().getParticle().setImage(FlameFootParticle.ParticleImage.ASSEMBLER);
    }

    @Override
    protected int soundDuration(boolean wasVisited) {
        return wasVisited ? 5 : 9;
    }

}
