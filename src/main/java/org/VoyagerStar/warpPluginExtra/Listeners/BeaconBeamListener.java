package org.VoyagerStar.warpPluginExtra.Listeners;

import org.VoyagerStar.warpPluginExtra.Features.BeaconBeamManager;
import org.VoyagerStar.warpPluginExtra.WarpPluginExtra;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class BeaconBeamListener implements Listener {
    
    private static final Set<String> displayedWarps = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (WarpPluginExtra.wpdata != null) {
                    showAllBeams();
                }
            }
        }.runTaskLater(WarpPluginExtra.instance, 60L); // 3 秒后执行
    }

    public static void startWarpFileWatcher() {
        new BukkitRunnable() {
            private long lastModified = 0;
            
            @Override
            public void run() {
                File dataFile = new File(WarpPluginExtra.instance.getDataFolder().getParentFile(), "WarpPlugin/data.yml");
                if (dataFile.exists()) {
                    long currentModified = dataFile.lastModified();
                    if (currentModified != lastModified) {
                        WarpPluginExtra.wpdata = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(dataFile);
                        checkNewWarps();
                        lastModified = currentModified;
                    }
                }
            }
        }.runTaskTimer(WarpPluginExtra.instance, 100L, 40L);
    }

    private static void checkNewWarps() {
        if (WarpPluginExtra.wpdata == null) {
            return;
        }
        
        ConfigurationSection warps = WarpPluginExtra.wpdata.getConfigurationSection("warps");
        if (warps == null) {
            return;
        }
        
        for (String warpName : warps.getKeys(false)) {
            String beamId = "warp_" + warpName;
            
            try {
                boolean isPublic = warps.getBoolean(warpName + ".public");
                
                if (isPublic) {
                    if (!displayedWarps.contains(beamId)) {
                        String world = warps.getString(warpName + ".world");
                        double x = warps.getDouble(warpName + ".x");
                        double y = warps.getDouble(warpName + ".y");
                        double z = warps.getDouble(warpName + ".z");
                        
                        if (world != null) {
                            org.bukkit.World bukkitWorld = org.bukkit.Bukkit.getWorld(world);
                            if (bukkitWorld != null) {
                                BeaconBeamManager.showBeam(bukkitWorld, x, y, z, beamId);
                                displayedWarps.add(beamId);
                                WarpPluginExtra.instance.getLogger().info("Added beacon beam for new warp: " + warpName);
                            }
                        }
                    }
                } else {
                    if (displayedWarps.contains(beamId)) {
                        BeaconBeamManager.hideBeam(beamId);
                        displayedWarps.remove(beamId);
                        WarpPluginExtra.instance.getLogger().info("Removed beacon beam for warp: " + warpName);
                    }
                }
            } catch (Exception e) {
                WarpPluginExtra.instance.getLogger().warning(
                    "Failed to process beacon beam for warp: " + warpName
                );
            }
        }
    }

    public static void showAllBeams() {
        displayedWarps.clear();
        
        if (WarpPluginExtra.wpdata == null) {
            return;
        }
        
        ConfigurationSection warps = WarpPluginExtra.wpdata.getConfigurationSection("warps");
        if (warps == null) {
            return;
        }
        
        for (String warpName : warps.getKeys(false)) {
            try {
                if (!warps.getBoolean(warpName + ".public")) {
                    continue;
                }

                String world = warps.getString(warpName + ".world");
                double x = warps.getDouble(warpName + ".x");
                double y = warps.getDouble(warpName + ".y");
                double z = warps.getDouble(warpName + ".z");
                
                if (world != null) {
                    org.bukkit.World bukkitWorld = org.bukkit.Bukkit.getWorld(world);
                    if (bukkitWorld != null) {
                        BeaconBeamManager.showBeam(bukkitWorld, x, y, z, "warp_" + warpName);
                        displayedWarps.add("warp_" + warpName);
                    }
                }
            } catch (Exception e) {
                WarpPluginExtra.instance.getLogger().warning(
                    "Failed to load beacon beam for warp: " + warpName
                );
            }
        }
    }

    public static void resetDisplayedWarps() {
        displayedWarps.clear();
    }
}
