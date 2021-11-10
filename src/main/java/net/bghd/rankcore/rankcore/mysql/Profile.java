package net.bghd.rankcore.rankcore.mysql;

import lombok.Getter;
import lombok.Setter;
import net.bghd.rankcore.rankcore.RankCore;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private RankCore plugin = RankCore.getInstance();

    private PlayerData data;
    private UUID UUID;
    private String playerName;

    public Profile(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
        this.data = new PlayerData(uuid, name);
    }
}
