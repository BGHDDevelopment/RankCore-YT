/*
 * Copyright (c) BGHDDevelopment LLC 2021
 */

package net.bghd.rankcore.rankcore.command.register;

import net.bghd.rankcore.rankcore.command.Command;
import net.bghd.rankcore.rankcore.command.CommandLib;
import net.bghd.rankcore.rankcore.command.DynamicCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BukkitRegister extends RegisterBase {

    private CommandMap commandMap;

    public BukkitRegister(CommandLib commandLib) {
        super(commandLib);
    }

    @Override
    public void setup() {
        try {
            Field commandMap = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMap.setAccessible(true);

            this.commandMap = (CommandMap) commandMap.get(Bukkit.getPluginManager());

        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public CommandLib registerCommand(Command command, Plugin plugin) {
        CommandLib commandLib = super.getCommandLib();

        for (Annotation annotation : command.getClass().getAnnotations()) {
            if (!DynamicCommand.class.isAssignableFrom(annotation.annotationType())) {
                continue;
            }

            WrappedCommand wrappedCommand = new WrappedCommand(
                    command, (DynamicCommand) annotation
            );

            this.commandMap.register(plugin.getName(), wrappedCommand);
        }

        return commandLib;
    }

    private class WrappedCommand extends org.bukkit.command.Command {

        private Command command;
        private DynamicCommand dynamicCommand;

        WrappedCommand(Command command, DynamicCommand dynamicCommand) {
            super(dynamicCommand.name(), dynamicCommand.description(),
                    dynamicCommand.usage(), Arrays.asList(dynamicCommand.aliases())
            );

            this.command = command;
            this.dynamicCommand = dynamicCommand;
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            if (!(sender instanceof Player) && !this.dynamicCommand.console()) {
                return true;
            }
            this.command.execute(sender, args);
            return true;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String... args)
                throws IllegalArgumentException {
            return this.command.tabComplete(sender, alias, args);
        }
    }
}
