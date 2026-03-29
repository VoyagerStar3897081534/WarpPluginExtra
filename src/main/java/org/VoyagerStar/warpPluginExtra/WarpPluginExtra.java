package org.VoyagerStar.warpPluginExtra;

import org.bukkit.plugin.java.JavaPlugin;

public final class WarpPluginExtra extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("WarpPluginExtra is enabled!");

        if (getServer().getPluginManager().getPlugin("WarpPlugin") == null) {
            getLogger().warning("WarpPluginExtra is disabled because WarpPlugin is not installed.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("WarpPluginExtra is disabled!");
    }
}
