package me.emmy.tulip.utils.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.annotation.Command;
import me.emmy.tulip.utils.command.annotation.Completer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class CommandFramework implements CommandExecutor {

    private final Map<String, Entry<Method, Object>> commandMap = new HashMap<String, Entry<Method, Object>>();
    private final Tulip plugin = Tulip.getInstance();
    private CommandMap map;

    public CommandFramework() {
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append("." + args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command command = method.getAnnotation(Command.class);

                if (command.isAdminOnly() && !sender.hasPermission("Tulip.admin")) {
                    sender.sendMessage(ChatColor.RED + "Only ops may execute this command.");
                    return true;
                }
                if (!command.permission().equals("") && (!sender.hasPermission(command.permission()))) {
                    sender.sendMessage(CC.translate("&cNo permission.")); // TODO change
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command is only performable in game.");
                    return true;
                }

                try {
                    method.invoke(methodObject,
                            new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        defaultCommand(new CommandArgs(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                registerCommand(command, command.name(), m, obj);
                for (String alias : command.aliases()) {
                    registerCommand(command, alias, m, obj);
                }
            } else if (m.getAnnotation(Completer.class) != null) {
                Completer comp = m.getAnnotation(Completer.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes().length == 0
                        || m.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println(
                            "Unable to register tab completer " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                if (m.getReturnType() != List.class) {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected return type");
                    continue;
                }
                registerCompleter(comp.name(), m, obj);
                for (String alias : comp.aliases()) {
                    registerCompleter(alias, m, obj);
                }
            }
        }
    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<HelpTopic>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : commandMap.keySet()) {
            if (!s.contains(".")) {
                org.bukkit.command.Command cmd = map.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, help,
                "Below is a list of all " + plugin.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    public void unregisterCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                commandMap.remove(command.name().toLowerCase());
                commandMap.remove(this.plugin.getName() + ":" + command.name().toLowerCase());
                map.getCommand(command.name().toLowerCase()).unregister(map);
            }
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(),
                new AbstractMap.SimpleEntry<Method, Object>(m, obj));
        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }
        if (!command.description().equalsIgnoreCase("") && cmdLabel == label) {
            map.getCommand(cmdLabel).setDescription(command.description());
        }
        if (!command.usage().equalsIgnoreCase("") && cmdLabel == label) {
            map.getCommand(cmdLabel).setUsage(command.usage());
        }
    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command command = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), command);
        }
        if (map.getCommand(cmdLabel) instanceof BukkitCommand) {
            BukkitCommand command = (BukkitCommand) map.getCommand(cmdLabel);
            if (command.completer == null) {
                command.completer = new BukkitCompleter();
            }
            command.completer.addCompleter(label, m, obj);
        } else if (map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                Object command = map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                if (field.get(command) == null) {
                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                } else if (field.get(command) instanceof BukkitCompleter) {
                    BukkitCompleter completer = (BukkitCompleter) field.get(command);
                    completer.addCompleter(label, m, obj);
                } else {
                    System.out.println("Unable to register tab completer " + m.getName()
                            + ". A tab completer is already registered for that command!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void defaultCommand(CommandArgs args) {

        String label = args.getLabel();
        String[] parts = label.split(":");

        if (args.getSender().hasPermission("tulip.owner")) {
            if (parts.length > 1) {
                String commandToExecute = parts[1];

                StringBuilder commandBuilder = new StringBuilder(commandToExecute);
                for (String arg : args.getArgs()) {
                    commandBuilder.append(" ").append(arg);
                }
                String command = commandBuilder.toString();

                if (args.getSender() instanceof Player) {
                    ((Player) args.getSender()).performCommand(command);
                } else {
                    args.getSender().getServer().dispatchCommand(args.getSender(), command);
                }
            } else {
                args.getSender().sendMessage(CC.translate("&cMissing arguments / Wrong format or Internal error."));
            }
        } else {
            args.getSender().sendMessage(CC.translate("&cNo permission."));
        }
    }

    public Set<Class<? extends BaseCommand>> registerCommandsInPackage(String packageName) {
        ConfigurationBuilder config = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(false))
                .filterInputsBy(new FilterBuilder().excludePackage("me.emmy.core.api"));

        Reflections reflections = new Reflections(config);
        Set<Class<? extends BaseCommand>> commandClasses = reflections.getSubTypesOf(BaseCommand.class);
        for (Class<? extends BaseCommand> clazz : commandClasses) {
            try {
                Constructor<? extends BaseCommand> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                BaseCommand commandInstance = constructor.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException e) {
                Bukkit.getLogger().severe("Failed to register command " + clazz.getSimpleName());
            }
        }
        return commandClasses;
    }
}
