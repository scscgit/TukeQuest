package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 5.4.2017.
 */
public abstract class AbstractTeacher extends RectangleActor {

    private GameScreen screen;
    private Music welcomeSound;
    private Music otherSound;
    private Boolean isVisited = false;
    private Boolean playingSound = false;

    protected AbstractTeacher(GameScreen screen, Animation animation, float x, float y, Music welcomeSound) {
        super(animation, BodyDef.BodyType.StaticBody, x, y);
        this.screen = screen;
        this.welcomeSound = welcomeSound;
        this.otherSound = welcomeSound;
    }

    protected AbstractTeacher(GameScreen screen, Animation animation, float x, float y, Music welcomeSound, Music otherSound) {
        super(animation, BodyDef.BodyType.StaticBody, x, y);
        this.screen = screen;
        this.welcomeSound = welcomeSound;
        this.otherSound = otherSound;
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        super.configureFixtureDef(fixtureDef);
        // Does not cause physical collision
        fixtureDef.isSensor = true;
    }

    protected Player getPlayer() {
        return this.screen.getPlayer();
    }

    protected void playSound() {
        if (isVisited) {
            otherSound.play();
        } else {
            welcomeSound.play();
            isVisited = true;
        }
    }

    @Override
    public void act() {
        if (collides(getPlayer()) && !playingSound) {
            playSound();
            playingSound = true;
            this.screen.setMusicVolume(GameScreen.SILENT_MUSIC_VOLUME);
            // Allow the sound again in a few seconds
            TaskManager.INSTANCE.scheduleTimer("teacherSound", soundDuration(), () -> playingSound = false);
            TaskManager.INSTANCE.removeTimers("teacherBackgroundMute");
            TaskManager.INSTANCE.scheduleTimer("teacherBackgroundMute", soundDuration(),
                () -> this.screen.setMusicVolume(GameScreen.DEFAULT_MUSIC_VOLUME));
        }
    }

    protected abstract int soundDuration();

}
