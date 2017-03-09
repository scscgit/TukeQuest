package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Steve on 08.03.2017.
 */
public interface Actor {

    void draw(Batch batch);

    void act();

}
