package cc.isotopestudio.bedwarp.cmd;
/*
 * Created by david on 2017/8/5.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.bedwarp.gui.WarpGUI;
import cc.isotopestudio.bedwarp.util.S;
import cc.isotopestudio.bedwarp.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cc.isotopestudio.bedwarp.BedWarp.playerData;

public class CommandBedWarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bedwarp")) {
            /*btp*/
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("���ִ�е�����"));
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 1) {
                if (playerData.isSet(player.getName())) {
                    new WarpGUI(player, 0).open(player);
                } else {
                    sender.sendMessage(S.toPrefixRed("��û�д��ر�"));
                }
//                sender.sendMessage(S.toYellow("/btp <�ر���> - ���͵����ر�"));
//                sender.sendMessage(S.toYellow("/blist  - ���ر��б�"));
                return true;
            }
            if (playerData.isSet(player.getName() + "." + args[0] + ".location1")) {
                player.teleport(Util.stringToLocation(playerData.getString(
                        player.getName() + "." + args[0] + ".location1")));
            } else {
                player.sendMessage(S.toPrefixRed("���ر겻���ڻ��ѱ����"));
            }
            return true;
        }
        return false;
    }
}
