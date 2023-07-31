package org.pebbleprojects.voidtp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.pebbleprojects.voidtp.VoidTP;
import org.pebbleprojects.voidtp.handlers.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityDamage implements Listener {

    private final List<UUID> cooldown = new ArrayList<>();

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            final Entity entity = event.getEntity();

            if (cooldown.contains(entity.getUniqueId())) return;

            final String worldName = entity.getWorld().getName();

            if (entity instanceof Player) {
                if (Handler.INSTANCE.isWorldAllowed(worldName)) {
                    cooldown.add(entity.getUniqueId());

                    event.setCancelled(true);

                    final Object o = Handler.INSTANCE.getData("locations." + worldName.toLowerCase());

                    final Location location = o instanceof Location ? (Location) o : entity.getWorld().getSpawnLocation();

                    Bukkit.getScheduler().runTaskLater(VoidTP.INSTANCE, () -> {
                        entity.teleport(location);
                        entity.setFallDistance(0);
                    }, 1);


                    entity.sendMessage("why are you gay?");

                    final int i = cooldown.size()-1;

                    Bukkit.getScheduler().runTaskLater(VoidTP.INSTANCE, () -> cooldown.remove(i), 1);
                }
            }
        }
    }

}
