package org.VoyagerStar.warpPluginExtra.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return Stream.of("help", "info", "reload", "wpinfo")
                    .filter(cmd -> cmd.startsWith(partial))
                    .toList();
        }

        return List.of();
    }
}
