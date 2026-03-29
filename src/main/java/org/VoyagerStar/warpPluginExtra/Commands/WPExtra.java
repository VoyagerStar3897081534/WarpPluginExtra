package org.VoyagerStar.warpPluginExtra.Commands;

import org.VoyagerStar.warpPluginExtra.WarpPluginExtra;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.mc_plfd_host.warpPlugin.WarpPlugin;

public class WPExtra implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("warppluginextra.admin")) {
            player.sendMessage(WarpPlugin.getMessages("no-perms"));
            return true;
        }

        if (args.length == 0) {
            info(player);
        }

        switch (args[0]) {
            case "reload" -> WarpPluginExtra.instance.reload();
            case "help" -> help(player);
            case "info" -> info(player);
            case "wpinfo" -> player.sendMessage("WarpPlugin v" + WarpPlugin.getPlugin(WarpPlugin.class).getDescription().getVersion());
        }
        return true;
    }

    private static void help(Player player) {
        player.sendMessage("§a/wpextra help : Show this help");
        player.sendMessage("§a/wpextra reload : Reload the plugin");
        player.sendMessage("§a/wpextra info : Show plugin info");
    }

    private static void info(Player player) {
        player.sendMessage("§aWarpPluginExtra v" + WarpPluginExtra.getPlugin(WarpPluginExtra.class).getDescription().getVersion());
        player.sendMessage("§aAuthor: " + WarpPluginExtra.getPlugin(WarpPluginExtra.class).getDescription().getAuthors());
    }
}
