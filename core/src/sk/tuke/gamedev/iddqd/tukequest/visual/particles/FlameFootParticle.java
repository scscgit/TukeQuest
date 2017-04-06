package sk.tuke.gamedev.iddqd.tukequest.visual.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;

import java.util.function.Supplier;

/**
 * Created by Steve on 06.04.2017.
 */
public class FlameFootParticle implements Particle {

    private Supplier<Boolean> condition;
    private ParticleEffect effect;

    public FlameFootParticle(Supplier<Boolean> condition) {
        this.condition = condition;
        this.effect = new ParticleEffect();
        this.effect.load(Gdx.files.internal("particles/flame"), Gdx.files.internal(""));
    }

    @Override
    public void draw(Batch batch, AnimatedActor actor) {
        if (!this.condition.get() ) {
            this.effect.allowCompletion();
        } else  {
            this.effect.start();
        }
         this.effect.setPosition(
            actor.getX() + actor.getAnimation().getWidth() / 2, actor.getY()
        );
        this.effect.update(Gdx.graphics.getDeltaTime());
        this.effect.draw(batch);
    }

}
