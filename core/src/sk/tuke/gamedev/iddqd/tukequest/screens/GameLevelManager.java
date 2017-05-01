package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameLevelManager implements Actor {

    List<GameLevelGenerator> gameLevelGeneratorList = new ArrayList<>();

    private GameScreen screen;

    private Camera camera;

    private float latestLevelEndingY;


    public GameLevelManager(GameScreen screen, Camera camera, float startingY) {
        this.screen = screen;
        this.camera = camera;
        this.latestLevelEndingY = startingY;
    }


    @Override
    public void draw(Batch batch) {

    }

    @Override
    public void act() {
        if (this.latestLevelEndingY < this.camera.position.y + TukeQuestGame.SCREEN_HEIGHT) {
            GameLevelGenerator gameLevelGenerator = new GameLevelGenerator(screen, camera, latestLevelEndingY);
            List<BodyActor> levelActors = gameLevelGenerator.generateLevel();

            levelActors.forEach(bodyActor -> {
                bodyActor.addToWorld(screen);
            });

            latestLevelEndingY = gameLevelGenerator.getLevelEndY();
        }
        // TODO: implement some cleanUp logic here -> remove old levels maybe?

    }
}
