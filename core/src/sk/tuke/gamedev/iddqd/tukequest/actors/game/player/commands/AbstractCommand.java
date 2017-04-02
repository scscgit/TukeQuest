package sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;

/**
 * Created by Steve on 27.03.2017.
 */
public abstract class AbstractCommand implements Command {

    private Command nextCommand;
    private Command lastCommand;

    protected AbstractCommand() {
    }

    protected AbstractCommand(Command nextCommand) {
        this.nextCommand = nextCommand;
    }

    public final void execute(Player player) {
        onExecute(player);
        runNextCommand(player);
    }

    protected abstract void onExecute(Player player);

    protected final void runNextCommand(Player player) {
        if (this.nextCommand == null) {
            return;
        }
        this.nextCommand.execute(player);
    }

    public final Command setNext(Command nextCommand) {
        if (this.lastCommand == null) {
            this.lastCommand = nextCommand;
            this.nextCommand = nextCommand;
        } else {
            this.lastCommand.setNext(nextCommand);
        }
        return this;
    }

}
