package sk.tuke.gamedev.iddqd.tukequest.managers;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class ScoreManager {

    private Set<Platform> alreadyCalculatedPlatforms = new HashSet<>();

    public static ScoreManager INSTANCE;

    private final Player player;

    private int score = 0;

    public ScoreManager(Player player) {
        this.player = player;
    }

    public int getCurrentScore() {
        return score;
    }

    public void addScoreForJumpedPlatform(Platform platform) {
        if (!alreadyCalculatedPlatforms.contains(platform)) {
            score = score + 10;
            alreadyCalculatedPlatforms.add(platform);
            System.out.println("added one more platform to score, score is now " + score);
        }


//        // TODO: score calculation logic
//        score = (int) player.getPosition().y;
    }
}
