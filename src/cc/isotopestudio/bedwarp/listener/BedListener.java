package cc.isotopestudio.bedwarp.listener;
/*
 * Created by david on 2017/8/5.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.bedwarp.util.S;
import cc.isotopestudio.bedwarp.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import static cc.isotopestudio.bedwarp.BedWarp.playerData;

public class BedListener implements Listener {
    private Map<Player, Location> bedLoc1Map = new HashMap<>();
    private Map<Player, Location> bedLoc2Map = new HashMap<>();
    private Map<Player, String> bedNameMap = new HashMap<>();

    @EventHandler
    public void onPlaceBed(BlockMultiPlaceEvent event) {
        List<BlockState> replacedBlockStates = event.getReplacedBlockStates();
        if ((replacedBlockStates.size() <= 1 ||
                !replacedBlockStates.get(0).getBlock().getType().equals(Material.BED_BLOCK))) {
            return;
        }
        Player player = event.getPlayer();
        int max = 0;
        if (playerData.isConfigurationSection(player.getName())) {
            OptionalInt optionalInt = playerData.getConfigurationSection(player.getName()).getKeys(false).stream()
                    .filter(bedName -> bedName.startsWith("bed"))
                    .mapToInt(bedName -> Integer.parseInt(bedName.replaceAll("bed", "")))
                    .max();
            if (optionalInt.isPresent())
                max = optionalInt.getAsInt();
        }
        ++max;
        playerData.set(player.getName() + ".bed" + max + ".location1",
                Util.locationToString(replacedBlockStates.get(0).getLocation()));
        playerData.set(player.getName() + ".bed" + max + ".location2",
                Util.locationToString(replacedBlockStates.get(1).getLocation()));
        playerData.save();
        bedLoc1Map.put(player, replacedBlockStates.get(0).getLocation());
        bedLoc2Map.put(player, replacedBlockStates.get(1).getLocation());
        bedNameMap.put(player, "bed" + max);
        player.sendMessage(S.toPrefixYellow("请给地标命名"));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNameBed(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (bedNameMap.containsKey(player)) {
            event.setCancelled(true);
            String oldName = bedNameMap.get(player);
            Location loc1 = bedLoc1Map.get(player);
            Location loc2 = bedLoc2Map.get(player);
            String newName = ChatColor.stripColor(event.getMessage());
            if (newName.startsWith("bed")) {
                player.sendMessage(S.toPrefixRed("请勿已bed开头, 再试一次吧"));
                return;
            }
            if (!newName.matches("[0-9a-z]*")) {
                player.sendMessage(S.toPrefixRed("只能为英文和数字, 再试一次吧"));
                return;
            }
            playerData.set(player.getName() + "." + oldName, null);
            playerData.set(player.getName() + "." + newName + ".location1", Util.locationToString(loc1));
            playerData.set(player.getName() + "." + newName + ".location2", Util.locationToString(loc2));
            playerData.save();
            bedNameMap.remove(player);
            bedLoc1Map.remove(player);
            bedLoc2Map.remove(player);
            player.sendMessage(S.toPrefixYellow("成功命名, 可使用 /btp " + newName + " 传送"));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        bedNameMap.remove(player);
        bedLoc1Map.remove(player);
        bedLoc2Map.remove(player);
    }

    @EventHandler
    public void onBedBroke(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.BED_BLOCK) {
            return;
        }
        Bed bed = (Bed) event.getBlock().getState();
        System.out.println(bed.getLocation());
        for (String playerName : playerData.getKeys(false)) {
            for (String bedName : playerData.getConfigurationSection(playerName).getKeys(false)) {
                if (bed.getLocation().getBlock().getLocation().equals(Util.stringToLocation(
                        playerData.getString(playerName + "." + bedName + ".location1")).getBlock().getLocation()) ||
                        bed.getLocation().getBlock().getLocation().equals(Util.stringToLocation(
                                playerData.getString(playerName + "." + bedName + ".location2")).getBlock().getLocation())) {
                    playerData.set(playerName + "." + bedName, null);
                    playerData.save();
                    Player owner = Bukkit.getPlayerExact(playerName);
                    if (owner != null) {
                        owner.sendMessage(S.toPrefixYellow("你的床领地 " + bedName + " 已被拆除"));
                        bedNameMap.remove(owner);
                        bedLoc1Map.remove(owner);
                        bedLoc2Map.remove(owner);
                    }
                    event.getPlayer().sendMessage("已拆除 " + playerName + " 的床领地 " + bedName + " 已被拆除");
                    return;
                }
            }
        }
    }
}
