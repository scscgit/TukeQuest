package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

/**
 * Created by Ruza on 5.4.2017.
 */
public class Binas extends AbstractTeacher {

    public Binas(GameScreen screen, float x, float y) {
        super(
            screen,
            Player.ANIMATION_LEFT_WALK,
            Player.ANIMATION_RIGHT_WALK,
            x,
            y,
            TukeQuestGame.manager.get("audio/sounds/binas1.mp3", Music.class),
            TukeQuestGame.manager.get("audio/sounds/binas2.mp3", Music.class));
    }

    @Override
    protected int soundDuration(boolean wasVisited) {
        return wasVisited ? 5 : 9;
    }

}
