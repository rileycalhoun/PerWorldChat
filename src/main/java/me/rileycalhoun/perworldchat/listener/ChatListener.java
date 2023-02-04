package me.rileycalhoun.perworldchat.listener;

import me.rileycalhoun.perworldchat.PerWorldChat;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class ChatListener implements Listener {

    private final FileConfiguration config;

    public ChatListener () {
        PerWorldChat plugin = JavaPlugin.getPlugin(PerWorldChat.class);
        this.config = plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);
        String message = event.getMessage();
        Player player = event.getPlayer();
        World world = player.getWorld();

        String format = config.getString("message-format");
        if(format == null) format = "%sender: %message";
        for(Player worldPlayer : world.getPlayers()) {
            worldPlayer.sendMessage(formatChat(format, player, message));
        }

        String spyFormat = config.getString("spy-format");
        if(spyFormat == null) spyFormat = "[%world] %sender: %message";
        for(Player admin : player.getServer()
                .getOnlinePlayers().stream()
                .filter(pl -> {
                    boolean isInWorld = world.getPlayers().contains(pl);
                    boolean hasPermission = pl.hasPermission("perworldchat.admin");
                    return (!isInWorld) && hasPermission;
                }).collect(Collectors.toList())) {
            admin.sendMessage(formatChat(spyFormat, player, message));
        }

    }

    private String formatChat(String format, Player sender, String message) {
        String formattedChat = format;

       if(formattedChat.contains("%world"))
           formattedChat = formattedChat.replace("%world", sender.getWorld().getName());

       if(formattedChat.contains("%sender"))
           formattedChat = formattedChat.replace("%sender", sender.getDisplayName());

       if(formattedChat.contains("%message"))
           formattedChat = formattedChat.replace("%message", message);

       return ChatColor.translateAlternateColorCodes('&', formattedChat);
    }

}
