package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

/**
 * Created by Steve on 26.03.2017.
 */
public interface Command {

    void execute(Player player);

    Command setNext(Command nextCommand);

}
