package net.bghd.rankcore.rankcore.mysql;

import lombok.Getter;
import lombok.Setter;
import net.bghd.rankcore.rankcore.Rank;
import net.bghd.rankcore.rankcore.RankCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private RankCore plugin = RankCore.getInstance();

    private java.util.UUID UUID;
    private String playerName;
    private Rank rank = Rank.DEFAULT;

    public PlayerData(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
    }

    public void load(Player player) {
        RankCore.getInstance().getMySQLManager().select("SELECT * FROM player WHERE uuid=?", resultSet -> {
            try {
                if(resultSet.next()) {
                    rank = (Rank.valueOf(resultSet.getString("rank")));
                } else {
                    RankCore.getInstance().getMySQLManager().execute("INSERT INTO player(uuid, name, rank) VALUES (?,?,?);", player.getUniqueId().toString(), player.getName(), Rank.DEFAULT.toString());
                }
            } catch(SQLException e) {
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
            }
        }, player.getUniqueId().toString());


    }

    public void save(Player player) {
        RankCore.getInstance().getMySQLManager().execute("UPDATE player SET rank=? WHERE uuid=?",
                rank.toString(), player.getUniqueId().toString());
    }

}
