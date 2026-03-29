package org.VoyagerStar.warpPluginExtra.Listeners;

import org.VoyagerStar.warpPluginExtra.Features.BeaconBeamManager;
import org.VoyagerStar.warpPluginExtra.WarpPluginExtra;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BeaconBeamListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 延迟一点执行，确保玩家完全加载
        new BukkitRunnable() {
            @Override
            public void run() {
                if (WarpPluginExtra.wpdata != null) {
                    showAllBeams();
                }
            }
        }.runTaskLater(WarpPluginExtra.instance, 60L); // 3 秒后执行
    }

    public static void showAllBeams() {
        if (WarpPluginExtra.wpdata == null) {
            return;
        }
        
        ConfigurationSection warps = WarpPluginExtra.wpdata.getConfigurationSection("warps");
        if (warps == null) {
            return;
        }
        
        for (String warpName : warps.getKeys(false)) {
            try {
                String world = warps.getString(warpName + ".world");
                double x = warps.getDouble(warpName + ".x");
                double y = warps.getDouble(warpName + ".y");
                double z = warps.getDouble(warpName + ".z");
                
                if (world != null) {
                    org.bukkit.World bukkitWorld = org.bukkit.Bukkit.getWorld(world);
                    if (bukkitWorld != null) {
                        BeaconBeamManager.showBeam(bukkitWorld, x, y, z, "warp_" + warpName);
                    }
                }
            } catch (Exception e) {
                WarpPluginExtra.instance.getLogger().warning(
                    "Failed to load beacon beam for warp: " + warpName
                );
            }
        }
    }
}
