package sk.tuke.gamedev.iddqd.tukequest.managers;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.FxFlameMaster;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.visual.HUD;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ScoreManager {

    private static final int ONE_PLATFORM_SCORE_VALUE = 20;
    private static final int JUMP_COMBO_MULTIPLIER = 5;
    private static final int COMBO_TIMEOUT_SECONDS = 3;
    public static ScoreManager INSTANCE;

    private int platformStreak;
    private int lastPlatformStreak;
    private int jumpingStreak;

    private List<Platform> platformsInCurrentJump = new ArrayList<>();

    private int score;
    private boolean playerIsJumping = false;
    private HUD hud;
    private FxFlameMaster fxFlame;

    public int addScore(int score) {
        if (score < 0) {
            throw new RuntimeException("Attempted to add negative score, this should never happen");
        }
        if (this.fxFlame != null) {
            score *= this.fxFlame.getScoreMultiplier();
        }
        this.score += score;
        updateScoreLabel();
        return score;
    }

    public int getCurrentScore() {
        return score;
    }

    // there are 3 reasons why streak might end:
    // 1. player has jumped only 1 platform (to continue streak, you have to jump 2+ platforms
    // 2. player has fallen to platform below without jumping
    public void notifyPlatformActiveChanged(Platform platform, boolean active) {
        // Player is jumping and reached the platform, so it should be added to the streak
        if (active && playerIsJumping) {
            platformsInCurrentJump.add(platform);
            platformStreak++;
            Log.d(this, "Increasing jump streak to " + platformStreak);
            updateComboLabel();
            return;
        }
        // Player fell down without jumping
//        if (!active && !playerIsJumping) {
//            Log.d(this, "Player FELL!");
//            endStreak();
//            return;
//        }
        // Player didn't manage to land on the platform while jumping, but was above it before
        if (!active) {
            platformsInCurrentJump.remove(platform);
            platformStreak = platformStreak > 0 ? platformStreak - 1 : 0;
            Log.d(this, "Decreasing jump streak down to " + platformStreak);
            updateComboLabel();
            return;
        }

        Log.w(this, "Other reason of active platform change happened!");
    }

    public void notifyJumpStarted() {
        this.playerIsJumping = true;
    }

    public void notifyOnGround() {
        this.playerIsJumping = false;
        if (this.platformStreak < this.lastPlatformStreak) {
            // The streak has ended
            addScoreForJump();
            TaskManager.INSTANCE.removeTimers("comboReset");
        } else if (this.platformStreak == this.lastPlatformStreak) {
            // If player jumps and lands on the same platform, the streak should continue
            Log.d(this, "Player landed on the same or already awarded platform, continuing streak!");
        } else {
            this.lastPlatformStreak = this.platformStreak;
            this.jumpingStreak++;
            TaskManager.INSTANCE.removeTimers("comboReset");
            TaskManager.INSTANCE.scheduleTimer("comboReset", COMBO_TIMEOUT_SECONDS, () -> {
                this.platformStreak--;
                addScoreForJump();
            });
        }
        updateComboLabel();
    }

    private void endStreak() {
        platformsInCurrentJump.clear();
        this.platformStreak = 0;
        this.lastPlatformStreak = 0;
        this.jumpingStreak = 0;
        Log.d(this, "Ending streak");
        updateComboLabel();
    }

    private void addScoreForJump() {
        int multipliedScore = calculateMultipliedScore();
        int actualScore = addScore(multipliedScore);
        Log.i(this,
            "Jump streak " + this.jumpingStreak
                + " added score bonus " + multipliedScore
                + "x" + actualScore / (multipliedScore == 0 ? 1 : multipliedScore)
                + " with " + this.platformStreak
                + " platforms, score is now " + getCurrentScore());
        // Mark all platforms that awarded score
        // Probably obsolete from now on, it makes more sense to let player get awarded again
        //platformsInCurrentJump.forEach(Platform::markAsScoreAwarded);
        endStreak();
    }

    private int calculateMultipliedScore() {
        return calculateMultipliedScore(this.platformStreak);
    }

    private int calculateMultipliedScore(int scoreForPlatformStreak) {
//        int multipliedScore = ONE_PLATFORM_SCORE_VALUE * platformsInCurrentJump.size();
        int multipliedScore = ONE_PLATFORM_SCORE_VALUE * scoreForPlatformStreak;
        for (int i = 0; i < this.jumpingStreak; i++) {
            // First jump is not rewarded
            multipliedScore += i * JUMP_COMBO_MULTIPLIER;
        }
        return multipliedScore < 0 ? 0 : multipliedScore;
    }

    public void setHud(HUD hud) {
        this.hud = hud;
        updateScoreLabel();
        updateComboLabel();
    }

    private void updateScoreLabel() {
        this.hud.setScore(getCurrentScore());
    }

    private void updateComboLabel() {
        if (this.platformStreak == 0) {
            this.hud.setCombo(0, 0, 0, 0);
            return;
        }
        this.hud.setCombo(
            this.platformStreak - 1,
            this.jumpingStreak,
            calculateMultipliedScore(this.platformStreak - 1),
            this.fxFlame != null ? this.fxFlame.getScoreMultiplier() : 1);
    }

    public void registerFxFlame(FxFlameMaster firstFlame) {
        this.fxFlame = firstFlame;
    }

}
