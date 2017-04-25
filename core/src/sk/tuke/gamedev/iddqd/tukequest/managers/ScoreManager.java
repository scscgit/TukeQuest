package sk.tuke.gamedev.iddqd.tukequest.managers;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ScoreManager {

    private int jumpingStreak = 0;

    public static List<Platform> platformsInRow = new ArrayList<>();

    public static ScoreManager INSTANCE;

    private int score = 0;

    public int getCurrentScore() {
        return score;
    }

    public void addScoreForJumpedPlatform() {
        score = score + 10;
        System.out.println("Added score for platofor, score is now: " + score);
    }

    public void addScoreForPlatforms(int countOfPlatformsReallyJumped) {

        // FIXME: 25/04/2017 Streak is not reseted if player falls down (without jumping)
        if (countOfPlatformsReallyJumped < 2) {
            // if no or only one platform was jumped, do not consider this a jumping streak
            jumpingStreak = 0;
        } else {
            jumpingStreak += countOfPlatformsReallyJumped;
        }

        System.out.println("JumpingStreak = " + jumpingStreak);
        // TODO: multiplier
        float comboMultiplier = 1 + jumpingStreak / 2;
        score =  score +  (int) (comboMultiplier * countOfPlatformsReallyJumped);
        System.out.println("Adding score for jumping " + countOfPlatformsReallyJumped + " score is now: " + score);
    }
}
