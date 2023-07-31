package org.pebbleprojects.voidtp;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.pebbleprojects.voidtp.handlers.Handler;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command command, final String label, final String[] args) {
        new Thread(() -> {
            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("admin")) {
                        Handler.INSTANCE.updateConfig();
                        Handler.INSTANCE.updateData();
                        sender.sendMessage(Handler.INSTANCE.getConfig("messages.reload", true).toString());
                        return;
                    }
                    sender.sendMessage(Handler.INSTANCE.getConfig("messages.no-permission", true).toString());
                    return;
                }

                if (args[0].equalsIgnoreCase("version")) {
                    sender.sendMessage("§fYou are running version §b" + VoidTP.INSTANCE.getDescription().getVersion() + " §fof SunVoidTp");
                    return;
                }

                if (args[0].equalsIgnoreCase("setSpawn")) {
                    if (sender.hasPermission("admin")) {
                        if (sender instanceof Player) {
                            final Player player = (Player) sender;

                            final String worldName = player.getWorld().getName();

                            if (Handler.INSTANCE.isWorldAllowed(worldName)) {
                                Handler.INSTANCE.writeData("locations." + worldName.toLowerCase(), player.getLocation());
                                player.sendMessage("Set void tp spawn of world " + worldName + " to your current location");
                                return;
                            }
                            player.sendMessage("You're not allowed to set void tp spawn here");
                        }
                        return;
                    }
                    sender.sendMessage(Handler.INSTANCE.getConfig("messages.no-permission", true).toString());
                    return;
                }
            }
            sender.sendMessage("§fAvailable commands:\n§b/SunVoidTp - §fShows this message\n§b/SunVoidTp reload - §fReloads the plugin\n§b/SunVoidTp version - §fGet the version of the plugin\n§b/SunVoidTp setSpawn - §fSet void tp spawn of the world you're in");
        }).start();

        return false;
    }
}
