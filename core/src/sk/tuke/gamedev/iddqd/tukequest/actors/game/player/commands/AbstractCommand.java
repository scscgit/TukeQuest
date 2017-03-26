package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

/**
 * Created by Steve on 27.03.2017.
 */
public abstract class AbstractCommand implements Command {

    private Command nextCommand;

    protected AbstractCommand(Command nextCommand) {
        this.nextCommand = nextCommand;
    }

    protected final void runNextCommand(Player player) {
        if (this.nextCommand == null) {
            return;
        }
        this.nextCommand.execute(player);
    }

}
