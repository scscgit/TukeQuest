package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;

import java.util.List;

/**
 *
 */
public class GameLevelManager implements Actor {
    private final List<Level> levels;

    //List<GameLevelGenerator> gameLevelGeneratorList = new ArrayList<>();

    private GameScreen screen;
    private Camera camera;
    private float latestLevelEndingY;

    private int levelIndex = 0;

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
        if (this.latestLevelEndingY < this.camera.position.y + TukeQuestGame.SCREEN_HEIGHT) {

            // change level
            Level level = levels.get(levelIndex % levels.size());
            GameLevelGenerator gameLevelGenerator = new GameLevelGenerator(screen, level, latestLevelEndingY);
            List<Actor> levelActors = gameLevelGenerator.generateLevel();

            levelActors.forEach(bodyActor -> {
                screen.addActor(bodyActor);
            });
            latestLevelEndingY = gameLevelGenerator.getLevelEndY();
            levelIndex++;
        }
        // TODO: implement some cleanUp logic here -> remove old levels maybe?
    }

}
