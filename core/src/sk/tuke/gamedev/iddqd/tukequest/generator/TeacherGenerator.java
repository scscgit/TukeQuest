package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.AbstractTeacher;
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

    public static List<Platform> levelPlatforms = new ArrayList<>();
    private static Random random = new Random();

//    public static void generateTeacherIfNeeded() {
//        if (!levelPlatforms.isEmpty()) {
//            Platform platform = levelPlatforms.get(0);
//            generateRandomTeacherAndAddToWorld(platform.getPosition().y + platform.getHeight());
//            levelPlatforms.remove(0);
//        }
//    }

    public static void generateRandomTeacherAndAddToWorld(float position, GameScreen screen) {
        generateRandomTeacher(position, screen).addToWorld(screen);
    }

    public static AbstractTeacher generateRandomTeacherOnPlatform(Platform platform, GameScreen screen) {
        return generateRandomTeacher(platform.getPosition().y + platform.getHeight(), screen);
    }

    public static AbstractTeacher generateRandomTeacher(float position, GameScreen screen) {
        int randomNumber = random.nextInt(3);
        float location = random.nextBoolean() ? 1.3f / 5f : 3.7f / 5f;
        switch (randomNumber) {
            case 0:
                return new Binas(screen, TukeQuestGame.SCREEN_WIDTH * location, position);
            case 1:
                return new Poruban(screen, TukeQuestGame.SCREEN_WIDTH * location, position);
            case 2:
                return new Genci(screen, TukeQuestGame.SCREEN_WIDTH * location, position);
        }
        throw new RuntimeException("Teacher generation random number out of bounds");
    }

}
