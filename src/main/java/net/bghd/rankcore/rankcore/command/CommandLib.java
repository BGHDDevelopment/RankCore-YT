/*
 * Copyright (c) BGHDDevelopment LLC 2021
 */

package net.bghd.rankcore.rankcore.command;

import lombok.NoArgsConstructor;
import net.bghd.rankcore.rankcore.command.register.BukkitRegister;
import net.bghd.rankcore.rankcore.command.register.RegisterBase;

@NoArgsConstructor
public class CommandLib {

    private org.bukkit.plugin.Plugin bukkitPlugin;
    private RegisterBase platform;

    public CommandLib setupBukkit(org.bukkit.plugin.Plugin bukkitPlugin) {
        this.bukkitPlugin = bukkitPlugin;
        this.platform = new BukkitRegister(this);
        this.platform.setup();

        return this;
    }

    public CommandLib register(Command command) {
        if (this.bukkitPlugin != null) {
            this.platform.registerCommand(command, this.bukkitPlugin);
        }
        return this;
    }
}
