package cc.isotopestudio.bedwarp;

import cc.isotopestudio.bedwarp.cmd.CommandBedList;
import cc.isotopestudio.bedwarp.cmd.CommandBedWarp;
import cc.isotopestudio.bedwarp.listener.BedListener;
import cc.isotopestudio.bedwarp.util.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWarp extends JavaPlugin {

    private static final String pluginName = "BedWarp";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("BedWarp").append("]").append(ChatColor.RED).toString();

    public static BedWarp plugin;

    public static PluginFile config;
    public static PluginFile playerData;

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginFile(this, "config.yml", "config.yml");
        config.setEditable(false);
        playerData = new PluginFile(this, "player.yml");

        this.getCommand("bedwarp").setExecutor(new CommandBedWarp());
        this.getCommand("bedlist").setExecutor(new CommandBedList());

        Bukkit.getPluginManager().registerEvents(new BedListener(), this);
        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    public void onReload() {
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
