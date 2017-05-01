package sk.tuke.gamedev.iddqd.tukequest.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.generator.GameLevelGenerator;
import sk.tuke.gamedev.iddqd.tukequest.levels.Level;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

import java.util.List;

/**
 *
 */
public class GameLevelManager implements Actor {

    private final GameScreen screen;
    private final Camera camera;
    private float latestLevelEndingY;
    private final List<Level> levels;
    private int levelIndex;

    public GameLevelManager(GameScreen screen, Camera camera, float startingY, List<Level> levels) {
        this.screen = screen;
        this.camera = camera;
        this.latestLevelEndingY = startingY;
        this.levels = levels;
    }

    @Override
    public void draw(Batch batch) {
    }

    @Override
    public void act() {
        // Change level when the player is at the end of a previous one
        if (this.latestLevelEndingY < this.camera.position.y + TukeQuestGame.SCREEN_HEIGHT) {
            GameLevelGenerator gameLevelGenerator = new GameLevelGenerator(
                this.screen,
                levels.get(this.levelIndex % levels.size()),
                this.latestLevelEndingY);
            gameLevelGenerator.generateLevel().forEach(this.screen::addActor);
            this.latestLevelEndingY = gameLevelGenerator.getLevelEndY();
            this.levelIndex++;
        }
        // TODO: implement some cleanUp logic here -> remove old levels maybe?
    }

}
