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

public class CommandBedWarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bedwarp")) {
            /*btp*/
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 1) {
                sender.sendMessage(S.toYellow("/btp <地标名> - 传送到床地标"));
                sender.sendMessage(S.toYellow("/blist  - 床地标列表"));
                return true;
            }
            return true;
        }
        return false;
    }
}
