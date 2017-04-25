package sk.tuke.gamedev.iddqd.tukequest.managers;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

import java.util.List;

/**
 * On the game initialization, it is required to set up an instance holding a current Player reference.
 * <p>
 * Created by Steve on 25.03.2017.
 */
public class PlatformManager {

    public static PlatformManager INSTANCE;

    private final Player player;

    public PlatformManager(Player player) {
        this.player = player;
    }

    public boolean isPlatformActive(Platform platform) {
        return this.player.getY() >= platform.getY() + platform.getHeight();
    }

}
