package org.VoyagerStar.warpPluginExtra.Features;

import org.bukkit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconBeamManager {
    
    private static final Map<String, List<Location>> activeBeams = new HashMap<>();
    private static int taskId = -1;

    public static void showBeam(World world, double x, double y, double z, String beamId) {
        Location loc = new Location(world, x, y, z);
        
        activeBeams.computeIfAbsent(beamId, k -> new ArrayList<>()).add(loc);
        
        if (taskId == -1) {
            startBeamTask();
        }
    }

    public static void hideAllBeams() {
        activeBeams.clear();
        stopBeamTask();
    }

    public static void hideBeam(String beamId) {
        activeBeams.remove(beamId);
        if (activeBeams.isEmpty()) {
            stopBeamTask();
        }
    }

    private static void startBeamTask() {
        Particle.DustOptions dustOptions = new Particle.DustOptions(
                Color.fromRGB(0, 0, 255),
                1.0f
        );
        Particle.DustOptions dustOptionsMain = new Particle.DustOptions(
                Color.fromRGB(255, 0, 0),
                1.0f
        );
        taskId = Bukkit.getScheduler().runTaskTimer(
            org.VoyagerStar.warpPluginExtra.WarpPluginExtra.instance,
            () -> {
                if (activeBeams.isEmpty()) {
                    return;
                }

                for (List<Location> locations : activeBeams.values()) {
                    Location baseLoc = locations.getFirst();
                    World world = baseLoc.getWorld();
                    if (world == null) continue;

                    for (double yOffset = -64; yOffset < 319; yOffset += 1) {
                        Location centerLoc = baseLoc.clone();
                        centerLoc.setY(yOffset);
                        spawnParticle(world, centerLoc, yOffset == baseLoc.getY() ? dustOptionsMain : dustOptions);
                        
                        double radius = 0.5;
                        
                        Location upLoc = baseLoc.clone().add(0, yOffset + radius, 0);
                        spawnParticle(world, upLoc, dustOptions);
                        
                        Location downLoc = baseLoc.clone().add(0, yOffset - radius, 0);
                        spawnParticle(world, downLoc, dustOptions);
                        
                        Location leftLoc = baseLoc.clone().add(radius, yOffset, 0);
                        spawnParticle(world, leftLoc, dustOptions);
                        
                        Location rightLoc = baseLoc.clone().add(-radius, yOffset, 0);
                        spawnParticle(world, rightLoc, dustOptions);
                        
                        Location neLoc = baseLoc.clone().add(radius, yOffset, radius);
                        spawnParticle(world, neLoc, dustOptions);
                        
                        Location nwLoc = baseLoc.clone().add(-radius, yOffset, radius);
                        spawnParticle(world, nwLoc, dustOptions);
                        
                        Location seLoc = baseLoc.clone().add(radius, yOffset, -radius);
                        spawnParticle(world, seLoc, dustOptions);
                        
                        Location swLoc = baseLoc.clone().add(-radius, yOffset, -radius);
                        spawnParticle(world, swLoc, dustOptions);
                    }
                }
            },
            0L,
            20L
        ).getTaskId();
    }

    private static void spawnParticle(World world, Location loc, Particle.DustOptions options) {
        world.spawnParticle(
                Particle.REDSTONE,
                loc,
                1,
                0, 0, 0, 0,
                options
        );
    }

    private static void stopBeamTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }
}
