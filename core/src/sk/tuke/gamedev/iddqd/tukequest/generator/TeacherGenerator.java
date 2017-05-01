package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.AbstractTeacher;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Binas;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Genci;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;
import sk.tuke.gamedev.iddqd.tukequest.levels.Level;
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

    public static AbstractTeacher generateTeacherOnPlatform(Level teacherLevel, Platform platform, GameScreen screen) {
        float position = platform.getPosition().y + platform.getHeight();
        float location = random.nextBoolean() ? 1.3f / 5f : 3.7f / 5f;
        if (teacherLevel.teacherClass == Genci.class) {
            return new Genci(screen, teacherLevel, TukeQuestGame.SCREEN_WIDTH * location, position);
        } else if (teacherLevel.teacherClass == Binas.class) {
            return new Binas(screen, teacherLevel, TukeQuestGame.SCREEN_WIDTH * location, position);
        } else if (teacherLevel.teacherClass == Poruban.class) {
            return new Poruban(screen, teacherLevel, TukeQuestGame.SCREEN_WIDTH * location, position);
        } else {
            throw new RuntimeException("Unexpected teacher generation request");
        }
    }

}
