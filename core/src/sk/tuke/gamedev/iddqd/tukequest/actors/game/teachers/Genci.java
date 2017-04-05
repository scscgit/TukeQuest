package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 5.4.2017.
 */
public class Genci extends AbstractTeacher {

    public Genci(GameScreen screen, float x, float y) {
        super(
            screen,
            new Animation("pixeljoint.png", 8, 4, 0, 9, 0.2f),
            x,
            y,
            TukeQuestGame.manager.get("audio/sounds/genci1.mp3", Music.class));
    }

    @Override
    protected int soundDuration() {
        return 3;
    }
}
