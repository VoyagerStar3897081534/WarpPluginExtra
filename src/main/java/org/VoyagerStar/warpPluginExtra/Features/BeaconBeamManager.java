package org.VoyagerStar.warpPluginExtra.Features;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconBeamManager {
    
    private static final Map<String, List<Location>> activeBeams = new HashMap<>();
    private static int taskId = -1;

    public static void showBeam(World world, double x, double y, double z, String beamId) {
        Location loc = new Location(world, x, y, z);
        
        // 添加到活动光束列表
        activeBeams.computeIfAbsent(beamId, k -> new ArrayList<>()).add(loc);
        
        // 启动定时任务更新光束
        if (taskId == -1) {
            startBeamTask();
        }
    }

    public static void hideBeam(String beamId) {
        activeBeams.remove(beamId);
        
        // 如果没有光束了，停止任务
        if (activeBeams.isEmpty()) {
            stopBeamTask();
        }
    }

    public static void hideAllBeams() {
        activeBeams.clear();
        stopBeamTask();
    }

    private static void startBeamTask() {
        taskId = Bukkit.getScheduler().runTaskTimer(
            org.VoyagerStar.warpPluginExtra.WarpPluginExtra.instance,
            () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (List<Location> locations : activeBeams.values()) {
                        for (Location loc : locations) {
                            // 发送粒子效果
                            player.spawnParticle(
                                org.bukkit.Particle.END_ROD,
                                loc.clone().add(0.5, 0, 0.5),
                                10,
                                0.1, 0.1, 0.1,
                                0.01
                            );
                            player.spawnParticle(
                                org.bukkit.Particle.END_ROD,
                                loc.clone().add(0.5, 1, 0.5),
                                10,
                                0.1, 0.1, 0.1,
                                0.01
                            );
                            player.spawnParticle(
                                org.bukkit.Particle.END_ROD,
                                loc.clone().add(0.5, 2, 0.5),
                                10,
                                0.1, 0.1, 0.1,
                                0.01
                            );
                        }
                    }
                }
            },
            0L,
            20L // 每秒更新一次
        ).getTaskId();
    }

    private static void stopBeamTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }
}
