/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.bedwarp.gui;

import cc.isotopestudio.bedwarp.util.S;
import cc.isotopestudio.bedwarp.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.isotopestudio.bedwarp.BedWarp.playerData;
import static cc.isotopestudio.bedwarp.BedWarp.plugin;

public class WarpGUI extends GUI {

    private List<String> warps;
    private Map<Integer, String> slotIDMap;

    private static final ItemStack PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);

    public WarpGUI(Player player, int page) {
        super(S.toBoldGold("床地标列表") + "[" + player.getName() + "]", 3 * 9, player);
        this.page = page;

        warps = new ArrayList<>(playerData.getConfigurationSection(player.getName()).getKeys(false));
        slotIDMap = new HashMap<>();

        if (page > 0) {
            ItemStack LASTPAGE = Util.buildItem(Material.ARROW, true, S.toBoldGold("上一页"), S.toRed("第 " + (page + 1) + " 页"));
            setOption(0, LASTPAGE);
            setOption(18, LASTPAGE);
        } else {
            setOption(0, PANE);
            setOption(18, PANE);
        }
        if (page < getTotalPage() - 1) {
            ItemStack NEXTPAGE = Util.buildItem(Material.ARROW, true, S.toBoldGold("上一页"), S.toRed("第 " + (page + 1) + " 页"));
            setOption(8, NEXTPAGE);
            setOption(26, NEXTPAGE);
        } else {
            setOption(8, PANE);
            setOption(26, PANE);
        }
        for (int i = 0; i < 3 * 7; i++) {
            if (i + page * 3 * 7 < warps.size()) {
                int row = i / 7;
                int col = i % 7;
                int pos = row * 9 + col + 1;
                String bedName = warps.get(page * 3 * 7 + i);

                slotIDMap.put(pos, bedName);

                ItemStack warpItem = new ItemStack(Material.BED);
                ItemMeta itemMeta = warpItem.getItemMeta();
                itemMeta.setDisplayName(S.toBoldRed(bedName));
                List<String> lore = new ArrayList<>();
                lore.add(S.toGray("----------"));
                lore.add(S.toAqua("坐标: " + playerData.getString(
                        player.getName() + "." + bedName + ".location1")));
                lore.add(S.toItalicYellow("单击 传送"));
                lore.add(S.toGray("----------"));
                itemMeta.setLore(lore);
                warpItem.setItemMeta(itemMeta);

                setOption(pos, warpItem);
            }
        }
    }

    private void onNextPage() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                (new WarpGUI(player, page + 1)).open(player), 2);
    }

    private void onPreviousPage() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                (new WarpGUI(player, page - 1)).open(player), 2);
    }

    private int getTotalPage() {
        int size = warps.size();
        int page = size / (7 * 3);
        if (size % (7 * 3) != 0)
            page++;
        return page;
    }

    private void onWarp(int slot) {
        String warpName = slotIDMap.get(slot);
        if (playerData.isSet(player.getName() + "." + warpName + ".location1")) {
            player.teleport(Util.stringToLocation(playerData.getString(
                    player.getName() + "." + warpName + ".location1")));
        } else {
            player.sendMessage(S.toPrefixRed("床地标不存在或已被拆除"));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot < 0 || slot >= size) {
                return;
            }

            if (optionIcons[slot] != null) {
                if (slot == 0 || slot == 18) {
                    if (page > 0)
                        onPreviousPage();
                    else
                        return;
                } else if (slot == 8 || slot == 26) {
                    if (page < getTotalPage() - 1)
                        onNextPage();
                    else
                        return;
                } else if (slot % 9 > 0 && slot % 9 < 8) {
                    onWarp(slot);
                }
                player.closeInventory();
            }
        }
    }

}
