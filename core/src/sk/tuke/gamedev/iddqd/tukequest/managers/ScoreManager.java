package sk.tuke.gamedev.iddqd.tukequest.managers;

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

    private int jumpingStreak = 0;

    private List<Platform> platformsInCurrentJump = new ArrayList<>();

    private int score = 0;
    private boolean playerIsJumping = false;

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
            return;
        }

        Log.w(this, "Other reason of active platform change happened!");
    }

    public void notifyJumpStarted() {
        playerIsJumping = true;
    }

    public void notifyJumpEnded() {
        playerIsJumping = false;

        // If player jumps and lands on the same platform, the streak should continue
        if (playerJumpedZeroPlatforms()) {
            Log.d(this, "Player landed on the same or already awarded platform, continuing streak!");
        } else if (playerJumpedOnlyOnePlatform()) {
            // if player jumped only one platform, we should end the streak and add score for one platform without multiplier
            addScoreForSinglePlatform();
            endStreak();
        } else {
            addScoreForThisJumpJumpedPlatforms();
            endStreak();
        }
    }

    private boolean playerJumpedOnlyOnePlatform() {
        return platformsInCurrentJump.size() == 1;
    }

    private boolean playerJumpedZeroPlatforms() {
        return platformsInCurrentJump.size() == 0;
    }

    private void addScoreForSinglePlatform() {
        score += ONE_PLATFORM_SCORE_VALUE;
        Log.i(this, "Adding score for single platform, score is now " + score);
    }

    private void endStreak() {
        platformsInCurrentJump.clear();
        jumpingStreak = 0;
        Log.d(this, "Ending streak");
    }

    private void addScoreForThisJumpJumpedPlatforms() {
        // TODO: multiplier calculation logic adjustment
        int baseScoreForNumberOfPlatforms = ONE_PLATFORM_SCORE_VALUE * platformsInCurrentJump.size();
        int multipliedScore = baseScoreForNumberOfPlatforms;
        for (int i = 0; i < jumpingStreak; i++) {
            multipliedScore += i * COMBO_MULTIPLIER;
        }
        score += multipliedScore;
        Log.i(this,
            "Jump streak " + jumpingStreak
                + " added score bonus " + multipliedScore
                + ", score is now " + score);


        // mark all platforms that they awarded score
        platformsInCurrentJump.forEach(Platform::markAsScoreAwarded);

        // clear platforms in this jump
        platformsInCurrentJump.clear();
    }
}
