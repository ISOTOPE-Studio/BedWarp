package cc.isotopestudio.bedwarp.cmd;
/*
 * Created by david on 2017/8/5.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.bedwarp.util.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cc.isotopestudio.bedwarp.BedWarp.playerData;

public class CommandBedList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bedlist")) {
            /*btp*/
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;
            if (playerData.isConfigurationSection(player.getName())) {
                sender.sendMessage(S.toPrefixAqua(" --- 床地标 ---"));
                playerData.getConfigurationSection(player.getName()).getKeys(false)
                        .forEach(bedName -> player.sendMessage(S.toYellow(
                                " - " + bedName + ": " + playerData.getString(
                                        player.getName() + "." + bedName + ".location1"))));
            } else {
                sender.sendMessage(S.toPrefixRed("你没有床地标"));
            }
            return true;
        }
        return false;
    }
}
