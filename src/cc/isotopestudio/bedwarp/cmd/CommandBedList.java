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

public class CommandBedList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bedlist")) {
            /*btp*/
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("Íæ¼ÒÖ´ÐÐµÄÃüÁî"));
                return true;
            }
            Player player = (Player) sender;
            return true;
        }
        return false;
    }
}
