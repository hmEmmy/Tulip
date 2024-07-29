package me.emmy.tulip.api.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Framework - CommandArgs <br>
 * This class is passed to the command methods and contains various utilities as
 * well as the command info.
 *
 * @author minnymin3
 */
public class CommandArgs {

    private final CommandSender sender;
    private final org.bukkit.command.Command command;
    private final String label;
    private final String[] args;

    protected CommandArgs(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
                          int subCommand) {
        String[] modArgs = new String[args.length - subCommand];
        if (args.length - subCommand >= 0) System.arraycopy(args, subCommand, modArgs, 0, args.length - subCommand);

        StringBuffer buffer = new StringBuffer();
        buffer.append(label);
        for (int x = 0; x < subCommand; x++) {
            buffer.append("." + args[x]);
        }
        String cmdLabel = buffer.toString();
        this.sender = sender;
        this.command = command;
        this.label = cmdLabel;
        this.args = modArgs;
    }

    public CommandSender getSender() {
        return sender;
    }

    public org.bukkit.command.Command getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgs(int index) {
        return args[index];
    }

    public int length() {
        return args.length;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            return null;
        }
    }

}
