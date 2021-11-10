package net.bghd.rankcore.rankcore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Rank {

    ADMIN("Admin", ChatColor.RED, true, false, 2),
    DEFAULT("Default", ChatColor.GRAY, false, false, 1);


    private String name;
    private ChatColor color;
    private boolean bolded, italicized;
    private int level;

    Rank(String name, ChatColor color, boolean bolded, boolean italicized, int level) {
        this.name = name;
        this.color = color;
        this.bolded = bolded;
        this.italicized = italicized;
        this.level = level;
    }

    public boolean isHigherOrEqualsTo(Player player, Rank rank, boolean callback) {
        if (callback && this.level < rank.level) {
            player.sendMessage("You do not have permission. You must be rank: " + rank.getNameHigher() + "!");
            return false;
        }
        if (level < rank.level) {
            return false;
        }
        if (this.level >= rank.level) {
            return true;
        }
        return false;
    }

    public static boolean contains(String rank) {
        for(Rank ranks: Rank.values()) {
            if(ranks.name().equals(rank)) {
                return true;
            }
        }
        return false;
    }

    public String getNameHigher() {
        return this.name.toUpperCase();
    }

    public static String translate(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    public String getPrefix() {
        String name = this.name.toUpperCase();

        if (bolded && italicized) return translate(this.color + "&o&l" + name);
        if (bolded) return translate(this.color + "&l" + name);
        if (italicized) return translate(this.color + "&o" + name);

        return translate(this.color + name);
    }

}
