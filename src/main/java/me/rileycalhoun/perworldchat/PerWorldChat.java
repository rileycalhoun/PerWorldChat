package me.rileycalhoun.perworldchat;

import me.rileycalhoun.perworldchat.listener.ChatListener;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PerWorldChat extends JavaPlugin {

    @Override
    public void onEnable() {
        Server server = this.getServer();
        FileConfiguration configuration = this.getConfig();

        configuration.options().copyDefaults(true);
        saveConfig();

        server.getPluginManager().registerEvents(new ChatListener(), this);
    }

}
