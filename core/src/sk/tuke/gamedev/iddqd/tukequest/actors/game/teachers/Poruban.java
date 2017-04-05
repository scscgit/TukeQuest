package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 5.4.2017.
 */
public class Poruban extends AbstracTeacher {
    public Poruban(Animation animation, float x, float y) {
        super(new Animation("pixeljoint.png", 8, 4, 0, 9, 0.2f), x, y, TukeQuestGame.manager.get("audio/sounds/poruban1.mp3", Music.class));
    }
}
