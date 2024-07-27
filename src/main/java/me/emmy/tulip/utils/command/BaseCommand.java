package me.emmy.tulip.utils.command;

import me.emmy.tulip.Tulip;

/**
 * @author minnymin3
 */
public abstract class BaseCommand {

    public Tulip main = Tulip.getInstance();

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
