package sk.tuke.gamedev.iddqd.tukequest.visual.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;

import java.util.function.Supplier;

/**
 * Created by Steve on 06.04.2017.
 */
public class FlameFootParticle implements Particle {

    public enum ParticleImage {

        NONE("", ""),
        ASSEMBLER(PARTICLE_IMAGE_PREFIX, "assembler"),
        PROGRAMMING(PARTICLE_IMAGE_PREFIX, "programming"),
        MATH(PARTICLE_IMAGE_PREFIX, "math");

        private String prefix;
        private String filePath;

        public String getPrefix() {
            return this.prefix;
        }

        public String getFilePath() {
            return this.filePath;
        }

        ParticleImage(String prefix, String filePath) {
            this.prefix = prefix;
            this.filePath = filePath;
        }
    }

    private static final String PARTICLE_FILE = "particles/flame-foot";
    private static final String PARTICLE_IMAGE_PREFIX = "particles";

    private Supplier<Boolean> condition;
    private ParticleEffect effect;

    public FlameFootParticle(Supplier<Boolean> condition) {
        this.condition = condition;
        this.effect = new ParticleEffect();
        this.effect.load(Gdx.files.internal(PARTICLE_FILE), Gdx.files.internal(ParticleImage.NONE.getFilePath()));
    }

    public void setImage(ParticleImage particleImage) {
        for (int i = 0; i < this.effect.getEmitters().size; i++) {
            ParticleEmitter emitter = this.effect.getEmitters().get(i);
            String imagePath;
            if ("".equals(particleImage.getFilePath())) {
                // Default particle image is always available on this path
                imagePath = "particle.png";
            } else {
                imagePath = Gdx.files.internal(particleImage.getFilePath()).path() + i + ".png";
            }
            emitter.setImagePath(imagePath);
        }
        this.effect.loadEmitterImages(Gdx.files.internal(particleImage.getPrefix()));
    }

    @Override
    public void draw(Batch batch, AnimatedActor actor) {
        if (!this.condition.get()) {
            this.effect.allowCompletion();
        } else if (this.effect.isComplete()) {
            this.effect.start();
        }
        this.effect.setPosition(
            actor.getX() + actor.getAnimation().getWidth() / 2, actor.getY()
        );
        this.effect.update(Gdx.graphics.getDeltaTime());
        this.effect.draw(batch);
    }

}
