package sk.tuke.gamedev.iddqd.tukequest.visual.particles;

import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;

/**
 * Created by Steve on 06.04.2017.
 */
public interface Particle {

    void draw(Batch batch, AnimatedActor actor);

}
