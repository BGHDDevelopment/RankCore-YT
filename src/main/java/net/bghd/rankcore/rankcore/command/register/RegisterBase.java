/*
 * Copyright (c) BGHDDevelopment LLC 2021
 */

package net.bghd.rankcore.rankcore.command.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bghd.rankcore.rankcore.command.Command;
import net.bghd.rankcore.rankcore.command.CommandLib;

@AllArgsConstructor
public abstract class RegisterBase {

    @Getter
    private CommandLib commandLib;

    public abstract void setup();


    public CommandLib registerCommand(Command command, org.bukkit.plugin.Plugin plugin) {
        return this.commandLib;
    }
}
