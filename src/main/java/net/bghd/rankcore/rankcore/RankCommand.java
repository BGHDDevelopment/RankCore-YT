package net.bghd.rankcore.rankcore;

import net.bghd.rankcore.rankcore.command.Command;
import net.bghd.rankcore.rankcore.command.DynamicCommand;
import net.bghd.rankcore.rankcore.mysql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DynamicCommand(name = "rank", console = true)
public class RankCommand extends Command {

    RankCore plugin = RankCore.getInstance();

    @Override
    public void execute(CommandSender sender, String... args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = plugin.getProfileManager().getProfile(player);
            if(!profile.getData().getRank().isHigherOrEqualsTo(player, Rank.ADMIN, true)) return;
        }
        if(args.length < 2) {
            sender.sendMessage("Usage: /rank <player> <rank>");
            return;
        }
        if(!Rank.contains(args[1])) {
            sender.sendMessage("Invalid Rank!");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        try {
            plugin.getMySQLManager().execute("UPDATE player SET rank=? WHERE UUID=?",
                    args[1], target.getUniqueId().toString());
        } catch(NullPointerException e) {
            sender.sendMessage("Player is not found in database!");
            return;
        }
        if(target.isOnline()) {
            Profile targetprofile = plugin.getProfileManager().getProfile(target);
            targetprofile.getData().setRank(Rank.valueOf(args[1]));
            target.getPlayer().sendMessage("Your rank was updated!");
        }
        sender.sendMessage("You set that players rank");
    }
}
