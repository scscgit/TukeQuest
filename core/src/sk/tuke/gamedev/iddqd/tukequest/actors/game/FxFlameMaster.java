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
    private static final float MAX_SPEED = 7;

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
        if (this.minSpeed + velocity > MAX_SPEED) {
            this.minSpeed = MAX_SPEED;
            return;
        }
        this.minSpeed += velocity;
    }

    @Override
    public void act() {
        // Master will not kill the player in order to honor transparent space on the top of the texture
        float flameDistance = this.player.getY() - getY();
        if (this.hud != null) {
            this.hud.setFlameDistance(flameDistance);
        }
        if (flameDistance < MIN_DISTANCE) {
            setFlameVelocity(this.minSpeed);
        } else if (flameDistance > MAX_DISTANCE) {
            setFlameVelocity(MAX_SPEED);
        } else {
            float distanceFactor = 1 - (MAX_DISTANCE - (flameDistance - MIN_DISTANCE)) / MAX_DISTANCE;
            setFlameVelocity(distanceFactor * MAX_SPEED + MIN_SPEED_START);
        }
    }

    public void addOtherFlame(FxFlame fxFlame) {
        this.otherFlames.add(fxFlame);
    }

    public void registerHud(HUD hud) {
        this.hud = hud;
    }

}
