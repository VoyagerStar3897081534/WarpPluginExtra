package org.VoyagerStar.warpPluginExtra;

import org.VoyagerStar.warpPluginExtra.Commands.WPExtra;
import org.VoyagerStar.warpPluginExtra.Features.BeaconBeamManager;
import org.VoyagerStar.warpPluginExtra.Listeners.BeaconBeamListener;
import org.VoyagerStar.warpPluginExtra.Utils.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class WarpPluginExtra extends JavaPlugin {
    public static WarpPluginExtra instance;
    public static YamlConfiguration wpdata;
    public static YamlConfiguration wpmessage;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("WarpPluginExtra is enabled!");

        if (getServer().getPluginManager().getPlugin("WarpPlugin") == null) {
            getLogger().warning("WarpPluginExtra is disabled because WarpPlugin is not installed.");
            getServer().getPluginManager().disablePlugin(this);
        }

        wpdata = YamlConfiguration.loadConfiguration(new File(Objects.requireNonNull(getServer().getPluginManager().getPlugin("WarpPlugin")).getDataFolder(), "data.yml"));
        wpmessage = YamlConfiguration.loadConfiguration(new File(Objects.requireNonNull(getServer().getPluginManager().getPlugin("WarpPlugin")).getDataFolder(), "message.yml"));

        Objects.requireNonNull(this.getCommand("wpextra")).setExecutor(new WPExtra());

        Objects.requireNonNull(this.getCommand("wpextra")).setTabCompleter(new TabCompleter());
        
        // 注册信标光束监听器
        getServer().getPluginManager().registerEvents(new BeaconBeamListener(), this);
        getLogger().info("Beacon beam feature is enabled!");
        
        // 显示所有传送点的信标光束
        BeaconBeamListener.showAllBeams();
        
        // 启动文件监听器，检测新添加的传送点
        BeaconBeamListener.startWarpFileWatcher();
        getLogger().info("Warp file watcher started!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WarpPluginExtra is disabled!");
        BeaconBeamManager.hideAllBeams();
    }

    public void reload() {
        wpdata = YamlConfiguration.loadConfiguration(new File(Objects.requireNonNull(getServer().getPluginManager().getPlugin("WarpPlugin")).getDataFolder(), "data.yml"));
        wpmessage = YamlConfiguration.loadConfiguration(new File(Objects.requireNonNull(getServer().getPluginManager().getPlugin("WarpPlugin")).getDataFolder(), "message.yml"));
        this.reloadConfig();
        
        // 重置并重新显示所有光柱
        BeaconBeamListener.resetDisplayedWarps();
        BeaconBeamManager.hideAllBeams();
        BeaconBeamListener.showAllBeams();
        
        getLogger().info("WPE Reloaded!");
    }
}
