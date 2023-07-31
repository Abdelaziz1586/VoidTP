package org.pebbleprojects.voidtp;

import org.bukkit.plugin.java.JavaPlugin;
import org.pebbleprojects.voidtp.handlers.Handler;

public final class VoidTP extends JavaPlugin {

    public static VoidTP INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        new Thread(Handler::new).start();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
