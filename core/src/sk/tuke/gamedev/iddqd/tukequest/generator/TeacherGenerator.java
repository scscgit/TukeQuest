package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Binas;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Genci;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class TeacherGenerator {

    public static GameScreen screen;

    public static List<Platform> levelPlatforms = new ArrayList<>();

    private static Random random = new Random();

    public static void generateTeacherIfNeeded() {

        if (!levelPlatforms.isEmpty()) {
            Platform platform = levelPlatforms.get(0);

            generateRandomTeacher(platform.getPosition().y + platform.getHeight());


            levelPlatforms.remove(0);
        }

//
//        new Poruban(this, TukeQuestGame.SCREEN_WIDTH * 1.3f / 5f, groundHeight)
//            .addToWorld(this);
    }

    private static void generateRandomTeacher(float position) {

        int randomNumber = random.nextInt(3);
        switch (randomNumber) {
            case 0:
                new Binas(screen, TukeQuestGame.SCREEN_WIDTH * 3.7f / 5f, position)
                    .addToWorld(screen);
                System.out.println("Adding BINAS!");
                break;
            case 1:
                new Poruban(screen, TukeQuestGame.SCREEN_WIDTH * 3.7f / 5f, position)
                    .addToWorld(screen);
                System.out.println("Adding PORUBAN!");
                break;
            case 2:
                new Genci(screen, TukeQuestGame.SCREEN_WIDTH * 3.7f / 5f, position)
                    .addToWorld(screen);
                System.out.println("Adding GENCI!");
                break;

        }



    }


}
