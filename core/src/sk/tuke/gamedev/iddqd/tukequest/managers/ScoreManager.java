package sk.tuke.gamedev.iddqd.tukequest.managers;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ScoreManager {

    private static final int ONE_PLATFORM_SCORE_VALUE = 10;
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
    public void notifyPlatformActiveChanged(Platform platform) {

        // if platform became active, it is not in list of platformsInStreak and player is jumping
        // -> player reached the platform and it should be added to streak
        if (platform.getBody().isActive() && !platformsInCurrentJump.contains(platform) && playerIsJumping) {
            platformsInCurrentJump.add(platform);
            jumpingStreak++;
            System.out.println("Addint to platformsInCurrentJump + increasing Streak to " + jumpingStreak);
            return;
        }
        // if platform became disabled, and it is in list of platformsInStreak
        // it means that the player did not reach the platform while jumping (didn't manage to land on it, but was above it before)
        // and it should be removed from platformsInCurrentJump list and jumpingStreak should be decreased
        if (!platform.getBody().isActive() && platformsInCurrentJump.contains(platform)) { //&& playerIsJumping
            platformsInCurrentJump.remove(platform);
            jumpingStreak = jumpingStreak > 0 ? jumpingStreak - 1 : 0;
            System.out.println("Remvoing from platformsInCurrentJump + decreasing Streak, now = " + jumpingStreak);
            return;
        }

        // third case: player fell without jumping
        // FIXME: 25/04/2017 THIS looks like not working properly
        if (!platform.getBody().isActive() && !playerIsJumping) {
            System.out.println("Player FELL!");
            endStreak();
            return;
        }

        System.out.println("Other case happened!");

    }

    public void notifyJumpStarted() {
        playerIsJumping = true;
    }

    public void notifyJumpEnded() {
        playerIsJumping = false;

        // if player jumps and lands on the same platform, the streak should continue
        if (playerJumpedZeroPlatforms()) {
            System.out.println("Player landed on the same platform OR jumped platforms were already awarded, continuing streak!!!");
            endStreak();


        } else if (playerJumpedOnlyOnePlatform()) {
            // if player jumped only one platform, we should end the streak and add score for one platform without multiplier
            endStreak();
            addScoreForSinglePlatform();
        } else {
            addScoreForThisJumpJumpedPlatforms();
        }

    }

    private boolean playerJumpedOnlyOnePlatform() {
        return platformsInCurrentJump.size() == 1;
    }

    private boolean playerJumpedZeroPlatforms() {
        return platformsInCurrentJump.size() == 0;
    }

    private void addScoreForSinglePlatform() {
        score = score + ONE_PLATFORM_SCORE_VALUE;
        System.out.println("Adding score for single platform");
    }

    private void endStreak() {
        platformsInCurrentJump.clear();
        jumpingStreak = 0;
        System.out.println("Ending streak;");
    }

    private void addScoreForThisJumpJumpedPlatforms() {
        // TODO: multiplier calculation logic adjustment
        float comboMultiplier = 2;
        int baseScoreForNumberOfPlatforms = ONE_PLATFORM_SCORE_VALUE * platformsInCurrentJump.size();
        int muplipliedScore = (int) (baseScoreForNumberOfPlatforms * comboMultiplier);
        score = score + muplipliedScore;
        System.out.println("Adding score, jumpingStreak for multiplier calculation is " + jumpingStreak + " score is now " + score);


        // mark all platforms that they awarded score
        platformsInCurrentJump.forEach(Platform::markAsScoreAwarded);

        // clear platforms in this jump
        platformsInCurrentJump.clear();
    }
}
