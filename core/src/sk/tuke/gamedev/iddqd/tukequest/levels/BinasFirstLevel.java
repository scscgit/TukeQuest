package sk.tuke.gamedev.iddqd.tukequest.levels;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.PublicClass;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Binas;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

/**
 * Created by Steve on 01.05.2017.
 */
public class BinasFirstLevel extends Level {

    public BinasFirstLevel() {
        super(
            "Binasov level",
            Platform.PlatformTexture.BINARY,
            50,
            Binas.class,
            Background.BackgroundTexture.CODE,
            VerticalWall.WallTexture.BINARY);
    }

    @Override
    public void levelAchieved(Player player) {
        super.levelAchieved(player);
        // After finishing Poruban, there will be default gravity once again
        player.getGameScreen().setGravity(new Vector2(0, -GameScreen.GRAVITY_START));
        TaskManager.INSTANCE.removeTimers(PublicClass.FAST_JUMP_TIMER_NAME);
    }

}
