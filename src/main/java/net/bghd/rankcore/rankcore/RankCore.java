package net.bghd.rankcore.rankcore;

import lombok.Getter;
import net.bghd.rankcore.rankcore.command.CommandLib;
import net.bghd.rankcore.rankcore.mysql.MySQLManager;
import net.bghd.rankcore.rankcore.mysql.Profile;
import net.bghd.rankcore.rankcore.mysql.ProfileManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class RankCore extends JavaPlugin implements Listener {

    @Getter
    public static RankCore instance;
    public MySQLManager MySQLManager;
    public ProfileManager profileManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        MySQLManager = new MySQLManager(this);
        profileManager = new ProfileManager(this);
        getServer().getPluginManager().registerEvents(this, this);
        new CommandLib().setupBukkit(this).register(new RankCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getMySQLManager().shutdown();
    }


    @EventHandler
    public void onProfileLoad(AsyncPlayerPreLoginEvent event) {
        try {
            getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
        } catch (NullPointerException e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "ERROR: Profile could not be created");
        }
    }

    @EventHandler
    public void saveData(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = getProfileManager().getProfile(player);
        if(profile != null) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> profile.getData().save(player));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = getProfileManager().getProfile(player);
        try {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> profile.getData().load(player));
        } catch (NullPointerException e) {
            player.kickPlayer("Error: Player profile could not be loaded!");
        }
    }

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        Profile profile = getProfileManager().getProfile(event.getPlayer());
        String message = event.getMessage();
        event.setCancelled(true);

        TextComponent formatted = new TextComponent("");
        TextComponent rankComponent = new TextComponent(profile.getData().getRank().getPrefix() + " ");
        TextComponent playerComponent = new TextComponent(event.getPlayer().getName());
        TextComponent messageContent = new TextComponent(" " + message);

        formatted.addExtra(rankComponent);
        formatted.addExtra(playerComponent);
        formatted.addExtra(":");
        formatted.addExtra(messageContent);

        event.getRecipients().forEach(player -> player.spigot().sendMessage(formatted));
    }
}
