/*
 * Copyright (c) BGHDDevelopment LLC 2021
 */

package net.bghd.rankcore.rankcore.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    public void execute(CommandSender sender, String... args) {
        // intentionally empty
    }

    public List<String> tabComplete(CommandSender sender, String alias, String... args) {
        return new ArrayList<>();
    }
}
