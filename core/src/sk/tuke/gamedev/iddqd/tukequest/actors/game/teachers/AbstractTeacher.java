package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.ActOnAdd;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.levels.Level;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.Random;

/**
 * Created by Ruza on 5.4.2017.
 */
public abstract class AbstractTeacher extends RectangleActor implements ActOnAdd, RenderLast {

    private static final int SCREEN_MOVEMENT_LIMIT_LEFT = 150;
    private static final int SCREEN_MOVEMENT_LIMIT_RIGHT = TukeQuestGame.SCREEN_WIDTH - 150;
    private static final float WALKING_SPEED = 3;
    private Animation animationLeft;
    private Animation animationRight;
    private GameScreen screen;
    private Level level;
    private Music welcomeSound;
    private Music otherSound;
    private boolean isVisited;
    private boolean playingSound;
    private boolean directionLeft;
    private boolean isWalking;

    // Walking teacher constructors

    protected AbstractTeacher(GameScreen screen, Level level, Animation animationLeft, Animation animationRight,
                              float x, float y, Music welcomeSound) {
        this(screen, level, animationLeft, x, y, welcomeSound);
        initializeWalkingAnimations(animationLeft, animationRight);
    }

    protected AbstractTeacher(GameScreen screen, Level level, Animation animationLeft, Animation animationRight,
                              float x, float y, Music welcomeSound, Music otherSound) {
        this(screen, level, animationLeft, x, y, welcomeSound, otherSound);
        initializeWalkingAnimations(animationLeft, animationRight);
    }

    // Standing teacher constructors

    protected AbstractTeacher(
        GameScreen screen, Level level, Animation animation, float x, float y, Music welcomeSound
    ) {
        this(screen, level, animation, x, y, welcomeSound, welcomeSound);
    }

    protected AbstractTeacher(
        GameScreen screen, Level level, Animation animation, float x, float y, Music welcomeSound, Music otherSound
    ) {
        super(animation, BodyDef.BodyType.KinematicBody, x, y);
        this.screen = screen;
        this.level = level;
        this.welcomeSound = welcomeSound;
        this.otherSound = otherSound;
    }

    private void initializeWalkingAnimations(Animation animationLeft, Animation animationRight) {
        this.animationLeft = animationLeft;
        this.animationRight = animationRight;
        this.isWalking = true;
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        super.configureFixtureDef(fixtureDef);
        // Does not cause physical collision
        fixtureDef.isSensor = true;
    }

    @Override
    public void act() {
        if (getPlayer().isAlive()
            // Meeting on sight, not on collision
            && getPlayer().getY() >= getY()
            && getPlayer().getY() <= getY() + getHeight()
            && !this.playingSound) {
            meetPlayer();
        }
        if (this.isWalking && shouldRotate()) {
            walk(!this.directionLeft);
        }
    }

    @Override
    public void onAddedToScreen(AbstractScreen screen) {
        if (this.isWalking) {
            walk(new Random().nextBoolean());
        }
    }

    private void walk(boolean directionLeft) {
        this.directionLeft = directionLeft;
        getBody().setLinearVelocity(this.directionLeft ? -WALKING_SPEED : WALKING_SPEED, 0);
        if (this.isWalking) {
            setAnimation(this.directionLeft ? this.animationLeft : this.animationRight);
        }
    }

    private boolean shouldRotate() {
        return this.directionLeft
            ? getX() < SCREEN_MOVEMENT_LIMIT_LEFT - +getWidth()
            : getX() > SCREEN_MOVEMENT_LIMIT_RIGHT;
    }

    protected Player getPlayer() {
        return this.screen.getPlayer();
    }

    private void meetPlayer() {
        this.screen.setMusicVolume(GameScreen.SILENT_MUSIC_VOLUME);
        // Allow the sound again in a few seconds
        TaskManager.INSTANCE.scheduleTimer("teacherSound", soundDuration(this.isVisited),
            () -> playingSound = false);
        TaskManager.INSTANCE.removeTimers("teacherBackgroundMute");
        TaskManager.INSTANCE.scheduleTimer("teacherBackgroundMute", soundDuration(this.isVisited),
            () -> this.screen.setMusicVolume(GameScreen.DEFAULT_MUSIC_VOLUME));
        if (this.isVisited) {
            otherSound.play();
        } else {
            welcomeSound.play();
            this.isVisited = true;
            if (this.screen.getFirstFlame() != null) {
                this.screen.getFirstFlame().playerMetTeacher();
            }
            this.level.levelAchieved(getPlayer());
            onMeetPlayer();
        }
        playingSound = true;
    }

    protected Level getLevel() {
        return this.level;
    }

    protected abstract void onMeetPlayer();

    protected abstract int soundDuration(boolean wasVisited);

    @Override
    public int getRenderLastOrder() {
        // Is behind the Player
        return 1;
    }

}
