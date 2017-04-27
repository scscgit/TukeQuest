package sk.tuke.gamedev.iddqd.tukequest.managers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ScoreManager {

    private static final int ONE_PLATFORM_SCORE_VALUE = 10;
    private static final int COMBO_MULTIPLIER = 5;
    public static ScoreManager INSTANCE;

    private int jumpingStreak;

    private List<Platform> platformsInCurrentJump = new ArrayList<>();

    private int score;
    private boolean playerIsJumping = false;
    private Label scoreLabel;
    private Label comboLabel;

    public void addScore(int score) {
        this.score += score;
        updateScoreLabel();
    }

    public int getCurrentScore() {
        return score;
    }

    // there are 3 reasons why streak might end:
    // 1. player has jumped only 1 platform (to continue streak, you have to jump 2+ platforms
    // 2. player has fallen to platform below without jumping
    // TODO: some timeout of streak
    public void notifyPlatformActiveChanged(Platform platform, boolean active) {
        // Player is jumping and reached the platform, so it should be added to the streak
        if (active && playerIsJumping) {
            platformsInCurrentJump.add(platform);
            jumpingStreak++;
            Log.d(this, "Increasing jump streak to " + jumpingStreak);
            updateComboLabel();
            return;
        }
        // Player fell down without jumping
        if (!active && !playerIsJumping) {
            Log.d(this, "Player FELL!");
            endStreak();
            return;
        }
        // Player didn't manage to land on the platform while jumping, but was above it before
        if (!active) {
            platformsInCurrentJump.remove(platform);
            jumpingStreak = jumpingStreak > 0 ? jumpingStreak - 1 : 0;
            Log.d(this, "Decreasing jump streak down to " + jumpingStreak);
            updateComboLabel();
            return;
        }

        Log.w(this, "Other reason of active platform change happened!");
    }

    public void notifyJumpStarted() {
        playerIsJumping = true;
    }

    public void notifyJumpEnded() {
        playerIsJumping = false;
        if (playerJumpedZeroPlatforms()) {
            // If player jumps and lands on the same platform, the streak should continue
            Log.d(this, "Player landed on the same or already awarded platform, continuing streak!");
        } else {
            addScoreForJump();
        }
    }

    private boolean playerJumpedZeroPlatforms() {
        return platformsInCurrentJump.size() == 0;
    }

    private void endStreak() {
        platformsInCurrentJump.clear();
        jumpingStreak = 0;
        Log.d(this, "Ending streak");
    }

    private void addScoreForJump() {
        int multipliedScore = calculateMultipliedScore();
        addScore(multipliedScore);
        Log.i(this,
            "Jump streak " + jumpingStreak
                + " added score bonus " + multipliedScore
                + ", score is now " + getCurrentScore());
        // mark all platforms that they awarded score
        platformsInCurrentJump.forEach(Platform::markAsScoreAwarded);
        endStreak();
    }

    private int calculateMultipliedScore() {
        int multipliedScore = ONE_PLATFORM_SCORE_VALUE * platformsInCurrentJump.size();
        for (int i = 0; i < jumpingStreak; i++) {
            multipliedScore += i * COMBO_MULTIPLIER;
        }
        return multipliedScore;
    }

    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
        updateScoreLabel();
    }

    private void updateScoreLabel() {
        this.scoreLabel.setText("Score: " + ScoreManager.INSTANCE.getCurrentScore());
    }

    public void setComboLabel(Label comboLabel) {
        this.comboLabel = comboLabel;
        updateComboLabel();
    }

    private void updateComboLabel() {
        if (this.jumpingStreak == 0) {
            this.comboLabel.setText("");
            return;
        }
        this.comboLabel.setText("Jump streak (" + this.jumpingStreak + ") combo: " + calculateMultipliedScore());
    }

}
