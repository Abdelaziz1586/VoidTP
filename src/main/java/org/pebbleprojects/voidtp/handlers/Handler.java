package org.pebbleprojects.voidtp.handlers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.pebbleprojects.voidtp.Command;
import org.pebbleprojects.voidtp.VoidTP;
import org.pebbleprojects.voidtp.listeners.EntityDamage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Handler {

    private final File dataFile;
    public static Handler INSTANCE;
    private FileConfiguration data, config;

    public Handler() {
        INSTANCE = this;

        VoidTP.INSTANCE.getConfig().options().copyDefaults(true);
        VoidTP.INSTANCE.saveDefaultConfig();
        updateConfig();
        dataFile = new File(VoidTP.INSTANCE.getDataFolder().getPath(), "data.yml");
        if (!dataFile.exists()) {
            try {
                if (dataFile.createNewFile())
                    VoidTP.INSTANCE.getServer().getConsoleSender().sendMessage("§aCreated data.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateData();

        Objects.requireNonNull(VoidTP.INSTANCE.getCommand("sunvoidtp")).setExecutor(new Command());

        VoidTP.INSTANCE.getServer().getPluginManager().registerEvents(new EntityDamage(), VoidTP.INSTANCE);
    }

    public void updateConfig() {
        VoidTP.INSTANCE.reloadConfig();
        config = VoidTP.INSTANCE.getConfig();
    }

    public void updateData() {
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void writeData(final String key, final Object value) {
        data.set(key, value);
        try {
            data.save(dataFile);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    public Object getConfig(final String key, final boolean translate) {
        return config.isSet(key) ? (translate ? ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(key))).replace("%nl%", "\n") : config.get(key)) : null;
    }

    public Object getData(final String key) {
        return data.isSet(key) ? data.get(key) : null;
    }

    public final boolean isWorldAllowed(final String worldName) {
        for (final String allowedWorld : getAllowedWorlds()) {
            if (worldName.equalsIgnoreCase(allowedWorld)) return true;
        }
        return false;
    }

    private List<String> getAllowedWorlds() {
        return config.isList("allowed-worlds") ? config.getStringList("allowed-worlds") : new ArrayList<>();
    }

}
