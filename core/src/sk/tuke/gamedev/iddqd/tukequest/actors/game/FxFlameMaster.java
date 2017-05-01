package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.HUD;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Steve on 27.04.2017.
 */
public class FxFlameMaster extends FxFlame {

    private static final float MIN_DISTANCE = 1000;
    public static final float MIN_SPEED_START = 1.5f;
    public static final float MAX_DISTANCE = 2500;
    private static final float MAX_SPEED = 12;
    private static final float MAX_INCREASE_OF_MIN_SPEED = 7;
    private static final float SCORE_MULTIPLIER_DISTANCE_RATIO = 500;
    public static final float SPEED_DECREASE_ON_PLAYER_LOST_LIFE = 1.5f;
    public static final float SPEED_DECREASE_ON_MET_TEACHER = 1.0f;

    private float minSpeed = MIN_SPEED_START;
    private HUD hud;
    private List<FxFlame> otherFlames = new LinkedList<>();

    public FxFlameMaster(Player player, float x, float y) {
        super(player, x, y);
    }

    @Override
    public void onAddedToScreen(AbstractScreen screen) {
        setFlameVelocity(this.minSpeed);
    }

    @Override
    public boolean setFlameVelocity(float velocity) {
        if (!super.setFlameVelocity(velocity)) {
            return false;
        }
        this.otherFlames.forEach(flame -> flame.setFlameVelocity(velocity));
        return true;
    }

    public void increaseMinFlameVelocity(float velocity) {
        if (this.minSpeed + velocity > MAX_INCREASE_OF_MIN_SPEED) {
            this.minSpeed = MAX_INCREASE_OF_MIN_SPEED;
            return;
        }
        if (this.minSpeed + velocity < MIN_SPEED_START) {
            this.minSpeed = MIN_SPEED_START;
            return;
        }
        this.minSpeed += velocity;
    }

    public float getMinSpeed() {
        return this.minSpeed;
    }

    @Override
    public void act() {
        // Master will not kill the player in order to honor transparent space on the top of the texture
        float flameDistance = getFlameDistance();
        if (this.hud != null) {
            this.hud.setFlameDistance(flameDistance);
        }
        if (flameDistance < MIN_DISTANCE) {
            setFlameVelocity(this.minSpeed);
        } else if (flameDistance > MAX_DISTANCE) {
            setFlameVelocity(MAX_SPEED);
        } else {
            float distanceFactor = (flameDistance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE);
            setFlameVelocity(distanceFactor * (MAX_SPEED - this.minSpeed) + this.minSpeed);
        }
    }

    private float getFlameDistance() {
        return this.player.getY() - getY();
    }

    public void addOtherFlame(FxFlame fxFlame) {
        this.otherFlames.add(fxFlame);
    }

    public void registerHud(HUD hud) {
        this.hud = hud;
    }

    public int getScoreMultiplier() {
        // Multiplier starts at half the distance
        float multiplier = (getFlameDistance() - (MAX_DISTANCE - MIN_DISTANCE) / 2) / SCORE_MULTIPLIER_DISTANCE_RATIO;
        int intMultiplier = (int) multiplier;
        // Multiplier cannot go below 1 when the flame is nearby
        return intMultiplier > 0 ? intMultiplier : 1;
    }

    public void playerLostLife() {
        increaseMinFlameVelocity(-SPEED_DECREASE_ON_PLAYER_LOST_LIFE);
    }

    public void playerMetTeacher() {
        increaseMinFlameVelocity(-SPEED_DECREASE_ON_MET_TEACHER);
    }

}
