package sk.tuke.gamedev.iddqd.tukequest.levels;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;

/**
 * Created by Steve on 01.05.2017.
 */
public class PorubanFirstLevel extends Level {

    public PorubanFirstLevel() {
        super(
            "Porubanov level",
            Platform.PlatformTexture.CHIMNEY,
            150,
            Poruban.class,
            Background.BackgroundTexture.ICY_TOWER,
            VerticalWall.WallTexture.BINARY);
    }

    @Override
    public void levelAchieved(Player player) {
        super.levelAchieved(player);
        player.getGameScreen().setGravity(new Vector2(0, -25));
    }

}
