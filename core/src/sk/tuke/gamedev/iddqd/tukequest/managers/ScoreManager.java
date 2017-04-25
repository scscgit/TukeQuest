package sk.tuke.gamedev.iddqd.tukequest.managers;

/**
 *
 */
public class ScoreManager {

    public static ScoreManager INSTANCE;

    private int score = 0;

    public int getCurrentScore() {
        return score;
    }

    public void addScoreForJumpedPlatform() {
        score = score + 10;
        System.out.println("Added score for platofor, score is now: " + score);
    }
}
